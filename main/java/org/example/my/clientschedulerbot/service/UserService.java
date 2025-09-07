package org.example.my.clientschedulerbot.service;

import org.example.my.clientschedulerbot.entity.User;
import org.example.my.clientschedulerbot.repository.UserRepos;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepos userRepos;

    public  void saveUser(User user) {
       userRepos.save(user);
    }

    public  User getUser(long chatId) {
        return userRepos.findUserByChatId(chatId).orElse(null);
    }
}
