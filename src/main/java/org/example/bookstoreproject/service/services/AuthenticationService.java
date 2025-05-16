package org.example.bookstoreproject.service.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.persistance.entity.RefreshToken;
import org.example.bookstoreproject.persistance.entity.User;
import org.example.bookstoreproject.persistance.repository.RefreshTokenRepository;
import org.example.bookstoreproject.persistance.repository.UserRepository;
import org.example.bookstoreproject.security.CustomUserDetails;
import org.example.bookstoreproject.security.CustomUserDetailsService;
import org.example.bookstoreproject.security.dto.LoginRequestDTO;
import org.example.bookstoreproject.security.dto.LoginResponseDTO;
import org.example.bookstoreproject.security.dto.RefreshTokenRequestDTO;
import org.example.bookstoreproject.security.dto.RefreshTokenResponseDTO;
import org.example.bookstoreproject.security.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public LoginResponseDTO authenticate(LoginRequestDTO loginRequest) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        final String accessToken = jwtUtil.generateAccessToken(userDetails);
        final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        saveRefreshToken(userDetails, refreshToken);

        return LoginResponseDTO.builder()
                .withUsername(userDetails.getUsername())
                .withAccessToken(accessToken)
                .withRefreshToken(refreshToken)
                .build();
    }

    @Transactional
    public RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequest) {
        if (!jwtUtil.isVerified(refreshTokenRequest.getRefreshToken()) ||
                !jwtUtil.isRefreshToken(refreshTokenRequest.getRefreshToken())) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshTokenRequest.getRefreshToken())
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));

        if (jwtUtil.isTokenExpired(refreshTokenRequest.getRefreshToken())) {
            refreshTokenRepository.delete(storedToken);
            throw new IllegalArgumentException("Refresh token expired");
        }

        String username = jwtUtil.getUsername(refreshTokenRequest.getRefreshToken());
        CustomUserDetails userDetails = (CustomUserDetails)
                userDetailsService.loadUserByUsername(username);

        String newAccessToken = jwtUtil.generateAccessToken(userDetails);
        String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

        refreshTokenRepository.delete(storedToken);
        saveRefreshToken(userDetails, newRefreshToken);

        return new RefreshTokenResponseDTO(newAccessToken, newRefreshToken);
    }
    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(refreshTokenRepository::delete);
    }

    @Transactional
    public void logoutAllForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        refreshTokenRepository.deleteByUser(user);
    }


    private void saveRefreshToken(CustomUserDetails userDetails, String refreshToken) {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        RefreshToken token = new RefreshToken();
        token.setToken(refreshToken);
        token.setUser(user);
        token.setExpiryDate(jwtUtil.getExpirationDateFromToken(refreshToken));
        refreshTokenRepository.save(token);
    }

}
