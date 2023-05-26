package ru.tolstonogova.littlemouseybot.client;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.tolstonogova.littlemouseybot.exception.BotServiceException;

import java.io.IOException;

@Component
public class CbrClient {

    @Autowired
    private OkHttpClient client;
    @Value("${cbr.currency.rates.xml.url}")
    private String cbrCurrencyRatesXmlUrl;

    public String getCurrencyRatesXml() throws BotServiceException {
        Request request = new Request.Builder()
                .url(cbrCurrencyRatesXmlUrl)
                .build();

        try (Response response = client.newCall(request).execute()) {
            ResponseBody body = response.body();
            return body == null ? null : body.string();
        } catch (IOException e) {
            throw new BotServiceException("Ошибка получения курсов валют от ЦБ РФ");
        }
    }
}
