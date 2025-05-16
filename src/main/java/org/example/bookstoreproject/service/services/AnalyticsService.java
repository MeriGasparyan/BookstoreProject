package org.example.bookstoreproject.service.services;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.persistance.repository.OrderItemRepository;
import org.example.bookstoreproject.service.dto.MostBoughtBookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final OrderItemRepository orderItemRepository;

    public Page<MostBoughtBookDTO> getMostBoughtBooks(int page, int size) {
        return orderItemRepository.findMostBoughtBooks(PageRequest.of(page, size));
    }
}