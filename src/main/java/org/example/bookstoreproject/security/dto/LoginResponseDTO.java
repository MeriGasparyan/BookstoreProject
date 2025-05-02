package org.example.bookstoreproject.security.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(setterPrefix = "with")
public class LoginResponseDTO {

    private String username;
    private String accessToken;
    private String refreshToken;
}
