package foodPanda.repository;

import foodPanda.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The Repository that handles {@link Restaurant} database transactions
 */
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
