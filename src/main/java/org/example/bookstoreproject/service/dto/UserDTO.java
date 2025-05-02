package org.example.bookstoreproject.service.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.bookstoreproject.persistance.entity.User;

@Getter
@Setter
public class UserDTO {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private Long createdAt;
    private Long updatedAt;

    public static UserDTO toDto(User user) {
        final UserDTO userDto = new UserDTO();

        userDto.setId(user.getId());
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        userDto.setEmail(user.getEmail());
        userDto.setCreatedAt(user.getCreatedAt().toEpochMilli());
        userDto.setUpdatedAt(user.getUpdatedAt().toEpochMilli());

        return userDto;
    }
}
