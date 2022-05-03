package foodPanda.service.impl;

import foodPanda.exception.DuplicateEntryException;
import foodPanda.exception.InvalidCredentialsException;
import foodPanda.exception.InvalidInputException;
import foodPanda.model.*;
import foodPanda.model.DTOs.AccountDTO;
import foodPanda.model.states.State;
import foodPanda.repository.*;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class AdministratorServiceImplTest {

    @Mock
    AdministratorRepository administratorRepository;

    @Mock
    RestaurantRepository restaurantRepository;

    @Mock
    MenuRepository menuRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    FoodRepository foodRepository;

    @Mock
    PandaOrderRepository pandaOrderRepository;

    @Mock
    StateRepository stateRepository;

    @InjectMocks
    AdministratorServiceImpl administratorServiceImpl;

    private final Long adminId = 111L;
    private final Long categoryId = 112L;
    private final Long orderId = 113L;

    @Test(expected = InvalidInputException.class)
    public void testSaveAdministrator_invalidCredentials() {
        administratorServiceImpl.saveAdministrator(invalidAccountDTOObject());
    }

    @Test(expected = DuplicateEntryException.class)
    public void testSaveAdministrator_duplicateEntry() {
        Mockito.when(administratorRepository.save(Mockito.any())).thenThrow(DataIntegrityViolationException.class);

        administratorServiceImpl.saveAdministrator(validAccountDTOObject());
    }

    @Test
    public void testSaveAdministrator_success() {
        AccountDTO accountDTO = validAccountDTOObject();
        Mockito.when(administratorRepository.save(Mockito.any())).thenReturn(administratorObjectReturned());

        Administrator _admin = administratorServiceImpl.saveAdministrator(accountDTO);

        Assertions.assertEquals(_admin.getEmail(), accountDTO.getCredential());
    }

    @Test(expected = InvalidInputException.class)
    public void testAuthenticate_invalidInput() {
        administratorServiceImpl.authenticate(null);
    }

    @Test(expected = InvalidCredentialsException.class)
    public void testAuthenticate_notFound() {
        Mockito.when(administratorRepository.findByEmail(Mockito.any())).thenReturn(Optional.empty());

        administratorServiceImpl.authenticate(validAccountDTOObjectBadPassword());
    }

    @Test(expected = InvalidInputException.class)
    public void testAuthenticate_invalidPassword() {
        Mockito.when(administratorRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(administratorObjectReturned()));

        administratorServiceImpl.authenticate(validAccountDTOObjectBadPassword());
    }

    @Test
    public void testAuthenticate_success() {
        Mockito.when(administratorRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(administratorObjectReturned()));

        Administrator _administrator = administratorServiceImpl.authenticate(validAccountDTOObject());

        Assertions.assertEquals("********", _administrator.getPassword());
        Assertions.assertEquals(_administrator.getEmail(), administratorObjectReturned().getEmail());
    }

    @Test(expected = RuntimeException.class)
    public void testAddRestaurant_invalidAdminId() {
        administratorServiceImpl.addRestaurant(adminId, invalidRestaurantObject());
    }

    @Test(expected = InvalidInputException.class)
    public void testAddRestaurant_invalidRestaurantObject() {
        Mockito.when(administratorRepository.findById(Mockito.any())).thenReturn(Optional.of(administratorObjectReturned()));

        administratorServiceImpl.addRestaurant(adminId, invalidRestaurantObject());
    }

    @Test(expected = DuplicateEntryException.class)
    public void testAddRestaurant_duplicateEntry() {
        Mockito.when(administratorRepository.findById(Mockito.any())).thenReturn(Optional.of(administratorObjectReturned()));
        Mockito.when(restaurantRepository.save(Mockito.any())).thenThrow(DataIntegrityViolationException.class);

        administratorServiceImpl.addRestaurant(adminId, validRestaurantObject());
    }

    @Test
    public void testAddRestaurant_success() {
        Mockito.when(administratorRepository.findById(Mockito.any())).thenReturn(Optional.of(administratorObjectReturned()));
        Mockito.when(restaurantRepository.save(Mockito.any())).thenReturn(validRestaurantObject());
        Mockito.when(menuRepository.save(Mockito.any())).thenReturn(new Menu());
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(new Category());

        Restaurant _restaurant = administratorServiceImpl.addRestaurant(adminId, validRestaurantObject());

        Assertions.assertEquals(_restaurant, validRestaurantObject());
    }

    @Test(expected = RuntimeException.class)
    public void testAddFoodForCategory_invalidCategoryId() {
        Mockito.when(categoryRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        administratorServiceImpl.addFoodForCategory(categoryId, validFoodObject());
    }

    @Test(expected = InvalidInputException.class)
    public void testAddFoodForCategory_invalidInput() {
        administratorServiceImpl.addFoodForCategory(categoryId, invalidFoodObject());
    }

    @Test
    public void testAddFoodForCategory_success() {
        Mockito.when(categoryRepository.findById(Mockito.any())).thenReturn(Optional.of(new Category()));
        Mockito.when(foodRepository.save(Mockito.any())).thenReturn(validFoodObject());

        Food _food = administratorServiceImpl.addFoodForCategory(categoryId, validFoodObject());

        Assertions.assertEquals(_food, validFoodObject());
    }

    @Test(expected = RuntimeException.class)
    public void testChangeOrderStatus_invalidOrderId() {
        Mockito.when(pandaOrderRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        administratorServiceImpl.changeOrderStatus(orderId, OrderStatus.PENDING);
    }

    @Test(expected = InvalidInputException.class)
    public void testChangeOrderStatus_invalidChange() {
        Mockito.when(pandaOrderRepository.findById(Mockito.any())).thenReturn(Optional.of(validPandaOrderObject(OrderStatus.PENDING)));

        administratorServiceImpl.changeOrderStatus(orderId, OrderStatus.DELIVERED);
    }

    @Test
    public void testChangeOrderStatus_success(){
        Mockito.when(pandaOrderRepository.findById(Mockito.any())).thenReturn(Optional.of(validPandaOrderObject(OrderStatus.IN_DELIVERY)));
        Mockito.when(stateRepository.findByOrderStatus(OrderStatus.DELIVERED)).thenReturn(State.builder().orderStatus(OrderStatus.DELIVERED).build());

        PandaOrder _pandaOrder = administratorServiceImpl.changeOrderStatus(orderId, OrderStatus.DELIVERED);

        Assertions.assertEquals(_pandaOrder.getState().getOrderStatus(), OrderStatus.DELIVERED);
    }


    public AccountDTO invalidAccountDTOObject() {
        return AccountDTO
                .builder()
                .credential("abC123..")
                .password("xx00-=+")
                .build();
    }

    public AccountDTO validAccountDTOObject() {
        return AccountDTO
                .builder()
                .credential("customer@com")
                .password("12345678Alex*")
                .build();
    }

    public AccountDTO validAccountDTOObjectBadPassword() {
        return AccountDTO
                .builder()
                .credential("customer@com")
                .password("12345678Alex")
                .build();
    }

    public Administrator administratorObjectReturned() {
        return Administrator
                .builder()
                .email("customer@com")
                .password(BCrypt.hashpw("12345678Alex*", BCrypt.gensalt()))
                .build();
    }

    public Restaurant invalidRestaurantObject() {
        return Restaurant
                .builder()
                .name("")
                .location("")
                .locationZone(new Zone())
                .deliveryZones(new ArrayList<>())
                .build();
    }

    public Restaurant validRestaurantObject() {
        return Restaurant
                .builder()
                .name("restaurant")
                .location("location")
                .locationZone(new Zone())
                .deliveryZones(new ArrayList<>())
                .build();
    }

    public Food validFoodObject() {
        return Food
                .builder()
                .name("name")
                .price(1.0f)
                .description("description")
                .category(new Category())
                .foodList(new ArrayList<>())
                .build();
    }

    public Food invalidFoodObject() {
        return Food
                .builder()
                .name("")
                .price(null)
                .description("")
                .category(null)
                .foodList(null)
                .build();
    }

    public PandaOrder validPandaOrderObject(OrderStatus orderStatus) {
        return PandaOrder
                .builder()
                .state(new State(orderId, orderStatus))
                .restaurantName("restaurant")
                .products(new ArrayList<>())
                .customer(new Customer())
                .build();
    }
}
