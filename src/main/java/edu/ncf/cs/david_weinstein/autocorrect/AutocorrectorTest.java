package edu.ncf.cs.david_weinstein.autocorrect;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AutocorrectorTest {

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testGetWhiteSpaceSuggestions() {
    final Autocorrector ac = new Autocorrector();
    ac.feedCorpusThreeWords("hello", "there", "sir");
    final List<String> shouldOnlyContainHelloThere = ac
        .getWhiteSpaceSuggestions("hell othere");

    assertEquals(1, shouldOnlyContainHelloThere.size());
    assertEquals("hello there", shouldOnlyContainHelloThere.get(0));
  }
}
