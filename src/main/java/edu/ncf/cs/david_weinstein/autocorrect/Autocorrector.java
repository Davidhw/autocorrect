package edu.ncf.cs.david_weinstein.autocorrect;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import edu.ncf.cs.david_weinstein.Bigram.BigramAndMonogram;
import edu.ncf.cs.david_weinstein.LevenshteinDistance.LevenshteinDistance;
import edu.ncf.cs.david_weinstein.trie.Trie;

public class Autocorrector {
  /**
   * Levenshtein distance max for suggestions. 0 if led is not used to generate suggestions.
   */
  private int ledDistance;
  private boolean usesWhiteSpaceCorrection;
  private boolean usesSmartOrdering;
  private boolean usesPrefixCorrection;
  private Trie trie;
  private final BigramAndMonogram bigram;

  public Autocorrector() {
    bigram = new BigramAndMonogram();
    ledDistance = 0;
    usesWhiteSpaceCorrection = false;
    usesSmartOrdering = false;
    usesPrefixCorrection = false;
  }

  // protected void setCorpus(final ArrayList<String> inputCorpus) {
  // corpus = inputCorpus;
  // }

  private Collection<String> getCorpus() {
    return bigram.getWordSet();
  }

  protected void feedCorpusThreeWords(final String s1, final String s2,
      final String s3) {
    // this function exsits to enable convienent testing of autocorrector
    bigram.insertWordPair("", s1);
    bigram.insertWordPair(s1, s2);
    bigram.insertWordPair(s2, s3);
  }

  protected void feedCorpus(final Collection<String> corporaFilepaths) {
    for (final String filepath : corporaFilepaths) {
      Scanner s;
      try {
        s = new Scanner(new File(filepath));
        String precedingWord = "";
        while (s.hasNext()) {
          final String nextWord = s.next().replaceAll("[^a-zA-Z ]", "")
              .replaceAll("\\s+", " ").toLowerCase();
          bigram.insertWordPair(precedingWord, nextWord);
          precedingWord = nextWord;
          // corpus.add(nextWord);
        }
        s.close();
        // trie = new Trie(corpus);
        trie = new Trie(getCorpus());

      } catch (final FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        System.out.println("Could not read from this file " + filepath);
        System.exit(1);
      }

    }
  }

  protected final int getLedDistance() {
    return ledDistance;
  }

  protected final void setLedDistance(final int ledDistance) {
    System.out.println("setting led dist to " + Integer.toString(ledDistance));
    this.ledDistance = ledDistance;
  }

  protected final boolean isCorrectsWhiteSpace() {
    return usesWhiteSpaceCorrection;
  }

  protected final void setCorrectsWhiteSpace(final boolean correctsWhiteSpace) {
    this.usesWhiteSpaceCorrection = correctsWhiteSpace;
  }

  protected final boolean isUsesSmartOrdering() {
    return usesSmartOrdering;
  }

  protected final void setUsesSmartOrdering(final boolean usesSmartOrdering) {
    this.usesSmartOrdering = usesSmartOrdering;
  }

  protected final boolean isUsesWhiteSpaceCorrection() {
    return usesWhiteSpaceCorrection;
  }

  protected final void setUsesWhiteSpaceCorrection(
      final boolean usesWhiteSpaceCorrection) {
    this.usesWhiteSpaceCorrection = usesWhiteSpaceCorrection;
  }

  protected final boolean isUsesPrefixCorrection() {
    return usesPrefixCorrection;
  }

  protected final void setUsesPrefixCorrection(
      final boolean usesPrefixCorrection) {
    this.usesPrefixCorrection = usesPrefixCorrection;
  }

