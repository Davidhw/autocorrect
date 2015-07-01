package edu.ncf.cs.david_weinstein.autocorrect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Hello world!
 *
 */
public class Main {
  public static void main(final String[] args) {
    // separating CommandLineOptions into its own class doesn't separate concerns much, but it at
    // least makes the main function more readable.
    // besides, you can basically guess how the options parsing works, given that I used Apache CLI
    // library
    final Autocorrector autocorrector = CommandLineOptions
        .getSpecifiedAutocorrector(args);

    // start read input loop
    final BufferedReader inputReader = new BufferedReader(
        new InputStreamReader(System.in));
    String inputLine;
    System.out.println("Ready for input.");
    try {
      while (!(inputLine = inputReader.readLine()).trim().equals("")) {
        inputLine.replaceAll("[^a-zA-Z ]", "").replaceAll("\\s+", " ")
            .toLowerCase();
        System.out.println(autocorrector.correct(inputLine));
      }
    } catch (final IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
}
