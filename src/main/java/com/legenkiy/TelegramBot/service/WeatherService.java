package com.legenkiy.TelegramBot.service;
import com.legenkiy.TelegramBot.config.properties.BotProperties;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

        String weatherDescription =  jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");

        // Форматування даних погоди
        String formattedWeather = String.format(
                        "\u2554\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2557\n" +
                        "           \uD83C\uDF25Weather ️: %s \n\n" +
                        "           \uD83C\uDF21Temperature : %.1f°C \n" +
                        "           \uD83C\uDFACFeels like : %.1f°C \n\n" +
                        "           \uD83D\uDCA8Wind : %.1f m/s \n" +
                        "           \uD83D\uDD2EPressure : %.0f mmHg \n" +
                        "           \uD83D\uDCA7Humidity : %d%% \n\n" +
                        "           \uD83C\uDF05Dawn : %s \n" +
                        "           \uD83C\uDF07Sunset : %s \n" +
                        "\u255A\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u255D",
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
