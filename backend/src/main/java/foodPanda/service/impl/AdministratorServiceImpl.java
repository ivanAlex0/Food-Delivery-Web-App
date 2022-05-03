package foodPanda.service.impl;

import foodPanda.exception.DuplicateEntryException;
import foodPanda.exception.InsufficientArgumentsException;
import foodPanda.exception.InvalidCredentialsException;
import foodPanda.exception.InvalidInputException;
import foodPanda.model.*;
import foodPanda.model.DTOs.AccountDTO;
import foodPanda.model.states.State;
import foodPanda.repository.*;
import foodPanda.service.services.AdministratorService;
import foodPanda.service.utils.Validator;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class implements the methods declared in the {@link AdministratorService}
 */
@Service
public class AdministratorServiceImpl implements AdministratorService {

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private PandaOrderRepository pandaOrderRepository;

    @Autowired
    private StateRepository stateRepository;

    /**
     * The singleton Validator instance which is used to validate the input received from the controller
     */
    Validator validator = Validator.getInstance();


    /**
     * Validates the input received from the {@link foodPanda.controller.AdministratorController} and saves the Administrator entity in the DB using the {@link AdministratorRepository} instance
     * The actual entity which is saved is built using {@link lombok.Builder} from the {@link lombok.Lombok} library
     * The password is encoded before saving it into the DB using {@link BCrypt}'s {@link BCrypt#hashpw(String, String)}
     *
     * @param accountDTO The information of the Administrator entity which is saved in the DB
     * @return The newly saved Administrator entity which is enhanced with the adminId field
     * @throws InvalidInputException Whenever some input is missing(Bad Request), the email or password are invalid or there already exists an Administrator entity with the passed email
     */
    @Override
    public Administrator saveAdministrator(AccountDTO accountDTO) throws InvalidInputException {
        if (accountDTO == null || accountDTO.getCredential() == null || accountDTO.getPassword() == null)
            throw new InvalidInputException("You request body is not a valid Administrator object. Please refer to the documentation!");
        if (!validator.isEmailValid(accountDTO.getCredential()))
            throw new InvalidInputException("Email is not valid! It should be a valid email(eg. foodpanda@glovo.com");
        if (!validator.isPasswordValid(accountDTO.getPassword()))
            throw new InvalidInputException("Password does not meet the requirements\n-At least 8 characters long\n-At least a digit\nAt least a letter");


        try {
            Administrator _admin = administratorRepository.save(
                    Administrator
                            .builder()
                            .email(accountDTO.getCredential())
                            .password(BCrypt.hashpw(accountDTO.getPassword(), BCrypt.gensalt()))
                            .build());
            _admin.setPassword("********");
            return _admin;
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new DuplicateEntryException("Email is already registered! Try to login");
        }

    }

    /**
     * Validates the input received from the {@link foodPanda.controller.AdministratorController} and checks whether the information passed in the {@link AccountDTO} entity matches an entry from the DB
     * The password fetched from the DB is decrypted using {@link BCrypt} and checked against the one provided
     *
     * @param accountDTO The accountDTO that is checked for authentication
     * @return The authenticated Administrator entity that is enhanced with the adminId field
     * @throws InvalidInputException Whenever some input is missing(Bad Request) or there is no match
     */
    @Override
    public Administrator authenticate(AccountDTO accountDTO) throws InvalidCredentialsException {
        if (accountDTO == null || accountDTO.getCredential() == null || accountDTO.getPassword() == null)
            throw new InvalidInputException("You request body is not a valid Administrator object. Please refer to the documentation!");

        Administrator _admin = administratorRepository.findByEmail(accountDTO.getCredential()).orElseThrow(
                () -> new InvalidCredentialsException("Invalid credentials"));

        if (BCrypt.checkpw(accountDTO.getPassword(), _admin.getPassword())) {
            _admin.setPassword("********");
            return _admin;
        } else throw new InvalidInputException("Invalid credentials");
    }

