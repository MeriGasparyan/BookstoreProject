package org.example.bookstoreproject.service.impl;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.enums.Role;
import org.example.bookstoreproject.persistance.entry.Author;
import org.example.bookstoreproject.persistance.entry.AuthorRole;
import org.example.bookstoreproject.persistance.entry.RoleEntity;
import org.example.bookstoreproject.persistance.repository.AuthorRepository;
import org.example.bookstoreproject.persistance.repository.AuthorRoleRepository;
import org.example.bookstoreproject.persistance.repository.RoleRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.format.AuthorFormatter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
public class AuthorProcessor implements CSVColumnProcessor {

    private final AuthorRepository authorRepository;
    private final RoleRepository roleRepository;
    private final AuthorRoleRepository authorRoleRepository;

    @Override
    public void process(List<CSVRow> data) {
        for (CSVRow row : data) {
            if (!row.getAuthor().isEmpty()) {
                Map<String, List<Role>> formattedAuthors = AuthorFormatter.formatAuthor(row.getAuthor().trim());

                for (Map.Entry<String, List<Role>> entry : formattedAuthors.entrySet()) {
                    String name = entry.getKey();
                    List<Role> roles = entry.getValue();
                    Author author = authorRepository.findByName(name)
                            .orElseGet(() -> authorRepository.save(new Author(name)));

                    for (Role roleEnum : roles) {
                        Optional<RoleEntity> roleEntityOpt = roleRepository.findByRoleName(roleEnum.name());

                        if (roleEntityOpt.isPresent()) {
                            RoleEntity roleEntity = roleEntityOpt.get();

                            boolean alreadyExists = authorRoleRepository
                                    .existsByAuthorAndRole(author, roleEntity);

                            if (!alreadyExists) {
                                AuthorRole authorRole = new AuthorRole();
                                authorRole.setAuthor(author);
                                authorRole.setRole(roleEntity);
                                authorRoleRepository.save(authorRole);
                            }
                        }
                    }
                }
            }
        }
    }
}
