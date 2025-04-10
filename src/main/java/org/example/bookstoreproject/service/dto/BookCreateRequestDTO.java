package org.example.bookstoreproject.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCreateRequestDTO {
    private String title;
    private String bookID;
    private String language;
    private String isbn;
    private String format;
    private Integer pages;
    private Float price;
    private String publishDate;
    private String firstPublishDate;
    private String publisherName;
    private String seriesTitle;
}