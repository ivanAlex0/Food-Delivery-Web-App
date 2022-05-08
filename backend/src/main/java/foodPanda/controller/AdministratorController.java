package foodPanda.controller;

import foodPanda.exception.InvalidCredentialsException;
import foodPanda.exception.InvalidInputException;
import foodPanda.model.*;
import foodPanda.model.DTOs.AccountDTO;
import foodPanda.service.impl.AdministratorServiceImpl;
import foodPanda.service.impl.RestaurantServiceImpl;
import foodPanda.service.impl.UserServiceImpl;
import foodPanda.service.impl.ZoneServiceImpl;
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
 * The Controller provides all the endpoints to be used by an administrator from the frontend
 */
@RestController
@RequestMapping("admin")
public class AdministratorController {

    private final Logger LOGGER = LogManager.getLogger(AdministratorController.class);

    @Autowired
    private AdministratorServiceImpl administratorServiceImpl;

    @Autowired
    private ZoneServiceImpl zoneServiceImpl;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private RestaurantServiceImpl restaurantServiceImpl;

    /**
     * Registers a new administrator in the DB with the information from the AccountDTO parameter
     *
     * @param accountDTO The body of the request converted to an AccountDTO {credential, password} that has to be registered
     * @return A ResponseEntity object with the newly created Administrator(HTTPStatus.CREATED) or an error otherwise
     */
    @PostMapping("register")
    public ResponseEntity<Administrator> register(@RequestBody(required = false) AccountDTO accountDTO) throws InvalidInputException {
        LOGGER.info("New register request with email=" + accountDTO.getCredential());
        return new ResponseEntity<>(administratorServiceImpl.saveAdministrator(accountDTO), HttpStatus.CREATED);
    }

    /**
     * Checks whether the information from the {accountDTO} matches an entity from the DB and returns the information from the entity
     *
     * @param accountDTO The body of the request converted to an AdminDTO {credential, password}
     * @return A ResponseEntity object with the authenticated Administrator(HTTPStatus.OK) or an error otherwise
     * @throws InvalidCredentialsException Whenever the combination {credential, password} is incorrect
     */
    @PostMapping("auth")
    public ResponseEntity<Administrator> authenticate(@RequestBody AccountDTO accountDTO) throws InvalidCredentialsException {
        LOGGER.info("New authentication request with email=" + accountDTO.getCredential());
        return new ResponseEntity<>(administratorServiceImpl.authenticate(accountDTO), HttpStatus.OK);
    }

    /**
     * Saves a new Restaurant instance in the DB, linking it to the Admin corresponding with the adminId specified
     *
     * @param adminId    The adminId that corresponds to the Administrator that will manage the restaurant from below
     * @param restaurant The body of the request converted to a Restaurant {name, location, locationZone} object
     * @return A ResponseEntity object with the newly added Restaurant(HTTPStatus.CREATED) or an error otherwise
     * @throws InvalidInputException Whenever some input is invalid (it includes missing input)
     */
    @PostMapping("addRestaurant")
    public ResponseEntity<Restaurant> addRestaurant(@RequestParam(name = "id") long adminId, @RequestBody Restaurant restaurant) throws InvalidInputException {
        LOGGER.info("New addRestaurant request for adminId=" + adminId);
        return new ResponseEntity<>(administratorServiceImpl.addRestaurant(adminId, restaurant), HttpStatus.CREATED);
    }

    /**
     * Saves a new Food instance in the DB, linking it to the Category corresponding to the categoryId specified
     *
     * @param categoryId The categoryId that corresponds to the Category that of the Food that will be added
     * @param food       The body of the request converted to a Food {name, description, price, category} object
     * @return A ResponseEntity object with the newly added Food(HTTPStatus.CREATED) or an error otherwise
     */
    @PostMapping("addFood")
    public ResponseEntity<Food> addFood(@RequestParam(name = "categoryId", required = false) Long categoryId, @RequestBody(required = false) Food food) {
        LOGGER.info("New addFood request with categoryId=" + categoryId);
        return new ResponseEntity<>(administratorServiceImpl.addFoodForCategory(categoryId, food), HttpStatus.CREATED);
    }

    /**
     * Fetches from the DB the Menu instance of a specified restaurant
     *
     * @param restaurantId The restaurantId that corresponds to the Restaurant whose Menu is to be fetched
     * @return A ResponseEntity object with the fetched Menu(HTTPStatus.OK) or an error otherwise
     * @throws RuntimeException Whenever restaurantId is not provided
     */
    @GetMapping("fetchMenu")
    public ResponseEntity<Menu> fetchMenu(@RequestParam(name = "restaurantId", required = false) Long restaurantId) throws RuntimeException {
        LOGGER.info("New fetchMenu request with restaurantId=" + restaurantId);
        return new ResponseEntity<>(restaurantServiceImpl.fetchMenu(restaurantId), HttpStatus.OK);
    }

    /**
     * Fetches all the Zones already saved in the DB
     *
     * @return A ResponseEntity object with the fetched list of Zone(HTTPStatus.OK) or an error otherwise
     */
    @GetMapping("fetchZones")
    public ResponseEntity<APIResponse<Zone>> fetchZones() {
        return new ResponseEntity<>(APIResponse.<Zone>builder().response(zoneServiceImpl.fetchAll()).build(), HttpStatus.OK);
    }

    /**
     * Changes the status of a specified order to the given status
     *
     * @param orderId   The id of the order whose status is to be changed
     * @param newStatus The new status that will be applied to the order specified above
     * @return A ResponseEntity object with the changed Order(HTTPStatus.OK) or an error otherwise
     * @throws InvalidInputException Whenever the status change is invalid
     */
    @PostMapping("changeStatus")
    public ResponseEntity<PandaOrder> changeOrderStatus(@RequestParam(name = "orderId", required = false) Long orderId, @RequestParam(name = "status", required = false) OrderStatus newStatus) throws InvalidInputException {
        LOGGER.info("New changeOrderStatus request with orderId=" + orderId);
        return new ResponseEntity<>(administratorServiceImpl.changeOrderStatus(orderId, newStatus), HttpStatus.OK);
    }

    /**
     * Fetches all the orders that correspond to a certain restaurant
     *
     * @param restaurantId The id of the restaurant whose orders are to be fetched
     * @return A ResponseEntity object with the list of Orders(HTTPStatus.OK) or an error otherwise
     * @throws InvalidInputException Whenever restaurantId is not provided or there is no restaurant matching that id
     */
    @GetMapping("fetchOrders")
    public ResponseEntity<APIResponse<PandaOrder>> fetchOrdersForRestaurant(@RequestParam(name = "restaurantId", required = false) Long restaurantId) throws InvalidInputException {
        LOGGER.info("New fetchOrdersForRestaurant request with restaurantId=" + restaurantId);
        return new ResponseEntity<>(APIResponse.<PandaOrder>builder().response(administratorServiceImpl.fetchOrders(restaurantId)).build(), HttpStatus.OK);
    }

    /**
     * Generates a PDF containing the Restaurant's Menu corresponding to the admin with adminId provided
     *
     * @param adminId The Admin that manages the Restaurant
     */
    @GetMapping("generatePDF")
    public void generateMenuPDF(@RequestParam(name = "adminId", required = false) Long adminId) {
        LOGGER.info("New generateMenuPDF request with adminId=" + adminId);
        administratorServiceImpl.generateMenuPDF(adminId);
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
