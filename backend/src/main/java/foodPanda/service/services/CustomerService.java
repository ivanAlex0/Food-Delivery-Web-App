package foodPanda.service.services;


import foodPanda.model.Customer;
import foodPanda.model.CustomerRegister;
import foodPanda.model.DTOs.AccountDTO;
import foodPanda.model.PandaOrder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The class that defines all functions to be implemented in the {@link foodPanda.service.impl.CustomerServiceImpl}
 */
@Service
public interface CustomerService {

    /**
     * Saves a {@link Customer} instance in the DB
     *
     * @param customerRegister The Customer information to be saved
     * @return The Customer instance enhanced with the customerId field
     */
    Customer save(CustomerRegister customerRegister);

    /**
     * Checks whether the information from the {@link AccountDTO} matches an entry in the DB
     *
     * @param accountDTO The actual information to be checked: email and password
     * @return The Customer instance enhanced with the customerId field
     */
    Customer authenticate(AccountDTO accountDTO);

    /**
     * Places an {@link PandaOrder} by a certain {@link Customer} at a certain {@link foodPanda.model.Restaurant}
     *
     * @param restaurantId The id of the Restaurant to which the PandaOrder is placed
     * @param customerId   The id of the Customer who places the PandaOrder
     * @param order        The actual information of the PandaOrder which is saved
     * @return The newly saved PandaOrder enhanced with the orderId field
     */
    PandaOrder placeOrder(Long restaurantId, Long customerId, String details, PandaOrder order);

    /**
     * Fetches all {@link PandaOrder}s that were placed by a certain {@link Customer}
     *
     * @param customerId The id of the Customer whose PandaOrders are fetched
     * @return A list of PandaOrders
     */
    List<PandaOrder> fetchOrdersForCustomer(Long customerId);

    void sendMail(Customer customer, PandaOrder order, String details);
}
