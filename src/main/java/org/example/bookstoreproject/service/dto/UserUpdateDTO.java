package org.example.bookstoreproject.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {
    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;
}
