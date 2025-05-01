package org.example.bookstoreproject.service.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.bookstoreproject.persistance.entity.Book;

@Getter
@Setter
public class TitleBookDTO {
    private Long id;
    private String title;
    public static TitleBookDTO fromEntity(Book book) {
        TitleBookDTO titleBookDTO = new TitleBookDTO();
        titleBookDTO.setTitle(book.getTitle());
        titleBookDTO.setId(book.getId());
        return titleBookDTO;
    }
}
