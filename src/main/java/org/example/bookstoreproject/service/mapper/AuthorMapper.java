package org.example.bookstoreproject.service.mapper;

import org.example.bookstoreproject.persistance.entry.Author;
import org.example.bookstoreproject.service.dto.AuthorDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorMapper {
    public Author mapDtoToEntity(AuthorDTO dto) {
        return new Author(dto.getName());
    }
    }
