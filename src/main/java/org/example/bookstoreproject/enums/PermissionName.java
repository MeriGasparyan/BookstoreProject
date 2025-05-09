package org.example.bookstoreproject.enums;

public enum PermissionName {
    // ===== Common user/book permissions =====
    VIEW_BOOKS,
    VIEW_AUTHORS,
    VIEW_RATINGS,
    VIEW_REVIEWS,

    // ===== User review permissions =====
    RATE_BOOKS,
    POST_REVIEW,
    EDIT_OWN_REVIEW,
    DELETE_OWN_REVIEW,

    // ===== Moderator permissions =====
    APPROVE_REVIEWS,
    DELETE_ANY_REVIEW,
    FLAG_REVIEW_CONTENT,
    VIEW_FLAGGED_CONTENT,

    // ===== Librarian permissions =====
    ADD_BOOK,
    EDIT_BOOK,
    DELETE_BOOK,
    MANAGE_INVENTORY,
    MANAGE_BOOK_METADATA, // genres, authors, etc.

    // ===== Reviewer permissions =====
    POST_PROFESSIONAL_REVIEW,
    ACCESS_REVIEWER_DASHBOARD,

    // ===== Analyst permissions =====
    VIEW_ANALYTICS_DASHBOARD,
    VIEW_MOST_VIEWED_BOOKS,
    VIEW_TOP_REVIEWERS,
    VIEW_RATING_TRENDS,
    EXPORT_REPORTS,

    // ===== Admin permissions =====
    MANAGE_USERS,
    ASSIGN_ROLES,
    VIEW_ALL_DATA,

    // ===== Misc/Utility =====
    UPLOAD_BOOK_COVER,
    DOWNLOAD_BOOK_DATA,
    ACCESS_API_DOCS,

    // ===== User-specific permissions =====
    BOOKMARK_BOOK,
    SUGGEST_BOOK,
    REQUEST_BOOK_COPY,
    LIKE_REVIEW,
    REPORT_REVIEW,
    VIEW_PERSONAL_REVIEW_STATS,
    DELETE_OWN_ACCOUNT,
    DOWNLOAD_ACCOUNT_DATA,
    ACCESS_BETA_FEATURES,

    // ===== Seller permissions =====
    PROCESS_ORDERS,
    VIEW_SALES_REPORTS,
    UPDATE_INVENTORY,
    MANAGE_DISCOUNTS,
    CANCEL_ORDERS,

    // ===== Customer purchase permissions =====
    ADD_TO_CART,
    MODIFY_CART,
    PLACE_ORDER,
    VIEW_ORDER_HISTORY,
    CANCEL_OWN_ORDER,
    REQUEST_REFUND
}
