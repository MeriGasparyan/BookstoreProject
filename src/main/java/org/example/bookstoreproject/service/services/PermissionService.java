package org.example.bookstoreproject.service.services;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.persistance.entity.User;
import org.example.bookstoreproject.persistance.repository.UserRepository;
import org.example.bookstoreproject.security.CustomUserDetails;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final UserRepository userRepository;

    public void checkPermission(CustomUserDetails user, String permission) {
        if (!hasPermission(user.getId(), permission)) {
            throw new AccessDeniedException("Permission denied: " + permission);
        }
    }

    public Set<String> getPermissionsForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<String> permissions = new HashSet<>();
        user.getRole().getRolePermissions().forEach(rp ->
                permissions.add(rp.getPermission().getName().name()));
        user.getUserPermissions().forEach(up ->
                permissions.add(up.getPermission().getName().name()));
        return permissions;
    }

    public boolean hasPermission(Long userId, String permission) {
        return getPermissionsForUser(userId).contains(permission);
    }
}
