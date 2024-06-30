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

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        // Encode password
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        // Create a new empty inventory for the user
        Inventory inventory = new Inventory();
        inventory.setNames(new ArrayList<>()); // Initialize with an empty array or leave it null

        // Save inventory to get its ID
        inventory = inventoryRepository.save(inventory);

        // Set the inventory_id for the user
        user.setInventory(inventory);

        // Save the user
        return userRepository.save(user);
    }
}