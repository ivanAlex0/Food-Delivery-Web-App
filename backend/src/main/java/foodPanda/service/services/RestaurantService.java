package foodPanda.service.services;

import foodPanda.model.Food;
import foodPanda.model.Menu;
import foodPanda.model.Restaurant;

import java.util.List;

public interface RestaurantService {

    Menu fetchMenu(Long restaurantId);

    List<Restaurant> fetchRestaurants();
}
