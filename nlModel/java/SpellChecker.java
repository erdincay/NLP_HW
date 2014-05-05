// WARNING! Do not modify this. This file will not be submited as part of the
// assignment. Hence, any change in this file will be discarded at the time of
// evaluation.

import java.util.List;

public class SpellChecker {
    public static void evaluate(String filename, LanguageModel languageModel) {
        int numCorrect = 0;
        int numTotal = 0;

        Data devCorpus = new Data(filename);
        List<Sentence> testData = devCorpus.generateTestCases();
        for (int i = 0; i < testData.size(); ++i) {
            Sentence sentence = testData.get(i);
            List<String> errorSentence = sentence.getErrorSentence(); // with misspelling
            List<String> correct = sentence.getCorrectSentence(); // without misspelling

            List<String> hypothesis = languageModel.correctSentence(errorSentence); // hypothesis

            if (sentence.isCorrection(hypothesis)) {
                numCorrect++;
            }
            numTotal++;
        }
        System.out.printf("correct: %d total: %d accuracy: %f\n",
                numCorrect, numTotal, ((double) numCorrect) / numTotal);
    }


    public static void main(String[] args) {
        // Training
        Data trainingCorpus = new Data("data/train");
        LanguageModel langModel = new LanguageModel();
        langModel.train(trainingCorpus);

        if (args.length == 0 || args[0].equals("train")) {
            evaluate("data/test", langModel);
        } else {
            evaluate("data/eval", langModel);
        }
    }
}
