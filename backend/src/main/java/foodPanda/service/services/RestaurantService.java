package foodPanda.service.services;

import foodPanda.model.Menu;
import foodPanda.model.Restaurant;

import java.util.List;

/**
 * The class that defines all functions to be implemented in the {@link foodPanda.service.impl.RestaurantServiceImpl}
 */
public interface RestaurantService {

    /**
     * Fetches the {@link Menu} for a certain {@link Restaurant}
     *
     * @param restaurantId The id of the Restaurant whose Menu is fetched
     * @return The fetched Menu that correspond to the Restaurant
     */
    Menu fetchMenu(Long restaurantId);

    /**
     * Fetches all {@link Restaurant}s from the DB
     *
     * @return A list of Restaurants
     */
    List<Restaurant> fetchRestaurants();
}
