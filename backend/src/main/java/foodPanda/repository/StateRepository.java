package foodPanda.repository;

import foodPanda.model.OrderStatus;
import foodPanda.model.states.State;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateRepository extends JpaRepository<State, Long> {

    State findByOrderStatus(OrderStatus orderStatus);
}
