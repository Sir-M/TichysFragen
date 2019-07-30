package org.tichybot;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Basically a queue
 */
public class QuestionManager {
    private Queue<Question> questions;
    private Question current;
    private List<Question> saved;

    public QuestionManager(List<Question> questions) {
        saved = new LinkedList<>(questions);
        restart();
    }

    public Question popQuestion() {
        current = questions.poll();
        questions.add(current);
        return current;
    }

    public void restart(){
        Collections.shuffle(saved);
        this.questions = new LinkedList<>(saved);
    }

}
