package org.example.bookstoreproject.service.dto;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookCharacterCreateDTO {
    @NotEmpty
    private List<Long> characters;
}
