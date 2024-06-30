package star_wars_card_collector.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import star_wars_card_collector.model.User;

import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 * Provides methods to perform CRUD operations and query {@link User} entities in the database.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user to retrieve
     * @return an {@link Optional} containing the user with the given username, or empty if not found
     */
    Optional<User> findByUsername(String username);
}
