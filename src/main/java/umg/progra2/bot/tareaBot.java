package umg.progra2.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import umg.progra2.utils.ConvertidorMoneda;
import umg.progra2.utils.WeatherService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static umg.progra2.utils.WeatherService.formatWeatherResponse;

public class tareaBot extends TelegramLongPollingBot {
    private static final String COMMANDS = "Comandos disponibles:\n" +
            "/start - Inicia interacción con el bot y muestra este mensaje.\n" +
            "/clima [ciudad] - Muestra el clima actual para la ciudad especificada.\n" +
            "/info - Muestra información del estudiante.\n" +
            "/progra - Comentarios sobre la clase de programación.\n" +
            "/hola - Saluda al usuario y muestra la fecha actual.\n" +
            "/grupal [mensaje] - Envía un mensaje grupal a todos los usuarios permitidos.\n";


    private final List<Long> chatIds = List.of(5454689659L, 6375701250L, 6699823249L, 6984229154L, 5792621349L);



    @Override
    public String getBotUsername() {
        return "@comapos_bot";
    }

    @Override
    public String getBotToken() {
        return "7287112660:AAHEs5x7YMIAaMkzq4KNyr_XIegKa3j8KmA";
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            try {
                if (messageText.equals("/start") || messageText.equals("/help")){
                    sendTextMessage(chatId, COMMANDS);
                } else if (messageText.startsWith("/cambio")) {
                    String amountText = messageText.split(" ")[1];
                    double amount = Double.parseDouble(amountText);
                    double rate = ConvertidorMoneda.getExchangeRate("EUR", "GTQ");
                    double result = amount * rate;
                    sendTextMessage(chatId, amount + " EUR son " + String.format("%.2f", result) + " GTQ");
                } else if (messageText.startsWith("/grupal")) {
                    String mensajeGrupal = messageText.substring(8); // Extrae el mensaje después del comando
                    for (Long id : chatIds) {
                        System.out.println("Enviando mensaje a " + id);
                        sendTextMessage(id, mensajeGrupal);
                    }
                } else if (messageText.startsWith("/clima")) {
                    String city = messageText.split(" ")[1];
                    try {
                        String weatherData = WeatherService.getWeather(city);
                        String formattedWeather = formatWeatherResponse(weatherData);
                        sendTextMessage(chatId, formattedWeather);
                    } catch (Exception e) {
                        sendTextMessage(chatId, "Error al obtener el clima para " + city);
                        e.printStackTrace();
                    }
                } else {
                    SendMessage message = new SendMessage();
                    message.setChatId(String.valueOf(chatId));
                    switch (messageText) {
                        case "/info":
                            message.setText("Número de carnet: 0905-23-10736, Nombre: Alejandro Osorio, Semestre: 4");
                            break;
                        case "/progra":
                            message.setText("La clase de programación me ha parecido interesante y he aprendido mucho!");
                            break;
                        case "/hola":
                            String nombre = update.getMessage().getFrom().getFirstName();
                            String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM"));
                            message.setText("Hola, " + nombre + ", hoy es " + fecha);
                            break;
                        default:
                            message.setText("Comando no reconocido.\n" + COMMANDS);
                            break;
                    }
                    execute(message);
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
                try {
                    sendTextMessage(chatId, "Ha ocurrido un error con el API de Telegram.");
                } catch (TelegramApiException ex) {
                    ex.printStackTrace();
                }
            } catch (NumberFormatException e) {
                try {
                    sendTextMessage(chatId, "Por favor ingresa un número válido para la conversión.");
                } catch (TelegramApiException ex) {
                    ex.printStackTrace();
                }
            } catch (Exception e) {
                try {
                    sendTextMessage(chatId, "Error procesando tu solicitud.");
                } catch (TelegramApiException ex) {
                    ex.printStackTrace();
                }
            }

        }


    }
    private void sendTextMessage(long chatId, String text) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        execute(message);
    }

}
