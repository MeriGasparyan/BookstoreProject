package org.example.bookstoreproject.service.services;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.UserRoleName;
import org.example.bookstoreproject.exception.ResourceAlreadyUsedException;
import org.example.bookstoreproject.exception.ResourceNotFoundException;
import org.example.bookstoreproject.persistance.entity.User;
import org.example.bookstoreproject.persistance.entity.UserRole;
import org.example.bookstoreproject.persistance.repository.UserRepository;
import org.example.bookstoreproject.persistance.repository.UserRoleRepository;
import org.example.bookstoreproject.service.dto.UserDTO;
import org.example.bookstoreproject.service.dto.UserRegistrationDTO;
import org.example.bookstoreproject.service.dto.UserUpdateDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository roleRepository;

    @Transactional
    public UserDTO createUser(UserRegistrationDTO registrationDto) {

        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new ResourceAlreadyUsedException("User with this email already exists");
        }

        final UserRole role = roleRepository.findByName(UserRoleName.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        final User user = new User();
        user.setFirstname(registrationDto.getFirstName());
        user.setLastname(registrationDto.getLastName());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setEnabled(true);
        user.setRole(role);

        return UserDTO.toDto(userRepository.save(user));
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDTO::toDto)
                .toList();
    }

    public UserDTO getById(Long id) {

        final User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return UserDTO.toDto(user);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserUpdateDTO updateDto) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setFirstname(updateDto.getFirstname());
        user.setLastname(updateDto.getLastname());

        return UserDTO.toDto(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.deleteById(id);
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }
}
