import java.util.HashSet;
import java.util.Set;

public class SpellCorrector {
    private Set<String> learnedWords;

    public SpellCorrector() {
        learnedWords = new HashSet<String>();
    }

    public void train(final String word) {
        learnedWords.add(word);
    }

    public String correct(final String mispelled_word) {
//        if (learnedWords.contains(mispelled_word)) {
//            return mispelled_word;
//        } else {
//            for (String word : learnedWords) {
//                if (LevenshteinDistance(mispelled_word, word) <= 1) {
//                    return word;
//                }
//            }
//        }

        for (String word : learnedWords) {
            if (!word.equals(mispelled_word) && LevenshteinDistance(mispelled_word, word) <= 1) {
                return word;
            }
        }

        return mispelled_word;
    }

    private int Minimum(int n1, int n2, int n3) {
        return Math.min(Math.min(n1, n2), n3);
    }

    private int LevenshteinDistance(String src, String dst) {
        int row_num = src.length() + 1;
        int col_num = dst.length() + 1;
        int[][] dis = new int[row_num][col_num];

        for (int i = 0; i < row_num; i++) {
            dis[i][0] = i;
        }

        for (int i = 0; i < col_num; i++) {
            dis[0][i] = i;
        }

        for (int i = 1; i < row_num; i++) {
            for (int j = 1; j < col_num; j++) {
                if (src.charAt(i - 1) == dst.charAt(j - 1)) {
                    dis[i][j] = Minimum(dis[i - 1][j] + 1, dis[i][j - 1] + 1, dis[i - 1][j - 1]);
                } else {
                    dis[i][j] = Minimum(dis[i - 1][j] + 1, dis[i][j - 1] + 1, dis[i - 1][j - 1] + 1);
                }
            }
        }
        return dis[row_num - 1][col_num - 1];
    }

}