  public String correct(final String line) {
    // split the word to be corrected from the rest of the line
    // but save the word preceding it for bigram purposes
    // and save the rest of the line so that we return the entire line plus the corrected word
    String word;

    // if there is only one word in the line these values will persist
    String precedingWord = "";
    String precedingWords = "";
    String precedingWordsMinusOne = "";

    final String[] words = line.split(" ");
    if (words.length == 1) {
      word = words[0];
    } else {
      word = words[words.length - 1];
      precedingWord = words[words.length - 2];
      for (int i = 0; i < words.length - 1; i++) {
        precedingWords += words[i] + " ";
      }
      precedingWords = precedingWords.trim();

      for (int i = 0; i < words.length - 2; i++) {
        precedingWordsMinusOne += words[i] + " ";
      }
      precedingWordsMinusOne = precedingWordsMinusOne.trim();
    }

    if (getCorpus().contains(word)) {
      return precedingWords + " " + word;
    } else if (usesWhiteSpaceCorrection) {
      for (final String w : getWhiteSpaceSuggestions(precedingWord + " " + word)) {
        if (getCorpus().contains(w)) {
          return precedingWordsMinusOne + " " + w;
        }
      }
    }

    final List<String> possibleCorrections = new ArrayList<>();
    if (usesPrefixCorrection) {
      possibleCorrections.addAll(trie.getPossibleCompletions(word));
    }

    if (ledDistance > 0) {
      for (final String wordInCorpus : getCorpus()) {
        if (LevenshteinDistance
            .getLevenshteinDistanceByWiki(word, wordInCorpus) <= ledDistance) {
          possibleCorrections.add(wordInCorpus);
        }
      }
    }

    if (usesSmartOrdering) {
      return precedingWords + " "
          + getBestCorrectionCustom(word, precedingWord, possibleCorrections);
    } else {
      return precedingWords + " "
          + getBestCorrection(word, precedingWord, possibleCorrections);
    }
  }

  private String getBestCorrectionCustom(final String word,
      final String precedingWord, final List<String> possibleCorrections) {
    if (possibleCorrections.size() == 0) {
      return word;
    }

    for (final String possibleCorrection : possibleCorrections) {
      if (LevenshteinDistance.getLevenshteinDistanceByWiki(word,
          possibleCorrection) < 2) {
        return possibleCorrection;
      }
    }

    return getBestCorrection(word, precedingWord, possibleCorrections);

  }

  private String getBestCorrection(final String word,
      final String precedingWord, final List<String> possibleCorrections) {
    // exact match is handled in correct
    System.out.println(possibleCorrections.size());
    if (possibleCorrections.size() == 0) {
      return word;
    } else {

      final List<String> bestCorrections = bigram.pickMostCommonFollowers(
          precedingWord, possibleCorrections);

      if (bestCorrections.size() == 1) {
        return bestCorrections.get(0);
      } else {

        if (bestCorrections.size() == 0) {

        }
        // System.out.println("adding possible corrections");
        // bestCorrections.addAll(possibleCorrections);
        // }
        final List<String> mostCommonBestCorrections = new ArrayList<>();
        int maxFreq = 0;
        for (final String correction : bestCorrections) {
          final int freq = bigram.getWordFrequency(correction);
          if (freq > maxFreq) {
            mostCommonBestCorrections.clear();
            mostCommonBestCorrections.add(correction);
            maxFreq = freq;
          } else if (freq == maxFreq) {
            mostCommonBestCorrections.add(correction);
          }
        }

        if (mostCommonBestCorrections.size() == 0) {
          return word;
        } else if (mostCommonBestCorrections.size() == 1) {
          return mostCommonBestCorrections.get(0);
        } else {
          mostCommonBestCorrections.sort(null);
          return mostCommonBestCorrections.get(0);
        }
      }
    }

  }

  protected List<String> getWhiteSpaceSuggestions(String s) {
    s = s.replaceAll("\\s", "");
    final int length = s.length();
    final List<String> suggestions = new ArrayList<>();
    for (int i = 1; i <= length; i++) {
      final String part1 = s.substring(0, i);
      final String part2 = s.substring(i, length);
      if (getCorpus().contains(part1)) {
        if (getCorpus().contains(part2)) {
          suggestions.add(part1 + " " + part2);
        } else {
          if (part2.length() == 0) {
            suggestions.add(part1);
          }
        }
      } else {
        if (getCorpus().contains(part2) && part1.length() == 0) {
          suggestions.add(part2);
        }
      }
    }

    return suggestions;
  }
}
