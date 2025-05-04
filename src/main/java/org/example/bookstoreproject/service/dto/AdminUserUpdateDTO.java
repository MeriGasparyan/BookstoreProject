package org.example.bookstoreproject.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookstoreproject.enums.UserRoleName;

import java.util.List;
import java.util.Set;
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class AdminUserUpdateDTO extends UserUpdateDTO {
    @NotNull
    private Boolean enabled;

    private List<UserRoleName> roles;
}