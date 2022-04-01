package foodPanda.controller;

import foodPanda.exception.InvalidCredentialsException;
import foodPanda.exception.InvalidInputException;
import foodPanda.model.Administrator;
import foodPanda.model.Category;
import foodPanda.model.DTOs.AdminDTO;
import foodPanda.model.Food;
import foodPanda.model.Restaurant;
import foodPanda.service.impl.AdministratorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdministratorController {

    @Autowired
    private AdministratorServiceImpl administratorServiceImpl;

    /**
     * @param administrator The administrator to be saved
     * @return A ResponseEntity object with the newly created Administrator(HTTPStatus.CREATED) or an error otherwise
     */
    @PostMapping("save")
    public ResponseEntity<Administrator> saveAdministrator(@RequestBody Administrator administrator) throws InvalidInputException {
        return new ResponseEntity<>(administratorServiceImpl.saveAdministrator(administrator), HttpStatus.CREATED);
    }

    @PostMapping("auth")
    public ResponseEntity<Administrator> authenticate(@RequestBody AdminDTO adminDTO) throws InvalidCredentialsException {
        return new ResponseEntity<>(administratorServiceImpl.authenticate(adminDTO), HttpStatus.OK);
    }

    @PostMapping("addRestaurant")
    public ResponseEntity<Restaurant> addRestaurant(@RequestParam(name = "id") long adminId, @RequestBody Restaurant restaurant) throws InvalidInputException {
        return new ResponseEntity<>(administratorServiceImpl.addRestaurant(adminId, restaurant), HttpStatus.CREATED);
    }

    @PostMapping("addFood")
    public ResponseEntity<Food> addFood(@RequestParam(name = "id") long adminId, @RequestBody Food food) throws InvalidInputException {
        return new ResponseEntity<>(administratorServiceImpl.addFoodForCategory(adminId, food), HttpStatus.CREATED);

    }

    @GetMapping("fetchMenu")
    public ResponseEntity<List<Food>> fetchMenu(@RequestParam(name = "adminId") long adminId) {
        return new ResponseEntity<>(administratorServiceImpl.fetchMenu(adminId), HttpStatus.OK);
    }

    @GetMapping("fetchCategories")
    public ResponseEntity<List<Category>> fetchCategories() {
        return new ResponseEntity<>(administratorServiceImpl.fetchAllCategories(), HttpStatus.OK);
    }


    /**
     * #TOBE DELETED
     *
     * @return All administrators from the database
     */
    @GetMapping("fetchAll")
    public ResponseEntity<List<Administrator>> fetchAll() {
        return new ResponseEntity<>(administratorServiceImpl.fetchAll(), HttpStatus.OK);
    }

    /**
     * @param id The id of the admin to be retrieved
     * @return A ResponseEntity object with the found Administrator(HTTPStatus.OK) or an error otherwise
     */
    @GetMapping("{id}")
    public ResponseEntity<Administrator> findById(@PathVariable("id") long id) {
        return new ResponseEntity<>(administratorServiceImpl.findAdministratorById(id), HttpStatus.OK);
    }

    /**
     * @param id            The id
     * @param administrator The administrator
     * @return A ResponseEntity object with the updated Administrator(HTTPStatus.OK) or an error otherwise
     */
    @PutMapping("{id}")
    public ResponseEntity<Administrator> updateAdministrator(@PathVariable("id") long id,
                                                             @RequestBody Administrator administrator) {
        return new ResponseEntity<>(administratorServiceImpl.updateAdmin(administrator, id), HttpStatus.OK);
    }

    /*@ExceptionHandler(value = InvalidCredentialsException.class)
    public ResponseEntity<APIError> handleInvalidCredentialsException(InvalidCredentialsException invalidCredentialsException, HttpStatus status) {
        return new ResponseEntity<>(new APIError(status, invalidCredentialsException), HttpStatus.CONFLICT);
    }*/
}
