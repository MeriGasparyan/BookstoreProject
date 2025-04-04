package org.example.bookstoreproject.service.impl;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Award;
import org.example.bookstoreproject.persistance.repository.AwardRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.dto.AwardDTO;
import org.example.bookstoreproject.service.mapper.AwardMapper;
import org.example.bookstoreproject.service.utility.ArrayStringParser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class AwardProcessor implements CSVColumnProcessor {
    private final AwardRepository awardRepository;
    private final AwardMapper awardMapper;


    @Override
    public void process(List<CSVRow> data) {
        for (CSVRow row : data) {
            String[] awardArr = ArrayStringParser.getArrElements(row.getAwards());
            if (awardArr == null)
                continue;
            for (String award : awardArr) {
                AwardDTO awardDTO = new AwardDTO(award);
                Optional<Award> existing = awardRepository.findByTitle(awardDTO.getTitle());
                if (existing.isEmpty()) {
                    Award awardEntity = awardMapper.mapDtoToEntity(awardDTO);
                    awardRepository.save(awardEntity);
                }
            }

        }
    }
}
