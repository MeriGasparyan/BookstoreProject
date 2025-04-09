package org.example.bookstoreproject.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.bookstoreproject.persistance.entry.FormatEntity;
import org.example.bookstoreproject.persistance.entry.LanguageEntity;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class BookDTO {
    private String title;
    private LanguageEntity language;
    private String isbn;
    private FormatEntity format;
    private Integer pages;
    private Float price;
    private Date publishDate;
    private Date firstPublishDate;
}
