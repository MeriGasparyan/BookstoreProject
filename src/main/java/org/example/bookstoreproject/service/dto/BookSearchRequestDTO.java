package org.example.bookstoreproject.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookSearchRequestDTO {
    private String title;
    private String author;
    private String genre;
    private String language;
    private String publisher;
    private String series;
    private String setting;
    private String award;
    private String character;
}
