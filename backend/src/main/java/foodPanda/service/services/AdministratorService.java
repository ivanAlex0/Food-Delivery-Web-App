package foodPanda.service.services;

import foodPanda.model.*;
import foodPanda.model.DTOs.AccountDTO;

import java.util.List;

/**
 * The class that defines all functions to be implemented in the {@link foodPanda.service.impl.AdministratorServiceImpl}
 */
public interface AdministratorService {

    /**
     * Saves an {@link Administrator} instance in the DB
     *
     * @param accountDTO The Administrator information to be saved
     * @return The Administrator instance enhanced with the adminId field
     */
    Administrator saveAdministrator(AccountDTO accountDTO);

    /**
     * Checks whether the information from the {@link AccountDTO} matches an entry in the DB
     *
     * @param accountDTO The actual information to be checked: email and password
     * @return The Administrator instance enhanced with the adminId field
     */
    Administrator authenticate(AccountDTO accountDTO);

    /**
     * Saves a {@link Restaurant} to the DB for a certain {@link Administrator}
     *
     * @param adminId    The id of the Admin that adds the Restaurant
     * @param restaurant The actual Restaurant information to be saved
     * @return The Restaurant instance enhanced with the restaurantId field
     */
    Restaurant addRestaurant(Long adminId, Restaurant restaurant);

    /**
     * Saves a {@link Food} instance in the DB corresponding to a certain {@link Category}
     *
     * @param categoryId The id of the Category to which the food will be saved
     * @param food       The actual Food information to be saved
     * @return The Food instance enhanced with the foodId field
     */
    Food addFoodForCategory(Long categoryId, Food food);

    /**
     * Changes the {@link foodPanda.model.states.State} of a certain {@link PandaOrder} given by a orderId
     *
     * @param orderId     The id of the PandaOrder whose state is to be changed
     * @param orderStatus The new OrderStatus to be set to the PandaOrder
     * @return The changed PandaOrder instance
     */
    PandaOrder changeOrderStatus(Long orderId, OrderStatus orderStatus);

    /**
     * Fetches all {@link PandaOrder}s that correspond to a certain {@link Restaurant}
     *
     * @param restaurantId The id of the Restaurant whose PandaOrders are fetched
     * @return A list of PandaOrders
     */
    List<PandaOrder> fetchOrders(Long restaurantId);

}
