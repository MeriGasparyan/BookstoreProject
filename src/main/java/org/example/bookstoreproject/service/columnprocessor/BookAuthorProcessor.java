package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Author;
import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.persistance.entry.BookAuthor;
import org.example.bookstoreproject.persistance.repository.BookAuthorRepository;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
public class BookAuthorProcessor implements CSVColumnProcessor {

    private final BookAuthorRepository bookAuthorRepository;
    private final AuthorProcessor authorProcessor;
    private final BookRepository bookRepository;

    @Override
    public void process(List<CSVRow> data) {
        Map<String, List<Author>> authorBookMap =
                authorProcessor.getAuthorBookMap();
        for(Map.Entry<String, List<Author>> entry : authorBookMap.entrySet()){
            List<Author> authors = entry.getValue();
            Optional<Book> book =
                    bookRepository.findByBookID(entry.getKey().trim());
            if(book.isPresent()){
                for(Author author : authors){
                    BookAuthor bookAuthor = new BookAuthor(book.get(), author);
                    bookAuthorRepository.save(bookAuthor);
                }

            }
        }

    }
}
