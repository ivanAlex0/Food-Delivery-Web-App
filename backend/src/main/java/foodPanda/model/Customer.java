package foodPanda.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "customer_generator")
    private Long customerId;

    @NonNull
    @Column(unique = true)
    private String email;

    @NonNull
    private String password;

    @NonNull
    @Column(unique = true)
    private String name;

    @NonNull
    private String address;

    @OneToOne
    private Zone addressZone;

    @OneToMany(mappedBy = "customer")
    private List<PandaOrder> pandaOrders;
}
