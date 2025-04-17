package org.example.bookstoreproject.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookSearchRequestDTO {
        private String title;
        private List<String> authors;
        private List<String> genres;
        private String language;
        private List<String> publishers;
        private List<String> series;
        private List<String> awards;
        private List<String> characters;
        private List<String> settings;
    }

