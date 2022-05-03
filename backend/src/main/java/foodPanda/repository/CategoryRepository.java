package foodPanda.repository;

import foodPanda.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The Repository that handles {@link Category} database transactions
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
