package org.example.bookstoreproject.service.columnprocessor;

import lombok.Getter;
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
        Map<String, Award> existingAwardsMap = new HashMap<>();
        List<Award> awardList = awardRepository.findAll();
        for (Award award : awardList) {
            existingAwardsMap.put(award.getTitle(), award);
        }

        List<Award> newAwardsToSave = new ArrayList<>();
        for (CSVRow row : data) {
            List<Award> awardsForBook = new ArrayList<>();
            String[] awardArr = ArrayStringParser.getArrElements(row.getAwards());
            if (awardArr == null) continue;

            for (String awardTitle : awardArr) {
                Award award = existingAwardsMap.get(awardTitle);
                if (award == null) {
                    award = new Award(awardTitle);
                    newAwardsToSave.add(award);
                    existingAwardsMap.put(awardTitle, award);
                }
                awardsForBook.add(award);
            }
            awardBookMap.put(row.getBookID().trim(), awardsForBook);
        }
        if (!newAwardsToSave.isEmpty()) {
            awardRepository.saveAll(newAwardsToSave);
            newAwardsToSave.clear();
        }
    }
}
