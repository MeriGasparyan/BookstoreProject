package org.example.bookstoreproject.service.criteria;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Data
public class SearchCriteria {
    private int size = 20;
    private int page = 0;
}
