package org.example.bookstoreproject.service.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@AllArgsConstructor
public class AuthorSearchCriteria extends SearchCriteria {
    private Long id;
    private String name;
}
