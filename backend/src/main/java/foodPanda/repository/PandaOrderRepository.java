package foodPanda.repository;

import foodPanda.model.PandaOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PandaOrderRepository extends JpaRepository<PandaOrder, Long> {
}
