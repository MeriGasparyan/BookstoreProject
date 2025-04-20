package org.example.bookstoreproject.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Author;
import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.persistance.repository.BookAuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorInformationDTO {
    Long id;
    private String name;
    private List<TitleBookDTO> books;

    public static AuthorInformationDTO fromEntity(Author author, BookAuthorRepository bookAuthorRepository) {
        AuthorInformationDTO dto = new AuthorInformationDTO();
        dto.setId(author.getId());
        dto.setName(author.getName());
        List<Book> books = bookAuthorRepository.findByAuthor(author);
        if (!books.isEmpty()) {
            dto.setBooks(books.stream().map(TitleBookDTO::fromEntity).collect(Collectors.toList()));
        }
        return dto;
    }
}
