package org.example.my.clientschedulerbot.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class KeyboardFactory {
    public static SendMessage mainKeyboard(long chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Select an action \uD83D\uDC47");
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();

        var addEntry = new InlineKeyboardButton();
        var viewEntry = new InlineKeyboardButton();
        var deleteEntry = new InlineKeyboardButton();

        addEntry.setText("Add a record");
        addEntry.setCallbackData("ADD_ENTRY");

        viewEntry.setText("View a records");
        viewEntry.setCallbackData("VIEW_ENTRY");

        deleteEntry.setText("Delete a record");
        deleteEntry.setCallbackData("DELETE_ENTRY");
        row.add(addEntry);
        row.add(viewEntry);
        row.add(deleteEntry);
        rows.add(row);
        markup.setKeyboard(rows);
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }
    public static SendMessage returnButton(long chatId,String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        var returnButton = new InlineKeyboardButton();
        returnButton.setText("Return");
        returnButton.setCallbackData("RETURN_ENTRY");
        row.add(returnButton);
        rows.add(row);
        markup.setKeyboard(rows);
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

}
