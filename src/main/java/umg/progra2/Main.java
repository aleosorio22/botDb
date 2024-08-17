package umg.progra2;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import umg.progra2.bot.tareaBot;

public class Main {
    public static void main(String[] args) {

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            tareaBot bot = new tareaBot();
            botsApi.registerBot(bot);
            System.out.println("Bot registrado y en ejecución.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}