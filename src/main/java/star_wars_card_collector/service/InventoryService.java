package star_wars_card_collector.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import star_wars_card_collector.model.Inventory;
import star_wars_card_collector.repository.InventoryRepository;

import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id).orElse(null);
    }

    public Inventory createInventory() {
        Inventory inventory = new Inventory();
         return inventoryRepository.save(inventory);
    }

    public Inventory updateInventory(Long id, Inventory inventory) {
        if (inventoryRepository.existsById(id)) {
            inventory.setId(id);
            return inventoryRepository.save(inventory);
        }
        return null;
    }

    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }
}
