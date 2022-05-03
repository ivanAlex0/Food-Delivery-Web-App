package foodPanda.repository;

import foodPanda.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Repository that handles {@link Zone} database transactions
 */
@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {
}
