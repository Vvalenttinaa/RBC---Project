package com.example.mybudget.services.impl;

import com.example.mybudget.models.dtos.Currency;
import com.example.mybudget.models.dtos.CurrencyInEUR;
import com.example.mybudget.services.CurrencyService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class CurrencyServiceImpl implements CurrencyService {

    @Value("${link}")
    private String link;
    ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(CurrencyServiceImpl.class);

    private static final HttpClient client = HttpClient.newHttpClient();

    private CurrencyInEUR getInEur(String currencyName) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder(new URI(link)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode rootNode = objectMapper.readTree(response.body());
        logger.info("root node" + rootNode.asText());
        logger.info(response.body());
        JsonNode eurNode = rootNode.get("eur");
        if (eurNode != null && eurNode.isObject()) {
            logger.info("if");
            Iterator<Map.Entry<String, JsonNode>> fields = eurNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String currName = field.getKey();
                String currencyValue = field.getValue().asText();
                logger.info(field.getKey() + " " + currencyName);
                if(field.getKey().toLowerCase().equals(currencyName.toLowerCase())){
                    logger.info("Currency: " + currName + ", Value: " + currencyValue);
                    return new CurrencyInEUR(field.getKey(), field.getValue().decimalValue());
                }
            }
        }
        return null;
    }

    @Override
    public List<String>getAll() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(new URI(link)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode rootNode = objectMapper.readTree(response.body());
        List<String> currencyNames = new ArrayList<>();
        logger.info(response.body());
        JsonNode eurNode = rootNode.get("eur");
        if (eurNode != null && eurNode.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = eurNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                currencyNames.add(field.getKey());
            }
        }
        return currencyNames;
    }

    @Override
    public BigDecimal convertToEuro(Currency currency) {
        CurrencyInEUR currencyInEUR = null;
        try {
            currencyInEUR = getInEur(currency.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return currency.getValue()
                .divide(currencyInEUR.getValue(), MathContext.DECIMAL128);
    }

    @Override
    public BigDecimal convertFromEuro(Currency currency){
        try {
            CurrencyInEUR currencyInEUR = getInEur(currency.getName());
            return currencyInEUR.getValue().multiply(currency.getValue());
        }catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BigDecimal conversion(String from, String to, BigDecimal number) {
        logger.error("from " + from + " to " + to + number);
        BigDecimal euro = convertToEuro(new Currency(from, number.abs()));
        return convertFromEuro(new Currency(to, euro));
    }
//
//    @Override
//    public Integer compare(Currency currency1, Currency currency2){
//        if(currency1.getName().equals(currency2.getName())){
//            return currency1.getValue().compareTo(currency2.getValue());
//        }else if (currency1.getName().toUpperCase().equals("EUR")){
//            BigDecimal c = convertToEuro(currency2);
//            return currency1.getValue().compareTo(c);
//        }else if(currency2.getName().toUpperCase().equals("EUR")){
//            BigDecimal c = convertToEuro(currency1);
//            return c.compareTo(currency2.getValue());
//        }else{
//            BigDecimal c1 = convertToEuro(currency1);
//            BigDecimal c2 = convertToEuro(currency2);
//            return c1.compareTo(c2);
//        }
//    }
}
