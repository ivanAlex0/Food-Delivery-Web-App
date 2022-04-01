package foodPanda.repository;

import foodPanda.model.Restaurant;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findRestaurantByName(String name);

    Optional<Restaurant> findByName(@NonNull String name);
}
