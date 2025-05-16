package org.example.bookstoreproject.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MostBoughtBookDTO {
    private Long bookId;
    private String title;
    private Long totalPurchases;
}
