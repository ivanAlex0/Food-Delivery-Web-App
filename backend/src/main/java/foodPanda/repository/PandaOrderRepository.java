package foodPanda.repository;

import foodPanda.model.PandaOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The Repository that handles {@link PandaOrder} database transactions
 */
public interface PandaOrderRepository extends JpaRepository<PandaOrder, Long> {

    /**
     * Finds all {@link PandaOrder} by the {@link foodPanda.model.Customer}'s id
     *
     * @param customerId The id of the customer whose orders are fetched
     * @return A list of all PandaOrders that correspond to the specified Customer
     */
    List<PandaOrder> findAllByCustomer_CustomerId(Long customerId);
}
