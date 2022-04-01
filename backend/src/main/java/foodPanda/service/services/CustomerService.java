package foodPanda.service.services;


import foodPanda.model.Customer;
import foodPanda.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CustomerService {

    Optional<Customer> findById(Long index);

    public Optional<Customer> findByEmail(String email);

    public List<Customer> findAll();

    public Customer save(Customer customer);
}
