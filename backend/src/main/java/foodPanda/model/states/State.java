package foodPanda.model.states;

import foodPanda.exception.InvalidInputException;
import foodPanda.model.OrderStatus;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class State implements StateInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "state_generator")
    private Long id;

    private OrderStatus orderStatus;

    @Override
    public OrderStatus changeState(OrderStatus _newStatus) {
        if (orderStatus == OrderStatus.PENDING && _newStatus == OrderStatus.DECLINED)
            return OrderStatus.DECLINED;
        if (orderStatus == OrderStatus.PENDING && _newStatus == OrderStatus.ACCEPTED)
            return OrderStatus.ACCEPTED;
        if (orderStatus == OrderStatus.ACCEPTED && _newStatus == OrderStatus.IN_DELIVERY)
            return OrderStatus.IN_DELIVERY;
        else if (orderStatus == OrderStatus.IN_DELIVERY && _newStatus == OrderStatus.DELIVERED)
            return OrderStatus.DELIVERED;
        else
            throw new InvalidInputException("The status change from {status}=" + orderStatus + " to {status}=" + _newStatus + " is not valid");
    }
}
