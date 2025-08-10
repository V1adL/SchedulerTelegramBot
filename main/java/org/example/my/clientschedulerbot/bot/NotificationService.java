package org.example.my.clientschedulerbot.bot;


import org.example.my.clientschedulerbot.db.DataBase;
import org.example.my.clientschedulerbot.entity.Client;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    private final DataBase dataBase;  // твой DAO или репозиторий
    private final TelegramBot telegramBot;

    public NotificationService(DataBase dataBase, TelegramBot telegramBot) {
        this.dataBase = dataBase;
        this.telegramBot = telegramBot;
    }

    @Scheduled(fixedDelay = 60000) // раз в минуту
    public void checkAndSendReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime targetTime = now.plusMinutes(15);

        List<Client> clientsToNotify = dataBase.findClientsByAppointmentTimeBetween(now, targetTime);

        for (Client client : clientsToNotify) {

            String message = String.format("Reminder: %s is scheduled for %s in 15 minutes",
                    client.getName(), client.getAppointmentDate());

            telegramBot.sendMessage(client.getUser().getChatId(), message);
            client.setReminderSent(true);
            dataBase.updateClient(client);
        }
    }
}
