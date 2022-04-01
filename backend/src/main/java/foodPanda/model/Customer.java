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
    @Column(unique = true)
    private String name;

    @NonNull
    private String address;

    @NonNull
    private Integer age;

    @NonNull
    private String password;

    @OneToOne
    private Zone addressZone;

    @OneToMany
    private List<PandaOrder> pandaOrders;
}
