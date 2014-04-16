// Copyright 2014 smanna@csupomona.edu
//
// !!WARNING!! STUDENT SHOULD NOT MODIFY THIS FILE.
// NOTE THAT THIS FILE WILL NOT BE SUBMITTED, WHICH MEANS MODIFYING THIS FILE
// WILL NOT TAKE EFFECT WHILE EVALUATING YOUR CODE.

import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Scanner;

public class ProcessFile {
    private String file_;

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

    public static void main(String[] args) throws Exception {
        HashSet<String> allResults;
        allResults = new HashSet<String>();
        HashSet<String> expectedResults;
        String path;
        String golden_file;
        if (args.length == 0 || args[0].equals("train")) {
            golden_file = "golden.txt";
            path = "data/";
        } else {
            golden_file = "testgolden.txt";
            path = "data/";
        }

        SpellCorrector corrector = new SpellCorrector();
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
                //System.out.println("Incorrect Correction: " + tokens[0] + " => " +
                //                   corrected);
            }
        }
        System.out.println("Total: " + num_entries + " Correct: " + num_correct +
                " Incorrect: " + num_incorrect);
        System.out.println("Score(%): " + 100.0 * num_correct / num_entries);
    }
}
