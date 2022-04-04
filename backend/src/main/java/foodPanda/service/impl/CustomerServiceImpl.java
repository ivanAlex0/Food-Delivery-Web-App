package foodPanda.service.impl;

import foodPanda.exception.DuplicateEntryException;
import foodPanda.exception.InvalidInputException;
import foodPanda.model.Customer;
import foodPanda.model.DTOs.AccountDTO;
import foodPanda.repository.CustomerRepository;
import foodPanda.repository.RestaurantRepository;
import foodPanda.service.services.CustomerService;
import foodPanda.service.utils.Validator;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    Validator validator = Validator.getInstance();

    @Override
    public Customer save(Customer customer) {
        if (customer == null || customer.getEmail() == null || customer.getPassword() == null || customer.getName() == null || customer.getAddress() == null || customer.getAddressZone() == null || customer.getAddressZone().getId() == null)
            throw new InvalidInputException("You request body is not a valid Customer object. Please refer to the documentation!");
        if (!validator.isEmailValid(customer.getEmail()))
            throw new InvalidInputException("Email is not valid! It should be a valid email(eg. foodpanda@glovo.com");
        if (!validator.isPasswordValid(customer.getPassword()))
            throw new InvalidInputException("Password does not meet the requirements\n-At least 8 characters long\n-At least a digit\nAt least a letter");
        if (customer.getName().isEmpty())
            throw new InvalidInputException("Customer {email} must not be null");
        if (customer.getAddress().isEmpty())
            throw new InvalidInputException("Customer {email} must not be null");

        try {
            Customer _customer = customerRepository.save(
                    Customer
                            .builder()
                            .name(customer.getName())
                            .address(customer.getAddress())
                            .email(customer.getEmail())
                            .password(BCrypt.hashpw(customer.getPassword(), BCrypt.gensalt()))
                            .addressZone(customer.getAddressZone())
                            .build()
            );
            _customer.setPassword("********");
            return _customer;
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new DuplicateEntryException("Email is already registered! Try to login");
        }
    }

    @Override
    public Customer authenticate(AccountDTO accountDTO) {
        if (accountDTO == null || accountDTO.getCredential() == null || accountDTO.getPassword() == null)
            throw new InvalidInputException("You request body is not a valid Customer object. Please refer to the documentation!");

        Customer _customer = customerRepository.findByEmail(accountDTO.getCredential()).orElseThrow(
                () -> new InvalidInputException("Invalid credentials")
        );
        if (BCrypt.checkpw(accountDTO.getPassword(), _customer.getPassword())) {
            _customer.setPassword("********");
            return _customer;
        } else throw new InvalidInputException("Invalid credentials");
    }
}
