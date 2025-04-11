package org.example.bookstoreproject.service.format;

import org.example.bookstoreproject.enums.RatingStarNumber;
import org.example.bookstoreproject.service.utility.ArrayStringParser;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RatingByStarFormatter {

    private final LongFormatter longFormatter = new LongFormatter();

    public Map<RatingStarNumber, Long> formatRatingsByStar(String ratings) {
        System.out.println(ratings);
        if (ratings == null || ratings.isEmpty()) {
            return null;
        }

        String[] ratingsArray = ArrayStringParser.getArrElements(ratings);
        if (ratingsArray == null || ratingsArray.length == 0) {
            return null;
        }
        int numberOfStars = Math.min(ratingsArray.length, RatingStarNumber.values().length);
        int j = RatingStarNumber.values().length-1;
        Map<RatingStarNumber, Long> ratingsByStar = new HashMap<>();
        int i = 0;
            while (i < numberOfStars) {
                RatingStarNumber starNumber = RatingStarNumber.values()[j];
                String ratingValueString = ratingsArray[i].trim().replaceAll("'", ""); // Remove single quotes
                Long ratingValue = longFormatter.getLong(ratingValueString);
                ratingsByStar.put(starNumber, ratingValue);
                i++;
                j--;
            }

            return ratingsByStar;
        }
}
