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
        // First, seed all permissions
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

        // Define role-permission mappings including the new seller role and purchase permissions
        Map<UserRoleName, Set<PermissionName>> rolePermissionMapping = Map.of(
                UserRoleName.ROLE_USER, Set.of(
                        // Basic viewing permissions
                        PermissionName.VIEW_BOOKS,
                        PermissionName.VIEW_AUTHORS,
                        PermissionName.VIEW_RATINGS,
                        PermissionName.VIEW_REVIEWS,

                        // Review permissions
                        PermissionName.RATE_BOOKS,
                        PermissionName.POST_REVIEW,
                        PermissionName.EDIT_OWN_REVIEW,
                        PermissionName.DELETE_OWN_REVIEW,

                        // Purchase-related permissions
                        PermissionName.ADD_TO_CART,
                        PermissionName.MODIFY_CART,
                        PermissionName.PLACE_ORDER,
                        PermissionName.VIEW_ORDER_HISTORY,
                        PermissionName.CANCEL_OWN_ORDER,
                        PermissionName.REQUEST_REFUND,

                        // User-specific permissions
                        PermissionName.BOOKMARK_BOOK,
                        PermissionName.SUGGEST_BOOK,
                        PermissionName.LIKE_REVIEW,
                        PermissionName.REPORT_REVIEW,
                        PermissionName.VIEW_PERSONAL_REVIEW_STATS,
                        PermissionName.DELETE_OWN_ACCOUNT,
                        PermissionName.DOWNLOAD_ACCOUNT_DATA
                ),

                UserRoleName.ROLE_ADMIN, EnumSet.allOf(PermissionName.class),

                UserRoleName.ROLE_MODERATOR, Set.of(
                        // Viewing permissions
                        PermissionName.VIEW_BOOKS,
                        PermissionName.VIEW_AUTHORS,
                        PermissionName.VIEW_RATINGS,
                        PermissionName.VIEW_REVIEWS,

                        // Moderation permissions
                        PermissionName.APPROVE_REVIEWS,
                        PermissionName.DELETE_ANY_REVIEW,
                        PermissionName.FLAG_REVIEW_CONTENT,
                        PermissionName.VIEW_FLAGGED_CONTENT,

                        // Basic analytics
                        PermissionName.VIEW_ANALYTICS_DASHBOARD
                ),

                UserRoleName.ROLE_LIBRARIAN, Set.of(
                        // Viewing permissions
                        PermissionName.VIEW_BOOKS,
                        PermissionName.VIEW_AUTHORS,
                        PermissionName.VIEW_RATINGS,
                        PermissionName.VIEW_REVIEWS,

                        // Book management
                        PermissionName.ADD_BOOK,
                        PermissionName.EDIT_BOOK,
                        PermissionName.DELETE_BOOK,
                        PermissionName.MANAGE_INVENTORY,
                        PermissionName.MANAGE_BOOK_METADATA,

                        // Additional utilities
                        PermissionName.UPLOAD_BOOK_COVER,
                        PermissionName.DOWNLOAD_BOOK_DATA
                ),

                UserRoleName.ROLE_REVIEWER, Set.of(
                        // Viewing permissions
                        PermissionName.VIEW_BOOKS,
                        PermissionName.VIEW_AUTHORS,
                        PermissionName.VIEW_RATINGS,
                        PermissionName.VIEW_REVIEWS,

                        // Review permissions
                        PermissionName.RATE_BOOKS,
                        PermissionName.POST_REVIEW,
                        PermissionName.EDIT_OWN_REVIEW,
                        PermissionName.DELETE_OWN_REVIEW,
                        PermissionName.POST_PROFESSIONAL_REVIEW,

                        // Reviewer tools
                        PermissionName.ACCESS_REVIEWER_DASHBOARD,
                        PermissionName.VIEW_PERSONAL_REVIEW_STATS
                ),

                UserRoleName.ROLE_ANALYST, Set.of(
                        // Analytics permissions
                        PermissionName.VIEW_ANALYTICS_DASHBOARD,
                        PermissionName.VIEW_MOST_VIEWED_BOOKS,
                        PermissionName.VIEW_TOP_REVIEWERS,
                        PermissionName.VIEW_RATING_TRENDS,
                        PermissionName.EXPORT_REPORTS,

                        // Basic viewing
                        PermissionName.VIEW_BOOKS,
                        PermissionName.VIEW_AUTHORS,
                        PermissionName.VIEW_RATINGS,
                        PermissionName.VIEW_REVIEWS
                ),

                // New seller role with purchase-related permissions
                UserRoleName.ROLE_SELLER, Set.of(
                        // Viewing permissions
                        PermissionName.VIEW_BOOKS,
                        PermissionName.VIEW_AUTHORS,
                        PermissionName.VIEW_RATINGS,
                        PermissionName.VIEW_REVIEWS,

                        // Order processing
                        PermissionName.PROCESS_ORDERS,
                        PermissionName.CANCEL_ORDERS,

                        // Sales management
                        PermissionName.VIEW_SALES_REPORTS,
                        PermissionName.MANAGE_DISCOUNTS,

                        // Inventory
                        PermissionName.UPDATE_INVENTORY,
                        PermissionName.MANAGE_INVENTORY,

                        // Analytics
                        PermissionName.VIEW_ANALYTICS_DASHBOARD,
                        PermissionName.VIEW_MOST_VIEWED_BOOKS,
                        PermissionName.EXPORT_REPORTS
                )
        );

        // Create roles and assign permissions
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