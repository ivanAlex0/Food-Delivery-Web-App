package foodPanda.repository;

import foodPanda.model.OrderStatus;
import foodPanda.model.states.State;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The Repository that handles {@link State} database transactions
 */
public interface StateRepository extends JpaRepository<State, Long> {

    /**
     * Finds a State by its {@link OrderStatus}
     *
     * @param orderStatus The status that is searched
     * @return The State instance from the DB
     */
    State findByOrderStatus(OrderStatus orderStatus);
}
