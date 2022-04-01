package foodPanda.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "category_generator")
    private Long categoryId;

    @Column(unique = true, nullable = false)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Food> foodList;
}
