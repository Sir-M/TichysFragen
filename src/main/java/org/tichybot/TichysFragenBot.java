package org.tichybot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Main bot logic
 */
public class TichysFragenBot extends TelegramLongPollingBot {

    private QuestionParser qP = new QuestionParser();
    private QuestionManager qM = new QuestionManager(qP.read());
    private Question current = qM.popQuestion();
    private Question last = current;
    private boolean started = false;

    public void onUpdateReceived(Update update) {

        if (update != null && update.hasMessage() && update.getMessage().hasText()) {

            Message received = update.getMessage();
            String chatId = received.getChatId().toString();
            String text = received.getText();
            if (current != null) {
                if (started) {
                    if (text.equals("/end")) {
                        started = false;
                    } else if (text.equals("/start")) {
                        sendMessage(Constants.START + current.getQuestion(), chatId);
                    } else {
                        if (text.equals("w") || text.equals("W")) {
                            if (last.getAnswer()) {
                                sendMessage(Constants.NICE + current.getQuestion(), chatId);
                            } else {
                                sendMessage(Constants.BAD + current.getQuestion(), chatId);
                            }
                            last = current;
                            current = qM.popQuestion();

                        } else if (text.equals("f") || text.equals("F")) {
                            if (!last.getAnswer()) {
                                sendMessage(Constants.NICE + current.getQuestion(), chatId);
                            } else {
                                sendMessage(Constants.BAD + current.getQuestion(), chatId);
                            }
                            last = current;
                            current = qM.popQuestion();
                        } else {
                            sendMessage(Constants.WTF + current.getQuestion(), chatId);

                        }
                    }


                } else {
                    if (text.equals("/start")) {
                        started = true;
                        qM.restart();
                        sendMessage(Constants.START + current.getQuestion(), chatId);
                    } else {
                        sendMessage(Constants.BEGIN, chatId);
                    }
                }
            }
        }
    }


    private void sendMessage(String text, String chatID) {
        SendMessage message;

        message = new SendMessage()
                .setChatId(chatID)
                .setText(text);

        try {
            sendApiMethod(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getBotUsername() {
        return Constants.BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return Constants.BOT_TOKEN;
    }

    public interface Constants {
        String DS = System.getProperty("line.separator") + System.getProperty("line.separator");
        String NICE = "Super!" + DS;
        String BAD = "Daneben!" + DS;
        String WTF = "Bitte antworte mit W für wahr oder F für falsch." + DS;
        String BOT_USERNAME = "tichys_fragen_bot";
        String BOT_TOKEN = "784338216:AAGjU1Qvj1k3VS4dvCZVupVn2OeRea0RaR4";
        String BEGIN = "/start , um den Bot zu starten und die Fragen neu zu mischen! Antworte auf die kommenden fragen mit wahr (w) oder falsch (f)";
        String START = "Bot gestartet! Antworte mit W für wahr und F für falsch." + DS;
    }

}
