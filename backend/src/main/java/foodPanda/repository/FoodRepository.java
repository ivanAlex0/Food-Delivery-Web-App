package foodPanda.repository;

import foodPanda.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The Repository that handles {@link Food} database transactions
 */
public interface FoodRepository extends JpaRepository<Food, Long> {
}
