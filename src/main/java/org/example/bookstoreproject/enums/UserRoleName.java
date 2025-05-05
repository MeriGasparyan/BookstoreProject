package org.example.bookstoreproject.enums;

public enum UserRoleName {
    /**
     * View books, authors, ratings, reviews
     * Rate books
     * Post personal reviews
     * Edit/delete own reviews
     **/
    ROLE_USER,
    /**
     * Full access
     */
    ROLE_ADMIN,

    /**
     * Moderate user reviews (approve/delete/report)
     * Manage flagged content
     * Cannot manage users or books
     */
    ROLE_MODERATOR,
    /**
     * Add/edit/delete books
     * Manage book inventory (stock, availability)
     * Manage book metadata (genres, authors, etc.)
     * Cannot manage users or reviews
     */
    ROLE_LIBRARIAN,
    /**
     * Everything ROLE_USER can do
     * Post professional reviews
     * Access reviewer dashboard / tools
     */
    ROLE_REVIEWER,
    /**
     * Access analytics dashboards
     * View reports (most viewed books, top reviewers, rating trends)
     * Cannot edit content
     */
    ROLE_ANALYST
}
