package star_wars_card_collector.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import star_wars_card_collector.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
