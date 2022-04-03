package foodPanda.service.impl;

import foodPanda.exception.InsufficientArgumentsException;
import foodPanda.exception.InvalidCredentialsException;
import foodPanda.exception.InvalidInputException;
import foodPanda.exception.ResourceNotFoundException;
import foodPanda.model.*;
import foodPanda.model.DTOs.AdminDTO;
import foodPanda.repository.*;
import foodPanda.service.services.AdministratorService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

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
    private ZoneRepository zoneRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public Administrator saveAdministrator(Administrator administrator) throws InvalidInputException {
        if (!isEmailValid(administrator.getEmail())) {
            throw new InvalidInputException("Email is not valid!");
        } else if (!isPasswordValid(administrator.getPassword())) {
            throw new InvalidInputException("Password does not meet the requirements\n-At least 8 characters long\n-At least a digit\nAt least a letter");
        } else {
            Administrator _admin = administratorRepository.save(
                    Administrator
                            .builder()
                            .email(administrator.getEmail())
                            .password(BCrypt.hashpw(administrator.getPassword(), BCrypt.gensalt()))
                            .build());
            _admin.setPassword("********");
            return _admin;
        }
    }

    @Override
    public Administrator authenticate(AdminDTO adminDTO) throws InvalidCredentialsException {
        Administrator _admin = administratorRepository.findByEmail(adminDTO.getCredential()).orElseThrow(
                () -> new InvalidCredentialsException("Invalid credentials"));

        if (BCrypt.checkpw(adminDTO.getPassword(), _admin.getPassword())) {
            _admin.setPassword("********");
            return _admin;
        } else throw new RuntimeException("Invalid credentials");
    }

    @Override
    public Restaurant addRestaurant(Long adminId, Restaurant restaurant) {
        Administrator _admin = getCurrentAdmin(adminId);

        if (_admin.getRestaurant() != null) {
            throw new RuntimeException("Restaurant already added for this administrator account");
        } else {
            if (restaurant.getName().isEmpty() || restaurant.getLocation().isEmpty()) {
                throw new InvalidInputException("Name and location of the restaurant cannot be null");
            } else if (restaurant.getLocationZone() == null || restaurant.getLocationZone().getId() == null) {
                throw new InvalidInputException("Location zone cannot be null, please select one");
            } else {
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
            }
        }
    }

    @Override
    public Food addFoodForCategory(Long categoryId, Food food) throws RuntimeException {
        if (categoryId == null)
            throw new InsufficientArgumentsException("No category selected! Please select one first!");
        if (food == null)
            throw new InvalidInputException("You might have omitted some fields, please contact the support team.");
        if (food.getName() == null || food.getName().isEmpty())
            throw new InvalidInputException("Food name cannot be empty");
        if (food.getDescription() == null || food.getDescription().isEmpty())
            throw new InvalidInputException("Food description cannot be empty");
        if (food.getPrice() == null)
            throw new InvalidInputException("Food price cannot be null");

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

    public List<Category> fetchAllCategories() {
        return categoryRepository.findAll();
    }

    public Administrator getCurrentAdmin(Long adminId) throws RuntimeException {
        return administratorRepository.findById(adminId).orElseThrow(
                () -> new RuntimeException("No administrator found for adminId=" + adminId)
        );
    }


    //////////////////////////////////////////////////////////////////////////////////unused

    @Override
    public List<Administrator> fetchAll() {
        return administratorRepository.findAll();
    }

    @Override
    public Administrator findAdministratorById(Long id) {
        return administratorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Administrator", "adminId", id));
    }

    @Override
    public Administrator updateAdmin(Administrator administrator, Long id) {
        Administrator _existing = administratorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Administrator", "adminId", id));

        administrator.setAdminId(id);
        BeanUtils.copyProperties(administrator, _existing);
        administratorRepository.save(_existing);

        return _existing;
    }

    String emailRegex = "^(.+)@(.+)$";
    Pattern emailPattern = Pattern.compile(emailRegex);

    String passwordRegex = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8,}$";
    Pattern passwordPattern = Pattern.compile(passwordRegex);

    public boolean isEmailValid(String email) {
        return emailPattern.matcher(email).matches();
    }

    public boolean isPasswordValid(String password) {
        return passwordPattern.matcher(password).matches();
    }

}