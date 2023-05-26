package ru.tolstonogova.littlemouseybot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import ru.tolstonogova.littlemouseybot.client.CbrClient;
import ru.tolstonogova.littlemouseybot.exception.BotServiceException;
import ru.tolstonogova.littlemouseybot.service.ExchangeRatesService;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

@Service
public class ExchangeRatesServiceImpl implements ExchangeRatesService {

    private static final String USD_XPATH = "/ValCurs//Valute[@ID='R01235']/Value";
    private static final String EUR_XPATH = "/ValCurs//Valute[@ID='R01239']/Value";

    @Autowired
    private CbrClient client;

    @Override
    public String getUSDExchangeRate() throws BotServiceException {
        String xml = client.getCurrencyRatesXml();
        return extractCurrencyValueFromXML(xml, USD_XPATH);
    }

    @Override
    public String getEURExchangeRate() throws BotServiceException {
        String xml = client.getCurrencyRatesXml();
        return extractCurrencyValueFromXML(xml, EUR_XPATH);
    }

    @Nullable
    private static String extractCurrencyValueFromXML(String xml, String xpathExpression) throws BotServiceException {
        var source = new InputSource(new StringReader(xml));
        try {
            var xpath = XPathFactory.newInstance().newXPath();
            var document = (Document) xpath.evaluate("/", source, XPathConstants.NODE);

            return xpath.evaluate(xpathExpression, document);
        } catch (XPathExpressionException e) {
            throw new BotServiceException("Не удалось распарсить XML");
        }
    }
}
