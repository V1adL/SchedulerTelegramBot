package org.example.my.clientschedulerbot.bot;

import org.example.my.clientschedulerbot.db.DataBase;
import org.example.my.clientschedulerbot.entity.Client;
import org.example.my.clientschedulerbot.entity.User;
import org.example.my.clientschedulerbot.state.BotState;
import org.example.my.clientschedulerbot.state.SessionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;


public class TelegramBot extends TelegramLongPollingBot {
    private final DataBase dataBase;

    @Autowired
    private SessionState sessionState;


    public TelegramBot(DefaultBotOptions options, String botToken, DataBase dataBase) {

        super(options, botToken);
        this.dataBase = dataBase;
    }


    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            String message = update.getMessage().getText();
            String userName = update.getMessage().getFrom().getUserName();
            long chatId = update.getMessage().getChatId();
            BotState state = sessionState.getUser(chatId);
            SendMessage sendMessage = new SendMessage();
            if (message.equals("/start")) {
                executeMessage(KeyboardFactory.mainKeyboard(chatId));

            } else if (state == BotState.WAITING_FOR_CLIENT_NAME) {
                sessionState.putClientName(chatId, message);

                sessionState.putUser(chatId, BotState.WAITING_FOR_CLIENT_DATE);

                executeMessage(KeyboardFactory.returnButton(chatId,
                        "Please enter the appointment date in the format (03.12 17:30)"));

            } else if (state == BotState.WAITING_FOR_CLIENT_DATE) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM HH:mm");
                    int currentYear = LocalDate.now().getYear();

                    LocalDateTime clientDate = LocalDateTime.parse(
                            message + " " + currentYear,
                            DateTimeFormatter.ofPattern("dd.MM HH:mm yyyy")
                    );
                    User user = dataBase.getUser(chatId);
                    if (user == null) {
                        user = new User(userName, chatId);
                        dataBase.saveUser(user);
                    }

                    dataBase.saveClient(new Client(sessionState.getClientName(chatId),
                            clientDate,
                            user));
                    sessionState.clearAll();

                    executeMessage(KeyboardFactory.mainKeyboard(chatId));

                } catch (DateTimeParseException e) {
                    sendMessage.setText("The date format is incorrect, please enter the date like (03.12 17:30)");
                    sendMessage.setChatId(chatId);
                    executeMessage(KeyboardFactory.returnButton(chatId, sendMessage.getText()));
                }

            } else if (state == BotState.WAITING_FOR_DELETE_NUMBER) {
                List<Client> clients = dataBase.getClient(dataBase.getUser(chatId));
                try {
                    int id = Integer.parseInt(message);
                    if (id > 0 && id > clients.size()) {
                        sendMessage.setText("Incorrect number entered");
                        sendMessage.setChatId(chatId);
                        executeMessage(KeyboardFactory.returnButton(chatId, sendMessage.getText()));
                    } else {
                        Client client = clients.get(id - 1);
                        dataBase.deleteClient(client.getId());
                        sessionState.clearAll();
                        executeMessage(KeyboardFactory.mainKeyboard(chatId));
                    }

                } catch (NumberFormatException e) {
                    sendMessage.setText("Incorrect number entered");
                    sendMessage.setChatId(chatId);
                    executeMessage(KeyboardFactory.returnButton(chatId, sendMessage.getText()));
                }
            }
        } else if (update.hasCallbackQuery()) {
            String callBackMessage = update.getCallbackQuery().getData();
            long massegId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            SendMessage sendMessage = new SendMessage();

            switch (callBackMessage) {
                case "ADD_ENTRY":

                    sessionState.putUser(chatId, BotState.WAITING_FOR_CLIENT_NAME);
                    executeMessage(KeyboardFactory.returnButton(chatId, "Please enter the name"));
                    break;

                case "VIEW_ENTRY":
                    sessionState.putUser(chatId, BotState.WAITING_FOR_CLIENT_VIEW);
                    viewClients(sendMessage, chatId);
                    break;

                case "DELETE_ENTRY":
                    sessionState.putUser(chatId, BotState.WAITING_FOR_DELETE_NUMBER);
                    viewClients(sendMessage, chatId);
                    break;

                case "RETURN_ENTRY":
                    executeMessage(KeyboardFactory.mainKeyboard(chatId));
                    sessionState.putUser(chatId, BotState.NONE);
                    break;


            }

        }

    }


    public void viewClients(SendMessage sendMessage, long chatId) {

        List<Client> clients = dataBase.getClient(dataBase.getUser(chatId));

        if (clients.isEmpty()) {
            sendMessage.setChatId(chatId);
            sendMessage.setText("No records found.");
            executeMessage(sendMessage);
            sessionState.clearAll();
            executeMessage(KeyboardFactory.mainKeyboard(chatId));
        } else if (sessionState.getUser(chatId) == BotState.WAITING_FOR_DELETE_NUMBER) {

            String message = "📅 Enter the record number to delete:\n";
            int count = 1;
            for (Client entry : clients) {
                message += count + ". Name: " + entry.getName() + ", Date: " + entry.getAppointmentDate() + "\n";
                count++;
            }
            sendMessage.setChatId(chatId);
            sendMessage.setText(message);
            executeMessage(KeyboardFactory.returnButton(chatId, message));

        } else {
            String message = "📅 Records:\n";
            int count = 1;
            for (Client entry : clients) {
                message += count + ". Name: " + entry.getName() + ", Date: " + entry.getAppointmentDate() + "\n";
                count++;
            }
            sendMessage.setChatId(chatId);
            sendMessage.setText(message);
            executeMessage(sendMessage);
            executeMessage(KeyboardFactory.mainKeyboard(chatId));
        }


    }
    public void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        executeMessage(message);
    }


    public void executeMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {

        return "RecordingPpl_bot";
    }


}
