package star_wars_card_collector.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import star_wars_card_collector.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
