package org.example.bookstoreproject.service.criteria;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AuthorSearchCriteria extends SearchCriteria{
    private Long id;
    private String name;
}
