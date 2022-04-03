package foodPanda.service.services;

import foodPanda.model.Food;
import foodPanda.model.Menu;

public interface RestaurantService {

    Menu fetchMenu(Long restaurantId);
}
