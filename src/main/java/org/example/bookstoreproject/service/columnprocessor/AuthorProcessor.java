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
        for (CSVRow row : data) {
            if (!row.getAuthor().isEmpty()) {
                List<Author> authors = new ArrayList<>();
                Map<String, List<Role>> formattedAuthors = authorFormatter.formatAuthor(row.getAuthor().trim());

                for (Map.Entry<String, List<Role>> entry : formattedAuthors.entrySet()) {
                    String name = entry.getKey();

                    List<Role> roles = entry.getValue();
                    Author author = authorRepository.findByName(name)
                            .orElseGet(() -> authorRepository.save(new Author(name)));
                    authors.add(author);


                    for (Role roleEnum : roles) {
                        Optional<RoleEntity> roleEntityOpt = roleRepository.findByRoleName(roleEnum.name());

                        if (roleEntityOpt.isPresent()) {
                            RoleEntity roleEntity = roleEntityOpt.get();

                            boolean alreadyExists = authorRoleRepository
                                    .existsByAuthorAndRole(author, roleEntity);

                            if (!alreadyExists) {
                                AuthorRole authorRole = new AuthorRole(author, roleEntity);
                                authorRoleRepository.save(authorRole);
                            }
                        }
                    }
                }
                authorBookMap.put(row.getBookID().trim(), authors);
            }
        }
    }
}