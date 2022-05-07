package foodPanda.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NonNull
@Builder
@NoArgsConstructor
@ToString
public class CustomerRegister {

    private Customer customer;
    private User user;
}
