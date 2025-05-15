package org.example.bookstoreproject.service.services;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.service.dto.OffensiveReviewDTO;
import org.example.bookstoreproject.persistance.entity.UserBookRating;
import org.example.bookstoreproject.persistance.repository.UserBookRatingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OffensiveReviewService {

    private final UserBookRatingRepository ratingRepository;

    private StanfordCoreNLP pipeline;
    private Set<String> offensiveWords;

    @Value("${offensive.words}")
    private String path;

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

    public Page<OffensiveReviewDTO> getOffensiveReviews(int page, int size) {
        List<UserBookRating> ratings = ratingRepository.findAll();
        List<OffensiveReviewDTO> flagged = new ArrayList<>();

        for (UserBookRating rating : ratings) {
            String review = rating.getReview();
            if (review == null || review.isBlank()) continue;

            String sentiment = analyzeSentiment(review);
            boolean containsBadWords = containsOffensiveWord(review);

            if (containsBadWords || sentiment.equalsIgnoreCase("Very negative")) {
                OffensiveReviewDTO dto = new OffensiveReviewDTO();
                dto.setId(rating.getId());
                dto.setUserId(rating.getUser().getId());
                dto.setBookId(rating.getBook().getId());
                dto.setReview(review);
                dto.setSentiment(sentiment);
                dto.setContainsOffensiveWords(containsBadWords);
                flagged.add(dto);
            }
        }

        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, flagged.size());
        List<OffensiveReviewDTO> paged = flagged.subList(
                Math.min(fromIndex, flagged.size()),
                toIndex
        );

        return new PageImpl<>(paged, PageRequest.of(page, size), flagged.size());
    }

    private boolean containsOffensiveWord(String text) {
        String[] words = text.toLowerCase().split("\\W+");
        for (String word : words) {
            if (getOffensiveWords().contains(word)) return true;
        }
        return false;
    }

    private String analyzeSentiment(String text) {
        Annotation annotation = new Annotation(text);
        getPipeline().annotate(annotation);

        return annotation.get(CoreAnnotations.SentencesAnnotation.class)
                .stream()
                .map(sentence -> sentence.get(SentimentCoreAnnotations.SentimentClass.class))
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Neutral");
    }
}
