// Copyright 2014 smanna@csupomona.edu
//
// !!WARNING!! STUDENT SHOULD NOT MODIFY THIS FILE.
// NOTE THAT THIS FILE WILL NOT BE SUBMITTED, WHICH MEANS MODIFYING THIS FILE
// WILL NOT TAKE EFFECT WHILE EVALUATING YOUR CODE.

import java.io.FileReader;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import java.io.File;

public class ProcessFile {
  public HashSet<String> processFile(String filename) throws Exception {
    FileReader file = new FileReader(filename);
    Scanner sc = new Scanner(file);
    String line;
    HashSet<String> ret = new HashSet<String>();
    LineProcessor lp = new LineProcessor();
    while (sc.hasNext()) {
      line = sc.nextLine();
      HashSet<String> result = lp.processLine(line);
      if (result != null && !result.isEmpty()) {
        ret.addAll(result);
      }
    }
    return ret;
  }

  public static HashSet<String> getGolden(String filename) throws Exception {
    FileReader file = new FileReader(filename);
    Scanner sc = new Scanner(file);
    String line;
    HashSet<String> ret = new HashSet<String>();
    while (sc.hasNext()) {
      line = sc.nextLine();
      ret.add(line);
    }
    return ret;
  }

  public static void score(HashSet<String> allResults,
                           HashSet<String> expectedResults) {
    try {
      // Convert both side to lower case
      replace(allResults);
      replace(expectedResults);

      int tp = 0, fp = 0, fn = 0;
      // Compare eact set
      for (String ret : allResults) {
        if (expectedResults.contains(ret)) {
          ++tp;
          //System.out.println("True Positive: " + ret);
        } else {
          ++fp;
          System.out.println("False Positive: " + ret);
        }
      }

      for (String ret : expectedResults) {
        if (!allResults.contains(ret)) {
          ++fn;
          System.out.println("False Negative: " + ret);
        }
      }
      System.out.println("Total TP: " + tp + " FP: " + fp + " FN: " + fn);
      System.out.println("Score: " + (tp - fp - fn));
    } catch (NullPointerException e) {
      System.out.println("Null Pointer");
    }
  }

  public static void replace(Set<String> strings) {
    String[] stringsArray = strings.toArray(new String[0]);
    for (int i = 0; i < stringsArray.length; ++i)
      stringsArray[i] = stringsArray[i].toLowerCase();
    strings.clear();
    strings.addAll(Arrays.asList(stringsArray));
  }

  public static void main(String[] args) throws Exception {
    HashSet<String> allResults;
    allResults = new HashSet<String>();
    HashSet<String> expectedResults;
    String path;
    if (args.length == 0 || args[0].equals("train")) {
      expectedResults = getGolden("golden.txt");
      path = "data/";
    } else {
      expectedResults = getGolden("testgolden.txt");
      path = "testdata/";
    }

    ProcessFile pf = new ProcessFile();
    File[] files = new File(path).listFiles();

    for (File file : files) {
      HashSet<String> results = pf.processFile(path + file.getName());
      for (String result : results) {
        allResults.add(file.getName() + '\t' + result);
      }
    }
    score(allResults, expectedResults);
  }
}
