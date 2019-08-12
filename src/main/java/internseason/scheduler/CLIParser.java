package internseason.scheduler;

import org.apache.commons.cli.*;

import java.util.Arrays;

public class CLIParser {

    private Options CLIOptions;
    private CommandLineParser parser;
    private HelpFormatter helpFormatter;

    public CLIParser() {
        CLIOptions = new Options();
        parser = new DefaultParser();
        helpFormatter = new HelpFormatter();
        setupCLIOptions();
    }

    public Config parse(String[] args) throws CLIException {

        CommandLine cmd = null;

        if(args.length < 2) {
            throw new CLIException("Must provide an input file and the number of processors.");
        }


        String inputFile = args[0];
        Integer numberOfProcessors;

        try {
            numberOfProcessors = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new CLIException("Number of processors (P) must be an integer");
        }

        ConfigBuilder builder = new ConfigBuilder(inputFile, numberOfProcessors);

        String[] argsToParse = Arrays.copyOfRange(args, 2, args.length);

        try {
            cmd = parser.parse(CLIOptions, argsToParse);
        } catch (ParseException e) {
            throw new CLIException("Malformed Arguments");
        }


        if(cmd.hasOption("p")) {
            try {
                int numberOfCores = Integer.parseInt(cmd.getOptionValue("p"));
                builder.setNumberOfCores(numberOfCores);
            } catch (NumberFormatException e) {
                throw new CLIException("The number of cores must be a valid positive integer");
            }
        }

        builder.setVisualisationEnabled(cmd.hasOption("v"));

        if(cmd.hasOption("o")) {
            builder.setOutputFileName(cmd.getOptionValue("o"));
        }

        return builder.build();
    }

    public void printHelp(String subtitle) {
        helpFormatter.printHelp(subtitle, CLIOptions);
    }

    private void setupCLIOptions() {
        Option numOfCores = Option.builder("p").argName("N").desc("use N cores for execution in parallel (default is sequential)").hasArg().build();
        Option visualisationEnabled = new Option("v", "visualise the search");
        Option outputFile = Option.builder("o").argName("OUTPUT").desc("output file (default is INPUT-output.dot)").hasArg().build();

        CLIOptions.addOption(numOfCores);
        CLIOptions.addOption(visualisationEnabled);
        CLIOptions.addOption(outputFile);
    }


}
