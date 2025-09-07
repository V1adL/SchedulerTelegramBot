package org.example.my.clientschedulerbot.repository;

import org.example.my.clientschedulerbot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepos extends JpaRepository<User,Long> {
       Optional <User> findUserByChatId(long chatId);
}
