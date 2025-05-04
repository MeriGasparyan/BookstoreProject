package org.example.bookstoreproject.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

        @Size(max = 50)
        private String firstName;


        @Size(max = 50)
        private String lastName;

        @Email
        @Size(max = 100)
        private String email;

        private String currentPassword;

        @Size(min = 8, max = 100)
        private String newPassword;

}
