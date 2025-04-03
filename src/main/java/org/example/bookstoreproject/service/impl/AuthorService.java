package org.example.bookstoreproject.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Author;
import org.example.bookstoreproject.persistance.repository.AuthorRepository;
import org.example.bookstoreproject.service.dto.AuthorDTO;
import org.example.bookstoreproject.service.mapper.AuthorMapper;
import org.example.bookstoreproject.service.utility.CSVParser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthorService {

    private final CSVParser csvParser;
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;


    @PostConstruct
    public void init() {
        processAuthors();
    }
    public void processAuthors() {
        List<Map<String, String>> data = csvParser.getData();
        for (Map<String, String> row : data) {
            AuthorDTO authorDTO = new AuthorDTO(row.get("author"));
            System.out.println(authorDTO.getName());

            Author author = authorMapper.mapDtoToEntity(authorDTO);
            authorRepository.save(author);
        }
    }
}

