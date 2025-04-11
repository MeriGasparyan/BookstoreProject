package org.example.bookstoreproject.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCreateRequestDTO {
    private String title;
    private String bookID;
    private String series;
    private String author;
    private String rating;
    private String description;
    private String language;
    private String isbn;
    private String genres;
    private String characters;
    private String format;
    private String edition;
    private String pages;
    private String publisher;
    private String publishDate;
    private String firstPublishDate;
    private String awards;
    private String ratingsByStar;
    private String settings;
    private String bbeScore;
    private String bbeVotes;
    private String price;
}