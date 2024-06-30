package star_wars_card_collector.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import star_wars_card_collector.model.Inventory;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findById(Long id);

    @Query("SELECT u.inventory.id FROM User u WHERE u.username = :username")
    Long findInventoryIdByUsername(@Param("username") String username);
    // Method to find Inventory by username
    @Query("SELECT inv FROM Inventory inv JOIN User u ON inv.id = u.inventory.id WHERE u.username = :username")
    Optional<Inventory> findByUsername(@Param("username") String username);
}