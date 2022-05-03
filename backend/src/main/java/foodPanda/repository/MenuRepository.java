package foodPanda.repository;

import foodPanda.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Repository that handles {@link Menu} database transactions
 */
@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
}
