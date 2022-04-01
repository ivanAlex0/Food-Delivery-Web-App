package foodPanda.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "restaurant_generator")
    @EqualsAndHashCode.Include
    private Long restaurantId;

    @NonNull
    @Column(unique = true)
    private String name;

    @NonNull
    private String location;

    @ManyToOne
    private Zone locationZone;

    @OneToMany
    private List<Zone> deliveryZones;

    @JsonIgnore
    @OneToOne(mappedBy = "restaurant")
    private Administrator administrator;

    @OneToMany(mappedBy = "restaurant")
    private List<Food> menu;
}
