package org.example.bookstoreproject.service.criteria;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.bookstoreproject.enums.Language;

import java.util.List;

@Getter
@Setter
@Data
public class BookSearchCriteria extends SearchCriteria {
    private String title;
    private List<Long> authors;
    private List<Long> genres;
    private Language language;
    private List<Long> publishers;
    private List<Long> series;
    private List<Long> awards;
    private List<Long> characters;
    private List<Long> settings;

}