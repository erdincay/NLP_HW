// WARNING! Do not modify this. This file will not be submited as part of the
// assignment. Hence, any change in this file will be discarded at the time of
// evaluation.

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;

public class Data {
    List<Sentence> data_;

    public Data(String fileName) {
        data_ = new ArrayList<Sentence>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                Sentence sentence = processLine(line);
                if (sentence != null) {
                    data_.add(sentence);
                }
            }
        } catch (IOException e) {
            System.err.println(String.format("[HolbrookReader.readHolbrook] Error reading corpus %s: %s",
                    fileName, e.getMessage()));
            e.printStackTrace();
        }
    }

    // add <s> and </s> markers
    Sentence processLine(String line) {
        Sentence sentence = new Sentence();
        sentence.add(new Word("<s>", "")); // start of sentence
        String[] tokens = line.split(" ");
        int i = 0;
        while (i < tokens.length) {
            String token = tokens[i];
            if (token.equals("<err")) {
                // slice out error substring, process, update i.
                int errorEnd = findNext(tokens, "</err>", i);
                if (errorEnd == -1) {
                    System.err.println("Misformatted error string: " + line);
                    return null;
                }
                String[] errorTokens = Arrays.copyOfRange(tokens, i, errorEnd + 1);

                Word word = processError(errorTokens);
                sentence.add(word);
                i = errorEnd + 1; // update index accordingly.
            } else {
                Word word = new Word(new String(token), "");
                sentence.add(word);
                i++;
            }
        }
        sentence.add(new Word("</s>", "")); // end of sentence
        return sentence;
    }

    // Finds the next occurence of query in tokens, starting from index start. -1 if missing.
    int findNext(String[] tokens, String query, int start) {
        for (int i = start; i < tokens.length; i++) {
            if (tokens[i].equals(query))
                return i;
        }
        return -1;
    }

    // process an error block of the form <err targ=w1 w2 w3> e1 e2 e3 </err>
    Word processError(String[] tokens) {
        StringBuffer correctWords = new StringBuffer(tokens[1].split("=")[1]);
        int end = 0; // the end of targ string
        while (end < tokens.length) {
            if (tokens[end].endsWith(">"))
                break;
            end++;
        }
        for (int i = 2; i < end; i++) {
            correctWords.append(" ").append(tokens[i]);
        }
        correctWords.deleteCharAt(correctWords.length() - 1);

        StringBuffer errorWords = new StringBuffer(tokens[end + 1]);
        for (int i = end + 2; i < tokens.length - 1; i++) {
            errorWords.append(" ");
            errorWords.append(tokens[i]);
        }
        return new Word(correctWords.toString(), errorWords.toString());
    }

    public List<Sentence> getSentences() {
        return data_;
    }


    // returns Sentences with just one error.
    public List<Sentence> generateTestCases() {
        List<Sentence> testCases = new ArrayList<Sentence>();
        for (Sentence sentence : data_) {
            Sentence cleanSentence = sentence.cleanSentence();
            for (int i = 0; i < sentence.size(); i++) {
                Word word_i = sentence.get(i);
                if (word_i.hasError() && word_i.isValidTest()) {
                    Sentence testSentence = new Sentence(cleanSentence);
                    testSentence.set(i, word_i); // add the error back.
                    testCases.add(testSentence);
                }
            }
        }
        return testCases;
    }
}
