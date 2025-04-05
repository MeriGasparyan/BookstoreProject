package org.example.bookstoreproject.service.mapper;

import org.example.bookstoreproject.persistance.entry.Publisher;
import org.example.bookstoreproject.service.dto.PublisherDTO;
import org.springframework.stereotype.Component;

@Component
public class PublisherMapper {
    public Publisher mapDtoToEntity(PublisherDTO dto) {
        return new Publisher(dto.getName());
    }
}
