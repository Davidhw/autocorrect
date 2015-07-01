package edu.ncf.cs.david_weinstein.Bigram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class BigramAndMonogram {
  // bigram functionality
  HashMap<String, List<String>> nextWords;
  // monogram functionality
  HashMap<String, Integer> wordFrequency;

  public BigramAndMonogram() {
    nextWords = new HashMap<>();
    wordFrequency = new HashMap<>();
  }

  public Set<String> getWordSet() {
    return nextWords.keySet();
  }

  public int getWordFrequency(final String word) {
    return wordFrequency.get(word);
  }

  public void insertWordPair(final String precedingWord, final String word) {
    if (nextWords.get(precedingWord) != null) {
      nextWords.get(precedingWord).add(word);
    } else {
      final List<String> pWords = new ArrayList<>();
      pWords.add(precedingWord);
      nextWords.put(word, pWords);
    }

    if (wordFrequency.get(word) != null) {
      wordFrequency.put(word, wordFrequency.get(word) + 1);
    } else {
      wordFrequency.put(word, 1);
    }
  }

  public List<String> pickMostCommonFollowers(final String precedingWord,
      final List<String> potentialFollowers) {
    System.out
    .println("pick most common followers called with preceding word: "
        + precedingWord);
    if (precedingWord.equals("")) {
      return potentialFollowers;
    }
    // followers are words that have come after a word in a corpus
    // potential followers are words that may follow a word in a corpus, we don't know.
    // we will return the potential follower that follows the precedingWord the most

    final List<String> maxFollowers = new ArrayList<>();

    final List<String> followers = nextWords.get(precedingWord);

    if (followers == null) {
      return potentialFollowers;
    }
    if (precedingWord == null || followers.size() == 0
        || potentialFollowers.size() == 0) {
      System.out
          .println("followers null, preceding word null, no followers, no potential followers");
      return followers;
    }

    int frequency = 0;
    int maxFrequency = 0;
    for (int followerIndex = 0; followerIndex < potentialFollowers.size(); followerIndex++) {
      frequency = Collections.frequency(followers,
          potentialFollowers.get(followerIndex));
      if (frequency > maxFrequency) {
        maxFollowers.clear();
        maxFollowers.add(potentialFollowers.get(followerIndex));
        maxFrequency = frequency;
      } else {
        if (frequency == maxFrequency) {
          maxFollowers.add(potentialFollowers.get(followerIndex));
          maxFrequency = frequency;
        }
      }
    }
    return maxFollowers;

  }

}
