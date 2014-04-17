import java.util.*;

public class SpellCorrector {
    private class MyEntry implements Comparable, Map.Entry<String, Integer> {
        private String key;
        private Integer val;

        public MyEntry(Map.Entry<String, Integer> pair) {
            key = pair.getKey();
            val = pair.getValue();
        }

        public Integer getValue() {
            return val;
        }

        @Override
        public Integer setValue(Integer value) {
            val = value;

            return 0;
        }

        public String getKey() {
            return key;
        }

        @Override
        public int compareTo(Object o) {
            if (this == o) return 0;
            if (!(o instanceof MyEntry)) return 0;

            MyEntry e = (MyEntry) o;

            return e.getValue().compareTo(getValue());
        }
    }

    private final Integer defaultEditDis = 2;
    private Map<String, Integer> learnedWords;
    private Map<String, String> mem;

    public SpellCorrector() {
        learnedWords = new HashMap<String, Integer>();
        mem = new HashMap<String, String>();
    }

    public void train(final String word) {
        String lWord = word.toLowerCase();
        if (learnedWords.containsKey(lWord)) {
            learnedWords.put(lWord, learnedWords.get(lWord) + 1);
        } else {
            learnedWords.put(lWord, 1);
        }
    }

    public String correct(final String mispelled_word) {
        Map<Integer, List<MyEntry>> ret = new HashMap<Integer, List<MyEntry>>();

        for (final Map.Entry<String, Integer> pair : learnedWords.entrySet()) {
            Integer distance = LevenshteinDistance(pair.getKey(), mispelled_word);
            if (distance <= defaultEditDis) {
                if (ret.containsKey(distance)) {
                    ret.get(distance).add(new MyEntry(pair));
                } else {
                    ret.put(distance, new LinkedList<MyEntry>() {{
                        add(new MyEntry(pair));
                    }});
                }
            }
        }

        for (int i = 1; i <= defaultEditDis; i++) {
            if (ret.containsKey(i)) {
                List<MyEntry> candidates = ret.get(i);
                Collections.sort(candidates);
                for (MyEntry e : candidates) {
                    String word = e.getKey();

                    if (mem.containsKey(word) && mem.get(word).equals(mispelled_word)){
                        continue;
                    }

                    mem.put(word,mispelled_word);
                    return word;
                }

                return candidates.get(0).getKey();
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
