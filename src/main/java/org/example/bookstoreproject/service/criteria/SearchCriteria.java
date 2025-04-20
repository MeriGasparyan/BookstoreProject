package org.example.bookstoreproject.service.criteria;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@Getter
@Setter
public class SearchCriteria {
    private Integer page = 0;
    private Integer size = 20;

    public Pageable toPageable() {
        return PageRequest.of(page, size);
    }
}
