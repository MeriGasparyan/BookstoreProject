package org.example.bookstoreproject.service.services;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.RatingStarNumber;
import org.example.bookstoreproject.enums.UserRoleName;
import org.example.bookstoreproject.exception.ResourceAlreadyUsedException;
import org.example.bookstoreproject.persistance.entity.*;
import org.example.bookstoreproject.persistance.repository.*;
import org.example.bookstoreproject.service.dto.CreateUserReturnDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ArtificialDataService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository roleRepository;
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final UserBookRatingRepository ratingRepository;
    private final StarRepository starRepository;
    private final RatingStarRepository bookRatingStarRepository;

    private static final String GOOD_REVIEWS_FILE = "good_reviews.txt";
    private static final String BAD_REVIEWS_FILE = "bad_reviews.txt";
    private static final int TOTAL_REVIEWS = 1000;
    private static final double GOOD_REVIEW_RATIO = 0.75;


    @Transactional
    public CreateUserReturnDTO setUpAdmin() {
        if (userRepository.findByEmail("admin@bookstore.com").isPresent()) {
            throw new ResourceAlreadyUsedException("Default Admin already exists");
        }

        UserRole adminRole = roleRepository.findByName(UserRoleName.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Admin role not found"));

        User admin = new User();
        admin.setFirstname("default");
        admin.setLastname("admin");
        admin.setEmail("admin@bookstore.com");
        admin.setPassword(passwordEncoder.encode("1234567"));
        admin.setEnabled(true);
        admin.setRole(adminRole);

        userRepository.save(admin);
        return CreateUserReturnDTO.fromEntity(admin);
    }

    @Transactional
    public void generateFakeUsers(int count) {
        Faker faker = new Faker();
        Random random = new Random();
        List<UserRole> existingUserRoles = roleRepository.findAll();
        Set<String> existingUsers = userRepository.findAllUsernames();
        List<User> newUsers = new ArrayList<>();
        List<Cart> newCarts = new ArrayList<>();

        Map<UserRoleName, UserRole> existingUserRolesMap = new HashMap<>();
        for (UserRole userRole : existingUserRoles) {
            existingUserRolesMap.put(userRole.getName(), userRole);
        }
        Map<UserRoleName, Integer> roleDistribution = new LinkedHashMap<>();
        roleDistribution.put(UserRoleName.ROLE_USER, (int) (count * 0.80));
        roleDistribution.put(UserRoleName.ROLE_LIBRARIAN, (int) (count * 0.05));
        roleDistribution.put(UserRoleName.ROLE_REVIEWER, (int) (count * 0.06));
        roleDistribution.put(UserRoleName.ROLE_SELLER, (int) (count * 0.02));
        roleDistribution.put(UserRoleName.ROLE_ANALYST, (int) (count * 0.02));
        roleDistribution.put(UserRoleName.ROLE_MODERATOR, (int) (count * 0.02));
        roleDistribution.put(UserRoleName.ROLE_ADMIN, count - roleDistribution.values().stream().mapToInt(i -> i).sum());

        for (Map.Entry<UserRoleName, Integer> entry : roleDistribution.entrySet()) {
            UserRoleName roleName = entry.getKey();
            int roleCount = entry.getValue();

            UserRole role = existingUserRolesMap.get(roleName);
            if (role == null) {
                throw new RuntimeException("Role not found: " + roleName);
            }

            for (int i = 0; i < roleCount; i++) {
                String email = faker.internet().emailAddress();
                if (existingUsers.contains(email)) continue;

                User user = new User();
                user.setFirstname(faker.name().firstName());
                user.setLastname(faker.name().lastName());
                user.setEmail(email);
                user.setPassword(passwordEncoder.encode("password123"));
                user.setEnabled(true);
                user.setRole(role);
                existingUsers.add(email);
                newUsers.add(user);
            }
        }
        if (!newUsers.isEmpty()) {
            userRepository.saveAll(newUsers);
        }
        for (User user : newUsers) {
            Cart cart = new Cart();
            cart.setUser(user);
            newCarts.add(cart);
        }
        if (!newCarts.isEmpty()) {
            cartRepository.saveAll(newCarts);
        }
    }

    @Transactional
    public void seedRatings() {
        try {
            List<String> goodReviews = loadReviewsFromResources(GOOD_REVIEWS_FILE);
            List<String> badReviews = loadReviewsFromResources(BAD_REVIEWS_FILE);

            List<User> users = userRepository.findAll();
            List<Book> books = bookRepository.findAll();
            List<UserBookRating> bookRatingStars = ratingRepository.findAll();
            Set<Star> starValues = starRepository.findAllStars();

            List<UserBookRating> newUserBookRatings = new ArrayList<>();

            if (users.isEmpty() || books.isEmpty()) {
                System.out.println("No users or books available for seeding.");
                return;
            }

            Random random = new Random();
            Set<String> usedUserBookPairs = new HashSet<>();
            for (UserBookRating rating : bookRatingStars) {
                usedUserBookPairs.add(rating.getUser().getId() + ":" + rating.getBook().getId());
            }

            int goodCount = 0;
            int badCount = 0;

            for (int i = 0; i < TOTAL_REVIEWS; i++) {
                boolean isGood = (i < TOTAL_REVIEWS * GOOD_REVIEW_RATIO);

                String review = isGood
                        ? goodReviews.get(random.nextInt(goodReviews.size()))
                        : badReviews.get(random.nextInt(badReviews.size()));

                int stars = isGood
                        ? 3 + random.nextInt(3)  // 3 to 5
                        : 1 + random.nextInt(2); // 1 to 2

                RatingStarNumber ratingEnum = RatingStarNumber.fromInt(stars);

                Star star = null;
                for (Star starValue : starValues) {
                    if (starValue.getLevel() == ratingEnum) {
                        star = starValue;
                    }
                }
                if (star == null) {
                    throw new NoSuchElementException("Star not found");
                }

                User user = users.get(random.nextInt(users.size()));
                Book book = books.get(random.nextInt(books.size()));
                String key = user.getId() + ":" + book.getId();

                usedUserBookPairs.add(key);
                UserBookRating userBookRating;
                try {
                    userBookRating = createUserBookRating(user, book, star, review);
                    newUserBookRatings.add(userBookRating);
                    if (isGood) goodCount++;
                    else badCount++;
                } catch (Exception e) {
                    System.out.println("Failed to rate: " + e.getMessage());
                }
            }
            if (!newUserBookRatings.isEmpty()) {
                ratingRepository.saveAll(newUserBookRatings);
            }
            System.out.println("Seeding complete: " + goodCount + " good, " + badCount + " bad reviews.");

        } catch (Exception e) {
            System.err.println("Failed to load reviews: " + e.getMessage());
        }


    }

    private UserBookRating createUserBookRating(User user, Book book, Star star, String review) {
        UserBookRating userBookRating = new UserBookRating();

        userBookRating.setUser(user);
        userBookRating.setBook(book);
        userBookRating.setStar(star);
        userBookRating.setReview(review);
        return userBookRating;
    }

    private List<String> loadReviewsFromResources(String filename) throws Exception {
        ClassPathResource resource = new ClassPathResource(filename);
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .toList();
        }
    }
}
