package org.example.bookstoreproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.persistance.entity.User;
import org.example.bookstoreproject.security.CustomUserDetails;
import org.example.bookstoreproject.service.dto.*;
import org.example.bookstoreproject.service.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public ResponseEntity<PageResponseDto<UserDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(PageResponseDto.from(userService.getAllUsers(page, size)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDTO updateDto,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        boolean hasPermission = currentUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals("MANAGE_USERS"));
        if (hasPermission) {
            return ResponseEntity.ok(userService.adminUpdateUser(id, updateDto));
        }

        User user = userService.getUserById(currentUser.getId());

        if (!user.getId().equals(id)) {
            if (!id.equals(currentUser.getId())) {
                throw new AccessDeniedException("You can only update your own profile");
            }
        }
        return ResponseEntity.ok(userService.updateUser(id,updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id,
                                           @AuthenticationPrincipal CustomUserDetails currentUser) {
        boolean hasPermission = currentUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals("MANAGE_USERS"));

        User user = userService.getUserById(currentUser.getId());

        if (hasPermission || user.getId().equals(id)) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }

        throw new AccessDeniedException("You can only delete your own profile");
    }


}