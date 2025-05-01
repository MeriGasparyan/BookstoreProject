package org.example.bookstoreproject.service.mapper;

import org.example.bookstoreproject.persistance.entity.Award;
import org.example.bookstoreproject.service.dto.AwardDTO;
import org.springframework.stereotype.Component;

@Component
public class AwardMapper {
    public Award mapDtoToEntity(AwardDTO dto) {
        return new Award(dto.getTitle());
    }
}
