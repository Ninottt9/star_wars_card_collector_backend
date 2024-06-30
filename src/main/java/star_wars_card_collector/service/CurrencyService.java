package star_wars_card_collector.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import star_wars_card_collector.model.User;
import star_wars_card_collector.repository.UserRepository;

import java.util.List;

@Service
public class CurrencyService {

    @Autowired
    private UserRepository userRepository;

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // Execute every 24 hours
    public void increaseCurrency() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            // Add logic to increase currency (e.g., +10)
            int increaseAmount = 10; // Example increase amount
            user.setCurrency(user.getCurrency() + increaseAmount);
        }

        // Save all updated users
        userRepository.saveAll(users);
    }
}
