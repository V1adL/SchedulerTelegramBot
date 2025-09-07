package org.example.my.clientschedulerbot.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.lang.model.element.Name;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "appointment_date")
    private LocalDateTime appointmentDate;

    @Column(name = "reminder_sent", nullable = false)
    private boolean reminderSent = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Client(String name, LocalDateTime appointmentDate, User user) {
        this.name = name;
        this.appointmentDate = appointmentDate;
        this.user = user;
    }

    public boolean isReminderSent() {
        return reminderSent;
    }

    public void setReminderSent(boolean reminderSent) {
        this.reminderSent = reminderSent;
    }


    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", appointmentDate=" + appointmentDate +
                ", user=" + user +
                '}';
    }

    public String getAppointmentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM | HH:mm");
        return appointmentDate.format(formatter);

    }


}
