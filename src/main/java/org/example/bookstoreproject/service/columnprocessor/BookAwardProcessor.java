package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.*;
import org.example.bookstoreproject.persistance.repository.BookAwardRepository;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
@Component
@AllArgsConstructor
public class BookAwardProcessor implements CSVColumnProcessor{
    private final BookAwardRepository bookAwardRepository;
    private final AwardProcessor awardProcessor;
    private final BookRepository bookRepository;

    @Override
    public void process(List<CSVRow> data) {
        Map<String, List<Award>> awardBookMap =
                awardProcessor.getAwardBookMap();
        for(Map.Entry<String, List<Award>> entry : awardBookMap.entrySet()){
            List<Award> awards = entry.getValue();
            Optional<Book> book =
                    bookRepository.findByBookID(entry.getKey().trim());
            if(book.isPresent()){
                for(Award award : awards){
                    if(bookAwardRepository.existsByBookAndAward(book.get(), award)){
                        continue;
                    }
                    BookAward bookAward = new BookAward(book.get(), award);
                    bookAwardRepository.save(bookAward);
                }

            }
        }

    }
}
