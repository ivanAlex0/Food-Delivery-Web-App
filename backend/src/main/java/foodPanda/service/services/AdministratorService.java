package foodPanda.service.services;

import foodPanda.model.Administrator;
import foodPanda.model.DTOs.AdminDTO;
import foodPanda.model.Food;
import foodPanda.model.Restaurant;

import java.util.List;

public interface AdministratorService {

    Administrator saveAdministrator(Administrator administrator);

    List<Administrator> fetchAll();

    Administrator findAdministratorById(Long id);

    Administrator updateAdmin(Administrator administrator, Long id);

    Administrator authenticate(AdminDTO adminDTO);

    Restaurant addRestaurant(Long adminId, Restaurant restaurant);

    Food addFoodForCategory(Long categoryId, Food food);

}
