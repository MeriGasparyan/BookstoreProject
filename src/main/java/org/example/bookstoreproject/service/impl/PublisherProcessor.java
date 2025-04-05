package org.example.bookstoreproject.service.impl;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Publisher;
import org.example.bookstoreproject.persistance.repository.PublisherRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.dto.PublisherDTO;
import org.example.bookstoreproject.service.mapper.PublisherMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class PublisherProcessor implements CSVColumnProcessor {
    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;
    @Override
    public void process(List<CSVRow> data) {
        for (CSVRow row : data) {
            if (!row.getPublisher().isEmpty()) {
                PublisherDTO publisherDTO = new PublisherDTO(row.getPublisher().trim());
                Optional<Publisher> existing = publisherRepository.findByName(publisherDTO.getName());
                if (existing.isEmpty()) {
                    Publisher publisher = publisherMapper.mapDtoToEntity(publisherDTO);
                    publisherRepository.save(publisher);
                }
            }
        }
    }
}
