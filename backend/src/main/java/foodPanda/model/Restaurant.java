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
    @JoinColumn(name = "locationZone_id")
    private Zone locationZone;

    @ManyToMany
    private List<Zone> deliveryZones;

    @OneToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "restaurant")
    private Menu menu;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "admin_id", nullable = false)
    private Administrator administrator;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<PandaOrder> orders;
}
