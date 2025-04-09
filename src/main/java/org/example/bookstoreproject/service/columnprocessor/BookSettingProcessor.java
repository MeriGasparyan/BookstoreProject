package org.example.bookstoreproject.service.columnprocessor;

@Component
@AllArgsConstructor
public class BookSettingProcessor implements CSVColumnProcessor {
    private final BookGenreRepository bookGenreRepository;
    private final GenreProcessor genreProcessor;
    private final BookRepository bookRepository;

    @Override
    public void process(List<CSVRow> data) {
        Map<String, List<Genre>> genreBookMap =
                genreProcessor.getGenreBookMap();
        for(Map.Entry<String, List<Genre>> entry : genreBookMap.entrySet()){
            List<Genre> genres = entry.getValue();
            Optional<Book> book =
                    bookRepository.findByBookID(entry.getKey().trim());
            if(book.isPresent()){
                for(Genre genre : genres){
                    if(bookGenreRepository.existsByBookAndGenre(book.get(), genre)){
                        continue;
                    }
                    BookGenre bookGenre = new BookGenre(book.get(), genre);
                    bookGenreRepository.save(bookGenre);
                }

            }
        }

    }
}

