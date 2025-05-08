package org.example.bookstoreproject.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.PermissionName;
import org.example.bookstoreproject.enums.UserRoleName;
import org.example.bookstoreproject.persistance.entity.Permission;
import org.example.bookstoreproject.persistance.entity.RolePermission;
import org.example.bookstoreproject.persistance.entity.UserRole;
import org.example.bookstoreproject.persistance.repository.PermissionRepository;
import org.example.bookstoreproject.persistance.repository.RolePermissionRepository;
import org.example.bookstoreproject.persistance.repository.UserRoleRepository;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RolePermissionSeeder {

    private final PermissionRepository permissionRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @PostConstruct
    public void seedRolesAndPermissions() {
        Map<PermissionName, Permission> permissionMap = new EnumMap<>(PermissionName.class);
        for (PermissionName permissionName : PermissionName.values()) {
            Permission permission = permissionRepository.findByName(permissionName)
                    .orElseGet(() -> {
                        Permission p = new Permission();
                        p.setName(permissionName);
                        return permissionRepository.save(p);
                    });
            permissionMap.put(permissionName, permission);
        }

        Map<UserRoleName, Set<PermissionName>> rolePermissionMapping = Map.of(
                UserRoleName.ROLE_USER, Set.of(
                        PermissionName.VIEW_BOOKS,
                        PermissionName.VIEW_AUTHORS,
                        PermissionName.VIEW_RATINGS,
                        PermissionName.VIEW_REVIEWS,
                        PermissionName.RATE_BOOKS,
                        PermissionName.POST_REVIEW,
                        PermissionName.EDIT_OWN_REVIEW,
                        PermissionName.DELETE_OWN_REVIEW
                ),
                UserRoleName.ROLE_ADMIN, EnumSet.allOf(PermissionName.class),
                UserRoleName.ROLE_MODERATOR, Set.of(
                        PermissionName.VIEW_BOOKS,
                        PermissionName.VIEW_REVIEWS,
                        PermissionName.APPROVE_REVIEWS,
                        PermissionName.DELETE_ANY_REVIEW,
                        PermissionName.FLAG_REVIEW_CONTENT,
                        PermissionName.VIEW_FLAGGED_CONTENT
                ),
                UserRoleName.ROLE_LIBRARIAN, Set.of(
                        PermissionName.VIEW_BOOKS,
                        PermissionName.VIEW_AUTHORS,
                        PermissionName.ADD_BOOK,
                        PermissionName.EDIT_BOOK,
                        PermissionName.DELETE_BOOK,
                        PermissionName.MANAGE_INVENTORY,
                        PermissionName.MANAGE_BOOK_METADATA
                ),
                UserRoleName.ROLE_REVIEWER, Set.of(
                        PermissionName.VIEW_BOOKS,
                        PermissionName.VIEW_AUTHORS,
                        PermissionName.VIEW_REVIEWS,
                        PermissionName.RATE_BOOKS,
                        PermissionName.POST_REVIEW,
                        PermissionName.EDIT_OWN_REVIEW,
                        PermissionName.DELETE_OWN_REVIEW,
                        PermissionName.POST_PROFESSIONAL_REVIEW,
                        PermissionName.ACCESS_REVIEWER_DASHBOARD
                ),
                UserRoleName.ROLE_ANALYST, Set.of(
                        PermissionName.VIEW_ANALYTICS_DASHBOARD,
                        PermissionName.VIEW_MOST_VIEWED_BOOKS,
                        PermissionName.VIEW_TOP_REVIEWERS,
                        PermissionName.VIEW_RATING_TRENDS,
                        PermissionName.EXPORT_REPORTS
                )
        );

        for (var entry : rolePermissionMapping.entrySet()) {
            UserRoleName roleName = entry.getKey();
            Set<PermissionName> assignedPermissions = entry.getValue();

            UserRole role = userRoleRepository.findByName(roleName)
                    .orElseGet(() -> userRoleRepository.save(new UserRole(roleName)));

            for (PermissionName permName : assignedPermissions) {
                Permission permission = permissionMap.get(permName);

                boolean alreadyExists = rolePermissionRepository.existsByRoleAndPermission(role, permission);
                if (!alreadyExists) {
                    RolePermission rp = new RolePermission();
                    rp.setRole(role);
                    rp.setPermission(permission);
                    rolePermissionRepository.save(rp);
                }
            }
        }
    }
}
