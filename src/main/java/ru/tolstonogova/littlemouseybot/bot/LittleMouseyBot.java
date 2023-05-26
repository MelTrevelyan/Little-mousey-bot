package ru.tolstonogova.littlemouseybot.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.tolstonogova.littlemouseybot.exception.BotServiceException;
import ru.tolstonogova.littlemouseybot.service.ChatService;
import ru.tolstonogova.littlemouseybot.service.ExchangeRatesService;

import java.time.LocalDate;

@Slf4j
@Component
public class LittleMouseyBot extends TelegramLongPollingBot {

    private static final String START = "/start";
    private static final String USD = "usd";
    private static final String EUR = "eur";
    private static final String HELP = "help";
    private static final String BIO = "you";
    private ExchangeRatesService exchangeRatesService;
    private ChatService chatService;

    public LittleMouseyBot(@Value("${bot.token}") String botToken, ExchangeRatesService exchangeRatesService,
                           ChatService chatService) {
        super(botToken);
        this.exchangeRatesService = exchangeRatesService;
        this.chatService = chatService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        String message = update.getMessage().getText().toLowerCase();
        Long chatId = update.getMessage().getChatId();
        switch (message) {
            case START:
                String userName = update.getMessage().getChat().getUserName();
                startCommand(chatId, userName);
                break;
            case USD:
                usdCommand(chatId);
                break;
            case EUR:
                eurCommand(chatId);
                break;
            case HELP:
                helpCommand(chatId);
                break;
            case BIO:
                tellAboutYouCommand(chatId);
                break;
            default:
                unknownCommand(chatId);
        }
    }

    @Override
    public String getBotUsername() {
        return "Little_mousey_bot";
    }

    private void startCommand(Long chatId, String userName) {
        String text = " Добро пожаловать в мышебот, %s! Я умею не только пищать, но и кое что посерьезней. " +
                "\n" + "Здесь Вы сможете узнать официальные курсы валют на сегодня," +
                " установленные ЦБ РФ. Для этого воспользуйтесь командами:" +
                "\n" + " usd - курс доллара;" +
                "\n" + "eur - курс евро;" +
                "\n" + "Дополнительные команды: " +
                "\n" + "help - получение справки" +
                "\n" + "you - узнать о мыше подробнее;";
        String formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText);
    }

    private void usdCommand(Long chatId) {
        String formattedText;
        try {
            String usd = exchangeRatesService.getUSDExchangeRate();
            String text = "Курс доллара на %s составляет %s рублей";
            formattedText = String.format(text, LocalDate.now(), usd);
        } catch (BotServiceException e) {
            log.error("Ошибка получения курса доллара", e);
            formattedText = "Не удалось получить текущий курс доллара. Попробуйте позже.";
        }
        sendMessage(chatId, formattedText);
    }

    private void eurCommand(Long chatId) {
        String formattedText;
        try {
            String usd = exchangeRatesService.getEURExchangeRate();
            String text = "Курс евро на %s составляет %s рублей";
            formattedText = String.format(text, LocalDate.now(), usd);
        } catch (BotServiceException e) {
            log.error("Ошибка получения курса евро", e);
            formattedText = "Не удалось получить текущий курс евро. Попробуйте позже.";
        }
        sendMessage(chatId, formattedText);
    }

    private void helpCommand(Long chatId) {
        String text = " Справочная информация по боту: " +
                "Для получения текущих курсов валют воспользуйтесь командами: usd - курс доллара, eur - курс евро;" +
                "\n" + "Если хочешь узнать побольше о мыше: you;";
        sendMessage(chatId, text);
    }

    private void tellAboutYouCommand(Long chatId) {
        String message = "Ого, тебе интесно узнать побольше о мышеботе? Я польщен! Открою тебе самое сокровенное.";
        sendMessage(chatId, message);
        String url = chatService.sendMouseUrl();
        sendMessage(chatId, url);
    }

    private void unknownCommand(Long chatId) {
        String text = "Не удалось распознать команду!";
        sendMessage(chatId, text);
    }

    private void sendMessage(Long chatId, String text) {
        String chatIdStr = String.valueOf(chatId);
        SendMessage sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения", e);
        }
    }
}
