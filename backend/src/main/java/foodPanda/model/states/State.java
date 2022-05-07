package foodPanda.model.states;

import foodPanda.exception.InvalidInputException;
import foodPanda.model.OrderStatus;
import lombok.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * This class is based on the State Design pattern and implements {@link StateInterface} to be able to change the State automatically
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class State implements StateInterface {

    private static final Logger LOGGER = LogManager.getLogger(State.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "state_generator")
    private Long id;

    /**
     * The actual status{@link OrderStatus} of the State
     */
    private OrderStatus orderStatus;

    /**
     * The method automatically changes the status of this State
     *
     * @param _newStatus The new status
     * @return the changed status if valid
     */
    @Override
    public OrderStatus changeState(OrderStatus _newStatus) {
        if (orderStatus == OrderStatus.PENDING && _newStatus == OrderStatus.DECLINED)
            return OrderStatus.DECLINED;
        if (orderStatus == OrderStatus.PENDING && _newStatus == OrderStatus.ACCEPTED)
            return OrderStatus.ACCEPTED;
        if (orderStatus == OrderStatus.ACCEPTED && _newStatus == OrderStatus.IN_DELIVERY)
            return OrderStatus.IN_DELIVERY;
        if (orderStatus == OrderStatus.IN_DELIVERY && _newStatus == OrderStatus.DELIVERED)
            return OrderStatus.DELIVERED;

        LOGGER.error("The status change from {status}=" + orderStatus + " to {status}=" + _newStatus + " is not valid");
        throw new InvalidInputException("The status change from {status}=" + orderStatus + " to {status}=" + _newStatus + " is not valid");

    }
}
