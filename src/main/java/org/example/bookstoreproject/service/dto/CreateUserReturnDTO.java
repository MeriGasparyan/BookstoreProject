package org.example.bookstoreproject.service.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.bookstoreproject.persistance.entity.User;
@Getter
@Setter
public class CreateUserReturnDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;

    public static CreateUserReturnDTO fromEntity(User user) {
        final CreateUserReturnDTO userDto = new CreateUserReturnDTO();

        userDto.setId(user.getId());
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        userDto.setEmail(user.getEmail());

        return userDto;
    }
}
