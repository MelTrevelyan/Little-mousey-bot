package ru.tolstonogova.littlemouseybot.service;

import ru.tolstonogova.littlemouseybot.exception.BotServiceException;

public interface ExchangeRatesService {

    String getUSDExchangeRate() throws BotServiceException;

    String getEURExchangeRate() throws BotServiceException;
}
