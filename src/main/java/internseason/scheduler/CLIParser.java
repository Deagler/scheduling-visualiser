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
            throw new CLIException("INPUT.dot and P are required");
        }


        String inputFile = args[0];
        Integer numberOfProcessors;

        try {
            numberOfProcessors = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new CLIException((e.getMessage()));
        }

        ConfigBuilder builder = new ConfigBuilder(inputFile, numberOfProcessors);

        String[] argsToParse = Arrays.copyOfRange(args, 2, args.length);

        try {
            cmd = parser.parse(CLIOptions, argsToParse);
        } catch (ParseException e) {
            throw new CLIException("Malformed Arguments");
        }


        if(cmd.getArgList().size() != 0) {
            throw new CLIException("Invalid Arguments");
        }


        if(cmd.hasOption("p")) {
            try {
                int numberOfCores = Integer.parseInt(cmd.getOptionValue("p"));
                builder.setNumberOfCores(numberOfCores);
            } catch (NumberFormatException e) {
                throw new CLIException("-p must be a valid integer");
            }
        }

        builder.setVisualisationEnabled(cmd.hasOption("v"));

        if(cmd.hasOption("o")) {
            builder.setOutputFileName(cmd.getOptionValue("o"));
        }

        return builder.build();
    }

    public void printHelp() {
        helpFormatter.printHelp("InternSeason: Scheduler", CLIOptions);
    }

    private void setupCLIOptions() {
        Option numOfCores = Option.builder("p").argName("N").desc("use N cores for execution in parallel (default is sequential)").hasArg().build();
        Option visualisationEnabled = new Option("v", "visualise the search");
        Option outputFile = Option.builder("o").argName("OUTPUT").desc("output file is named OUTPUT (default is INPUT−output.dot)").build();

        CLIOptions.addOption(numOfCores);
        CLIOptions.addOption((visualisationEnabled));
        CLIOptions.addOption((outputFile));
    }


}
