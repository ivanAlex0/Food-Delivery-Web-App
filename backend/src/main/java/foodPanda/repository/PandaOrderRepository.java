package foodPanda.repository;

import foodPanda.model.PandaOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PandaOrderRepository extends JpaRepository<PandaOrder, Long> {

    List<PandaOrder> findAllByCustomer_CustomerId(Long customerId);
}
