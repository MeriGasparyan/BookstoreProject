package org.example.bookstoreproject.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.RatingStarNumber;
import org.example.bookstoreproject.persistance.entry.Star;
import org.example.bookstoreproject.persistance.repository.StarRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StarSeeder {

    private final StarRepository starRepository;

    @PostConstruct
    public void seedStars() {
        for (RatingStarNumber rating : RatingStarNumber.values()) {
            String level = rating.name();

            starRepository.findByLevel(level).orElseGet(() -> {
                Star star = new Star(level);
                return starRepository.save(star);
            });
        }

        System.out.println("Star table populated from RatingNumber enum!");
    }
}
