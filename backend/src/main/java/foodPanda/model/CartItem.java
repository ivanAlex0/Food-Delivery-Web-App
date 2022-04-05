package foodPanda.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "cart_item_generator")
    private Long id;

    @JoinColumn(name = "food_id")
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Food item;

    @JsonIgnore
    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private PandaOrder order;

    @Column(name = "quantity")
    private Integer quantity;
}
