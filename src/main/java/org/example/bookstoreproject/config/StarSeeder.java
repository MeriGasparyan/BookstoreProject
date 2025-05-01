package org.example.bookstoreproject.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.RatingStarNumber;
import org.example.bookstoreproject.persistance.entity.Star;
import org.example.bookstoreproject.persistance.repository.StarRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StarSeeder {

    private final StarRepository starRepository;

    @PostConstruct
    public void seedStars() {
        for (RatingStarNumber rating : RatingStarNumber.values()) {

            starRepository.findByLevel(rating).orElseGet(() -> {
                Star star = new Star(rating);
                return starRepository.save(star);
            });
        }

        System.out.println("Star table populated from RatingNumber enum!");
    }
}
