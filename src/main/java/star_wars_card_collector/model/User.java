package star_wars_card_collector.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    private String password;
    private String email;
    private String description;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;
}
