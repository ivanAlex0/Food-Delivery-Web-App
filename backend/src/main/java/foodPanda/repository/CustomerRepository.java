package foodPanda.repository;

import foodPanda.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The Repository that handles {@link Customer} database transactions
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Finds a Customer by its id
     *
     * @param idAccount The id to be searched
     * @return The Customer instance wrapped in an {@link Optional}
     */
    Optional<Customer> findById(Long idAccount);

    /**
     * Finds a Customer by its email
     *
     * @param email The email to be searched
     * @return The Customer instance wrapped in an {@link Optional}
     */
    Optional<Customer> findByEmail(String email);

}
