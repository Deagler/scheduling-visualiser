package internseason.scheduler;
import internseason.scheduler.algorithm.AStarAlgorithm;
import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.gui.FXVisualisation;
import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Schedule;
import internseason.scheduler.output.DOTOutputWriter;
import javafx.application.Application;
import org.apache.commons.cli.*;

public class Main {
    public static Config config;
    public static void main(String[] args) {

        CLIParser parser = new CLIParser();



        try {
            config = parser.parse(args);

            if (config.isVisualisationEnabled()) {
                Application.launch(FXVisualisation.class);
            } else {
                startAlgorithm(config);
            }

        } catch (CLIException | InputException e) {
            System.out.println("Error: "+e.getMessage());
            parser.printHelp("scheduler-basic-T10 INPUT.dot <NumberOfProcessors>");
        }

    }


    public static void startAlgorithm(Config config) throws InputException {
        DOTParser dotParser = new DOTParser();

        Graph newGraph = dotParser.parse(config.getInputDotFile());

        AStarAlgorithm algorithm = new AStarAlgorithm(newGraph, config.getNumberOfProcessors());
        Schedule schedule = algorithm.execute();
        System.out.println(schedule);
        DOTOutputWriter outputWriter = new DOTOutputWriter();

        outputWriter.write(config.getOutputFileName(), schedule, newGraph.getTasks());
    }

}
