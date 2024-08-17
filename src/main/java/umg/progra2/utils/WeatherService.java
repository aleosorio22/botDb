package umg.progra2.utils;

import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.io.IOException;

public class WeatherService {

    //debe registrarse en la pagina de WeatherService para obtener su APIKEY
    private static final String API_KEY = "la api key va aqui";
    private static final HttpClient client = HttpClient.newHttpClient();

    public static String getWeather(String city) {
        String uri = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=metric";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ApiException("La API no está disponible o la ciudad no es válida, código de error: " + response.statusCode());
            }
            return response.body();
        } catch (ApiException e) {
            System.err.println(e.getMessage());
            return e.getMessage();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error al obtener el clima debido a un problema de red o interrupción.";
        }
    }

    public static String formatWeatherResponse(String jsonResponse) {
        try {
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
        } catch (Exception e) {
            return "Error al procesar la respuesta de la API.";
        }
    }
}

class ApiException extends Exception {
    public ApiException(String message) {
        super(message);
    }
}
