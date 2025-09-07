package org.example.my.clientschedulerbot.service;

import org.example.my.clientschedulerbot.entity.Client;
import org.example.my.clientschedulerbot.entity.User;
import org.example.my.clientschedulerbot.repository.ClientRepos;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class ClientService {
    @Autowired
    private ClientRepos clientRepos;

    public  void saveClient(Client client) {
        clientRepos.save(client);
    }

    public void deleteClient(Long id) {
        clientRepos.deleteById(id);
    }
    public List<Client> getClient(User user) {
        return clientRepos.findByUser(user);
    }
    public List<Client> findClientsByAppointmentTimeBetween(LocalDateTime start, LocalDateTime end) {
       return clientRepos.findByAppointmentDateBetweenAndReminderSentFalse(start,end);
    }

}
