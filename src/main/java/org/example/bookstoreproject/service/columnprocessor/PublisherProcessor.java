package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Publisher;
import org.example.bookstoreproject.persistance.repository.PublisherRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
@Order(6)
public class PublisherProcessor implements CSVColumnProcessor {
    private final PublisherRepository publisherRepository;
    @Override
    public void process(List<CSVRow> data) {
        for (CSVRow row : data) {
            if (!row.getPublisher().isEmpty()) {
                String publisher = row.getPublisher().trim();
                Optional<Publisher> existing = publisherRepository.findByName(publisher);
                if (existing.isEmpty()) {
                    Publisher publisherEntity = new Publisher(publisher);
                    publisherRepository.save(publisherEntity);
                }
            }
        }
    }
}
