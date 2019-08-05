package internseason.scheduler;

import org.apache.commons.cli.*;

public class Main {

    public static void main(String[] args) {

        CLIParser parser = new CLIParser();
        Config config;

        try {
            config = parser.parse(args);
            System.out.println(config.toString());
        } catch (CLIException e) {
            System.out.println("Error: "+e.getMessage());
            parser.printHelp();
        }

    }

}
