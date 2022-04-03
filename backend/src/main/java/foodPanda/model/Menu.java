package foodPanda.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "menu_generator")
    private Long menuId;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<Category> categories;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
}
