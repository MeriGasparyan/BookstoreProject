package org.example.bookstoreproject.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.persistance.repository.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private String title;
    private LanguageDTO language;
    private String isbn;
    private FormatDTO format;
    private Integer pages;
    private Float price;
    private Date publishDate;
    private Date firstPublishDate;
    private String publisherName;
    private String seriesTitle;
    private List<String> authors;
    private List<String> awards;
    private List<String> characters;
    private List<String> genres;
    private List<String> settings;

    public static BookDTO fromEntity(Book book, BookAuthorRepository bookAuthorRepository,
                                     BookAwardRepository bookAwardRepository,
                                     BookCharacterRepository bookCharacterRepository,
                                     BookGenreRepository bookGenreRepository,
                                     BookSettingRepository bookSettingRepository) {
        BookDTO dto = new BookDTO();
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setPages(book.getPages());
        dto.setPrice(book.getPrice());
        dto.setPublishDate(book.getPublishDate());
        dto.setFirstPublishDate(book.getFirstPublishDate());

        if (book.getLanguage() != null) {
            dto.setLanguage(new LanguageDTO(book.getLanguage().getLanguage()));
        }
        if (book.getFormat() != null) {
            dto.setFormat(new FormatDTO(book.getFormat().getFormat()));
        }
        if (book.getPublisher() != null) {
            dto.setPublisherName(book.getPublisher().getName());
        }
        if (book.getSeries() != null) {
            dto.setSeriesTitle(book.getSeries().getTitle());
        }

        dto.setAuthors(bookAuthorRepository.findByBook(book).stream()
                .map(ba -> ba.getAuthor().getName())
                .collect(Collectors.toList()));

        dto.setAwards(bookAwardRepository.findByBook(book).stream()
                .map(ba -> ba.getAward().getTitle())
                .collect(Collectors.toList()));

        dto.setCharacters(bookCharacterRepository.findByBook(book).stream()
                .map(bc -> bc.getCharacter().getName())
                .collect(Collectors.toList()));

        dto.setGenres(bookGenreRepository.findByBook(book).stream()
                .map(bg -> bg.getGenre().getName())
                .collect(Collectors.toList()));

        dto.setSettings(bookSettingRepository.findByBook(book).stream()
                .map(bs -> bs.getSetting().getName())
                .collect(Collectors.toList()));

        return dto;
    }
}
