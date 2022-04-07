package foodPanda.service.services;


import foodPanda.model.Customer;
import foodPanda.model.DTOs.AccountDTO;
import foodPanda.model.PandaOrder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {

    Customer save(Customer customer);

    Customer authenticate(AccountDTO accountDTO);

    PandaOrder placeOrder(Long restaurantId, Long customerId, PandaOrder order);

    List<PandaOrder> fetchOrdersForCustomer(Long customerId);
}
