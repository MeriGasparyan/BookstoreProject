package org.example.bookstoreproject.persistance.entry;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "author")
@Setter
@Getter
@NoArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

//    @Column(nullable = false)
//    private String role;

//    public Author(String name, String role) {
//        this.name = name;
//        this.role = role;
//    }

    public Author(String name) {
        this.name = name;
    }

}
