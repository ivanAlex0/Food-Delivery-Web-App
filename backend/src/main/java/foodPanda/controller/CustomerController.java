package foodPanda.controller;

import foodPanda.exception.InvalidInputException;
import foodPanda.model.*;
import foodPanda.model.DTOs.AccountDTO;
import foodPanda.service.impl.CustomerServiceImpl;
import foodPanda.service.impl.RestaurantServiceImpl;
import foodPanda.service.impl.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The Controller provides all the endpoints to be used by a customer from the frontend
 */
@RestController
@RequestMapping("customer")
public class CustomerController {

    private final Logger LOGGER = LogManager.getLogger(CustomerController.class);

    @Autowired
    CustomerServiceImpl customerServiceImpl;

    @Autowired
    RestaurantServiceImpl restaurantServiceImpl;

    @Autowired
    UserServiceImpl userServiceImpl;

    /**
     * Registers a new customer in the DB with the information from the Customer parameter
     *
     * @param customerRegister The body of the request converted to a CustomerRegister {customer: {email, password, name, address, locationZone}, user: {email, password}} that has to be registered
     * @return A ResponseEntity object with the newly created Customer(HTTPStatus.CREATED) or an error otherwise
     */
    @PostMapping("register")
    public ResponseEntity<Customer> register(@RequestBody(required = false) CustomerRegister customerRegister) {
        LOGGER.info("New register request with email=" + customerRegister.getUser().getEmail());
        return new ResponseEntity<>(customerServiceImpl.save(customerRegister), HttpStatus.CREATED);
    }

    /**
     * Checks whether the information from the {accountDTO} matches an entity from the DB and returns the information from the entity
     *
     * @param accountDTO The body of the request converted to an AdminDTO {credential, password}
     * @return A ResponseEntity object with the authenticated Customer(HTTPStatus.OK) or an error otherwise
     */
    @PostMapping("auth")
    public ResponseEntity<Customer> authenticate(@RequestBody(required = false) AccountDTO accountDTO) {
        LOGGER.info("New authentication request with email=" + accountDTO.getCredential());
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
    public ResponseEntity<PandaOrder> placeOrder(@RequestParam(name = "restaurantId", required = false) Long restaurantId, @RequestParam(name = "customerId", required = false) Long customerId, @RequestParam(name = "details", required = false) String details, @RequestBody(required = false) PandaOrder order) throws InvalidInputException {
        LOGGER.info("New placeOrder request with restaurantId=" + restaurantId + " and customerId=" + customerId);
        return new ResponseEntity<>(customerServiceImpl.placeOrder(restaurantId, customerId, details, order), HttpStatus.CREATED);
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
        LOGGER.info("New fetchOrdersForCustomer request with customerId=" + customerId);
        return new ResponseEntity<>(APIResponse.<PandaOrder>builder().response(customerServiceImpl.fetchOrdersForCustomer(customerId)).build(), HttpStatus.OK);
    }

    /**
     * The method provides the user with a new accessToken whenever it expires by using the already provided refreshToken
     *
     * @param request  The HTTP request
     * @param response The HTTP response
     * @throws IOException Whenever something didn't work well - request writing / token generation
     */
    @GetMapping("refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.info("New refreshToken request");
        userServiceImpl.refreshToken(request, response);
    }
}
