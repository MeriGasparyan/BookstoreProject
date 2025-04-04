package org.example.bookstoreproject.service.impl;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Author;
import org.example.bookstoreproject.persistance.repository.AuthorRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.dto.AuthorDTO;
import org.example.bookstoreproject.service.mapper.AuthorMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
public class AuthorProcessor implements CSVColumnProcessor{

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;


    public void process(List<CSVRow> data) {
        for (CSVRow row : data) {
            if (!row.getAuthor().isEmpty()) {
                AuthorDTO authorDTO = new AuthorDTO(row.getAuthor().trim());

                Optional<Author> existing = authorRepository.findByName(authorDTO.getName());
                if (existing.isEmpty()) {
                    Author author = authorMapper.mapDtoToEntity(authorDTO);
                    authorRepository.save(author);
                }
            }
        }
    }

}

