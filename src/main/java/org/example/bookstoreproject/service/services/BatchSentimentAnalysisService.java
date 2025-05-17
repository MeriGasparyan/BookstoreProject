package org.example.bookstoreproject.service.services;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.ReviewStatus;
import org.example.bookstoreproject.persistance.entity.UserBookRating;
import org.example.bookstoreproject.persistance.repository.UserBookRatingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BatchSentimentAnalysisService {
    private final UserBookRatingRepository ratingRepository;
    private StanfordCoreNLP pipeline;
    private Set<String> offensiveWords;

    @Value("${offensive.words}")
    private String path;

    @Value("${sentiment.batch.size:100}")
    private int batchSize;

    private static final Set<String> NEGATIVE_SENTIMENTS = Set.of(
            "Negative", "Very negative"
    );private static final Set<String> POSITIVE_PHRASES = Set.of(
            "couldn't put it down",
            "highly recommend",
            "must read",
            "loved it"
    );

    @Transactional
    public void analyzeAndClassifyReview(UserBookRating rating) {
        String review = rating.getReview().toLowerCase();

        if (containsOffensiveWord(review)) {
            rating.setReviewStatus(ReviewStatus.PENDING_REVIEW);
            rating.setContainsOffensiveWords(true);
            rating.setSentimentResult("Contains offensive language");
            ratingRepository.save(rating);
            return;
        }
        if (containsPositivePhrases(review)) {
            rating.setReviewStatus(ReviewStatus.AUTO_APPROVED);
            rating.setContainsOffensiveWords(false);
            rating.setSentimentResult("Positive (phrase match)");
            ratingRepository.save(rating);
            return;
        }

        String sentiment = analyzeSentiment(review);
        rating.setSentimentResult(sentiment);
        rating.setContainsOffensiveWords(false);

        if (NEGATIVE_SENTIMENTS.contains(sentiment)) {
            rating.setReviewStatus(ReviewStatus.PENDING_REVIEW);
        } else {
            rating.setReviewStatus(ReviewStatus.AUTO_APPROVED);
        }

        ratingRepository.save(rating);
    }

    private boolean containsPositivePhrases(String text) {
        return POSITIVE_PHRASES.stream()
                .anyMatch(phrase -> text.contains(phrase.toLowerCase()));
    }


    private StanfordCoreNLP getPipeline() {
        if (pipeline == null) {
            Properties props = new Properties();
            props.setProperty("annotators", "tokenize,ssplit,parse,sentiment");
            pipeline = new StanfordCoreNLP(props);
        }
        return pipeline;
    }

    private Set<String> getOffensiveWords() {
        if (offensiveWords == null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new ClassPathResource(path).getInputStream()))) {
                offensiveWords = reader.lines()
                        .map(String::toLowerCase)
                        .collect(Collectors.toSet());
            } catch (Exception e) {
                throw new RuntimeException("Could not load offensive words file", e);
            }
        }
        return offensiveWords;
    }

    @Scheduled(fixedRateString = "${sentiment.analysis.interval:3600000}")
    @Transactional
    public void processReviewsBatch() {
        ratingRepository
                .findByReviewIsNotNullAndReviewStatus(
                        ReviewStatus.UNCHECKED,
                        PageRequest.of(0, batchSize)
                )
                .forEach(this::analyzeAndClassifyReview);
    }


    private boolean containsOffensiveWord(String text) {
        if (text == null || text.isBlank()) return false;

        String[] words = text.toLowerCase().split("\\W+");
        Set<String> offensiveWordsSet = getOffensiveWords();

        return Arrays.stream(words)
                .anyMatch(offensiveWordsSet::contains);
    }

    private String analyzeSentiment(String text) {
        Annotation annotation = new Annotation(text);
        getPipeline().annotate(annotation);

        return annotation.get(CoreAnnotations.SentencesAnnotation.class)
                .stream()
                .map(sentence -> sentence.get(SentimentCoreAnnotations.SentimentClass.class))
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        sentiment -> sentiment,
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Neutral");
    }
}