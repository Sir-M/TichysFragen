package org.tichybot;

public class Question {
    private String question;
    private boolean answer;

    public Question(boolean answer, String question) {
        this.answer = answer;
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public boolean getAnswer() {
        return answer;
    }

}
