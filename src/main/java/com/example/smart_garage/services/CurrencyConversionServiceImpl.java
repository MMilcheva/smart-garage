package com.example.smart_garage.services;

import com.example.smart_garage.services.contracts.CurrencyConversionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;

@Service
public class CurrencyConversionServiceImpl implements CurrencyConversionService {


    @Override
    public Double getExchangerateOnDate(LocalDate date, String symbols, String base) throws IOException {

        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        Request request = new Request.Builder()
                .url("https://api.apilayer.com/exchangerates_data/" + date.toString() + "?symbols="
                        + symbols + "&base=" + base + "")
                .addHeader("apikey", "LVoI1XOZtljOcGE17lKO6Rl7iWCchTel")
                .method("GET", null)
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();

        } catch (IOException e) {
            return null;
        }
        String jsonResponse = response.body().string();

        System.out.println(jsonResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        String exchangeRate = jsonNode.get("rates").get(symbols).asText();

        return Double.parseDouble(exchangeRate);
    }

}
