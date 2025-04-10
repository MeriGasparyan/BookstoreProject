package org.example.bookstoreproject.service.columnprocessor;

import lombok.Getter;
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

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

@Component
@Order(1)
public class AuthorProcessor implements CSVColumnProcessor {

    private final AuthorRepository authorRepository;
    private final RoleRepository roleRepository;
    private final AuthorRoleRepository authorRoleRepository;
    private final AuthorFormatter authorFormatter;

    public AuthorProcessor(AuthorRepository authorRepository, RoleRepository roleRepository, AuthorRoleRepository authorRoleRepository) {
        this.authorRepository = authorRepository;
        this.roleRepository = roleRepository;
        this.authorRoleRepository = authorRoleRepository;
        this.authorFormatter = new AuthorFormatter();
        this.authorBookMap = new HashMap<>();
    }

    @Getter
    private final Map<String, List<Author>> authorBookMap;

    @Override
    public void process(List<CSVRow> data) {
        Map<String, Author> existingAuthorMap = new HashMap<>();
        Map<String, RoleEntity> existingRoleMap = new HashMap<>();

        List<Author> authorList = authorRepository.findAll();
        List<AuthorRole> authorRoleList = authorRoleRepository.findAll();
        List<RoleEntity> roleList = roleRepository.findAll();

        for (Author author : authorList) {
            existingAuthorMap.put(author.getName(), author);
        }
        for (RoleEntity role : roleList) {
            existingRoleMap.put(role.getRoleName(), role);
        }

        Set<Pair<Long, Long>> existingAuthorRoleSet = new HashSet<>();
        for (AuthorRole authorRole : authorRoleList) {
            Long authorId = authorRole.getAuthor().getId();
            Long roleId = authorRole.getRole().getId();
            existingAuthorRoleSet.add(Pair.of(authorId, roleId));
        }

        List<Author> newAuthorsToSave = new ArrayList<>();
        List<AuthorRole> newAuthorRolesToSave = new ArrayList<>();

        for (CSVRow row : data) {
            if (!row.getAuthor().isEmpty()) {
                List<Author> authors = new ArrayList<>();
                Map<String, Author> newAuthors = new HashMap<>();
                Map<String, List<Role>> formattedAuthors = authorFormatter.formatAuthor(row.getAuthor().trim());

                for (Map.Entry<String, List<Role>> entry : formattedAuthors.entrySet()) {
                    String name = entry.getKey();
                    List<Role> roles = entry.getValue();

                    Author existingAuthor = existingAuthorMap.get(name);
                    if (existingAuthor != null || newAuthors.containsKey(name)) {
                        continue;
                    }

                    Author author = new Author(name);
                    newAuthors.put(name, author);
                    authors.add(author);
                    newAuthorsToSave.add(author);

                    for (Role roleEnum : roles) {
                        RoleEntity roleEntity = existingRoleMap.get(roleEnum.name());
                        if (roleEntity == null) continue;

                        Long authorId = author.getId();
                        Long roleId = roleEntity.getId();

                        Pair<Long, Long> pair = Pair.of(authorId, roleId);
                        if (!existingAuthorRoleSet.contains(pair)) {
                            AuthorRole authorRole = new AuthorRole(author, roleEntity);
                            newAuthorRolesToSave.add(authorRole);
                            existingAuthorRoleSet.add(pair);
                        }
                    }
                }

                authorBookMap.put(row.getBookID().trim(), authors);
            }
        }

        if (!newAuthorsToSave.isEmpty()) {
            authorRepository.saveAll(newAuthorsToSave);
        }

        if (!newAuthorRolesToSave.isEmpty()) {
            authorRoleRepository.saveAll(newAuthorRolesToSave);
        }
    }

}
