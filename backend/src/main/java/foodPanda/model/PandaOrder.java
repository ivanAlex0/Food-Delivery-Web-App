package foodPanda.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PandaOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "pandaOder_generator")
    private Long orderId;

    @ManyToOne
    private Customer customer;
}
