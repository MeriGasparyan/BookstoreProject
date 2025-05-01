package org.example.bookstoreproject.service.utility;

import org.example.bookstoreproject.enums.RatingStarNumber;
import org.example.bookstoreproject.persistance.entity.BookRatingStar;

import java.util.List;


public class RatingCalculator {
    public static float calculateAverageRating(List<BookRatingStar> ratingStarsList) {
        if (ratingStarsList == null || ratingStarsList.isEmpty()) {
            return 0f;
        }

        long totalRatings = 0;
        long weightedSum = 0;

        for (BookRatingStar ratingStar : ratingStarsList) {
            if (ratingStar != null && ratingStar.getStar() != null) {
                long count = ratingStar.getNumRating();
                int starValue = ratingStar.getStar().getLevel().getValue();
                weightedSum += starValue * count;
                totalRatings += count;
            }
        }

        return totalRatings > 0 ? (float) weightedSum / totalRatings : 0f;
    }

    public static long calculateTotalRatingCount(List<BookRatingStar> ratingStarsList) {
        if (ratingStarsList == null || ratingStarsList.isEmpty()) {
            return 0L;
        }

        return ratingStarsList.stream()
                .filter(r -> r != null && r.getStar() != null)
                .mapToLong(BookRatingStar::getNumRating)
                .sum();
    }

    public static int calculateLikedPercentage(List<BookRatingStar> ratingStarsList) {
        if (ratingStarsList == null || ratingStarsList.isEmpty()) {
            return 0;
        }

        long likedCount = 0;
        long totalCount = 0;

        for (BookRatingStar ratingStar : ratingStarsList) {
            if (ratingStar != null && ratingStar.getStar() != null) {
                long count = ratingStar.getNumRating();
                RatingStarNumber level = ratingStar.getStar().getLevel();
                totalCount += count;

                if (level == RatingStarNumber.FIVE_STAR || level == RatingStarNumber.FOUR_STAR) {
                    likedCount += count;
                }
            }
        }

        return totalCount > 0 ? (int) ((likedCount * 100) / totalCount) : 0;
    }
}