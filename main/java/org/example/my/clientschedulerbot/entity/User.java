package org.example.my.clientschedulerbot.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @Column(name = "user_name", nullable = false)
    private String userName;
    @Column(name = "chat_id", nullable = false)
    private long chatId;

    @OneToMany(mappedBy = "user")
    private List<Client> clients;

    public User(String userName, long chatId) {
        this.userName = userName;
        this.chatId = chatId;
    }

}
