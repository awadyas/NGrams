// Ngram.java

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.ArrayList;

public class Loader {
  // This is a hard-coded variable to include "stopwords" in analysis.
  public static boolean sw = true;
  // ArrayList that will contain all stopwords
  public static ArrayList<String> stopwords = new ArrayList<String>();

  /**
   * Main function
   *
   * Checks for correct commandline syntax and then loads ngrams into file
   */
  public static void main(String[] args){
    if (args.length != 2){
      printUsage();
    }
    stopwords = loadStopWords();
    loadNGrams(getScanner(args[0]), args[1]);
  }

  /**
   * Load NGrams
   *
   * Loads the ngrams into out.txt, given a Scanner and string specifying which nGramType
   * of ngram is wanted (unigram/bigram/trigram)
   */
  public static void loadNGrams(Scanner scanner, String nGramType){
    NGramCountMap tree = null;
    if (nGramType.equals("uni")){
      tree = loadUnigrams(scanner);
    } else if(nGramType.equals("bi")){
      tree = loadBigrams(scanner);
    } else if(nGramType.equals("tri")){
      tree = loadTrigrams(scanner);
    } else{
      printUsage();
    }

    FileWriter writer = null;
    try{
      writer = new FileWriter(new File("model.txt"));
    } catch(IOException e){
      System.err.println(e);
      System.exit(1);
    }

    ArrayList<NGramCount> nGrams = tree.getNgramCountsByCount();
    for (int i=0; i<nGrams.size(); i++){
      try{
        writer.write(nGrams.get(i).nGramToString() + "\n");
      } catch(IOException e){
        System.err.println(e);
        System.exit(1);
      }
    }
  }

  /**
   * Get Scanner
   *
   * Creates and returns a Scanner object for a given file
   */
  public static Scanner getScanner(String filename){
    Scanner scanner = null;
    try{
      scanner = new Scanner(new File(filename));
    }catch(FileNotFoundException e){
      System.err.println(e);
      System.exit(1);
    }

    return scanner;
  }

  /**
   * Load Stopwords
   *
   * Loads "stopwords" from text file into an ArrayList, and returns ArrayList
   */
  public static ArrayList<String> loadStopWords(){

    Scanner scanner = getScanner("stopwords.txt");

    ArrayList<String> stopwords = new ArrayList<String>();
    while (scanner.hasNext()){
      stopwords.add(scanner.next());
    }

    return stopwords;
  }

  /**
   * Load Unigrams
   *
   * Returns a binary search tree of all possible unigrams and their counts from
   * the file attatched to the given Scanner
   */
  public static NGramCountMap loadUnigrams(Scanner scanner){

    NGramCountMap tree = new NGramCountMap();
    String word = scanner.next();

        while (scanner.hasNext()){
          word = word.toLowerCase();
          if (!sw){
            if (!inStopWords(stopwords, word)){
              tree.put(word);
            }
          }else{
            tree.put(word);
          }
          word = scanner.next();
        }

        if (!sw){
          if (!inStopWords(stopwords, word)){
            tree.put(word);
          }
        }else{
          tree.put(word);
        }

      scanner.close();
      return tree;
  }

  /**
   * Load Bigrams
   *
   * Returns a binary search tree of all possible bigrams and their counts from
   * the file attatched to the given Scanner
   */
  public static NGramCountMap loadBigrams(Scanner scanner){

    NGramCountMap tree = new NGramCountMap();
    String word = scanner.next();

      while (scanner.hasNext()){
          word = word.toLowerCase();
          String prevWord = word;
          word = scanner.next();
          word = word.toLowerCase();
          if (!sw){
              if (!inStopWords(stopwords, word) && !inStopWords(stopwords, prevWord)){
                  tree.put(prevWord + " " + word);
              }
          } else{
              tree.put(prevWord + " " + word);
          }
      }
    return tree;

  }

  /**
   * Load Trigrams
   *
   * Returns a binary search tree of all possible trigrams and their counts from
   * the file attatched to the given Scanner
   */
  public static NGramCountMap loadTrigrams(Scanner scanner){

    NGramCountMap tree = new NGramCountMap();

    String word = scanner.next();
    String word2 = "";
    String word3 = "";
    try{
        word2 = scanner.next();
        word3 = scanner.next();
    } catch (NoSuchElementException e){}

    while (scanner.hasNext()){
        word = word.toLowerCase();
        word2 = word2.toLowerCase();
        word3 = word3.toLowerCase();
        if (!sw){
            if (!inStopWords(stopwords, word) && !inStopWords(stopwords, word2) && !inStopWords(stopwords, word3)){
                tree.put(word + " " + word2 + " " + word3);
            }
        } else{
            tree.put(word + " " + word2 + " " + word3);
        }
        word = word2;
        word2 = word3;
        word3 = scanner.next();
    }
    word = word.toLowerCase();
    word2 = word2.toLowerCase();
    word3 = word3.toLowerCase();
    if (!sw){
        if (!inStopWords(stopwords, word) && !inStopWords(stopwords, word2) && !inStopWords(stopwords, word3)){
            tree.put(word + " " + word2 + " " + word3);
        }
    } else{
        tree.put(word + " " + word2 + " " + word3);
    }
    return tree;
  }

  /**
   * In Stopwords
   *
   * Returns true if the given word is in the ArrayList of stopwords
   */
  public static boolean inStopWords(ArrayList<String> stopwords, String word){
    for (int i = 0; i < stopwords.size(); i++){
        if (stopwords.get(i).equals(word)){
            return true;
        }
    }
    return false;
  }

  /**
   * Print Usage
   *
   * Prints a usage statement for incorrect commandline syntax
   */
  public static void printUsage(){
    System.err.println("Usage: java Loader filename.txt uni/bi/tri");
    System.exit(1);
  }



}
