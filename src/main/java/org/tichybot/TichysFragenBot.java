package org.tichybot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
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
    //private Question current = null;
    //private Question last = current;
    //  private boolean started = false;
    private Map<Long, Question> lastQuestion = new HashMap<>();
    private Map<Long, Boolean> running = new HashMap<>();

    public void onUpdateReceived(Update update) {

        if (update != null && update.hasMessage() && update.getMessage().hasText()) {

            Message received = update.getMessage();
            String chatId = received.getChatId().toString();
            String text = received.getText();

            long id = received.getChatId();
            boolean isStarted = running.getOrDefault(id, false);
            if (isStarted) {
                Question current = lastQuestion.getOrDefault(id, null);
                if (current != null) {
                    if (text.equals("w") || text.equals("W")) {

                        Question newQuestion = qM.getRandomQuestion();
                        if (current.getAnswer()) {
                            sendMessage(Constants.NICE + newQuestion.getQuestion(), chatId);
                        } else {
                            sendMessage(Constants.BAD + newQuestion.getQuestion(), chatId);
                        }
                        lastQuestion.put(id, newQuestion);


                    } else if (text.equals("f") || text.equals("F")) {
                        Question newQuestion = qM.getRandomQuestion();
                        if (!current.getAnswer()) {
                            sendMessage(Constants.NICE + newQuestion.getQuestion(), chatId);
                        } else {
                            sendMessage(Constants.BAD + newQuestion.getQuestion(), chatId);
                        }
                        lastQuestion.put(id, newQuestion);

                    } else {
                        sendMessage(Constants.WTF + current.getQuestion(), chatId);
                    }
                } else {
                    //current is null!
                }
            } else {
                if (text.equals("/start")) {
                    isStarted = true;
                    running.put(id, isStarted);

                    Question current = qM.getRandomQuestion();
                    lastQuestion.put(id, current);

                    sendMessage(Constants.START + current.getQuestion(), chatId);
                } else {
                    sendMessage(Constants.BEGIN, chatId);
                }

/*

            if (current != null) {
                if (started) {
                    if (text.equals("/end")) {
                        started = false;
                    } else if (text.equals("/start")) {
                        qM.restart();
                        current = qM.popQuestion();
                        last = current;
                        sendMessage(Constants.START + current.getQuestion(), chatId);
                        //current = qM.popQuestion();

                    } else {
                        if (text.equals("w") || text.equals("W")) {
                            last = current;
                            current = qM.popQuestion();
                            if (last.getAnswer()) {
                                sendMessage(Constants.NICE + current.getQuestion(), chatId);
                            } else {
                                sendMessage(Constants.BAD + current.getQuestion(), chatId);
                            }


                        } else if (text.equals("f") || text.equals("F")) {
                            last = current;
                            current = qM.popQuestion();
                            if (!last.getAnswer()) {
                                sendMessage(Constants.NICE + current.getQuestion(), chatId);
                            } else {
                                sendMessage(Constants.BAD + current.getQuestion(), chatId);
                            }


                        } else {
                            sendMessage(Constants.WTF + current.getQuestion(), chatId);
                        }
                    }

                } else {
                    if (text.equals("/start")) {
                        started = true;
                        qM.restart();
                        current = qM.popQuestion();
                        last = current;
                        sendMessage(Constants.START + current.getQuestion(), chatId);
                        //current = qM.popQuestion();

                    } else {
                        sendMessage(Constants.BEGIN, chatId);
                    }
                }
            }
        }
        */
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
        try {
            InputStream in = getClass().getClassLoader().getResourceAsStream("token.txt");
            BufferedReader br = null;
            if (in != null) {
                br = new BufferedReader(new InputStreamReader(in));
                String line = null;
                if ((line = br.readLine()) != null) {
                    return line;
                }
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    public interface Constants {
        String DS = System.getProperty("line.separator") + System.getProperty("line.separator");
        String NICE = "Super!" + DS;
        String BAD = "Daneben!" + DS;
        String WTF = "Bitte antworte mit W für wahr oder F für falsch." + DS;
        String BOT_USERNAME = "tichys_fragen_bot";
        //  String BOT_TOKEN = "784338216:AAGjU1Qvj1k3VS4dvCZVupVn2OeRea0RaR4"; never do this :)
        String BEGIN = "/start , um den Bot zu starten! Antworte auf die kommenden Fragen mit wahr (w) oder falsch (f)";
        String START = "Bot gestartet! Antworte mit W für wahr und F für falsch." + DS;
    }

}
