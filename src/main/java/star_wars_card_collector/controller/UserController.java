package star_wars_card_collector.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import star_wars_card_collector.model.Inventory;
import star_wars_card_collector.repository.InventoryRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private InventoryRepository inventoryRepository;

    @GetMapping("/inventory")
    public ResponseEntity<?> getInventoryNames(Authentication authentication) {
        // Get logged-in user's username
        String username = authentication.getName();

        // Fetch the user's inventory id based on the username
        Long inventoryId = inventoryRepository.findInventoryIdByUsername(username);

        if (inventoryId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inventory not found for user: " + username);
        }

        // Fetch inventory details based on the inventoryId
        Optional<Inventory> optionalInventory = inventoryRepository.findById(inventoryId);

        if (((Optional<?>) optionalInventory).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inventory not found with id: " + inventoryId);
        }

        // Return the names from the inventory
        List<String> names = optionalInventory.get().getNames();
        return ResponseEntity.ok(names);
    }
}
