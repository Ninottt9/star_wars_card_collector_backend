package star_wars_card_collector.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import star_wars_card_collector.model.Inventory;
import star_wars_card_collector.repository.InventoryRepository;
import star_wars_card_collector.repository.UserRepository;
import star_wars_card_collector.service.UserDetailsServiceImpl;
import star_wars_card_collector.util.JwtUtil;
import star_wars_card_collector.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller class that handles authentication and registration operations.
 */
@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    /**
     * Authenticates a user based on username and password, generates a JWT token upon successful authentication.
     *
     * @param user The User object containing username and password.
     * @return Map with a JWT token upon successful authentication.
     * @throws Exception If authentication fails due to incorrect username or password.
     */
    @PostMapping("/authenticate")
    public Map<String, String> createAuthenticationToken(@RequestBody User user) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);

        return response;
    }

    /**
     * Registers a new user by encrypting the password, creating a new inventory, and saving the user to the database.
     *
     * @param user The User object containing username, password, and optionally email and description.
     * @return The registered User object with encoded password and assigned inventory.
     */
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        // Encode password using BCryptPasswordEncoder
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        // Create a new empty inventory for the user
        Inventory inventory = new Inventory();
        inventory.setNames(new ArrayList<>()); // Initialize with an empty array or leave it null

        // Save inventory to get its ID
        inventory = inventoryRepository.save(inventory);

        // Set the inventory_id for the user
        user.setInventory(inventory);

        // Save the user to the database
        return userRepository.save(user);
    }
}
