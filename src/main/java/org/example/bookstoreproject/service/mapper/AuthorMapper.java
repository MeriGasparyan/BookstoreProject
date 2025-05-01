package org.example.bookstoreproject.service.mapper;

import org.example.bookstoreproject.persistance.entity.Author;
import org.example.bookstoreproject.service.dto.AuthorDTO;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {
    public Author mapDtoToEntity(AuthorDTO dto) {
        return new Author(dto.getName());
    }
    }
