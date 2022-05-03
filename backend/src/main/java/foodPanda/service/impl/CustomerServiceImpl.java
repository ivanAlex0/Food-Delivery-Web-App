package foodPanda.service.impl;

import foodPanda.exception.DuplicateEntryException;
import foodPanda.exception.InvalidInputException;
import foodPanda.model.*;
import foodPanda.model.DTOs.AccountDTO;
import foodPanda.model.states.State;
import foodPanda.repository.*;
import foodPanda.service.services.CustomerService;
import foodPanda.service.utils.Validator;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class implements the methods declared in the {@link CustomerService}
 */
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

    @Autowired
    StateRepository stateRepository;

    /**
     * The singleton Validator instance which is used to validate the input received from the controller
     */
    Validator validator = Validator.getInstance();

    /**
     * Validates the input received from the {@link foodPanda.controller.CustomerController} and saves the Customer entity in the DB using the {@link CustomerRepository}
     * The actual entity which is saved is built using {@link lombok.Builder} from the {@link lombok.Lombok} library
     * The password is encoded before saving it into the DB using {@link BCrypt}'s {@link BCrypt#hashpw(String, String)}
     *
     * @param customer The Customer entity which is saved in the DB
     * @return The newly saved Customer entity which is enhanced with the customerId field
     * @throws InvalidInputException Whenever some input is missing(Bad Request) or the email or password are not valid. It also throws it when the name and address are empty strings.
     */
    @Override
    public Customer save(Customer customer) throws InvalidInputException {
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

    /**
     * Validates the input received from the {@link foodPanda.controller.CustomerController} and checks whether the information passed in the {@link AccountDTO} entity matches an entry from the DB
     * The password fetched from the DB is decrypted using {@link BCrypt} and checked against the one provided
     *
     * @param accountDTO The accountDTO that is checked for authentication
     * @return The authenticated Customer entity that is enhanced with the customerId field
     * @throws InvalidInputException Whenever some input is missing(Bad Request) or there is no match
     */
    @Override
    public Customer authenticate(AccountDTO accountDTO) throws InvalidInputException {
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

    /**
     * Validates the input received from the {@link foodPanda.controller.CustomerController} and places and order to a specified restaurant and by a specified customer
     * For every product in the list of {@link CartItem}s, a new entity of the same type is created, linked to the current order and saved in the DB
     * All placed orders get the initial and default {@link State} {@link OrderStatus#PENDING}
     *
     * @param restaurantId The id of the restaurant which the order will be placed to
     * @param customerId   The id of the customer which places the order
     * @param order        The actual information about the order which is saved in the DB: a list of {@link CartItem}
     * @return The newly saved {@link PandaOrder} instance which is enhanced with the id field
     * @throws InvalidInputException Whenever some input is missing(Bad Request), the list of products has a size less than 1 or the Customer's zone isn't present in the list of delivery zones of the Restaurant
     */
    @Override
    public PandaOrder placeOrder(Long restaurantId, Long customerId, PandaOrder order) throws InvalidInputException {
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

        if (!_restaurant.getDeliveryZones().contains(_customer.getAddressZone()))
            throw new InvalidInputException("The restaurant " + _restaurant.getName() + " doesn't deliver to your zone at the moment");

        State _pendingState = stateRepository.findByOrderStatus(OrderStatus.PENDING);

        PandaOrder _pandaOrder = pandaOrderRepository.save(
                PandaOrder
                        .builder()
                        .customer(_customer)
                        .products(order.getProducts())
                        .restaurant(_restaurant)
                        .state(_pendingState)
                        .restaurantName(_restaurant.getName())
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

    /**
     * Fetches all the orders for a certain customer
     *
     * @param customerId The id of the Customer whose orders are fetched
     * @return A List of {@link PandaOrder} entities
     */
    @Override
    public List<PandaOrder> fetchOrdersForCustomer(Long customerId) {
        return pandaOrderRepository.findAllByCustomer_CustomerId(customerId);
    }
}
