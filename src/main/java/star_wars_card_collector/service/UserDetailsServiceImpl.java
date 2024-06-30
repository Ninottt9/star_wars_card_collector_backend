package star_wars_card_collector.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import star_wars_card_collector.model.User;
import star_wars_card_collector.repository.UserRepository;

import java.util.ArrayList;

/**
 * Implementation of Spring Security's UserDetailsService to load user-specific data.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves a UserDetails object based on the username.
     *
     * @param username The username to retrieve UserDetails for.
     * @return UserDetails containing user details fetched from the database.
     * @throws UsernameNotFoundException If the username is not found in the database.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>() // Assuming roles are not implemented in User entity
        );
    }
}
