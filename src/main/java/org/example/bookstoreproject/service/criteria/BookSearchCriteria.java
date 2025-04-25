package org.example.bookstoreproject.service.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.bookstoreproject.enums.Language;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BookSearchCriteria extends SearchCriteria {
    private String title;
    private List<Long> authors;
    private List<Long> genres;
    private Language language;
    private Long publisher;
    private Long series;
    private List<Long> awards;
    private List<Long> characters;
    private List<Long> settings;

}