package com.legenkiy.TelegramBot.service;

import com.legenkiy.TelegramBot.config.properties.BotProperties;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {
    private final BotProperties botProperties;
    private boolean flag;
    private List<String> forecastList = new ArrayList<>();

    @Autowired
    public WeatherService(BotProperties botProperties) {
        this.botProperties = botProperties;
    }

    public void getWeatherByLocationDayOne(String cityName) {
        forecastList.clear();
        flag = true;
        try {
            String apiUrl = botProperties.getApiUrl() + "forecast?q=" + cityName + "&cnt=5&appid=" + botProperties.getApiKey();
            RestTemplate restTemplate = new RestTemplate();
            String weatherData = restTemplate.getForObject(apiUrl, String.class);
            JSONObject jsonObject = new JSONObject(weatherData);

            JSONArray forecastArray = jsonObject.getJSONArray("list");

            LocalDate currentDate = LocalDate.now(); // Поточна дата
            for (int i = 0; i < forecastArray.length(); i++) {
                JSONObject forecastObject = forecastArray.getJSONObject(i);

                // Отримання дати прогнозу (додавання `i` днів до поточної дати)
                LocalDate date = currentDate.plusDays(i);

                // Отримання погодних даних
                String city = jsonObject.getJSONObject("city").getString("name");
                double temperature = forecastObject.getJSONObject("main").getDouble("temp") - 273.15;
                double feelsLike = forecastObject.getJSONObject("main").getDouble("feels_like") - 273.15;
                double windSpeed = forecastObject.getJSONObject("wind").getDouble("speed");
                double pressure = forecastObject.getJSONObject("main").getDouble("pressure");
                int humidity = forecastObject.getJSONObject("main").getInt("humidity");
                String weatherDescription = forecastObject.getJSONArray("weather").getJSONObject(0).getString("description");

                // Формування рядка з погодними даними та датою
                String formattedWeather = new StringBuilder()
                        .append("Погода в ").append(city).append(" на ").append(date).append('\n')
                        .append('╔').append("═".repeat(21)).append('╗').append('\n')
                        .append("\t\t\t\uD83C\uDF25Погода ️: ").append(weatherDescription).append("\n\n")
                        .append("\t\t\t\uD83C\uDF21Температура : ").append(String.format("%.1f°C", temperature)).append('\n')
                        .append("\t\t\t\uD83C\uDFACВідчувається як : ").append(String.format("%.1f°C", feelsLike)).append("\n\n")
                        .append("\t\t\t\uD83D\uDCA8Вітер : ").append(String.format("%.1f m/s", windSpeed)).append('\n')
                        .append("\t\t\t\uD83D\uDD2EТиск : ").append(Math.round(pressure)).append(" mmHg").append('\n')
                        .append("\t\t\t\uD83D\uDCA7Вологість : ").append(humidity).append('%').append('\n')
                        .append('╚').append("═".repeat(21)).append('╝')
                        .toString();
                forecastList.add(formattedWeather);
            }
        } catch (Exception e) {
            // Виконати дії, якщо виникає виняток
            e.printStackTrace();
            // або можна додати обробку виключення тут, наприклад:
            System.out.println("Виникла помилка при отриманні погоди для міста: " + cityName);
            flag = false;
        }
    }

    public String getWeatherForDays(int dayNumber) {
        if (!flag) {
            return "Дані для цього міста застаріли, або введені некоректно!\n" +
                    "введіть /wt та коректну назву міста.";
        }
        return forecastList.get(dayNumber);
    }
}




