package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.example.bookstoreproject.enums.RatingStarNumber;
import org.example.bookstoreproject.persistance.entry.*;
import org.example.bookstoreproject.persistance.repository.RatingRepository;
import org.example.bookstoreproject.persistance.repository.RatingStarRepository;
import org.example.bookstoreproject.persistance.repository.StarRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.format.RatingByStarFormatter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
@Order(11)
public class RatingStarProcessor implements CSVColumnProcessor {

    private final RatingStarRepository ratingStarRepository;
    private final StarRepository starRepository;
    private final RatingByStarFormatter ratingByStarFormatter;
    private final RatingRepository ratingRepository;

    @Override
    public void process(List<CSVRow> data) {
        Map<String, Star> starMap = new HashMap<>();
        Set<Pair<Long, String>> existingRatingStarPairs = new HashSet<>();
        Map<String, Rating> existingRatingsMap = new HashMap<>();
        List<Rating> existingRatings = ratingRepository.findAll();
        List<RatingStar> ratingStarsToSave = new ArrayList<>();
        List<RatingStar> existingRatingStars = ratingStarRepository.findAll();

        for (RatingStar rs : existingRatingStars) {
            existingRatingStarPairs.add(Pair.of(rs.getRating().getId(), rs.getStar().getLevel()));
        }
        for(Rating rating: existingRatings){
            existingRatingsMap.put(rating.getBook().getBookID(), rating);
        }
        for (CSVRow row : data) {
            try {
                Map<RatingStarNumber, Long> ratingsByStar = ratingByStarFormatter.formatRatingsByStar(row.getRatingsByStar());
                if (ratingsByStar == null) {
                    continue;
                }

                for (Map.Entry<RatingStarNumber, Long> entry : ratingsByStar.entrySet()) {
                    Star star = starMap.computeIfAbsent(entry.getKey().name(), starLevel ->
                            starRepository.findByLevel(starLevel).orElse(null));

                    if (star == null) {
                        System.out.println("No star found for level: " + entry.getKey().name());
                        continue;
                    }

                    Rating rating = existingRatingsMap.get(row.getBookID().trim());
                    Pair<Long, String> ratingStarPair = Pair.of(rating.getId(), star.getLevel());
                    if (!existingRatingStarPairs.contains(ratingStarPair)) {
                        RatingStar ratingStar = new RatingStar(rating, star, entry.getValue());
                        ratingStarsToSave.add(ratingStar);
                        existingRatingStarPairs.add(ratingStarPair);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error processing row for BookID: " + row.getBookID() + ". Error: " + e.getMessage());
            }
        }

        if (!ratingStarsToSave.isEmpty()) {
            ratingStarRepository.saveAll(ratingStarsToSave);
        }
    }
}
