package star_wars_card_collector.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import star_wars_card_collector.model.Inventory;

import java.util.Optional;

/**
 * Repository interface for managing {@link Inventory} entities.
 * Provides methods to perform CRUD operations and custom queries for {@link Inventory} entities in the database.
 */
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    /**
     * Retrieves an inventory by its ID.
     *
     * @param id the ID of the inventory to retrieve
     * @return an {@link Optional} containing the inventory with the given ID, or empty if not found
     */
    Optional<Inventory> findById(Long id);

    /**
     * Retrieves the ID of the inventory associated with the given username.
     *
     * @param username the username of the user whose inventory ID to retrieve
     * @return the ID of the inventory associated with the user, or null if not found
     */
    @Query("SELECT u.inventory.id FROM User u WHERE u.username = :username")
    Long findInventoryIdByUsername(@Param("username") String username);

    /**
     * Retrieves the inventory associated with the given username.
     *
     * @param username the username of the user whose inventory to retrieve
     * @return an {@link Optional} containing the inventory associated with the user, or empty if not found
     */
    @Query("SELECT inv FROM Inventory inv JOIN User u ON inv.id = u.inventory.id WHERE u.username = :username")
    Optional<Inventory> findByUsername(@Param("username") String username);
}
