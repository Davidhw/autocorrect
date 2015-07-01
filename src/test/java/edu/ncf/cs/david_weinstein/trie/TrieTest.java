package edu.ncf.cs.david_weinstein.trie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

public class TrieTest extends TestCase {

  final Trie trie = new Trie();
  final String run = "run";
  final String runs = "runs";
  final String ruNotInserted = "ru";
  final String raNotInserted = "ra";
  final String rrNotInserted = "rn";
  final String ranNotInserted = "ran";
  List<String> insertedStrings = Arrays.asList(new String[] { run, runs });
  List<String> notInsertedStrings = Arrays.asList(new String[] { ruNotInserted,
      raNotInserted, rrNotInserted, ranNotInserted });

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    trie.insertIntoEntireDictionary(run);
    trie.insertIntoEntireDictionary(runs);
  }

  public void testContains() {
    for (final String inserted : insertedStrings) {
      assertTrue(trie.entireDictionaryContains(inserted));
    }
    for (final String notInserted : notInsertedStrings) {
      assertFalse(trie.entireDictionaryContains(notInserted));
    }
  }

  public void testPossibleCompletions() {
    assertTrue(trie.getPossibleCompletions("b").size() == 0);
    final List<String> rCompletions = trie.getPossibleCompletions("r");
    final List<String> ruCompletions = trie.getPossibleCompletions("ru");
    final List<String> runCompletions = trie.getPossibleCompletions("run");

    assertTrue(rCompletions.contains(run));
    assertTrue(rCompletions.contains(runs));
    assertFalse(rCompletions.contains(ranNotInserted));
    assertFalse(rCompletions.contains(ruNotInserted));
    assertFalse(rCompletions.contains("r"));

    assertTrue(ruCompletions.contains(run));
    assertTrue(ruCompletions.contains(runs));
    assertFalse(ruCompletions.contains(ruNotInserted));

    assertTrue(runCompletions.contains(run));
    assertTrue(runCompletions.contains(runs));

  }

  public void testCreationFromList() {
    final List<String> addThese = new ArrayList<String>();
    addThese.add("youth");
    addThese.add("you");
    addThese.add("your");
    final Trie trie = new Trie(addThese);
    trie.insertIntoEntireDictionary("test");
    for (final String s : addThese) {
      System.out.println(s);
      assertTrue(trie.entireDictionaryContains(s));
    }

    assertFalse(trie.entireDictionaryContains("yo"));
    final List<String> possibleCompletions = trie.getPossibleCompletions("yo");
    assertFalse(possibleCompletions.contains("yo"));
    assertFalse(possibleCompletions.contains("yo"));
    assertFalse(possibleCompletions.contains("yout"));

  }

}
