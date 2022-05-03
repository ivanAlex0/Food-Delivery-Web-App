package foodPanda.repository;

import foodPanda.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The Repository that handles {@link CartItem} database transactions
 */
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
