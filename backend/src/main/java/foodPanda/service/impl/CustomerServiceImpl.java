package foodPanda.service.impl;

import foodPanda.exception.DuplicateEntryException;
import foodPanda.exception.InvalidInputException;
import foodPanda.model.*;
import foodPanda.model.DTOs.AccountDTO;
import foodPanda.repository.*;
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

    @Autowired
    PandaOrderRepository pandaOrderRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

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

    @Override
    public PandaOrder placeOrder(Long restaurantId, Long customerId, PandaOrder order) {
        if (restaurantId == null)
            throw new InvalidInputException("Required request parameter {restaurantId} cannot be null or missing");
        if (customerId == null)
            throw new InvalidInputException("Required request parameter {customerId} cannot be null or missing");
        if (order == null || order.getProducts() == null)
            throw new InvalidInputException("You request body is not a valid PandaOrder object. Please refer to the documentation!");
        if (order.getProducts().size() == 0)
            throw new InvalidInputException("The order's list of {product} is empty (size == 0)");

        Customer _customer = customerRepository.findById(customerId).orElseThrow(
                () -> new InvalidInputException("No customer found for customerId=" + customerId)
        );

        Restaurant _restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new InvalidInputException("No restaurant found for restaurantId=" + restaurantId)
        );

        PandaOrder _pandaOrder = pandaOrderRepository.save(
                PandaOrder
                        .builder()
                        .customer(_customer)
                        .products(order.getProducts())
                        .restaurant(_restaurant)
                        .status(OrderStatus.PENDING)
                        .build()
        );
        for (CartItem cartItem : order.getProducts()) {
            Food _foodItem = foodRepository.findById(cartItem.getItem().getFoodId()).orElseThrow(
                    () -> new InvalidInputException("No Food found for foodId=" + cartItem.getItem().getFoodId())
            );
            CartItem _item = cartItemRepository.save(
                    CartItem
                            .builder()
                            .item(_foodItem)
                            .quantity(cartItem.getQuantity())
                            .order(_pandaOrder)
                            .build()
            );
        }
        return _pandaOrder;
    }

    public PandaOrder find(Long id) {
        return pandaOrderRepository.findById(id).orElseThrow(
                () -> new InvalidInputException("no order found for id=" + id)
        );
    }
}
