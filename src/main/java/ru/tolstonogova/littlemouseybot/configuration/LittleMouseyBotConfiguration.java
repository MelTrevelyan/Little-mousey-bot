package ru.tolstonogova.littlemouseybot.configuration;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.tolstonogova.littlemouseybot.bot.LittleMouseyBot;

@Configuration
public class LittleMouseyBotConfiguration {

    @Bean
    public TelegramBotsApi telegramBotsApi(LittleMouseyBot littleMouseyBot) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(littleMouseyBot);
        return api;
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }
}
