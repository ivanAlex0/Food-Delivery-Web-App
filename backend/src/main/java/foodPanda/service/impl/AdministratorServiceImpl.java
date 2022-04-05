package foodPanda.service.impl;

import foodPanda.exception.DuplicateEntryException;
import foodPanda.exception.InsufficientArgumentsException;
import foodPanda.exception.InvalidCredentialsException;
import foodPanda.exception.InvalidInputException;
import foodPanda.model.*;
import foodPanda.model.DTOs.AccountDTO;
import foodPanda.repository.*;
import foodPanda.service.services.AdministratorService;
import foodPanda.service.utils.Validator;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    Validator validator = Validator.getInstance();

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

    @Override
    public Administrator authenticate(AccountDTO accountDTO) throws InvalidCredentialsException {
        if (accountDTO == null || accountDTO.getCredential() == null || accountDTO.getPassword() == null)
            throw new InvalidInputException("You request body is not a valid Administrator object. Please refer to the documentation!");

        Administrator _admin = administratorRepository.findByEmail(accountDTO.getCredential()).orElseThrow(
                () -> new InvalidCredentialsException("Invalid credentials"));

        if (BCrypt.checkpw(accountDTO.getPassword(), _admin.getPassword())) {
            _admin.setPassword("********");
            return _admin;
        } else throw new RuntimeException("Invalid credentials");
    }

    @Override
    public Restaurant addRestaurant(Long adminId, Restaurant restaurant) {
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

    @Override
    public PandaOrder changeOrderStatus(Long orderId, OrderStatus orderStatus) {
        if (orderId == null)
            throw new InvalidInputException("Required request parameter orderId cannot be null or missing");
        if (orderStatus == null)
            throw new InvalidInputException("Required request parameter orderStatus cannot be null or missing");

        PandaOrder _pandaOrder = pandaOrderRepository.findById(orderId).orElseThrow(
                () -> new InvalidInputException("No PandaOrder found for orderId=" + orderId)
        );

        OrderStatus _currentStatus = _pandaOrder.getStatus();

        if (!validStatusChange(_currentStatus, orderStatus))
            throw new InvalidInputException("The status change from {status}=" + _currentStatus + " to {status}=" + orderStatus + " is not valid");

        _pandaOrder.setStatus(orderStatus);
        pandaOrderRepository.save(_pandaOrder);
        return _pandaOrder;
    }

    public boolean validStatusChange(OrderStatus _current, OrderStatus _newStatus) {
        if (_current == OrderStatus.PENDING && _newStatus == OrderStatus.DECLINED)
            return true;
        if (_current == OrderStatus.PENDING && _newStatus == OrderStatus.ACCEPTED)
            return true;
        if (_current == OrderStatus.ACCEPTED && _newStatus == OrderStatus.IN_DELIVERY)
            return true;
        return _current == OrderStatus.IN_DELIVERY && _newStatus == OrderStatus.DELIVERED;
    }
}
