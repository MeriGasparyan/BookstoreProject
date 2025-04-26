package org.example.bookstoreproject.service.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthorSearchCriteria extends SearchCriteria {
    private Long id;
    private String name;
}
