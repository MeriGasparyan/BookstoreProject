package org.example.bookstoreproject.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "org.example.bookstoreproject")
@EnableJpaRepositories("org.example.bookstoreproject.persistance.repository")
@EntityScan("org.example.bookstoreproject.persistance.entry")
public class BookstoreProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreProjectApplication.class, args);
    }

}
