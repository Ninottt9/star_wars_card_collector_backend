package star_wars_card_collector.model;

import lombok.Getter;
import lombok.Setter;
import star_wars_card_collector.model.Inventory;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private String description;

    @OneToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    // Getters and Setters
}

