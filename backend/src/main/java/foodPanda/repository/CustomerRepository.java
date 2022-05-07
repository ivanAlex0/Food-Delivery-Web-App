package foodPanda.repository;

import foodPanda.model.Customer;
import foodPanda.model.User;
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
     * Finds a Customer that corresponds to a User
     *
     * @param user The User that corresponds to the Customer we're searching
     * @return The Customer instance wrapped in an {@link Optional}
     */
    Optional<Customer> findByUser(User user);

}
