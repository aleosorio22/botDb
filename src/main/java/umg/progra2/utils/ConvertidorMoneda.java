package umg.progra2.utils;

import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

public class ConvertidorMoneda {
    //debe registrarse en la pagina de exchangerate para generar su api key
    private static final String API_KEY = "Tu api key va aqui";

    public static double getExchangeRate(String from, String to) throws CurrencyConversionException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String baseUrl = "https://v6.exchangerate-api.com/v6/";
            String endpoint = baseUrl + API_KEY + "/pair/" + from + "/" + to;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new CurrencyConversionException("Failed to retrieve data from API: HTTP status " + response.statusCode());
            }
            return parseExchangeRate(response.body());
        } catch (IOException | InterruptedException e) {
            throw new CurrencyConversionException("Error communicating with currency conversion API", e);
        }
    }

    private static double parseExchangeRate(String responseBody) throws CurrencyConversionException {
        try {
            JSONObject json = new JSONObject(responseBody);
            if (!json.has("conversion_rate")) {
                throw new CurrencyConversionException("Invalid response: Missing 'conversion_rate'");
            }
            return json.getDouble("conversion_rate");
        } catch (Exception e) {
            throw new CurrencyConversionException("Error parsing the response: " + responseBody, e);
        }
    }

    public static class CurrencyConversionException extends Exception {
        public CurrencyConversionException(String message) {
            super(message);
        }

        public CurrencyConversionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
