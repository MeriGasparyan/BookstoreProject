package org.example.bookstoreproject.service.columnprocessor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.example.bookstoreproject.persistance.entry.Author;
import org.example.bookstoreproject.persistance.entry.Award;
import org.example.bookstoreproject.persistance.repository.AwardRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.utility.ArrayStringParser;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class AwardProcessor{
    private final AwardRepository awardRepository;

    public Pair<Map<String, Award>, Map<String, List<Award>>> process(List<CSVRow> data) {
        Map<String, List<Award>> awardBookMap = new HashMap<>();
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
        return Pair.of(existingAwardsMap, awardBookMap);
    }
}
