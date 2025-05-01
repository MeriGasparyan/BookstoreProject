package org.example.bookstoreproject.service.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookstoreproject.persistance.entity.Award;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AwardDTO {
    private Long id;
    private String title;

    public static AwardDTO fromEntity(Award award) {
        AwardDTO awardDTO = new AwardDTO();
        awardDTO.setId(award.getId());
        awardDTO.setTitle(award.getTitle());
        return awardDTO;
    }
}
