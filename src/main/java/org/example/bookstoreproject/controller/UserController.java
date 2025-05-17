package org.example.bookstoreproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.security.CustomUserDetails;
import org.example.bookstoreproject.service.criteria.BookSearchCriteria;
import org.example.bookstoreproject.service.dto.*;
import org.example.bookstoreproject.service.services.PermissionService;
import org.example.bookstoreproject.service.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<PageResponseDto<UserDTO>> getAllUsers(
            @ModelAttribute BookSearchCriteria criteria,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (!permissionService.hasPermission(currentUser.getId(), "MANAGE_USERS")) {
            throw new AccessDeniedException("You do not have permission to manage users.");
        }
        return ResponseEntity.ok(PageResponseDto.from(userService.getAllUsers(criteria.getPage(), criteria.getSize())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id,
                                           @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (!permissionService.hasPermission(currentUser.getId(), "MANAGE_USERS")) {
            throw new AccessDeniedException("You do not have permission to view other users.");
        }
        return ResponseEntity.ok(userService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDTO updateDto,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        boolean isSelf = currentUser.getId().equals(id);
        boolean hasPermission = permissionService.hasPermission(currentUser.getId(), "MANAGE_USERS");

        if (hasPermission) {
            return ResponseEntity.ok(userService.adminUpdateUser(id, updateDto));
        }

        if (!isSelf) {
            throw new AccessDeniedException("You can only update your own profile.");
        }

        return ResponseEntity.ok(userService.updateUser(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id,
                                           @AuthenticationPrincipal CustomUserDetails currentUser) {
        boolean isSelf = currentUser.getId().equals(id);
        boolean hasPermission = permissionService.hasPermission(currentUser.getId(), "MANAGE_USERS");

        if (hasPermission || isSelf) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }

        throw new AccessDeniedException("You can only delete your own profile.");
    }


}