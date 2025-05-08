package org.example.bookstoreproject.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.UserRoleName;
import org.example.bookstoreproject.persistance.entity.UserRole;
import org.example.bookstoreproject.persistance.repository.UserRoleRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRoleSeeder {
    private final UserRoleRepository userRoleRepository;

    @PostConstruct
    public void seedRoles() {
        for (UserRoleName roleEnum : UserRoleName.values()) {


            userRoleRepository.findByName(roleEnum).orElseGet(() -> {
                UserRole newRole = new UserRole(roleEnum);
                return userRoleRepository.save(newRole);
            });
        }

        System.out.println("User Role table seeded with enum values!");
    }
}
