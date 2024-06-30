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

    @PostMapping("/pushToInventory")
    public ResponseEntity<?> pushToInventory(Authentication authentication) {
        // Get logged-in user's username
        String username = authentication.getName();

        // Fetch the user based on username
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + username);
        }

        // Get the user entity
        User user = optionalUser.get();

        // Check if user has enough currency
        int costToPush = 1; // Example cost to push to inventory
        if (user.getCurrency() < costToPush) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient currency to push to inventory.");
        }

        // Decrease user's currency
        user.setCurrency(user.getCurrency() - costToPush);
        // Save the updated user
        userRepository.save(user);

        // Fetch the user's inventory based on username
        Optional<Inventory> optionalInventory = inventoryRepository.findByUsername(username);

        if (optionalInventory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inventory not found for user: " + username);
        }

        // Get the inventory entity
        Inventory inventory = optionalInventory.get();

        int peopleCount = swapiService.getPeopleCount();
        // Generate a random index within the range of peopleCount
        Random random = new Random();
        int randomIndex = random.nextInt(peopleCount);

        // Get the random name from results
        JSONObject randomPerson = swapiService.getPersonAt(randomIndex);

        JSONObject dataToSave = new JSONObject();
        dataToSave.put("name", randomPerson.getString("name"));
        dataToSave.put("birth_year", randomPerson.getString("birth_year"));
        dataToSave.put("eye_color", randomPerson.getString("eye_color"));
        dataToSave.put("gender", randomPerson.getString("gender"));
        dataToSave.put("hair_color", randomPerson.getString("hair_color"));
        dataToSave.put("height", randomPerson.getString("height"));
        dataToSave.put("mass", randomPerson.getString("mass"));
        dataToSave.put("mass", randomPerson.getString("mass"));
        dataToSave.put("skin_color", randomPerson.getString("skin_color"));

        // Push the random person's details to the inventory names array
        inventory.getNames().add(dataToSave.toString());

        // Save the updated inventory
        inventoryRepository.save(inventory);

        // Prepare the response HashMap with remaining currency
        HashMap<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("remainingCurrency", user.getCurrency());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        // Get logged-in user's username
        String username = authentication.getName();

        // Fetch the user based on username
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + username);
        }

        // Return user information
        return ResponseEntity.ok(optionalUser.get());
    }

    @PutMapping("/user")
    public ResponseEntity<?> updateUser(@RequestBody User updatedUser, Authentication authentication) {
        // Get logged-in user's username
        String username = authentication.getName();

        // Fetch the existing user entity
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + username);
        }

        // Update user information
        User existingUser = optionalUser.get();
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setDescription(updatedUser.getDescription());

        // Save the updated user
        userRepository.save(existingUser);

        return ResponseEntity.ok(existingUser);
    }
}
