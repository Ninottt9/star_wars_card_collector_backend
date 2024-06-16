package star_wars_card_collector.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import star_wars_card_collector.model.Inventory;
import star_wars_card_collector.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("api/inventories")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public List<Inventory> getAllInventories() {
        return inventoryService.getAllInventory();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        Inventory inventory = inventoryService.getInventoryById(id);
        if (inventory != null) {
            return ResponseEntity.ok(inventory);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Inventory createInventory(@RequestBody Inventory inventory) {
        return inventoryService.createInventory(inventory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long id, @RequestBody Inventory inventory) {
        Inventory updatedInventory = inventoryService.updateInventory(id, inventory);
        if (updatedInventory != null) {
            return ResponseEntity.ok(updatedInventory);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }
}
