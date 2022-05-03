package foodPanda.repository;

import foodPanda.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The Repository that handles {@link Administrator} database transactions
 */
public interface AdministratorRepository extends JpaRepository<Administrator, Long> {

    /**
     * Finds an Administrator by Email
     *
     * @param email The email to be searched
     * @return The Administrator instance wrapped in an {@link Optional}
     */
    Optional<Administrator> findByEmail(String email);
}
