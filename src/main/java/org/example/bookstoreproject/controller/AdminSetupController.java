package org.example.bookstoreproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.UserRoleName;
import org.example.bookstoreproject.persistance.entity.User;
import org.example.bookstoreproject.persistance.entity.UserRole;
import org.example.bookstoreproject.persistance.repository.RoleRepository;
import org.example.bookstoreproject.persistance.repository.UserRepository;
import org.example.bookstoreproject.persistance.repository.UserRoleRepository;
import org.example.bookstoreproject.service.dto.CreateUserReturnDTO;
import org.example.bookstoreproject.service.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminSetupController {

    private final UserService userService;

    @PostMapping("/setup")
    public ResponseEntity<CreateUserReturnDTO> setupAdmin() {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.setUpAdmin());
    }
}
