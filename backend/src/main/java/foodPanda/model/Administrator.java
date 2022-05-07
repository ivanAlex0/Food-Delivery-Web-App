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
@AllArgsConstructor
public class Administrator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "admin_generator")
    private Long adminId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "administrator")
    Restaurant restaurant;
}
