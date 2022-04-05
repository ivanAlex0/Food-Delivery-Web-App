package foodPanda.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "zone_generator")
    @EqualsAndHashCode.Include
    private Long id;

    @NonNull
    @Column(unique = true)
    private String name;
}
