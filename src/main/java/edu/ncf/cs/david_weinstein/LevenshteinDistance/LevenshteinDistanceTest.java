package edu.ncf.cs.david_weinstein.LevenshteinDistance;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class LevenshteinDistanceTest {

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testGetLevenshteinDistanceByDavid() {
    assertEquals(0,
        LevenshteinDistance.getLevenshteinDistanceByDavid("DW", "DW"));
    assertEquals(1,
        LevenshteinDistance.getLevenshteinDistanceByDavid("DW", "D"));
    assertEquals(2,
        LevenshteinDistance.getLevenshteinDistanceByDavid("NW", "D"));

    assertEquals(1,
        LevenshteinDistance.getLevenshteinDistanceByDavid("you", "yvu"));
    assertEquals(1,
        LevenshteinDistance.getLevenshteinDistanceByDavid("you", "yu"));

    assertEquals(1,
        LevenshteinDistance.getLevenshteinDistanceByDavid("David", "DavidW"));
    assertEquals(1,
        LevenshteinDistance.getLevenshteinDistanceByDavid("David", "Dawid"));
    assertEquals(2,
        LevenshteinDistance.getLevenshteinDistanceByDavid("David", "DawidW"));

    assertEquals(3,
        LevenshteinDistance.getLevenshteinDistanceByDavid("David", "awidW"));

  }

  @Test
  public void testGetLevenshteinDistance() {
    assertEquals(0, LevenshteinDistance.getLevenshteinDistanceByWiki("DW", "DW"));
    assertEquals(1, LevenshteinDistance.getLevenshteinDistanceByWiki("DW", "D"));
    assertEquals(2, LevenshteinDistance.getLevenshteinDistanceByWiki("NW", "D"));

    assertEquals(1, LevenshteinDistance.getLevenshteinDistanceByWiki("you", "yvu"));
    assertEquals(1, LevenshteinDistance.getLevenshteinDistanceByWiki("you", "yu"));

    assertEquals(1,
        LevenshteinDistance.getLevenshteinDistanceByWiki("David", "DavidW"));
    assertEquals(1,
        LevenshteinDistance.getLevenshteinDistanceByWiki("David", "Dawid"));
    assertEquals(2,
        LevenshteinDistance.getLevenshteinDistanceByWiki("David", "DawidW"));

    assertEquals(3,
        LevenshteinDistance.getLevenshteinDistanceByWiki("David", "awidW"));

  }

}
