package org.example.bookstoreproject.enums;

public enum UserRoleName {
    ROLE_USER,       // Regular user - can view books, rate books, etc.
    ROLE_ADMIN,      // Full admin access
    ROLE_MODERATOR,  // Can manage content but not users
    ROLE_LIBRARIAN,  // Can manage books and inventory
    ROLE_REVIEWER,   // Can post professional reviews
    ROLE_ANALYST     // Can view analytics and reports
}
