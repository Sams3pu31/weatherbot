package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class WeatherBot extends TelegramLongPollingBot {

    private WeatherService weatherService;

    public WeatherBot() {
        weatherService = new WeatherService();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();

            if (messageText.equalsIgnoreCase("/start") || messageText.equalsIgnoreCase("/restart")) {
                sendMsg(chatId, "Привет! Напиши мне название города, чтобы узнать погоду.");
            } else {
                String weatherInfo = weatherService.getWeatherInfo(messageText);
                sendMsg(chatId, weatherInfo);
            }
        }
    }

    private void sendMsg(long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(chatId)) // Установка идентификатора чата
                .text(text) // Установка текста сообщения
                .build();

        try {
            execute(message); // Отправка сообщения
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "TestYourWeatherBot";
    }

    @Override
    public String getBotToken() {
        return "6549787892:AAGplSx7oZQhDSwUFzUO3TcsGshsCEU-IgQ";
    }

    public static void main(String[] args) {
        WeatherBot bot = new WeatherBot();
        try {
            bot.startBot();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void startBot() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
