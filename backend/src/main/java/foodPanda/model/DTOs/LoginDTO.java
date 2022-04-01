package foodPanda.model.DTOs;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    @NotNull
    private String credential;

    @NotNull
    private String password;
}
