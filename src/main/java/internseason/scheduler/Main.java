package internseason.scheduler;
import internseason.scheduler.algorithm.AlgorithmFactory;
import internseason.scheduler.algorithm.AlgorithmType;
import internseason.scheduler.algorithm.BaseAlgorithm;
import internseason.scheduler.algorithm.SystemInformation;
import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.gui.FXVisualisation;
import internseason.scheduler.input.CLIException;
import internseason.scheduler.input.CLIParser;
import internseason.scheduler.input.Config;
import internseason.scheduler.input.DOTParser;
import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Schedule;
import internseason.scheduler.output.DOTOutputWriter;
import javafx.application.Application;
import javafx.util.Pair;

public class Main {
    public static Config config;
    public static void main(String[] args) {

        CLIParser parser = new CLIParser();

        try {
            config = parser.parse(args);
             if (config.isVisualisationEnabled()) {
                Application.launch(FXVisualisation.class);
            } else {
                startAlgorithm(config, new SystemInformation());
            }
        } catch (CLIException e) {
            System.out.println("Error: "+e.getMessage());
            parser.printHelp("internseason.scheduler.algorithm-basic-T10 INPUT.dot <NumberOfProcessors>");
        }

    }

    public static Pair<Schedule, Graph> startAlgorithm(Config config, SystemInformation sysInfo) {
        DOTParser dotparser = new DOTParser();
        Graph graph = null;
        try {
            graph = dotparser.parse(config.getInputDotFile());
        } catch (InputException e) {
            System.out.println("Error reading file: "+e.getMessage());
        }

        BaseAlgorithm algorithm = AlgorithmFactory.getAlgorithm(
                AlgorithmType.A_STAR_ALGORITHM,
                config.getNumberOfCores()
        );

        Schedule schedule = algorithm.execute(graph, config.getNumberOfProcessors(), sysInfo);
        DOTOutputWriter outputWriter = new DOTOutputWriter();

        outputWriter.write(config.getOutputFileName(), schedule, graph.getTasks());

        Pair<Schedule, Graph> result = new Pair<>(schedule, graph);
        return result;


    }
}
