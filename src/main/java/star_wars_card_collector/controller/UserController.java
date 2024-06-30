package star_wars_card_collector.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import star_wars_card_collector.model.Inventory;
import star_wars_card_collector.model.User;
import star_wars_card_collector.repository.InventoryRepository;
import star_wars_card_collector.repository.UserRepository;
import star_wars_card_collector.service.CurrencyService;
import star_wars_card_collector.service.SwapiService;

import java.util.*;

/**
 * Controller class that handles HTTP requests related to user operations.
 */
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SwapiService swapiService;

    @Autowired
    private CurrencyService currencyService;

    /**
     * Retrieves the names from the inventory of the authenticated user.
     *
     * @param authentication The authentication object representing the authenticated user.
     * @return ResponseEntity with the list of names from the inventory or error message if not found.
     */
    @GetMapping("/inventory")
    public ResponseEntity<?> getInventoryNames(Authentication authentication) {
        String username = authentication.getName();
        Long inventoryId = inventoryRepository.findInventoryIdByUsername(username);

        if (inventoryId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inventory not found for user: " + username);
        }

        Optional<Inventory> optionalInventory = inventoryRepository.findById(inventoryId);

        if (optionalInventory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inventory not found with id: " + inventoryId);
        }

        List<String> names = optionalInventory.get().getNames();
        return ResponseEntity.ok(names);
    }

    /**
     * Pushes a randomly selected Star Wars character's details to the inventory of the authenticated user.
     *
     * @param authentication The authentication object representing the authenticated user.
     * @return ResponseEntity with a success message and remaining currency or error message if not found.
     */
    @PostMapping("/pushToInventory")
    public ResponseEntity<?> pushToInventory(Authentication authentication) {
        String username = authentication.getName();
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + username);
        }

        User user = optionalUser.get();
        int costToPush = 1;

        if (user.getCurrency() < costToPush) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient currency to push to inventory.");
        }

        user.setCurrency(user.getCurrency() - costToPush);
        userRepository.save(user);

        Optional<Inventory> optionalInventory = inventoryRepository.findByUsername(username);

        if (optionalInventory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inventory not found for user: " + username);
        }

        Inventory inventory = optionalInventory.get();
        int peopleCount = swapiService.getPeopleCount();
        Random random = new Random();
        int randomIndex = random.nextInt(peopleCount);
        JSONObject randomPerson = swapiService.getPersonAt(randomIndex);

        JSONObject dataToSave = new JSONObject();
        dataToSave.put("name", randomPerson.getString("name"));
        dataToSave.put("birth_year", randomPerson.getString("birth_year"));
        dataToSave.put("eye_color", randomPerson.getString("eye_color"));
        dataToSave.put("gender", randomPerson.getString("gender"));
        dataToSave.put("hair_color", randomPerson.getString("hair_color"));
        dataToSave.put("height", randomPerson.getString("height"));
        dataToSave.put("mass", randomPerson.getString("mass"));
        dataToSave.put("skin_color", randomPerson.getString("skin_color"));

        inventory.getNames().add(dataToSave.toString());
        inventoryRepository.save(inventory);

        HashMap<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("remainingCurrency", user.getCurrency());

        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves the information of the authenticated user.
     *
     * @param authentication The authentication object representing the authenticated user.
     * @return ResponseEntity with user information or error message if not found.
     */
    @GetMapping("/user")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        String username = authentication.getName();
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + username);
        }

        return ResponseEntity.ok(optionalUser.get());
    }

    /**
     * Updates the information of the authenticated user.
     *
     * @param updatedUser    The updated User object with new information.
     * @param authentication The authentication object representing the authenticated user.
     * @return ResponseEntity with the updated user information or error message if not found.
     */
    @PutMapping("/user")
    public ResponseEntity<?> updateUser(@RequestBody User updatedUser, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + username);
        }

        User existingUser = optionalUser.get();
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setDescription(updatedUser.getDescription());

        userRepository.save(existingUser);

        return ResponseEntity.ok(existingUser);
    }
}
