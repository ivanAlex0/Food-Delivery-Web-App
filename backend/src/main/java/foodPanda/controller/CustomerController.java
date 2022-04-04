package foodPanda.controller;

import foodPanda.model.APIResponse;
import foodPanda.model.Customer;
import foodPanda.model.DTOs.AccountDTO;
import foodPanda.model.Restaurant;
import foodPanda.service.impl.CustomerServiceImpl;
import foodPanda.service.impl.RestaurantServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    CustomerServiceImpl customerServiceImpl;

    @Autowired
    RestaurantServiceImpl restaurantServiceImpl;

    @PostMapping("register")
    public ResponseEntity<Customer> register(@RequestBody(required = false) Customer customer) {
        return new ResponseEntity<>(customerServiceImpl.save(customer), HttpStatus.CREATED);
    }

    @PostMapping("auth")
    public ResponseEntity<Customer> authenticate(@RequestBody(required = false) AccountDTO accountDTO) {
        return new ResponseEntity<>(customerServiceImpl.authenticate(accountDTO), HttpStatus.OK);
    }

    @GetMapping("fetchRestaurants")
    public ResponseEntity<APIResponse<Restaurant>> fetchRestaurants() {
        return new ResponseEntity<>(APIResponse.<Restaurant>builder().response(restaurantServiceImpl.fetchRestaurants()).build(), HttpStatus.OK);
    }
}
