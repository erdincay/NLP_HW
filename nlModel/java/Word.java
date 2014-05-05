// WARNING! Do not modify this. This file will not be submited as part of the
// assignment. Hence, any change in this file will be discarded at the time of
// evaluation.
public class Word {
    protected String word_; // correct word
    protected String error_; // spelling error

    public Word(String w, String e) {
        word_ = w;
        error_ = e;
    }

    public Word cleanWord() {
        return new Word(word_, "");
    }

    public boolean hasError() {
        return !(error_ == null || error_.equals(""));
    }

    public String getCorrectWord() {
        return word_;
    }

    public String getError() {
        return error_;
    }

    // returns true if the error is within edit distance one and contains no numerics/punctuation
    public boolean isValidTest() {
        if (!hasError()) {
            return false;
        }
        return true;
    }
}
