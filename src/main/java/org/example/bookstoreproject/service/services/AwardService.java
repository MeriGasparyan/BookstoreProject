package org.example.bookstoreproject.service.services;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Award;
import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.persistance.entry.BookAward;
import org.example.bookstoreproject.persistance.repository.AwardRepository;
import org.example.bookstoreproject.persistance.repository.BookAwardRepository;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.service.dto.CreateAwardDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AwardService {
    private final AwardRepository awardRepository;
    private final BookRepository bookRepository;
    private final BookAwardRepository bookAwardRepository;

    @Transactional
    public Award createAward(CreateAwardDTO awardDTO) {
        String title = awardDTO.getTitle();
        if (awardRepository.findByTitle(title).isPresent()) {
            throw new IllegalArgumentException("Award with name '" + title + "' already exists");
        }
        Award award = new Award(title);
        return awardRepository.save(award);
    }

    @Transactional
    public Book addAwardsToBook(Long bookId, List<Long> awardIds) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book not found"));
        List<Award> awards = awardRepository.findAllById(awardIds);

        if (awards.size() != awardIds.size()) {
            Set<Long> foundAwardIds = awards.stream()
                    .map(Award::getId)
                    .collect(Collectors.toSet());

            List<Long> missingIds = awardIds.stream()
                    .filter(id -> !foundAwardIds.contains(id))
                    .toList();

            throw new NoSuchElementException("Award(s) with ID(s) " + missingIds + " not found");
        }

        awards.forEach(award -> {
            if (bookAwardRepository.findByBookAndAward(book, award).isEmpty()) {
                book.addBookAward(new BookAward(book, award));
            }
        });
        return bookRepository.save(book);
    }

    @Transactional
    public Book removeAwardsFromBook(Long bookId, List<Long> awardIds) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book with ID " + bookId + " not found"));

        List<Award> awards = awardRepository.findAllById(awardIds);
        if (awards.size() != awardIds.size()) {
            Set<Long> foundAwardIds = awards.stream()
                    .map(Award::getId)
                    .collect(Collectors.toSet());

            List<Long> missingIds = awardIds.stream()
                    .filter(id -> !foundAwardIds.contains(id))
                    .toList();

            throw new NoSuchElementException("Award(s) with ID(s) " + missingIds + " not found");
        }

        awards.forEach(award -> {
            bookAwardRepository.findByBookAndAward(book, award)
                    .ifPresentOrElse(
                            bookAward -> {
                                book.getBookAwards().remove(bookAward);
                                bookAwardRepository.delete(bookAward);
                                bookAward.setBook(null);
                                bookAward.setAward(null);
                            },
                            () -> {
                                throw new IllegalStateException(
                                        "Award with ID " + award.getId() +
                                                " is not associated with book " + bookId);
                            }
                    );
        });

        return bookRepository.save(book);
    }
}