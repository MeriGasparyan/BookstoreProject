package org.example.bookstoreproject.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.example.bookstoreproject.persistance.entry.*;
import org.example.bookstoreproject.persistance.entry.Character;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.service.columnprocessor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CSVColumnDataProcessor {

    private final AuthorProcessor authorProcessor;
    private final AuthorRoleProcessor authorRoleProcessor;
    private final AwardProcessor awardProcessor;
    private final BookAuthorProcessor bookAuthorProcessor;
    private final BookAwardProcessor bookAwardProcessor;
    private final BookCharacterProcessor bookCharacterProcessor;
    private final BookGenreProcessor bookGenreProcessor;
    private final BookProcessor bookProcessor;
    private final CharacterProcessor characterProcessor;
    private final GenreProcessor genreProcessor;
    private final ImageProcessor imageProcessor;
    private final PublisherProcessor publisherProcessor;
    private final BookRatingStarProcessor bookRatingStarProcessor;
    private final SeriesProcessor seriesProcessor;
    private final SettingProcessor settingProcessor;
    private final BookSettingProcessor bookSettingProcessor;
    private final ExecutorService executorService;
    private final BookRepository bookRepository;

    public void processColumns(List<CSVRow> data) {
        if (data == null || data.isEmpty()) {
            System.err.println("CSV data is empty. Skipping service initialization.");
            return;
        }
        CompletableFuture<Map<String, Publisher>> publisherFuture =
                CompletableFuture.supplyAsync(() -> publisherProcessor.process(data), executorService);

        CompletableFuture<Map<String, Series>> seriesFuture =
                CompletableFuture.supplyAsync(() -> seriesProcessor.process(data), executorService);
        CompletableFuture<Pair<Map<String, Author>, Map<String, List<Author>>>> authorFuture =
                CompletableFuture.supplyAsync(() -> authorProcessor.process(data), executorService);

        Map<String, Publisher> publisherMap = publisherFuture.join();
        Map<String, Series> seriesMap = seriesFuture.join();
        Pair<Map<String, Author>, Map<String, List<Author>>> authorResults = authorFuture.join();
        Map<String, Author> existingAuthorMap = authorResults.getLeft();
        Map<String, List<Author>> authorBookMap = authorResults.getRight();

        CompletableFuture<Map<String, Book>> bookFuture =
                CompletableFuture.supplyAsync(
                        () -> bookProcessor.process(data, publisherMap, seriesMap),
                        executorService);

        CompletableFuture<Map<String, List<Genre>>> genreFuture =
                CompletableFuture.supplyAsync(() -> genreProcessor.process(data), executorService);

        CompletableFuture<Map<String, List<Character>>> characterFuture =
                CompletableFuture.supplyAsync(() -> characterProcessor.process(data), executorService);

        CompletableFuture<Map<String, List<Setting>>> settingFuture =
                CompletableFuture.supplyAsync(() -> settingProcessor.process(data), executorService);

        CompletableFuture<Map<String, List<Award>>> awardFuture =
                CompletableFuture.supplyAsync(() -> awardProcessor.process(data), executorService);

        Map<String, Book> bookMap = bookFuture.join();
        Map<String, List<Genre>> genreResults = genreFuture.join();
        Map<String, List<Character>> characterResults = characterFuture.join();
        Map<String, List<Setting>> settingResults = settingFuture.join();
        Map<String, List<Award>> awardResults = awardFuture.join();
        CompletableFuture.allOf(
                CompletableFuture.runAsync(() ->
                        authorRoleProcessor.process(data, existingAuthorMap), executorService),
                CompletableFuture.runAsync(() ->
                        bookAuthorProcessor.process(bookMap, authorBookMap), executorService),
                CompletableFuture.runAsync(() ->
                        bookGenreProcessor.process(bookMap, genreResults), executorService),
                CompletableFuture.runAsync(() ->
                        bookCharacterProcessor.process(bookMap, characterResults), executorService),
                CompletableFuture.runAsync(() ->
                        bookSettingProcessor.process(bookMap, settingResults), executorService),
                CompletableFuture.runAsync(() ->
                        bookAwardProcessor.process(bookMap, awardResults), executorService),
                CompletableFuture.runAsync(() ->
                        bookRatingStarProcessor.process(data, bookMap), executorService),
                CompletableFuture.runAsync(() ->
                        imageProcessor.process(data, bookRepository), executorService)
        );
    }
}
