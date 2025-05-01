package org.example.bookstoreproject.service.columnprocessor;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.persistance.entity.Award;
import org.example.bookstoreproject.persistance.repository.AwardRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.utility.ArrayStringParser;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RequiredArgsConstructor
public class AwardProcessor {

    private final AwardRepository awardRepository;

    @Transactional
    public Map<String, List<Award>> process(List<CSVRow> data) {
        Map<String, List<Award>> awardBookMap = new ConcurrentHashMap<>();
        Map<String, Award> existingAwardsMap = new ConcurrentHashMap<>();
        List<Award> newAwardsToSave = new CopyOnWriteArrayList<>();

        List<Award> awardList = awardRepository.findAll();
        awardList.forEach(award -> existingAwardsMap.put(award.getTitle(), award));

        data.parallelStream().forEach(row -> {
            List<Award> awardsForBook = new CopyOnWriteArrayList<>();
            String[] awardArr = ArrayStringParser.getArrElements(row.getAwards());
            if (awardArr == null) return;

            for (String awardTitle : awardArr) {
                String trimmedAwardTitle = awardTitle.trim();
                Award award = existingAwardsMap.computeIfAbsent(trimmedAwardTitle, k -> {
                    Award newAward = new Award(trimmedAwardTitle);
                    newAwardsToSave.add(newAward);
                    return newAward;
                });
                awardsForBook.add(award);
            }
            awardBookMap.compute(row.getBookID().trim(), (bookId, awardListForBook) -> {
                if (awardListForBook == null) {
                    awardListForBook = new CopyOnWriteArrayList<>();
                }
                awardListForBook.addAll(awardsForBook);
                return awardListForBook;
            });
        });

        if (!newAwardsToSave.isEmpty()) {
            awardRepository.saveAll(newAwardsToSave);
        }
        return awardBookMap;
    }
}