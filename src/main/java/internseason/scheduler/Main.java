package internseason.scheduler;
import internseason.scheduler.algorithm.AlgorithmFactory;
import internseason.scheduler.algorithm.AlgorithmType;
import internseason.scheduler.algorithm.BaseAlgorithm;
import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.input.CLIException;
import internseason.scheduler.input.CLIParser;
import internseason.scheduler.input.Config;
import internseason.scheduler.input.DOTParser;
import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Schedule;
import internseason.scheduler.output.DOTOutputWriter;

public class Main {
    public static void main(String[] args) {

        CLIParser parser = new CLIParser();
        DOTParser dotParser = new DOTParser();
        Config config;

        try {
            config = parser.parse(args);
            startAlgorithm(config);
        } catch (CLIException e) {
            System.out.println("Error: "+e.getMessage());
            parser.printHelp("internseason.scheduler.algorithm-basic-T10 INPUT.dot <NumberOfProcessors>");
        }

    }

    public static void startAlgorithm(Config config) {
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

        Schedule schedule = algorithm.execute(graph, config.getNumberOfProcessors());
        DOTOutputWriter outputWriter = new DOTOutputWriter();

        outputWriter.write(config.getOutputFileName(), schedule, graph.getTasks());



    }

}
