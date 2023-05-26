package ru.tolstonogova.littlemouseybot.service.impl;

import org.springframework.stereotype.Service;
import ru.tolstonogova.littlemouseybot.service.ChatService;

@Service
public class ChatServiceImpl implements ChatService {

    @Override
    public String sendMouseUrl() {
        return "https://faunagid.ru/mysh";
    }
}
