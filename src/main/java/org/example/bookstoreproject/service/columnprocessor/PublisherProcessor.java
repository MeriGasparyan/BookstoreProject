package org.example.bookstoreproject.service.columnprocessor;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Publisher;
import org.example.bookstoreproject.persistance.repository.PublisherRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RequiredArgsConstructor
public class PublisherProcessor {

    private final PublisherRepository publisherRepository;

    @Transactional
    public Map<String, Publisher> process(List<CSVRow> data) {
        Map<String, Publisher> existingPublisherMap = new ConcurrentHashMap<>();
        List<Publisher> newPublishersToSave = new CopyOnWriteArrayList<>();

        List<Publisher> publisherList = publisherRepository.findAll();
        publisherList.forEach(publisher -> existingPublisherMap.put(publisher.getName(), publisher));

        data.parallelStream().forEach(row -> {
            if (!row.getPublisher().isEmpty()) {
                String publisherName = row.getPublisher().trim();
                Publisher publisher = existingPublisherMap.computeIfAbsent(publisherName, k -> {
                    Publisher newPublisher = new Publisher(publisherName);
                    newPublishersToSave.add(newPublisher);
                    return newPublisher;
                });
            }
        });

        if (!newPublishersToSave.isEmpty()) {
            publisherRepository.saveAll(newPublishersToSave);
        }
        return existingPublisherMap;
    }
}