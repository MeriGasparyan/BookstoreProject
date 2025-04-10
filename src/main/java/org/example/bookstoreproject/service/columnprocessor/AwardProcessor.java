package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.bookstoreproject.persistance.entry.Author;
import org.example.bookstoreproject.persistance.entry.Award;
import org.example.bookstoreproject.persistance.repository.AwardRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.utility.ArrayStringParser;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Order(3)
public class AwardProcessor implements CSVColumnProcessor {
    private final AwardRepository awardRepository;
    @Getter
    private final Map<String, List<Award>> awardBookMap;

    public AwardProcessor(AwardRepository awardRepository) {
        this.awardRepository = awardRepository;
        this.awardBookMap = new HashMap<>();
    }

    @Override
    public void process(List<CSVRow> data) {
        for (CSVRow row : data) {
            List<Award> awards = new ArrayList<>();
            String[] awardArr = ArrayStringParser.getArrElements(row.getAwards());
            if (awardArr == null)
                continue;
            for (String award : awardArr) {
                Optional<Award> existing = awardRepository.findByTitle(award);
                if (existing.isEmpty()) {
                    Award awardEntity = new Award(award);
                    awards.add(awardEntity);
                    awardRepository.save(awardEntity);
                }
            }
            awardBookMap.put(row.getBookID().trim(), awards);
        }
    }
}
