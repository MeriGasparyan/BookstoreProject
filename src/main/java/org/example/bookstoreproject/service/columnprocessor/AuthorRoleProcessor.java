package org.example.bookstoreproject.service.columnprocessor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.example.bookstoreproject.enums.Role;
import org.example.bookstoreproject.persistance.entry.Author;
import org.example.bookstoreproject.persistance.entry.AuthorRole;
import org.example.bookstoreproject.persistance.entry.RoleEntity;
import org.example.bookstoreproject.persistance.repository.AuthorRepository;
import org.example.bookstoreproject.persistance.repository.AuthorRoleRepository;
import org.example.bookstoreproject.persistance.repository.RoleRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.format.AuthorFormatter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RequiredArgsConstructor
public class AuthorRoleProcessor {

    private final RoleRepository roleRepository;
    private final AuthorRoleRepository authorRoleRepository;

    @Transactional // Ensure database operations are transactional
    public void process(List<CSVRow> data, Map<String, Author> existingAuthorMap) {
        // Use thread-safe collections
        Map<String, RoleEntity> existingRoleMap = new ConcurrentHashMap<>();
        Set<Pair<Long, Long>> existingAuthorRoleSet = new ConcurrentSkipListSet<>(); // Thread-safe Set
        List<AuthorRole> newAuthorRolesToSave = new CopyOnWriteArrayList<>();

        // Load data once at the beginning
        List<RoleEntity> roleList = roleRepository.findAll();
        roleList.forEach(role -> existingRoleMap.put(role.getRoleName(), role));

        List<AuthorRole> authorRoleList = authorRoleRepository.findAll();
        authorRoleList.forEach(authorRole -> existingAuthorRoleSet.add(Pair.of(authorRole.getAuthor().getId(), authorRole.getRole().getId())));

        data.parallelStream().forEach(row -> {
            if (!row.getAuthor().isEmpty()) {
                Map<String, List<Role>> formattedAuthors = new AuthorFormatter().formatAuthor(row.getAuthor().trim());

                formattedAuthors.forEach((authorName, roles) -> {
                    Author author = existingAuthorMap.get(authorName);
                    if (author == null) {
                        return; // Skip if author is not found
                    }

                    roles.forEach(roleEnum -> {
                        RoleEntity roleEntity = existingRoleMap.get(roleEnum.name());
                        if (roleEntity == null) {
                            return; // Skip if role is not found
                        }

                        Pair<Long, Long> pair = Pair.of(author.getId(), roleEntity.getId());
                        // Atomic operation to add if absent
                        if (existingAuthorRoleSet.add(pair)) {
                            AuthorRole authorRole = new AuthorRole(author, roleEntity);
                            newAuthorRolesToSave.add(authorRole); // Add to thread-safe list
                        }
                    });
                });
            }
        });

        // Save new author roles outside the stream
        if (!newAuthorRolesToSave.isEmpty()) {
            authorRoleRepository.saveAll(newAuthorRolesToSave);
        }
    }
}