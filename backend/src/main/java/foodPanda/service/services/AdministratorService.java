package foodPanda.service.services;

import foodPanda.model.Administrator;
import foodPanda.model.DTOs.AccountDTO;
import foodPanda.model.Food;
import foodPanda.model.Restaurant;

public interface AdministratorService {

    Administrator saveAdministrator(AccountDTO accountDTO);

    Administrator authenticate(AccountDTO accountDTO);

    Restaurant addRestaurant(Long adminId, Restaurant restaurant);

    Food addFoodForCategory(Long categoryId, Food food);

}
