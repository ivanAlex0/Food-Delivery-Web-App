package foodPanda.service.impl;

import foodPanda.exception.InvalidInputException;
import foodPanda.model.Menu;
import foodPanda.model.Restaurant;
import foodPanda.repository.RestaurantRepository;
import foodPanda.service.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Override
    public Menu fetchMenu(Long restaurantId) throws InvalidInputException {
        if (restaurantId == null)
            throw new InvalidInputException("Restaurant id cannot be null.");

        return restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new RuntimeException("No restaurant found for restaurantId=" + restaurantId)
        ).getMenu();
    }

    @Override
    public List<Restaurant> fetchRestaurants() {
        return restaurantRepository.findAll();
    }
}
