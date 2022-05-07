package foodPanda.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_generator")
    private Long userId;

    @NonNull
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NonNull
    private String password;
}
