// WARNING! Do not modify this. This file will not be submited as part of the
// assignment. Hence, any change in this file will be discarded at the time of
// evaluation.

import java.util.HashMap;
import java.util.HashSet;
import java.util.*;

public class EditDistanceModel {
    private HashMap<String, Integer> freq_map_;

    EditDistanceModel() {
        freq_map_ = new HashMap<String, Integer>();
    }

    public void train(final String word) {
        if (freq_map_.containsKey(word)) {
            freq_map_.put(word, freq_map_.get(word) + 1);
        } else {
            freq_map_.put(word, 1);
        }
    }

    public void printDict() {
        Iterator it = freq_map_.entrySet().iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }
    }

    // TODO
    public HashMap<String, Double> score(final String wrong_word) {
        HashMap<String, Double> ret = new HashMap<String, Double>();
        // Model based on training data:
        // NumWords=13847 %SpellError=8.868346934354012
        // %edit1=5.4668881346139955 %edit2=2.397631255867697
        // %edit3=0.7655087744637827 %editGt3=0.23831876940853614
        //
        // So same word is correct => 91%
        //         Edit Distance 1 => 5.5%
        //         Edit Distance 2 => 2.5%
        //       Edit Distance > 2 => 1%
        //
        HashSet<String> known_edit1 = known(edit1(wrong_word));
        int total1 = 0;
        for (String w : known_edit1) {
            total1 += getFreq(w);
        }
        boolean isGivenWordValid = freq_map_.containsKey(wrong_word);
        if (total1 == 0) {
            ret.put(wrong_word, 1.0);
        } else if (isGivenWordValid) {
            ret.put(wrong_word, 0.91);
        }
        for (String w : known_edit1) {
            if (isGivenWordValid) {
                ret.put(w, 0.09 * getFreq(w) / total1);
            } else {
                ret.put(w, 1.0 * getFreq(w) / total1);
            }
        }
        // END GOLDEN
        return ret;
    }

    public String correct(final String wrong_word) {
        // Check if original word is valid
        if (freq_map_.containsKey(wrong_word)) {
            //System.out.println("DEBUG: Wrong is right=" + wrong_word);
            return wrong_word;
        }
        HashSet<String> known_edit1 = known(edit1(wrong_word));
        if (!known_edit1.isEmpty()) {
            //System.out.println("DEBUG: Edit1: " + known_edit1);
            String ret = mostFrequent(known_edit1);
            if (!ret.isEmpty()) {
                return ret;
            }
        }

        HashSet<String> known_edit2 = known(edit2(wrong_word));
        if (!known_edit2.isEmpty()) {
            String ret = mostFrequent(known_edit2);
            if (!ret.isEmpty()) {
                return ret;
            }
        }
        return wrong_word;
    }

    private int getFreq(final String word) {
        if (freq_map_.containsKey(word)) {
            return freq_map_.get(word);// + 1;
        } else {
            return 0;//1;
        }
    }

    private String mostFrequent(HashSet<String> words) {
        int max_count = 0;
        String most_frequent = "";
        for (String w : words) {
            int count = getFreq(w);
            if (count > max_count) {
                max_count = count;
                most_frequent = w;
            }
        }
        return most_frequent;
    }

    private HashSet<String> known(HashSet<String> words) {
        HashSet<String> known = new HashSet<String>();
        for (String word : words) {
            //System.out.println("DEBUG: known: " + word);
            if (freq_map_.containsKey(word)) {
                known.add(word);
                //System.out.println("DEBUG: known=" + word);
            }
        }
        return known;
    }

    private HashSet<String> edit1(String word) {
        HashSet<String> edit1 = new HashSet<String>();
        // Insert
        for (int i = 0; i <= word.length(); ++i) {
            for (char c = 'a'; c <= 'z'; ++c) {
                edit1.add(word.substring(0, i) + c + word.substring(i));
            }
        }
        // Replaces
        for (int i = 0; i < word.length(); ++i) {
            for (char c = 'a'; c <= 'z'; ++c) {
                edit1.add(word.substring(0, i) + c + word.substring(i + 1));
            }
        }
        // Deletes
        if (word.length() > 1) {
            // No deletion for single letter word
            for (int i = 0; i < word.length(); ++i) {
                edit1.add(word.substring(0, i) + word.substring(i + 1));
            }
        }
        // Transpose
        for (int i = 0; i < word.length() - 1; ++i) {
            edit1.add(word.substring(0, i) + word.charAt(i + 1) + word.charAt(i) +
                    word.substring(i + 2));
        }
        //for (String e1 : edit1) {
        //  System.out.println("DEBUG: edit1(" + word + "): " + e1);
        //}
        return edit1;
    }

    private HashSet<String> edit2(String word) {
        HashSet<String> e1s = edit1(word);
        HashSet<String> e2s = new HashSet<String>();
        for (String e1 : e1s) {
            for (String e2 : edit1(e1)) {
                e2s.add(e2);
            }
        }
        return e2s;
    }
}

//alphabet = 'abcdefghijklmnopqrstuvwxyz'
//
//def edits1(word):
//   splits     = [(word[:i], word[i:]) for i in range(len(word) + 1)]
//   deletes    = [a + b[1:] for a, b in splits if b]
//   transposes = [a + b[1] + b[0] + b[2:] for a, b in splits if len(b)>1]
//   replaces   = [a + c + b[1:] for a, b in splits for c in alphabet if b]
//   inserts    = [a + c + b     for a, b in splits for c in alphabet]
//   return set(deletes + transposes + replaces + inserts)
//
//def known_edits2(word):
//    return set(e2 for e1 in edits1(word) for e2 in edits1(e1) if e2 in NWORDS)
//
//def known(words): return set(w for w in words if w in NWORDS)
//
//def correct(word):
//    candidates = known([word]) or known(edits1(word)) or known_edits2(word) or [word]
//    return max(candidates, key=NWORDS.get)
