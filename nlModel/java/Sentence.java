// WARNING! Do not modify this. This file will not be submited as part of the
// assignment. Hence, any change in this file will be discarded at the time of
// evaluation.

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class Sentence implements Iterable<Word> {
    protected ArrayList<Word> data;

    public Sentence() {
        data = new ArrayList<Word>();
    }

    public Sentence(Sentence sentence) {
        data = new ArrayList<Word>(sentence.data);
    }

    public Word get(int i) {
        return data.get(i);
    }

    public void set(int i, Word d) {
        data.set(i, d);
    }

    public int size() {
        return data.size();
    }

    public void add(Word word) {
        data.add(word);
    }

    public Iterator<Word> iterator() {
        return data.iterator();
    }

    // returns the sentence as written, flattened into list of strings
    public List<String> getErrorSentence() {
        List<String> errorSentence = new ArrayList<String>(size());
        for (Word word : data) {
            if (word.hasError()) {
                errorSentence.add(word.getError());
            } else {
                errorSentence.add(word.getCorrectWord());
            }
        }
        return errorSentence;
    }

    // returns the corrected sentence
    public List<String> getCorrectSentence() {
        List<String> correctSentence = new ArrayList<String>(size());
        for (Word word : data) {
            correctSentence.add(word.getCorrectWord());
        }
        return correctSentence;
    }

    // returns a new sentence with all word's having error removed.
    public Sentence cleanSentence() {
        Sentence sentence = new Sentence();
        for (Word word : data) {
            Word clean = word.cleanWord();
            sentence.add(clean);
        }
        return sentence;
    }

    // checks if a candidate list of strings matches every word with this sentence.
    public boolean isCorrection(List<String> candidate) {
        if (data.size() != candidate.size()) {
            return false;
        }
        for (int i = 0; i < data.size(); i++) {
            if (!candidate.get(i).equals(data.get(i).getCorrectWord())) {
                return false;
            }
        }
        return true;
    }
}
