package org.example.my.clientschedulerbot.config;

import org.example.my.clientschedulerbot.bot.TelegramBot;
import org.example.my.clientschedulerbot.db.DataBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TelegramConfig {

   @Bean
    public TelegramBot telegramBot(@Value(value = "${bot.token}" ) String token,
                                   TelegramBotsApi telegramBotsApi,
                                   DataBase dataBase) throws TelegramApiException {
        DefaultBotOptions botOptions = new DefaultBotOptions();
        TelegramBot bot = new TelegramBot(botOptions,token,dataBase);
        telegramBotsApi.registerBot(bot);

        return bot;
   }

   @Bean
   public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
       return new TelegramBotsApi(DefaultBotSession.class);
   }
}