    /**
     * Validates the input received from the {@link foodPanda.controller.AdministratorController} and adds a {@link Restaurant} entity in the DB using the {@link RestaurantRepository} instance
     * The implicit location {@link Zone} of the Restaurant is added to its list of delivery zones if it isn't there already
     * This method also creates a new {@link Menu} instance and all its corresponding {@link Category} list, adding them also in the DB
     *
     * @param adminId    The id of the Administrator which adds the Restaurant
     * @param restaurant The actual Restaurant which is saved in the DB
     * @return The newly added Restaurant entity enhanced with the restaurantId field
     * @throws InvalidInputException Whenever some input is missing(Bad Request), there already is a Restaurant added to the DB for the current admin or there are empty required fields
     */
    @Override
    public Restaurant addRestaurant(Long adminId, Restaurant restaurant) throws InvalidInputException {
        Administrator _admin = administratorRepository.findById(adminId).orElseThrow(
                () -> new RuntimeException("No administrator found for adminId=" + adminId)
        );

        if (_admin.getRestaurant() != null) {
            throw new RuntimeException("Restaurant already added for this administrator account");
        } else {
            if (restaurant == null || restaurant.getName() == null || restaurant.getLocation() == null || restaurant.getLocationZone() == null)
                throw new InvalidInputException("You request body is not a valid Restaurant object. Please refer to the documentation!");
            if (restaurant.getName().isEmpty() || restaurant.getLocation().isEmpty())
                throw new InvalidInputException("Restaurant {name} and {location} cannot be empty");

            if (!restaurant.getDeliveryZones().contains(restaurant.getLocationZone()))
                restaurant.getDeliveryZones().add(restaurant.getLocationZone());

            try {
                Restaurant _restaurant = restaurantRepository.save(
                        Restaurant
                                .builder()
                                .name(restaurant.getName())
                                .location(restaurant.getLocation())
                                .locationZone(restaurant.getLocationZone())
                                .deliveryZones(restaurant.getDeliveryZones())
                                .administrator(_admin)
                                .build()
                );
                Menu _menu = menuRepository.save(
                        Menu
                                .builder()
                                .restaurant(_restaurant)
                                .build()
                );
                for (CategoryType type : CategoryType.values()) {
                    categoryRepository.save(
                            Category
                                    .builder()
                                    .category(type)
                                    .menu(_menu)
                                    .build()
                    );
                }
                return _restaurant;
            } catch (DataIntegrityViolationException dataIntegrityViolationException) {
                throw new DuplicateEntryException("Name of the restaurant is already taken!");
            }
        }
    }

    /**
     * Validates the input received from the {@link foodPanda.controller.AdministratorController} and save to the DB a {@link Food} entity corresponding to a certain {@link Category} given as input
     *
     * @param categoryId the id of the Category to which the Food will be added to
     * @param food       The actual Food that will be saved to the DB
     * @return The newly saved Food entity enhanced with the foodId field
     * @throws RuntimeException Whenever there some input is missing(Bad Request), empty or no category has been provided
     */
    @Override
    public Food addFoodForCategory(Long categoryId, Food food) throws RuntimeException {
        if (categoryId == null)
            throw new InsufficientArgumentsException("No category selected! Please select one first!");
        if (food == null || food.getName() == null || food.getDescription() == null || food.getPrice() == null)
            throw new InvalidInputException("You request body is not a valid Food object. Please refer to the documentation!");
        if (food.getName().isEmpty())
            throw new InvalidInputException("Food {name} cannot be empty");
        if (food.getDescription().isEmpty())
            throw new InvalidInputException("Food {description} cannot be empty");

        Category _category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new RuntimeException("No category found for categoryId=" + categoryId)
        );
        food.setCategory(_category);

        return foodRepository.save(
                Food
                        .builder()
                        .name(food.getName())
                        .description(food.getDescription())
                        .price(food.getPrice())
                        .category(_category)
                        .build()
        );
    }

    /**
     * Validates the input received from the {@link foodPanda.controller.AdministratorController} and changes the {@link OrderStatus} of the {@link PandaOrder} identified by the orderId parameter in the DB
     *
     * @param orderId     The id of the PandaOrder whose OrderStatus is changed
     * @param orderStatus The new OrderStatus to be set
     * @return The changed PandaOrder enhanced with the orderId status
     * @throws InvalidInputException Whenever some input is missing(Bad Request) or the status change is invalid
     */
    @Override
    public PandaOrder changeOrderStatus(Long orderId, OrderStatus orderStatus) throws InvalidInputException {
        if (orderId == null)
            throw new InvalidInputException("Required request parameter {orderId} cannot be null or missing");
        if (orderStatus == null)
            throw new InvalidInputException("Required request parameter {orderStatus} cannot be null or missing");

        PandaOrder _pandaOrder = pandaOrderRepository.findById(orderId).orElseThrow(
                () -> new InvalidInputException("No PandaOrder found for orderId=" + orderId)
        );

        State _state = _pandaOrder.getState();
        OrderStatus _newStatus = _state.changeState(orderStatus);
        State _newState = stateRepository.findByOrderStatus(_newStatus);

        _pandaOrder.setState(_newState);
        pandaOrderRepository.save(_pandaOrder);
        return _pandaOrder;
    }

    /**
     * Fetches all the {@link PandaOrder} that correspond to a {@link Restaurant}
     *
     * @param restaurantId The id that corresponds to the Restaurant whose orders are fetched
     * @return A list of {@link PandaOrder}s
     * @throws InvalidInputException Whenever some input is missing(Bad Request) or there is no restaurant found for the restaurantId provided
     */
    @Override
    public List<PandaOrder> fetchOrders(Long restaurantId) throws InvalidInputException {
        if (restaurantId == null)
            throw new InvalidInputException("Required request parameter {restaurantId} cannot be null or missing");

        Restaurant _restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new InvalidInputException("No restaurant found for restaurantId=" + restaurantId)
        );

        return _restaurant.getOrders();
    }
}
