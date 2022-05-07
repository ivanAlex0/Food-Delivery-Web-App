package foodPanda.service.impl;

import foodPanda.exception.InvalidInputException;
import foodPanda.model.Menu;
import foodPanda.model.Restaurant;
import foodPanda.repository.RestaurantRepository;
import foodPanda.service.services.RestaurantService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class implements the methods declared in the {@link RestaurantService}
 */
@Service
public class RestaurantServiceImpl implements RestaurantService {

    private static final Logger LOGGER = LogManager.getLogger(RestaurantServiceImpl.class);

    @Autowired
    RestaurantRepository restaurantRepository;

    /**
     * Fetches the {@link Menu} for a given {@link Restaurant}
     *
     * @param restaurantId The id of the Restaurant whose menu is fetched
     * @return The Menu instance from the DB
     * @throws InvalidInputException Whenever some input is missing(Bad Request) or there is no restaurant found for the given restaurantId
     */
    @Override
    public Menu fetchMenu(Long restaurantId) throws InvalidInputException {
        if (restaurantId == null)
            throw new InvalidInputException("Restaurant id cannot be null.");

        return restaurantRepository.findById(restaurantId).orElseThrow(
                () -> {
                    LOGGER.error("No restaurant found for restaurantId=" + restaurantId);
                    throw new RuntimeException("No restaurant found for restaurantId=" + restaurantId);
                }
        ).getMenu();
    }

    /**
     * Fetches all {@link Restaurant}s from the DB
     *
     * @return A List of Restaurants
     */
    @Override
    public List<Restaurant> fetchRestaurants() {
        return restaurantRepository.findAll();
    }
}
