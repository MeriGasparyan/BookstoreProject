package org.example.bookstoreproject.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookstoreproject.enums.PermissionName;
import org.example.bookstoreproject.enums.UserRoleName;

import java.util.Set;

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

        @Size(min = 6, max = 100)
        private String newPassword;

        private Boolean enabled;

        private UserRoleName role;

        private Set<PermissionName> permissionsToAdd;

        private Set<PermissionName> permissionsToRemove;

}
