package edu.ncf.cs.david_weinstein.trie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Trie {
  List<Trie> children;
  Trie parent;
  Character key;
  Trie root;
  boolean endOfAWord = false;

  public Trie() {
    root = this;
    children = new ArrayList<Trie>();
  }

  public Trie(final Collection<String> words) {
    root = this;
    children = new ArrayList<Trie>();
    for (final String word : words) {
      insertIntoEntireDictionary(word);
    }
  }

  public Trie(final Character c, final Trie inputRoot, final Trie inputParent) {
    key = c;
    root = inputRoot;
    children = new ArrayList<Trie>();
    parent = inputParent;
  }

  public Boolean entireDictionaryContains(final String searchKey) {
    return root.recContains(searchKey);
  }

  public void insertIntoEntireDictionary(final String wordToInsert) {
    if (wordToInsert.equals("younger")) {
      System.out.println("enountered younger in insertIntoEntireDictionary");
    }
    root.recInsert(wordToInsert, root);
  }

  private Boolean recContains(final String searchKey) {
    final Trie child = childWithKey(searchKey.charAt(0));
    if (child == null) {
      return false;
    } else {
      if (searchKey.length() == 1) {
        return child.getEndOfWord();
      } else {
        return child.recContains(searchKey.substring(1));
      }
    }
  }

  private Trie childWithKey(final Character searchKey) {
    for (final Trie child : children) {
      if (child.getKey().equals(searchKey)) {
        return child;
      }
    }
    return null;
  }

  public List<String> getPossibleCompletions(final String prefix) {
    final List<String> completions = new ArrayList<String>();
    final Trie current = search(prefix);
    if (current == null) {
      return completions;
    }

    current.getPossibleCompletionsRec(prefix, completions);
    return completions;
  }

  private void getPossibleCompletionsRec(final String current,
      final List<String> completions) {
    if (endOfAWord) {
      completions.add(current);
    }

    for (final Trie child : children) {
      child.getPossibleCompletionsRec(current + child.getKey(), completions);
    }
  }

  private boolean getEndOfWord() {
    return endOfAWord;
  }

  /**
   * Returns Trie in tree that ends at the path searchKey, or null
   *
   * @param searchKey
   *          path to search
   * @return a Trie or null
   */
  private Trie search(final String searchKey) {
    final Trie child = childWithKey(searchKey.charAt(0));
    // no where to recurse, return what you have - success or failure both go through here
    if (child == null || searchKey.length() == 1) {
      return child;
    } else {
      // recurse to continue searching
      return child.search(searchKey.substring(1));
    }
  }

  private final void recInsert(final String wordToInsert, final Trie r) {
    if (wordToInsert.length() == 0) {
      endOfAWord = true;
      root = r;
      return;
    }
    final Trie child = childWithKey(wordToInsert.charAt(0));
    if (child != null) {
      if (wordToInsert.length() == 1) {
        child.endOfAWord = true;
        child.root = r;
        return;
      } else {
        child.recInsert(wordToInsert.substring(1), r);
        return;
      }
    }

    final Trie newTrie = new Trie(wordToInsert.charAt(0), r, this);
    children.add(newTrie);
    newTrie.recInsert(wordToInsert.substring(1), r);
  }

  private final Trie getParent() {
    return parent;
  }

  private final void setParent(final Trie parent) {
    this.parent = parent;
  }

  private final Character getKey() {
    return key;
  }

  private final void setKey(final Character key) {
    this.key = key;
  }

  private final List<Trie> getChildren() {
    return children;
  }
}
