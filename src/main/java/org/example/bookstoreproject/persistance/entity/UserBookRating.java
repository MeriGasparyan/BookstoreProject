package org.example.bookstoreproject.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.bookstoreproject.enums.ReviewStatus;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "user_book_rating")
public class UserBookRating {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_book_rating_id_seq")
    @SequenceGenerator(
            name = "user_book_rating_id_seq",
            sequenceName = "user_book_rating_id_seq",
            allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "star_id", nullable = false)
    @Enumerated(EnumType.STRING)
    private Star star;

    @Column(name = "review")
    private String review;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_status", nullable = false)
    private ReviewStatus reviewStatus = ReviewStatus.UNCHECKED;

    @Column(name = "sentiment_result")
    private String sentimentResult;

    @Column(name = "contains_offensive_words")
    private Boolean containsOffensiveWords;


}
