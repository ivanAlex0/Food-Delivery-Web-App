package foodPanda.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "category_generator")
    private Long categoryId;

    @NonNull
    private CategoryType category;

    @OneToMany(mappedBy = "category")
    private List<Food> foodList;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "menu_id")
    private Menu menu;

}
