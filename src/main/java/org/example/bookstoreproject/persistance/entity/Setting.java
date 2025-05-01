package org.example.bookstoreproject.persistance.entity;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "setting")
@Setter
@Getter
@NoArgsConstructor
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "setting_id_seq")
    @SequenceGenerator(
            name = "setting_id_seq",
            sequenceName = "setting_id_seq",
            allocationSize = 100)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public Setting(String name) {
        this.name = name;
    }
}
