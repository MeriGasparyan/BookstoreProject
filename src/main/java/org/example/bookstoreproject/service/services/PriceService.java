package org.example.bookstoreproject.service.services;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.exception.ResourceNotFoundException;
import org.example.bookstoreproject.persistance.entity.Book;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.service.dto.BookDTO;
import org.example.bookstoreproject.service.dto.BookPriceUpdateDTO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceService {
    private final BookRepository bookRepository;

    public BookDTO setPriceInformation(Long id, BookPriceUpdateDTO bookPriceUpdateDTO) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Book not found with id: " + id));

        if(bookPriceUpdateDTO.getPrice() != null) {
            book.setPrice(bookPriceUpdateDTO.getPrice());
        }
        if(bookPriceUpdateDTO.getDiscount() != null) {
            book.setDiscount(bookPriceUpdateDTO.getDiscount());
        }
        bookRepository.save(book);
        return BookDTO.fromEntity(book);
    }
}
