package umg.progra2.utils;

import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherService {

    private static final String API_KEY = "b4ebce660dc4a0ae037680193f97d352";
    private static final HttpClient client = HttpClient.newHttpClient();

    public static String getWeather(String city) {
        String uri = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=metric";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body(); // Devuelve la respuesta como string
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al obtener el clima";
        }

    }
    public static String formatWeatherResponse(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONObject main = jsonObject.getJSONObject("main");
        JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
        JSONObject wind = jsonObject.getJSONObject("wind");

        String description = weather.getString("description");
        double temp = main.getDouble("temp");
        double feelsLike = main.getDouble("feels_like");
        int humidity = main.getInt("humidity");
        double windSpeed = wind.getDouble("speed");

        return String.format("Clima: %s\nTemperatura: %.1f°C (Sensación térmica: %.1f°C)\nHumedad: %d%%\nViento: %.1f km/h",
                description, temp, feelsLike, humidity, windSpeed);
    }
}

