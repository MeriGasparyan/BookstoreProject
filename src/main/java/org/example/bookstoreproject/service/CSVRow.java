package org.example.bookstoreproject.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CSVRow {
    private String title;
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
    private String image;
    private String bbeScore;
    private String bbeVotes;
    private String price;

    public CSVRow(String[] values) {
        this.title = values[1];
        this.series = values[2];
        this.author = values[3];
        this.rating = values[4];
        this.description = values[5];
        this.language = values[6];
        this.isbn = values[7];
        this.genres = values[8];
        this.characters = values[9];
        this.format = values[10];
        this.edition = values[11];
        this.pages = values[12];
        this.publisher = values[13];
        this.publishDate = values[14];
        this.firstPublishDate = values[15];
        this.awards = values[16];
        this.ratingsByStar = values[18];
        this.settings = values[20];
        this.image = values[21];
        this.bbeScore = values[22];
        this.bbeVotes = values[23];
        this.price = values[24];
    }
}
