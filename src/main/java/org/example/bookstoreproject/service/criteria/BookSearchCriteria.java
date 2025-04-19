package org.example.bookstoreproject.service.criteria;

import lombok.Getter;
import lombok.Setter;
import org.example.bookstoreproject.enums.Language;

import java.util.List;

@Getter
@Setter
public class BookSearchCriteria extends SearchCriteria {
    private String title;
    private List<Long> authorIds;
    private List<Long> genreIds;
    private Language language;
    private List<Long> publisherIds;
    private List<Long> seriesIds;
    private List<Long> awardIds;
    private List<Long> characterIds;
    private List<Long> settingIds;

}