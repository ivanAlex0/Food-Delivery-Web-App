package foodPanda.service.impl;

import foodPanda.exception.DuplicateEntryException;
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
public class CustomerServiceImplTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    RestaurantRepository restaurantRepository;

    @Mock
    PandaOrderRepository pandaOrderRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    StateRepository stateRepository;

    @Mock
    FoodRepository foodRepository;

    @Mock
    CartItemRepository cartItemRepository;

    @InjectMocks
    CustomerServiceImpl customerServiceImpl;

    private final Long zoneId = 111L;
    private final Long orderId = 112L;
    private final Long restaurantId = 113L;
    private final Long customerId = 114L;
    private final Long foodId = 114L;

    /*@Test(expected = InvalidInputException.class)
    public void testSaveCustomer_invalidInput() {
        customerServiceImpl.save(invalidCustomerObject());
    }

    @Test(expected = DuplicateEntryException.class)
    public void testSaveCustomer_duplicateEntry() {
        Mockito.when(customerRepository.save(Mockito.any())).thenThrow(DataIntegrityViolationException.class);

        customerServiceImpl.save(validCustomerObject());
    }

    @Test
    public void testSaveCustomer_success() {
        Mockito.when(customerRepository.save(Mockito.any())).thenReturn(validCustomerObject());

        Customer _customer = customerServiceImpl.save(validCustomerObject());

        Assertions.assertEquals("********", _customer.getPassword());
        Assertions.assertEquals(_customer.getEmail(), validCustomerObject().getEmail());
    }*/

    @Test(expected = InvalidInputException.class)
    public void testAuthenticate_invalidInput() {
        customerServiceImpl.authenticate(invalidAccountDTObject());
    }

    /*@Test(expected = RuntimeException.class)
    public void testAuthenticate_notFound() {
        Mockito.when(customerRepository.findByEmail(Mockito.any())).thenReturn(Optional.empty());

        customerServiceImpl.authenticate(validAccountDTOObject());
    }

    @Test(expected = InvalidInputException.class)
    public void testAuthenticate_invalidPassword() {
        Mockito.when(customerRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(validCustomerObject()));

        customerServiceImpl.authenticate(validAccountDTOObjectBadPassword());
    }

    @Test
    public void testAuthenticate_success() {
        Mockito.when(customerRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(validCustomerObject()));

        Customer _customer = customerServiceImpl.authenticate(validAccountDTOObject());

        Assertions.assertEquals("********", _customer.getPassword());
        Assertions.assertEquals(_customer.getEmail(), validCustomerObject().getEmail());
    }*/

    @Test(expected = InvalidInputException.class)
    public void testPlaceOrder_nullIds() {
        customerServiceImpl.placeOrder(null, null, validPandaOrderObject(OrderStatus.PENDING));
    }

    @Test(expected = InvalidInputException.class)
    public void testPlaceOrder_invalidSize() {
        customerServiceImpl.placeOrder(restaurantId, customerId, validPandaOrderObjectSizeZero(OrderStatus.PENDING));
    }

    @Test(expected = InvalidInputException.class)
    public void testPlaceOrder_customerNotFound() {
        Mockito.when(customerRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        customerServiceImpl.placeOrder(restaurantId, customerId, validPandaOrderObject(OrderStatus.PENDING));
    }

    @Test(expected = InvalidInputException.class)
    public void testPlaceOrder_restaurantNotFound() {
        Mockito.when(customerRepository.findById(Mockito.any())).thenReturn(Optional.of(validCustomerObject()));
        Mockito.when(restaurantRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        customerServiceImpl.placeOrder(restaurantId, customerId, validPandaOrderObject(OrderStatus.PENDING));
    }

    @Test(expected = InvalidInputException.class)
    public void testPlaceOrder_restaurantDoestDeliverToCustomerZone() {
        Mockito.when(customerRepository.findById(Mockito.any())).thenReturn(Optional.of(validCustomerObject()));
        Mockito.when(restaurantRepository.findById(Mockito.any())).thenReturn(Optional.of(validRestaurantObjectZeroZones()));

        customerServiceImpl.placeOrder(restaurantId, customerId, validPandaOrderObject(OrderStatus.PENDING));
    }

    @Test
    public void testPlaceOrder_success() {
        Mockito.when(customerRepository.findById(Mockito.any())).thenReturn(Optional.of(validCustomerObject()));
        Mockito.when(restaurantRepository.findById(Mockito.any())).thenReturn(Optional.of(validRestaurantObject()));
        Mockito.when(pandaOrderRepository.save(Mockito.any())).thenReturn(validPandaOrderObject(OrderStatus.PENDING));
        Mockito.when(stateRepository.findByOrderStatus(OrderStatus.PENDING)).thenReturn(State.builder().orderStatus(OrderStatus.PENDING).build());
        Mockito.when(foodRepository.findById(Mockito.any())).thenReturn(Optional.of(new Food()));
        Mockito.when(cartItemRepository.save(Mockito.any())).thenReturn(new CartItem());

        PandaOrder _pandaOrder = customerServiceImpl.placeOrder(restaurantId, customerId, validPandaOrderObject(OrderStatus.PENDING));

        Assertions.assertEquals(_pandaOrder.getState().getOrderStatus(), OrderStatus.PENDING);
    }

    public AccountDTO invalidAccountDTObject() {
        return AccountDTO
                .builder()
                .credential("abC123..")
                .password("xx00-=+")
                .build();
    }

    public AccountDTO validAccountDTOObjectBadPassword() {
        return AccountDTO
                .builder()
                .credential("customer@com")
                .password("12345678Alex*")
                .build();
    }

    public AccountDTO validAccountDTOObject() {
        return AccountDTO
                .builder()
                .credential("customer@com")
                .password("12345678Alex!")
                .build();
    }

    public Customer invalidCustomerObject() {
        return Customer
                .builder()
                .addressZone(validZoneObject())
                .name("")
                .address("")
                .build();
    }

    public Customer validCustomerObject() {
        return Customer
                .builder()
                .addressZone(validZoneObject())
                .name("name")
                .address("address")
                .build();
    }

    public Zone validZoneObject() {
        return Zone
                .builder()
                .id(zoneId)
                .name("name")
                .build();
    }

    public PandaOrder validPandaOrderObjectSizeZero(OrderStatus orderStatus) {
        return PandaOrder
                .builder()
                .state(new State(orderId, orderStatus))
                .restaurantName("restaurant")
                .products(new ArrayList<>())
                .customer(new Customer())
                .build();
    }

    public PandaOrder validPandaOrderObject(OrderStatus orderStatus) {
        return PandaOrder
                .builder()
                .state(new State(orderId, orderStatus))
                .restaurantName("restaurant")
                .products(new ArrayList<CartItem>() {{
                    add(CartItem
                            .builder()
                            .item(Food
                                    .builder()
                                    .foodId(foodId)
                                    .build()
                            )
                            .build());
                }})
                .customer(new Customer())
                .build();
    }

    public Restaurant validRestaurantObjectZeroZones() {
        return Restaurant
                .builder()
                .name("name")
                .administrator(new Administrator())
                .location("location")
                .locationZone(validZoneObject())
                .deliveryZones(new ArrayList<>())
                .menu(new Menu())
                .build();
    }

    public Restaurant validRestaurantObject() {
        return Restaurant
                .builder()
                .name("name")
                .administrator(new Administrator())
                .location("location")
                .locationZone(validZoneObject())
                .deliveryZones(new ArrayList<Zone>() {{
                    add(validZoneObject());
                }})
                .menu(new Menu())
                .build();
    }
}
