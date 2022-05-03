package foodPanda.controller;

import foodPanda.exception.InvalidInputException;
import foodPanda.model.APIResponse;
import foodPanda.model.Customer;
import foodPanda.model.DTOs.AccountDTO;
import foodPanda.model.PandaOrder;
import foodPanda.model.Restaurant;
import foodPanda.service.impl.CustomerServiceImpl;
import foodPanda.service.impl.RestaurantServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The Controller provides all the endpoints to be used by a customer from the frontend
 */
@RestController
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    CustomerServiceImpl customerServiceImpl;

    @Autowired
    RestaurantServiceImpl restaurantServiceImpl;

    /**
     * Registers a new customer in the DB with the information from the Customer parameter
     *
     * @param customer The body of the request converted to a Customer {email, password, name, address, locationZone} that has to be registered
     * @return A ResponseEntity object with the newly created Customer(HTTPStatus.CREATED) or an error otherwise
     */
    @PostMapping("register")
    public ResponseEntity<Customer> register(@RequestBody(required = false) Customer customer) {
        return new ResponseEntity<>(customerServiceImpl.save(customer), HttpStatus.CREATED);
    }

    /**
     * Checks whether the information from the {accountDTO} matches an entity from the DB and returns the information from the entity
     *
     * @param accountDTO The body of the request converted to an AdminDTO {credential, password}
     * @return A ResponseEntity object with the authenticated Customer(HTTPStatus.OK) or an error otherwise
     */
    @PostMapping("auth")
    public ResponseEntity<Customer> authenticate(@RequestBody(required = false) AccountDTO accountDTO) {
        return new ResponseEntity<>(customerServiceImpl.authenticate(accountDTO), HttpStatus.OK);
    }

    /**
     * Places an order for a certain restaurant by a certain customer
     *
     * @param restaurantId The id of the restaurant which the order is placed to
     * @param customerId   The id of the customer who places the order
     * @param order        The actual order that is saved in the DB
     * @return A ResponseEntity object with the newly saved Order(HTTPStatus.CREATED) or an error otherwise
     * @throws InvalidInputException Whenever some input is invalid missing, invalid or not found
     */
    @PostMapping("placeOrder")
    public ResponseEntity<PandaOrder> placeOrder(@RequestParam(name = "restaurantId", required = false) Long restaurantId, @RequestParam(name = "customerId", required = false) Long customerId, @RequestBody(required = false) PandaOrder order) throws InvalidInputException {
        return new ResponseEntity<>(customerServiceImpl.placeOrder(restaurantId, customerId, order), HttpStatus.CREATED);
    }

    /**
     * Fetches all the Restaurants already saved in the DB
     *
     * @return A ResponseEntity object with the fetched list of Restaurants(HTTPStatus.OK) or an error otherwise
     * @throws InvalidInputException Whenever some input is invalid missing, invalid or not found
     */
    @GetMapping("fetchRestaurants")
    public ResponseEntity<APIResponse<Restaurant>> fetchRestaurants() throws InvalidInputException {
        return new ResponseEntity<>(APIResponse.<Restaurant>builder().response(restaurantServiceImpl.fetchRestaurants()).build(), HttpStatus.OK);
    }

    /**
     * Fetches all orders that correspond to a certain customer
     *
     * @param customerId The id of the customer whose orders we want to fetch
     * @return A ResponseEntity object with the list of Orders(HTTPStatus.CREATED) or an error otherwise
     * @throws InvalidInputException Whenever some input is invalid missing, invalid or the customer is not found
     */
    @GetMapping("fetchOrders")
    public ResponseEntity<APIResponse<PandaOrder>> fetchOrdersForCustomer(@RequestParam(name = "customerId") Long customerId) throws InvalidInputException {
        return new ResponseEntity<>(APIResponse.<PandaOrder>builder().response(customerServiceImpl.fetchOrdersForCustomer(customerId)).build(), HttpStatus.OK);
    }
}
