package org.example.my.clientschedulerbot.repository;

import org.example.my.clientschedulerbot.entity.Client;
import org.example.my.clientschedulerbot.entity.User;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientRepos extends JpaRepository<Client,Long> {
    List<Client> findByUser(User user);
    List<Client> findByAppointmentDateBetweenAndReminderSentFalse(LocalDateTime start, LocalDateTime end);
}
