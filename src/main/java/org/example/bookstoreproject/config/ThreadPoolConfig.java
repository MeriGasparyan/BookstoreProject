package org.example.bookstoreproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public ExecutorService csvProcessingExecutor() {
        return Executors.newFixedThreadPool(64); // or higher based on your needs
    }

}
