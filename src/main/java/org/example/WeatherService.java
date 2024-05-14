package org.example;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherService {

    private final String openWeatherMapApiKey = "033694b0bd5207aeaa89dff0d262efa2";

    public String getWeatherInfo(String city) {
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + openWeatherMapApiKey);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());

            double temperature = jsonResponse.getJSONObject("main").getDouble("temp");

            double temperatureInCelsius = temperature - 273.15;

            String advice = getOutfitAdvice(temperatureInCelsius);

            return "Температура в " + city + ": " + String.format("%.1f", temperatureInCelsius) + "°C\n" + advice;
        } catch (Exception e) {
            e.printStackTrace();
            return "Произошла ошибка при получении погоды для указанного города.";
        }
    }

    private String getOutfitAdvice(double temperature) {
        // Логика советов по одежде на основе температуры
        if (temperature <= -25) {
            return "Совет по одежде: Очень холодно! Наденьте много теплой одежды, шарф, перчатки и теплую обувь.";
        } else if (temperature > -25 && temperature <= -15) {
            return "Совет по одежде: Очень холодно. Рекомендуется надеть теплую куртку, шапку и теплую обувь.";
        } else if (temperature > -15 && temperature <= -5) {
            return "Совет по одежде: Холодно. Рекомендуется надеть теплую куртку, шарф и теплую обувь.";
        } else if (temperature <= 0) {
            return "Совет по одежде: Холодно. Рекомендуется надеть свитер, теплую куртку и шапку!";
        } else if (temperature > 0 && temperature <= 15) {
            return "Совет по одежде: Прохладно. Рекомендуется надеть свитер или кофту.";
        } else if (temperature > 15 && temperature <= 25) {
            return "Совет по одежде: Приятная температура. Легкая одежда подойдет для комфортной прогулки.";
        } else {
            return "Совет по одежде: Жарко. Оденьтесь легко и не забудьте пить воду. Проводите время в тени.";
        }
    }
}
