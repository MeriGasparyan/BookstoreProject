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

    public void processColumns(List<CSVRow> data) {
        if (data == null || data.isEmpty()) {
            System.err.println("CSV data is empty. Skipping service initialization.");
            return;
        }
        Pair<Map<String, Author>, Map<String, List<Author>>> authorProcess = authorProcessor.process(data);
        Map<String, Author> existingAuthorMap = authorProcess.getLeft();
        Map<String, List<Author>> sameBookAuthorsMap = authorProcess.getRight();
        authorRoleProcessor.process(data, existingAuthorMap);

        Pair<Map<String, Genre>, Map<String, List<Genre>>> genreProcess = genreProcessor.process(data);
        Map<String, Genre> existingGenreMap = genreProcess.getLeft();
        Map<String, List<Genre>> sameBookGenreMap = genreProcess.getRight();

        Pair<Map<String, Character>, Map<String, List<Character>>> characterProcess = characterProcessor.process(data);
        Map<String, Character> existingCharacterMap = characterProcess.getLeft();
        Map<String, List<Character>> sameBookCharactersMap = characterProcess.getRight();

        Pair<Map<String, Setting>, Map<String, List<Setting>>> settingProcess = settingProcessor.process(data);
        Map<String, Setting> existingSettingMap = settingProcess.getLeft();
        Map<String, List<Setting>> sameBookSettingMap = settingProcess.getRight();

        Pair<Map<String, Award>, Map<String, List<Award>>> awardProcess = awardProcessor.process(data);
        Map<String, Award> existingAwardMap = awardProcess.getLeft();
        Map<String, List<Award>> sameBookAwardMap = awardProcess.getRight();

        Map<String, Publisher> existingPublisherMap = publisherProcessor.process(data);
        Map<String, Series> existingSeriesMap = seriesProcessor.process(data);

        Map<String, Book> bookMap = bookProcessor.process(data, existingPublisherMap, existingSeriesMap);
        bookAuthorProcessor.process(bookMap, sameBookAuthorsMap);
        bookAwardProcessor.process(bookMap, sameBookAwardMap);
        bookCharacterProcessor.process(bookMap, sameBookCharactersMap);
        bookGenreProcessor.process(bookMap, sameBookGenreMap);
        bookSettingProcessor.process(bookMap, sameBookSettingMap);
        bookAwardProcessor.process(bookMap, sameBookAwardMap);

        bookRatingStarProcessor.process(data, bookMap);

        imageProcessor.process(data);

    }
}
