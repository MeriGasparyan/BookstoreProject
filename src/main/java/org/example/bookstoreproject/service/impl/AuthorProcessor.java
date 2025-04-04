package org.example.bookstoreproject.service.impl;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Author;
import org.example.bookstoreproject.persistance.repository.AuthorRepository;
import org.example.bookstoreproject.service.dto.AuthorDTO;
import org.example.bookstoreproject.service.mapper.AuthorMapper;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthorProcessor implements CSVColumnProcessor, Service {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;


    public void process(List<Map<String, String>> data) {
        for (Map<String, String> row : data) {
            if (!row.get("author").isEmpty()) {
                AuthorDTO authorDTO = new AuthorDTO(row.get("author").trim());
                //System.out.println(authorDTO.getName());

                Author author = authorMapper.mapDtoToEntity(authorDTO);
                authorRepository.save(author);
            }
        }
    }

    @Override
    public String value() {
        return "";
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}

