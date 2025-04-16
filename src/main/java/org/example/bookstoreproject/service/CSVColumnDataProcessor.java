package org.example.bookstoreproject.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.example.bookstoreproject.persistance.entry.*;
import org.example.bookstoreproject.persistance.entry.Character;
import org.example.bookstoreproject.service.columnprocessor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

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

    public void processColumns(List<CSVRow> data) {
        if (data == null || data.isEmpty()) {
            System.err.println("CSV data is empty. Skipping service initialization.");
            return;
        }
        // Phase 1: Process publishers and series first (these are needed for books)
        CompletableFuture<Map<String, Publisher>> publisherFuture =
                CompletableFuture.supplyAsync(() -> publisherProcessor.process(data), executorService);

        CompletableFuture<Map<String, Series>> seriesFuture =
                CompletableFuture.supplyAsync(() -> seriesProcessor.process(data), executorService);

        // Wait for these to complete before processing books
        Map<String, Publisher> publisherMap = publisherFuture.join();
        Map<String, Series> seriesMap = seriesFuture.join();

        // Phase 2: Process books AND start processing other independent entities in parallel
        CompletableFuture<Map<String, Book>> bookFuture =
                CompletableFuture.supplyAsync(
                        () -> bookProcessor.process(data, publisherMap, seriesMap),
                        executorService);

        CompletableFuture<Pair<Map<String, Author>, Map<String, List<Author>>>> authorFuture =
                CompletableFuture.supplyAsync(() -> authorProcessor.process(data), executorService);

        CompletableFuture<Pair<Map<String, Genre>, Map<String, List<Genre>>>> genreFuture =
                CompletableFuture.supplyAsync(() -> genreProcessor.process(data), executorService);

        CompletableFuture<Pair<Map<String, Character>, Map<String, List<Character>>>> characterFuture =
                CompletableFuture.supplyAsync(() -> characterProcessor.process(data), executorService);

        CompletableFuture<Pair<Map<String, Setting>, Map<String, List<Setting>>>> settingFuture =
                CompletableFuture.supplyAsync(() -> settingProcessor.process(data), executorService);

        CompletableFuture<Pair<Map<String, Award>, Map<String, List<Award>>>> awardFuture =
                CompletableFuture.supplyAsync(() -> awardProcessor.process(data), executorService);

        // Wait for all phase 2 tasks to complete
        Map<String, Book> bookMap = bookFuture.join();
        Pair<Map<String, Author>, Map<String, List<Author>>> authorResults = authorFuture.join();
        Pair<Map<String, Genre>, Map<String, List<Genre>>> genreResults = genreFuture.join();
        Pair<Map<String, Character>, Map<String, List<Character>>> characterResults = characterFuture.join();
        Pair<Map<String, Setting>, Map<String, List<Setting>>> settingResults = settingFuture.join();
        Pair<Map<String, Award>, Map<String, List<Award>>> awardResults = awardFuture.join();

        // logThreadPoolStatus();


//        CompletableFuture.allOf(
//                CompletableFuture.runAsync(() ->
//                        bookAuthorProcessor.process(bookMap, authorFuture.join().getRight()), executorService),
//                CompletableFuture.runAsync(() ->
//                        bookGenreProcessor.process(bookMap, genreFuture.join().getRight()), executorService),
//                CompletableFuture.runAsync(() ->
//                        bookCharacterProcessor.process(bookMap, characterFuture.join().getRight()), executorService),
//                CompletableFuture.runAsync(() ->
//                        bookSettingProcessor.process(bookMap, settingFuture.join().getRight()), executorService),
//                CompletableFuture.runAsync(() ->
//                        bookAwardProcessor.process(bookMap, awardFuture.join().getRight()), executorService),
//                CompletableFuture.runAsync(() ->
//                        bookRatingStarProcessor.process(data, bookMap), executorService)
//        ).join();
    }

    private void logThreadPoolStatus() {
        if (executorService instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor tpe = (ThreadPoolExecutor) executorService;
            System.out.printf("Thread pool status: Active=%d, Queue=%d, Completed=%d%n",
                    tpe.getActiveCount(),
                    tpe.getQueue().size(),
                    tpe.getCompletedTaskCount());
        }
    }
}
