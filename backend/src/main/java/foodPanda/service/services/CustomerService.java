package foodPanda.service.services;


import foodPanda.model.Customer;
import foodPanda.model.DTOs.AccountDTO;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {

    Customer save(Customer customer);

    Customer authenticate(AccountDTO accountDTO);
}
