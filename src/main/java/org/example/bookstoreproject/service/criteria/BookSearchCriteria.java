package org.example.bookstoreproject.service.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.bookstoreproject.enums.Language;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
    private String sortCriteria;
    private Sort.Direction sortDirection;

    @Override
    public Pageable toPageable() {
        if (sortCriteria == null || sortDirection == null) {
            return PageRequest.of(this.getPage(), this.getSize(), Sort.by(Sort.Direction.DESC, "title"));
        }

        return PageRequest.of(this.getPage(), this.getSize(), Sort.by(sortDirection, sortCriteria));
    }

}