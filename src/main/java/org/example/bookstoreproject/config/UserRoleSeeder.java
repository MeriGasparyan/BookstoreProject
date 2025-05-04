package org.example.bookstoreproject.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.UserRoleName;
import org.example.bookstoreproject.persistance.entity.UserRoleEntity;
import org.example.bookstoreproject.persistance.repository.UserRoleEntityRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRoleSeeder {
    private final UserRoleEntityRepository userRoleEntityRepository;

    @PostConstruct
    public void seedRoles() {
        for (UserRoleName roleEnum : UserRoleName.values()) {


            userRoleEntityRepository.findByName(roleEnum).orElseGet(() -> {
                UserRoleEntity newRole = new UserRoleEntity(roleEnum);
                return userRoleEntityRepository.save(newRole);
            });
        }

        System.out.println("User Role table seeded with enum values!");
    }
}
