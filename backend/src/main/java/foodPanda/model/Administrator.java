package foodPanda.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Table(name = "administrator")
@Getter
@Setter
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Administrator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "admin_generator")
    private Long adminId;

    @NonNull
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NonNull
    private String password;

    @OneToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "administrator")
    Restaurant restaurant;
}
