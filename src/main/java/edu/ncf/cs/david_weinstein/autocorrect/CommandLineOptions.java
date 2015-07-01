package edu.ncf.cs.david_weinstein.autocorrect;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineOptions {
  public static CommandLine getParsed(final String[] args) {
    final Options options = new Options();

    // add commandline options
    options
    .addOption(
        "led",
        true,
        "led<distance> Activate Levenshtein Edit Distance suggestions up to the given distance.");
    options.addOption("prefix", false, "Activate prefix suggestions.");
    options.addOption("smart", false, "Activate Weinstein's special ordering.");
    options.addOption("whitespace", false, "Activate whitespace corrections.");
    options.addOption("gui", false, "Activate the gui.");
    final Option filename = OptionBuilder
        .withArgName("filename")
        .isRequired()
        .withValueSeparator(' ')
        .hasArg()
        .withDescription(
            "use given file(s) as corpus. Space separated if there are multiple files.")
            .create("filename");
    options.addOption(filename);

    final CommandLineParser parser = new DefaultParser();
    try {
      return parser.parse(options, args);
    } catch (final ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.out
      .println("Sorry, I'm unable to parse that input. I've printed the error message above.");
      return null;
    }
  }

  protected static Autocorrector getSpecifiedAutocorrector(final String[] args) {
    final CommandLine parsedInput = CommandLineOptions.getParsed(args);
    final Autocorrector autocorrector = new Autocorrector();

    // ensure they specified a corpus and feed it into autocorrect
    final List<String> filePathStrings = Arrays.asList(parsedInput
        .getOptionValues("filename"));
    if (filePathStrings.size() < 1) {
      System.out
      .println("You need to specify at least filename via --filename <filepath (or paths separated by spaces)");
      System.exit(1);
    } else {
      autocorrector.feedCorpus(filePathStrings);
    }

    // get the led distance if any
    final String ledDistString = parsedInput.getOptionValue("led");
    if (ledDistString != null) {
      try {
        final int ledDist = new Integer(ledDistString);
        autocorrector.setLedDistance(ledDist);
      } catch (final Exception e) {
        e.printStackTrace();
      }
    }

    // get the boolean options
    if (parsedInput.hasOption("prefix")) {
      autocorrector.setUsesPrefixCorrection(true);
    }
    if (parsedInput.hasOption("smart")) {
      autocorrector.setUsesSmartOrdering(true);
    }
    if (parsedInput.hasOption("whitespace")) {
      autocorrector.setCorrectsWhiteSpace(true);
    }
    if (parsedInput.hasOption("gui")) {
      // not implemented yet
    }
    return autocorrector;
  }

}
