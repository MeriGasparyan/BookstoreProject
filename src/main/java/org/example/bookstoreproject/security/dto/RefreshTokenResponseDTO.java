package org.example.bookstoreproject.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RefreshTokenResponseDTO {
    private String accessToken;
    private String refreshToken;
}