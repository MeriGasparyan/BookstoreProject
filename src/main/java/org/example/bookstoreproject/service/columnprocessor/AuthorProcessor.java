package org.example.bookstoreproject.service.columnprocessor;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.example.bookstoreproject.enums.Role;
import org.example.bookstoreproject.persistance.entry.Author;
import org.example.bookstoreproject.persistance.repository.AuthorRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.format.AuthorFormatter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RequiredArgsConstructor
public class AuthorProcessor {

    private final AuthorRepository authorRepository;
    private final AuthorFormatter authorFormatter;

    public Pair<Map<String, Author>, Map<String, List<Author>>> process(List<CSVRow> data) {

        Map<String, Author> existingAuthorMap = new ConcurrentHashMap<>();
        Map<String, List<Author>> authorBookMap = new ConcurrentHashMap<>();
        List<Author> newAuthorsToSave = new CopyOnWriteArrayList<>();

        List<Author> authorList = authorRepository.findAll();
        authorList.forEach(author -> existingAuthorMap.put(author.getName(), author));

        data.parallelStream().forEach(row -> {
            if (!row.getAuthor().isEmpty()) {
                Map<String, List<Role>> formattedAuthors = authorFormatter.formatAuthor(row.getAuthor().trim());

                formattedAuthors.forEach((name, roles) -> {
                    // ComputeIfAbsent is atomic
                    Author author = existingAuthorMap.computeIfAbsent(name, k -> {
                        Author newAuthor = new Author(name);
                        newAuthorsToSave.add(newAuthor);
                        return newAuthor;
                    });

                    authorBookMap.compute(row.getBookID().trim(), (k, v) -> {
                        if (v == null) {
                            v = new CopyOnWriteArrayList<>();
                        }
                        v.add(author);
                        return v;
                    });
                });
            }
        });

        if (!newAuthorsToSave.isEmpty()) {
            authorRepository.saveAll(newAuthorsToSave);
        }

        return Pair.of(existingAuthorMap, authorBookMap);
    }
}