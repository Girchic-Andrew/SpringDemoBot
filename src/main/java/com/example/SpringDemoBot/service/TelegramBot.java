package com.example.SpringDemoBot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.example.SpringDemoBot.config.BotConfig;

//@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

	final BotConfig config;

	static final String HELP_TEXT = "This bot is created to demonstrate Spring capabilities.\n\n"
			+ "You can execute commands from the main menu on the left or by typing a command:\n\n"
			+ "Type /start to see a welcome message\n\n" + "Type /mydata to see data stored about yourself\n\n"
			+ "Type /help to see this message again";

	public TelegramBot(BotConfig config) {
		this.config = config;
		List<BotCommand> listOfCommands = new ArrayList<>();
		listOfCommands.add(new BotCommand("/start", "Get a welcome message"));
		listOfCommands.add(new BotCommand("/mydata", "Get your data stored"));
		listOfCommands.add(new BotCommand("/deletedata", "Delete my data"));
		listOfCommands.add(new BotCommand("/help", "Info how to use this bot"));
		listOfCommands.add(new BotCommand("/settings", "Set your preferences"));
		try {
			this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
		} catch(TelegramApiException e) {
			
		}
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
//		log.info("Replied to user " + name);
	}

	private void sendMessage(long chatId, String textToSend) {
		SendMessage message = new SendMessage();
		message.setChatId(String.valueOf(chatId));
		message.setText(textToSend);

		try {
			execute(message);
		} catch (TelegramApiException e) {
//			log.error("Error occurred: " + e.getMessage());
		}
	}

}
