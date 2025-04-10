package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Publisher;
import org.example.bookstoreproject.persistance.repository.PublisherRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
@Order(6)
public class PublisherProcessor implements CSVColumnProcessor {
    private final PublisherRepository publisherRepository;

    @Override
    public void process(List<CSVRow> data) {
        Map<String, Publisher> existingPublisherMap = new HashMap<>();
        List<Publisher> publisherList = publisherRepository.findAll();
        for (Publisher publisher : publisherList) {
            existingPublisherMap.put(publisher.getName(), publisher);
        }

        List<Publisher> newPublishersToSave = new ArrayList<>();
        for (CSVRow row : data) {
            if (!row.getPublisher().isEmpty()) {
                String publisherName = row.getPublisher().trim();
                Publisher publisher = existingPublisherMap.get(publisherName);
                if (publisher == null) {
                    publisher = new Publisher(publisherName);
                    existingPublisherMap.put(publisherName, publisher);
                    newPublishersToSave.add(publisher);
                }
            }
        }
        if (!newPublishersToSave.isEmpty()) {
            publisherRepository.saveAll(newPublishersToSave);
        }
    }
}
