package foodPanda.repository;

import foodPanda.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds an User by Email
     *
     * @param email The email to be searched
     * @return The User instance wrapped in an {@link Optional}
     */
     Optional<User> findByEmail(String email);
}
