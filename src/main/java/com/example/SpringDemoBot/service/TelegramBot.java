package com.example.SpringDemoBot.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.example.SpringDemoBot.config.BotConfig;

@Component
public class TelegramBot extends TelegramLongPollingBot {

	final BotConfig config;

	static final String HELP_TEXT = "This bot is created to demonstrate Spring capabilities.\n\n"
			+ "You can execute commands from the main menu on the left or by typing a command:\n\n"
			+ "Type /start to see a welcome message\n\n" + "Type /mydata to see data stored about yourself\n\n"
			+ "Type /help to see this message again";

	public TelegramBot(BotConfig config) {
		this.config = config;
	}

	@Override
	public String getBotUsername() {
		return config.getBotName();
	}

	@Override
	public String getBotToken() {
		return config.getToken();
	}

	@Override
	public void onUpdateReceived(Update update) {

		if (update.hasMessage() && update.getMessage().hasText()) {
			String messageText = update.getMessage().getText();
			long chatId = update.getMessage().getChatId();
			switch (messageText) {
			case "/start":
				startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
				break;
			case "/help":
				sendMessage(chatId, HELP_TEXT);
				break;
			default:
				sendMessage(chatId, "Sorry, command wasn't recognized...\n"
						+ "You have said: " + update.getMessage().getText());
			}

		}
	}

	private void startCommandReceived(long chatId, String name) {
		String answer = "Hi, " + name + " nice to meet you!";
		sendMessage(chatId, answer);
	}

	private void sendMessage(long chatId, String textToSend) {
		SendMessage message = new SendMessage();
		message.setChatId(String.valueOf(chatId));
		message.setText(textToSend);

		try {
			execute(message);
		} catch (TelegramApiException e) {

		}
	}

}
