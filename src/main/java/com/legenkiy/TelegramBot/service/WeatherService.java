package com.legenkiy.TelegramBot.service;
import com.legenkiy.TelegramBot.config.properties.BotProperties;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class WeatherService {
    private final BotProperties botProperties;
    @Autowired
    public WeatherService(BotProperties botProperties) {
        this.botProperties = botProperties;
    }

    public String getWeatherByLocation(String cityName) {
        String apiUrl = botProperties.getApiUrl() + cityName + "&appid=" + botProperties.getApiKey();
        RestTemplate restTemplate = new RestTemplate();
        String weatherData = restTemplate.getForObject(apiUrl, String.class);
        JSONObject jsonObject = new JSONObject(weatherData);


        double temperature = jsonObject.getJSONObject("main").getDouble("temp") - 273.15; // Перетворення температури з Кельвінів в Цельсії
        double feelsLike = jsonObject.getJSONObject("main").getDouble("feels_like") - 273.15; // Перетворення відчуття температури з Кельвінів в Цельсії
        double windSpeed = jsonObject.getJSONObject("wind").getDouble("speed");
        double pressure = jsonObject.getJSONObject("main").getDouble("pressure");
        int humidity = jsonObject.getJSONObject("main").getInt("humidity");
        long sunrise = jsonObject.getJSONObject("sys").getLong("sunrise") * 1000; // Перетворення в мілісекунди
        long sunset = jsonObject.getJSONObject("sys").getLong("sunset") * 1000; // Перетворення в мілісекунди

        String weatherDescription = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");

        String formattedWeather = String.format(
                """
                                               Погода прямо зараз:
                        \u2554\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2557
                                   \uD83C\uDF25Погода ️: %s\s

                                   \uD83C\uDF21Температура : %.1f°C\s
                                   \uD83C\uDFACВідчувається як : %.1f°C\s

                                   \uD83D\uDCA8Вітер : %.1f m/s\s
                                   \uD83D\uDD2EТиск : %.0f mmHg\s
                                   \uD83D\uDCA7Вологість : %d%%\s

                                   \uD83C\uDF05Захід : %s\s
                                   \uD83C\uDF07Схід : %s\s
                        \u255A\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u255D""",
                weatherDescription, temperature, feelsLike, windSpeed, pressure, humidity, formatTime(sunrise), formatTime(sunset));

        return formattedWeather;
    }
    // Метод для форматування часу з мілісекунд у рядок у форматі "HH:mm:ss"
    private String formatTime(long timeInMillis) {
        java.util.Date date = new java.util.Date(timeInMillis);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
        return sdf.format(date);
    }
}




