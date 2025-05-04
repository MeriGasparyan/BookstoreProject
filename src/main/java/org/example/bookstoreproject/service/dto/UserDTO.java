package org.example.bookstoreproject.service.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.bookstoreproject.persistance.entity.User;

import java.time.Instant;

@Getter
@Setter
public class UserDTO {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private Instant createdAt;
    private Instant updatedAt;

    public static UserDTO toDto(User user) {
        final UserDTO userDto = new UserDTO();

        userDto.setId(user.getId());
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        userDto.setEmail(user.getEmail());
        userDto.setUpdatedAt(user.getUpdatedAt());
        userDto.setCreatedAt(user.getCreatedAt());

        return userDto;
    }
}
