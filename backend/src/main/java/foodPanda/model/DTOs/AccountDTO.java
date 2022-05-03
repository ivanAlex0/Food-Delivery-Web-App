package foodPanda.model.DTOs;

import lombok.*;

/**
 * A simplified version of the {@link foodPanda.model.Customer} and {@link foodPanda.model.Administrator} models that are received from the frontend
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDTO {

    private String credential;
    private String password;
}
