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

        public boolean isEmpty() {
                return (title == null || title.isBlank()) &&
                        (authors == null || authors.isEmpty()) &&
                        (genres == null || genres.isEmpty()) &&
                        (language == null || language.isBlank()) &&
                        (publishers == null || publishers.isEmpty()) &&
                        (series == null || series.isEmpty()) &&
                        (awards == null || awards.isEmpty()) &&
                        (characters == null || characters.isEmpty()) &&
                        (settings == null || settings.isEmpty());
        }
    }

