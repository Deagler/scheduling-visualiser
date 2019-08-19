package internseason.scheduler;
import internseason.scheduler.algorithm.AStarAlgorithm;
import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Schedule;
import internseason.scheduler.output.DOTOutputWriter;
import org.apache.commons.cli.*;

public class Main {

    public static void main(String[] args) {

        CLIParser parser = new CLIParser();
        DOTParser dotParser = new DOTParser();
        Config config;

        try {
            config = parser.parse(args);

            Graph newGraph = dotParser.parse(config.getInputDotFile());
            AStarAlgorithm algorithm = new AStarAlgorithm(newGraph, config.getNumberOfProcessors());
            Schedule schedule = algorithm.execute();

            DOTOutputWriter outputWriter = new DOTOutputWriter();

            outputWriter.write(config.getOutputFileName(), schedule, newGraph.getTasks());

        } catch (CLIException | InputException e) {
            System.out.println("Error: "+e.getMessage());
            parser.printHelp("scheduler-basic-T10 INPUT.dot <NumberOfProcessors>");
        }

    }

}
