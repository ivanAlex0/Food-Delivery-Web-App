package foodPanda.repository;

import foodPanda.model.Administrator;
import foodPanda.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The Repository that handles {@link Administrator} database transactions
 */
public interface AdministratorRepository extends JpaRepository<Administrator, Long> {


    /**
     * Finds an Administrator corresponding to a User
     *
     * @param user The User that corresponds to the Administrator we are searching
     * @return The Administrator instance wrapped in an {@link Optional}
     */
    Optional<Administrator> findByUser(User user);
}
