package org.example.bookstoreproject.service.columnprocessor;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Publisher;
import org.example.bookstoreproject.persistance.repository.PublisherRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class PublisherProcessor{
    private final PublisherRepository publisherRepository;

    public Map<String, Publisher> process(List<CSVRow> data) {
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
        return existingPublisherMap;
    }
}
