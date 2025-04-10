package org.example.bookstoreproject.service.columnprocessor;

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
@Order(2)
public class AuthorRoleProcessor implements CSVColumnProcessor {

    private final AuthorRepository authorRepository;
    private final RoleRepository roleRepository;
    private final AuthorRoleRepository authorRoleRepository;

    public AuthorRoleProcessor(AuthorRepository authorRepository, RoleRepository roleRepository, AuthorRoleRepository authorRoleRepository) {
        this.authorRepository = authorRepository;
        this.roleRepository = roleRepository;
        this.authorRoleRepository = authorRoleRepository;
    }

    @Override
    public void process(List<CSVRow> data) {
        List<Author> authorList = authorRepository.findAll();
        List<RoleEntity> roleList = roleRepository.findAll();
        List<AuthorRole> authorRoleList = authorRoleRepository.findAll();

        Map<String, Author> existingAuthorMap = new HashMap<>();
        Map<String, RoleEntity> existingRoleMap = new HashMap<>();
        Set<Pair<Long, Long>> existingAuthorRoleSet = new HashSet<>();

        for (Author author : authorList) {
            existingAuthorMap.put(author.getName(), author);
        }
        for (RoleEntity role : roleList) {
            existingRoleMap.put(role.getRoleName(), role);
        }
        for (AuthorRole authorRole : authorRoleList) {
            existingAuthorRoleSet.add(Pair.of(authorRole.getAuthor().getId(), authorRole.getRole().getId()));
        }

        List<AuthorRole> newAuthorRolesToSave = new ArrayList<>();

        for (CSVRow row : data) {
            if (!row.getAuthor().isEmpty()) {
                Map<String, List<Role>> formattedAuthors = new AuthorFormatter().formatAuthor(row.getAuthor().trim());

                for (Map.Entry<String, List<Role>> entry : formattedAuthors.entrySet()) {
                    String authorName = entry.getKey();
                    List<Role> roles = entry.getValue();

                    Author author = existingAuthorMap.get(authorName);
                    if (author == null) {
                        continue;
                    }

                    for (Role roleEnum : roles) {
                        RoleEntity roleEntity = existingRoleMap.get(roleEnum.name());
                        if (roleEntity == null) {
                            continue;
                        }

                        Pair<Long, Long> pair = Pair.of(author.getId(), roleEntity.getId());
                        if (!existingAuthorRoleSet.contains(pair)) {
                            AuthorRole authorRole = new AuthorRole(author, roleEntity);
                            newAuthorRolesToSave.add(authorRole);
                            existingAuthorRoleSet.add(pair);
                        }
                    }
                }
            }
        }
        if (!newAuthorRolesToSave.isEmpty()) {
            authorRoleRepository.saveAll(newAuthorRolesToSave);
        }
    }
}
