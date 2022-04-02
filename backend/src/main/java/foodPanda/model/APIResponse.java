package foodPanda.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
public class APIResponse<T> {

    private List<T> response;
}
