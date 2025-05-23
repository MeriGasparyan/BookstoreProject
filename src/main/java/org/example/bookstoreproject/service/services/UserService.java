package org.example.bookstoreproject.service.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.UserRoleName;
import org.example.bookstoreproject.exception.ResourceAlreadyUsedException;
import org.example.bookstoreproject.exception.ResourceNotFoundException;
import org.example.bookstoreproject.persistance.entity.*;
import org.example.bookstoreproject.persistance.repository.*;
import org.example.bookstoreproject.service.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository roleRepository;
    private final CartRepository cartRepository;
    private final PermissionRepository permissionRepository;
    private final UserPermissionRepository userPermissionRepository;

    @Transactional
    public CreateUserReturnDTO createUser(UserRegistrationDTO registrationDto) {
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new ResourceAlreadyUsedException("User with this email already exists");
        }

        UserRole role = roleRepository.findByName(UserRoleName.ROLE_USER)
                .orElseThrow(()-> new NoSuchElementException("Role not found"));
        User user = new User();
        user.setFirstname(registrationDto.getFirstName());
        user.setLastname(registrationDto.getLastName());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setEnabled(true);
        user.setRole(role);

        User savedUser = userRepository.save(user);

        Cart cart = new Cart();
        cart.setUser(savedUser);
        cartRepository.save(cart);

        return CreateUserReturnDTO.fromEntity(savedUser);
    }

    public Page<UserDTO> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable)
                .map(UserDTO::toDto);
    }

    public UserDTO getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserDTO.toDto(user);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserUpdateDTO updateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (updateDto.getFirstName() != null && !updateDto.getLastName().isEmpty()) {
            user.setFirstname(updateDto.getFirstName());
        }
        if (updateDto.getLastName() != null && !updateDto.getLastName().isEmpty()) {
            user.setLastname(updateDto.getLastName());
        }
        if (updateDto.getEmail() != null && !updateDto.getEmail().isEmpty()) {
            user.setEmail(updateDto.getEmail());
        }
        if (updateDto.getNewPassword() != null && !updateDto.getNewPassword().isEmpty()) {
            if (updateDto.getCurrentPassword() == null || updateDto.getCurrentPassword().isEmpty()) {
                throw new IllegalArgumentException("Current password is required to change password");
            }

            if (!passwordEncoder.matches(updateDto.getCurrentPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Current password is incorrect");
            }

            user.setPassword(passwordEncoder.encode(updateDto.getNewPassword()));
        }

        return UserDTO.toDto(userRepository.save(user));
    }

    @Transactional
    public UserDTO adminUpdateUser(Long id, UserUpdateDTO adminUpdateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (adminUpdateDto.getEnabled() != null) {
            user.setEnabled(adminUpdateDto.getEnabled());
        }

        if (adminUpdateDto.getRole() != null) {
            UserRole newRole = roleRepository.findByName(adminUpdateDto.getRole())
                    .orElseThrow(() -> new IllegalArgumentException("Role not found: " + adminUpdateDto.getRole()));
            user.setRole(newRole);
        }

        if (adminUpdateDto.getPermissionsToAdd() != null && !adminUpdateDto.getPermissionsToAdd().isEmpty()) {
            Set<Permission> findPermissions = permissionRepository.findPermissions(adminUpdateDto.getPermissionsToAdd());
            Set<Permission> existingPermissions = user.getUserPermissions().stream()
                    .map(UserPermission::getPermission)
                    .collect(Collectors.toSet());
            List<UserPermission> newPermissions = new ArrayList<>();

            for (Permission permission : findPermissions) {
                if (!existingPermissions.contains(permission)) {
                    UserPermission newPermission = new UserPermission();
                    newPermission.setPermission(permission);
                    newPermission.setUser(user);
                    user.addUserPermission(newPermission);
                    newPermissions.add(newPermission);
                }
            }
            if (!newPermissions.isEmpty()) {
                userPermissionRepository.saveAll(newPermissions);
            }
        }

        if (adminUpdateDto.getPermissionsToRemove() != null && !adminUpdateDto.getPermissionsToRemove().isEmpty()) {
            Set<Permission> permissionsToRemove = permissionRepository.findPermissions(adminUpdateDto.getPermissionsToRemove());
            List<UserPermission> userPermissionsToRemove = user.getUserPermissions().stream()
                    .filter(userPermission -> permissionsToRemove.contains(userPermission.getPermission()))
                    .collect(Collectors.toList());

            if (!userPermissionsToRemove.isEmpty()) {
                userPermissionsToRemove.forEach(user.getUserPermissions()::remove);
                userPermissionRepository.deleteAll(userPermissionsToRemove);
            }
        }

        return UserDTO.toDto(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }
}

