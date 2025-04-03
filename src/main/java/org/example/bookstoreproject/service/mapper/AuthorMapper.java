package org.example.bookstoreproject.service.mapper;

import org.example.bookstoreproject.business.AuthorC;
import org.example.bookstoreproject.persistance.entry.Author;
import org.example.bookstoreproject.service.dto.AuthorDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorMapper {

        public AuthorDTO mapToDto(AuthorC author) {
            if (author == null) {
                return null;
            }

            return new AuthorDTO(
                    author.getName()
            );
        }

        public List<AuthorDTO> mapToDtos(List<AuthorC> users) {
            List<AuthorDTO> dtos = new ArrayList<>();

            for (AuthorC user : users) {
                dtos.add(mapToDto(user));
            }

            return dtos;
        }

        public AuthorC mapDtoToUser(AuthorDTO dto) {
            return new AuthorC(dto.getName());
        }
    public Author mapDtoToEntity(AuthorDTO dto) {
        return new Author(dto.getName());
    }
    }
