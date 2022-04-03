package foodPanda.service.impl;

import foodPanda.exception.InvalidInputException;
import foodPanda.model.Menu;
import foodPanda.repository.CategoryRepository;
import foodPanda.repository.FoodRepository;
import foodPanda.repository.RestaurantRepository;
import foodPanda.service.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Menu fetchMenu(Long restaurantId) throws InvalidInputException {
        if (restaurantId == null)
            throw new InvalidInputException("Restaurant id cannot be null.");

        return restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new RuntimeException("No restaurant found for restaurantId=" + restaurantId)
        ).getMenu();
    }
}
