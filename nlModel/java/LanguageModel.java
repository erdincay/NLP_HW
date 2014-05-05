import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageModel {
    // Edit Distance Model as we learnt in previous assignment. Only difference
    // is that, we are returning all the possible 1-edit-distant words with a
    // probability.
    private EditDistanceModel edit_dist_model_;
    private final int gramSize = 2;

    Map<List<String>, Integer> wordDic;
    Map<List<String>, Integer> preDic;
//    int totalWords;


    // TODO(student): Define your data-structure here.

    public LanguageModel() {
//        totalWords = 0;
        edit_dist_model_ = new EditDistanceModel();
        wordDic = new HashMap<List<String>, Integer>();
        preDic = new HashMap<List<String>, Integer>();

    }

    private static void AddGram(List<String> key, Map<List<String>, Integer> dic) {
        if (!key.isEmpty()) {
            if (dic.containsKey(key)) {
                dic.put(key, dic.get(key) + 1);
            } else {
                dic.put(key, 1);
            }
        }
    }

    private static int GetGram(List<String> key, Map<List<String>, Integer> dic) {
        if (dic.containsKey(key)) {
            return dic.get(key);
        }

        return 0;
    }

    public void train(Data corpus) {
        // TODO(student): your training code should go here
        for (Sentence sentence : corpus.getSentences()) { // iterate over sentences
            int Length = sentence.size();
            if (gramSize <= 1) {
                Length -= 1;
            }
            for (int i = 1; i < Length; i++) {
                Word word = sentence.get(i);
                String w = word.getCorrectWord(); // get the actual word
                // training edit distance model
                edit_dist_model_.train(w);

                //language model
                List<String> preKey = new ArrayList<String>(gramSize);
                for (int j = gramSize - 1; j > 0; j--) {
                    int index = i - j;
                    if (index < 0) {
                        index = 0;
                    }
                    preKey.add(sentence.get(index).getCorrectWord());
                }
                AddGram(preKey, preDic);

                List<String> wordKey = new ArrayList<String>(preKey);
                wordKey.add(w);
                AddGram(wordKey, wordDic);
//                totalWords++;
            }
        }
    }

    private double score(List<String> sentence) {
        // TODO(student): Score a sentence using your learnt model
        // NOTE THAT, we are returning log-probability here and using in
        // correctSentence() method. If you use probability directly, you might
        // need to change correctSentence() as well.
        double probability = 1.0;

        int Length = sentence.size();
        if (gramSize <= 1) {
            Length -= 1;
        }
        for (int i = 1; i < Length; i++) {
            List<String> preKey = new ArrayList<String>(gramSize);
            for (int j = gramSize - 1; j > 0; j--) {
                int index = i - j;
                if (index < 0) {
                    index = 0;
                }
                preKey.add(sentence.get(index));
            }
            int countPre = GetGram(preKey, preDic) + preDic.size();

            List<String> wordKey = new ArrayList<String>(preKey);
            wordKey.add(sentence.get(i));
            int countWord = GetGram(wordKey, wordDic) + 1;

            probability *= ((double) countWord) / countPre;
        }
        double score = Math.log(probability);
        return score;
    }

    // NOTE: you don't need to modify following method for this assignment. But
    // if you are done with your model and want to tweak following ranking, feel
    // free to try tweaking.
    List<String> correctSentence(List<String> sentence) {
        int argmax_i = 0;
        String argmax_w = sentence.get(0);
        double max = Double.NEGATIVE_INFINITY;

        // skip first and last tokens.
        for (int i = 1; i < sentence.size() - 1; i++) {
            String word = sentence.get(i);
            Map<String, Double> editProbs = edit_dist_model_.score(word);
            for (String alternative : editProbs.keySet()) {
                // skip non-edits:
                if (alternative.equals(word)) {
                    continue;
                }
                sentence.set(i, alternative);
                double score = score(sentence) + Math.log(editProbs.get(alternative));
                if (score >= max) {
                    max = score;
                    argmax_i = i;
                    argmax_w = alternative;
                }
            }
            sentence.set(i, word); // restore sentence to original state
        }
        List<String> argmax = new ArrayList<String>(sentence);
        argmax.set(argmax_i, argmax_w);
        return argmax;
    }
}
