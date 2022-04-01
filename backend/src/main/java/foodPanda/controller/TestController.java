package foodPanda.controller;

import foodPanda.model.Restaurant;
import foodPanda.model.Zone;
import foodPanda.repository.RestaurantRepository;
import foodPanda.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/test")
public class TestController {

    @Autowired
    ZoneRepository zoneRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @PostMapping("/addZone")
    public ResponseEntity<Zone> save(@RequestBody Zone zone) {
        try {
            Zone _zone = zoneRepository
                    .save(Zone.builder()
                            .name(zone.getName())
                            .build());
            return new ResponseEntity<>(_zone, HttpStatus.CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addRest")
    public ResponseEntity<Restaurant> saveRest(@RequestBody Restaurant restaurant) {
        try {
            Restaurant _restaurant = restaurantRepository
                    .save(Restaurant.builder()
                            .name(restaurant.getName())
                            .location(restaurant.getLocation())
                            .locationZone(restaurant.getLocationZone())
                            .deliveryZones(restaurant.getDeliveryZones())
                            .build());
            return new ResponseEntity<>(_restaurant, HttpStatus.CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/updateZone")
    public ResponseEntity<Restaurant> updateZone(@RequestBody Restaurant restaurant) {
        try {
            Optional<Restaurant> restaurant1 = restaurantRepository.findByName(restaurant.getName());
            System.out.println(restaurant1.get().getLocation());
            restaurant1.ifPresent(value -> value.getDeliveryZones().addAll(restaurant.getDeliveryZones()));
            restaurantRepository.save(restaurant1.get());
            return new ResponseEntity<>(restaurant1.get(), HttpStatus.CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/allRest")
    public ResponseEntity<List<Restaurant>> getAllAccounts() {
        try {
            List<Restaurant> restaurants = new ArrayList<>(restaurantRepository.findAll());
            return new ResponseEntity<>(restaurants, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
