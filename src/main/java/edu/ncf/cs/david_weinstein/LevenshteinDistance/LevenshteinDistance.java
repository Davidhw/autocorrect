package edu.ncf.cs.david_weinstein.LevenshteinDistance;

import java.util.HashMap;

public class LevenshteinDistance {

  static class TwoWords {
    String word1;
    String word2;

    public TwoWords(final String w1, final String w2) {
      if (w1.compareTo(w2) == -1) {
        word1 = w1;
        word2 = w2;
      } else {
        word1 = w2;
        word2 = w1;
      }
    }

    @Override
    public int hashCode() {
      return word1.hashCode() + word2.hashCode() * 3;
    }

  }

  public static int getLevenshteinDistanceByDavid(final String word1,
      final String word2) {
    final HashMap<TwoWords, Integer> map = new HashMap<>();
    return getLevenshteinDistanceByDavidRec(word1, word2, map);
  }

  public static int getLevenshteinDistanceByDavidRec(final String word1,
      final String word2, final HashMap<TwoWords, Integer> map) {
    final TwoWords tw = new TwoWords(word1, word2);
    if (map.containsKey(tw)) {
      return map.get(tw);
    }

    if (word2.length() == 0) {
      return word1.length();
    } else if (word1.length() == 0) {
      return word2.length();
    }

    // not a misspelling. playful representation of what the variables mean
    final String wor1 = word1.substring(0, word1.length() - 1);
    final String wor2 = word2.substring(0, word2.length() - 1);

    final int rec1 = getLevenshteinDistanceByDavidRec(wor1, word2, map) + 1;
    final int rec2 = getLevenshteinDistanceByDavidRec(word1, wor2, map) + 1;
    int cost = 0;
    if (word1.charAt(word1.length() - 1) == word2.charAt(word2.length() - 1)) {
      cost = 0;
    } else {
      cost = 1;
    }
    final int rec3 = getLevenshteinDistanceByDavidRec(wor1, wor2, map) + cost;

    final int minVal = Math.min(rec3, Math.min(rec1, rec2));
    map.put(tw, minVal);
    return minVal;
  }

  // essentially directly from
  // http://en.wikipedia.org/wiki/Levenshtein_distance#Iterative_with_two_matrix_rows

  // this solution is way faster than my own (which was also based on an algorithm described in
  // wikipedia) and should use less memory than others, which will help balance out storing the
  // corpus, word freqs, previous word lists, etc in memory
  public static int getLevenshteinDistanceByWiki(final String s, final String t) {
    // if they're the same string there's no distance
    if (s == t)
      return 0;

    // if either string is 0, then every addition from the other string is the distance
    if (s.length() == 0)
      return t.length();
    if (t.length() == 0)
      return s.length();

    // create two work vectors of integer distances
    final int[] v0 = new int[t.length() + 1];
    final int[] v1 = new int[t.length() + 1];

    // initialize v0 (the previous row of distances)
    // this row is A[0][i]: edit distance for an empty s
    // the distance is just the number of characters to delete from t
    for (int i = 0; i < v0.length; i++)
      v0[i] = i;

    for (int i = 0; i < s.length(); i++) {
      // calculate v1 (current row distances) from the previous row v0

      // first element of v1 is A[i+1][0]
      // edit distance is delete (i+1) chars from s to match empty t
      v1[0] = i + 1;

      // use formula to fill in the rest of the row
      for (int j = 0; j < t.length(); j++) {
        final int cost = s.charAt(i) == t.charAt(j) ? 0 : 1;
        v1[j + 1] = Math.min(Math.min(v1[j] + 1, v0[j + 1] + 1), v0[j] + cost);
      }

      // copy v1 (current row) to v0 (previous row) for next iteration
      for (int j = 0; j < v0.length; j++)
        v0[j] = v1[j];
    }

    return v1[t.length()];
  }
}
