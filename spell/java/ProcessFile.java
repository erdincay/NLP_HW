// Copyright 2014 smanna@csupomona.edu
//
// !!WARNING!! STUDENT SHOULD NOT MODIFY THIS FILE.
// NOTE THAT THIS FILE WILL NOT BE SUBMITTED, WHICH MEANS MODIFYING THIS FILE
// WILL NOT TAKE EFFECT WHILE EVALUATING YOUR CODE.

import java.io.*;
import java.util.*;

public class ProcessFile {
    private String file_;
    static private final String strOldGolden = "goldenOld.txt";
    static private final String strNewGolden = "goldenNew.txt";

    // constructor
    public ProcessFile(String filename) {
        file_ = filename;
    }

    public void learn(SpellCorrector corrector) throws Exception {
        FileReader file = new FileReader(file_);
        Scanner sc = new Scanner(file);
        String line;
        while (sc.hasNext()) {
            line = sc.nextLine();
            line = line.replaceAll("[^A-Za-z]", " ").trim();
            //System.out.println("LINE:" + line);
            String[] splittedStr = line.split(" ");
            for (String str : splittedStr) {
                if (str.isEmpty()) {
                    continue;
                }
                //System.out.println(str);
                corrector.train(str);
            }
        }
    }

    private static List<String> generateNewWords(List<String> allWords, int size) {
        List<String> ret = new ArrayList<String>();
        Random generator = new Random();
        for (int i = 0; i < size; i++) {
            int index = generator.nextInt(allWords.size());
            String word = allWords.get(index);
            ret.add(word + " " + word);
            allWords.remove(index);
        }

        return ret;
    }

    public static void generateNewGolden(String oldName, String newName, Map<String, Integer> dic) throws IOException {

        List<String> new_words = generateNewWords(new LinkedList<String>(dic.keySet()), 400);
        List<String> old_words = new ArrayList<String>();
        Scanner sc = new Scanner(new File(oldName));
        while (sc.hasNextLine()) {
            old_words.add(sc.nextLine());
        }
        old_words.addAll(new_words);
        sc.close();

        //System.out.println(oldName + " has " + old_words.size() + " words.");

        File file = new File(newName);
        if (file.exists()) {
            file.delete();
        }
        FileWriter writer= new FileWriter(file, true);
        for (String word : old_words) {
            writer.write(word + System.lineSeparator());
        }
        writer.close();
    }

    public static void correctGolden(String golden_file, SpellCorrector corrector) throws IOException {
        FileReader file = new FileReader(golden_file);
        Scanner sc = new Scanner(file);
        String line;
        int num_entries = 0, num_incorrect = 0, num_correct = 0;
        while (sc.hasNext()) {
            line = sc.nextLine();
            String[] tokens = line.split(" ");
            if (tokens.length != 2) {
                continue;
            }
            String corrected = corrector.correct(tokens[0]);
            ++num_entries;
            if (corrected.equals(tokens[1])) {
                ++num_correct;
            } else {
                ++num_incorrect;
                //System.out.println("Incorrect Correction: " + tokens[0] + " => " + corrected + " : " + tokens[1]);
            }
        }
        String total = golden_file + " Total: " + num_entries + " Correct: " + num_correct + " Incorrect: " + num_incorrect + System.lineSeparator();
        String score = golden_file + " Score(%): " + 100.0 * num_correct / num_entries + System.lineSeparator();
        String separate = "------------------------------------------------------------------" + System.lineSeparator() + System.lineSeparator();
        System.out.print(total);
        System.out.print(score);
        System.out.print(separate);

        FileWriter writer = new FileWriter("log.txt", true);
        writer.write(total);
        writer.write(score);
        writer.write(separate);
        writer.close();
    }

    public static void learnData(String path, SpellCorrector corrector) throws Exception {
        File[] subdirs = new File(path).listFiles();

        for (File d : subdirs) {
            if (d.getName().charAt(0) == '.') {
                continue;
            }
            File[] files = new File(path + d.getName()).listFiles();
            for (File file : files) {
                if (file.getName().charAt(0) == '.') {
                    continue;
                }
                ProcessFile pf = new ProcessFile(path + d.getName() +
                        "/" + file.getName());
                pf.learn(corrector);
            }
        }
    }

    public static void main(String[] args) throws Exception {
//        HashSet<String> allResults;
//        allResults = new HashSet<String>();
//        HashSet<String> expectedResults;
//        String path;
//        String golden_file;
//        if (args.length == 0 || args[0].equals("train")) {
//            golden_file = strOldGolden;
//            path = "data/";
//        } else {
//            golden_file = "testgolden.txt";
//            path = "data/";
//        }

        SpellCorrector corrector = new SpellCorrector();
        learnData("data/", corrector);
        generateNewGolden(strOldGolden, strNewGolden, corrector.getLearnedWords());
        correctGolden(strOldGolden, corrector);
        correctGolden(strNewGolden, corrector);
    }
}
