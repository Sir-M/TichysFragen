package org.tichybot;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class QuestionParser {


    public List<Question> read() {
        List<Question> questions = new LinkedList<>();

        try {
            InputStream in = getClass().getClassLoader().getResourceAsStream("input.txt");
            BufferedReader br = null;
            if (in != null) {
                br = new BufferedReader(new InputStreamReader(in));

                String line = null;
                while ((line = br.readLine()) != null) {
                    questions.add(parseLine(line));
                }

                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return questions;
    }


    private Question parseLine(String s) {
        boolean b = false;
        if (s.length() > 2) {
            if (s.charAt(0) == 'w') {
                b = true;
            }
            return new Question(b, s.substring(2));
        }
        return new Question(b, "Error while parsing this one.");

    }
}
