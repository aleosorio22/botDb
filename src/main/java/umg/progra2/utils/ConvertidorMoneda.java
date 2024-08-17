package umg.progra2.utils;

import netscape.javascript.JSException;
import org.json.JSONObject;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class ConvertidorMoneda {
    private static final String API_KEY = "33dea94e910fecfd22972a84";

    public static double getExchangeRate(String from, String to) throws IOException, InterruptedException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String baseUrl = "https://v6.exchangerate-api.com/v6/";
            String endpoint = baseUrl + API_KEY + "/pair/" + from + "/" + to;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return parseExchangeRate(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return 0;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    private static double parseExchangeRate(String responseBody) {
        JSONObject json = new JSONObject(responseBody);
        if (json.has("conversion_rate")) {
            return json.getDouble("conversion_rate");
        } else {
            throw new IllegalArgumentException("Invalid response: " + responseBody);
        }
    }
}
