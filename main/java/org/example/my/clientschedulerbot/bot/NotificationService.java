package org.example.my.clientschedulerbot.bot;



import org.example.my.clientschedulerbot.entity.Client;
import org.example.my.clientschedulerbot.service.ClientService;
import org.example.my.clientschedulerbot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    private final TelegramBot telegramBot;
    @Autowired
    private ClientService clientService;
    public NotificationService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Scheduled(fixedDelay = 60000)
    public void checkAndSendReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime targetTime = now.plusMinutes(15);

        List<Client> clientsToNotify = clientService.findClientsByAppointmentTimeBetween(now,targetTime);
        for (Client client : clientsToNotify) {

            String message = String.format("Reminder: %s is scheduled for %s in 15 minutes",
                    client.getName(), client.getAppointmentDate());

            telegramBot.sendMessage(client.getUser().getChatId(), message);
            client.setReminderSent(true);
            clientService.saveClient(client);
        }
    }
}
