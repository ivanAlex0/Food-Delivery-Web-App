package foodPanda.service.services;

import foodPanda.model.*;
import foodPanda.model.DTOs.AccountDTO;

public interface AdministratorService {

    Administrator saveAdministrator(AccountDTO accountDTO);

    Administrator authenticate(AccountDTO accountDTO);

    Restaurant addRestaurant(Long adminId, Restaurant restaurant);

    Food addFoodForCategory(Long categoryId, Food food);

    PandaOrder changeOrderStatus(Long orderId, OrderStatus orderStatus);

}
