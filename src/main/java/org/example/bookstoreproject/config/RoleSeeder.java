package org.example.bookstoreproject.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.Role;
import org.example.bookstoreproject.persistance.entry.RoleEntity;
import org.example.bookstoreproject.persistance.repository.RoleRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleSeeder {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void seedRoles() {
        for (Role roleEnum : Role.values()) {
            String roleName = roleEnum.name();

            roleRepository.findByRoleName(roleName).orElseGet(() -> {
                RoleEntity newRole = new RoleEntity(roleName);
                return roleRepository.save(newRole);
            });
        }

        System.out.println("Role table seeded with enum values!");
    }
}
