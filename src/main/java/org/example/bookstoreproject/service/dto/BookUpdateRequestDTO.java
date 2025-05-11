package org.example.bookstoreproject.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookstoreproject.enums.Format;
import org.example.bookstoreproject.enums.Language;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookUpdateRequestDTO {
    private String title;
    private Language language;
    private String isbn;
    private Format format;
    private Integer pages;
    private BigDecimal price;
    private Date publishDate;
    private Date firstPublishDate;
    private String publisherName;
    private String seriesTitle;
}

