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

@Component
@RequiredArgsConstructor
public class AuthorProcessor {

    private final AuthorRepository authorRepository;
    private final AuthorFormatter authorFormatter;

    public Pair<Map<String, Author>, Map<String, List<Author>>> process(List<CSVRow> data) {
        Map<String, List<Author>> authorBookMap = new HashMap<>();
        Map<String, Author> existingAuthorMap = new HashMap<>();
        List<Author> authorList = authorRepository.findAll();
        for (Author author : authorList) {
            existingAuthorMap.put(author.getName(), author);
        }
        List<Author> newAuthorsToSave = new ArrayList<>();

        for (CSVRow row : data) {
            if (!row.getAuthor().isEmpty()) {
                Map<String, List<Role>> formattedAuthors = authorFormatter.formatAuthor(row.getAuthor().trim());
                for (Map.Entry<String, List<Role>> entry : formattedAuthors.entrySet()) {
                    String name = entry.getKey();
                    if (!existingAuthorMap.containsKey(name)) {
                        Author author = new Author(name);
                        existingAuthorMap.put(name, author);
                        newAuthorsToSave.add(author);
                    }
                    List<Author> authorsForBook = authorBookMap.getOrDefault(row.getBookID().trim(), new ArrayList<>());
                    authorsForBook.add(existingAuthorMap.get(name));
                    authorBookMap.put(row.getBookID().trim(), authorsForBook);
                }
            }
        }
        if (!newAuthorsToSave.isEmpty()) {
            authorRepository.saveAll(newAuthorsToSave);
        }

        return Pair.of(existingAuthorMap, authorBookMap);

    }
}
