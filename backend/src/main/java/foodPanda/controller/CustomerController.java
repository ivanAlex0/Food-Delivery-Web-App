package foodPanda.controller;

import foodPanda.model.Customer;
import foodPanda.service.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class CustomerController {

    CustomerService customerService;

    @GetMapping("/fetchAll")
    public ResponseEntity<List<Customer>> getAllAccounts() {
        try {
            List<Customer> accounts = new ArrayList<>(customerService.findAll());
            return new ResponseEntity<>(accounts, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/auth")
    public ResponseEntity<Customer> authenticate(@RequestParam(name = "email", defaultValue = "") String email) {
        try {
            Optional<Customer> account = customerService.findByEmail(email);
            return account.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED));
        } catch (Exception exception) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Customer> save(@RequestBody Customer account) {
        try {
            Customer _customer = customerService
                    .save(Customer.builder()
                            .email(account.getEmail())
                            .age(account.getAge())
                            .password(account.getPassword())
                            .build());
            return new ResponseEntity<>(_customer, HttpStatus.CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
