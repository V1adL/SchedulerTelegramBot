package org.example.my.clientschedulerbot.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_name", nullable = false)
    private String userName;
    @Column(name = "chat_id", nullable = false)
    private long chatId;



    @OneToMany(mappedBy = "user")
    private List<Client> clients;


    public User() {
    }

    public User(String userName, long chatId) {
        this.userName = userName;
        this.chatId = chatId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getChatId() {
        return chatId;
    }

    public Long getId() {
        return id;
    }
}
