package internseason.scheduler.algorithm;

import com.sun.javafx.scene.traversal.Algorithm;
import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.input.DOTParser;
import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Schedule;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BBAlgorithmTest {
    private Graph graph;
    private DOTParser parser;
    @Before
    public void setup() {
        this.parser = new DOTParser();
    }


    @Test
    public void testBBSchedule1() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Test_Diamond.dot");
            BaseAlgorithm algorithm = AlgorithmFactory.getAlgorithm(AlgorithmType.BRANCH_AND_BOUND_ALGORITHM, 0);
            Schedule schedule = algorithm.execute(graph,2);
            assertEquals(schedule.getCost(), 8);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBBSchedule2() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_7_OutTree.dot");
            BaseAlgorithm algorithm = AlgorithmFactory.getAlgorithm(AlgorithmType.BRANCH_AND_BOUND_ALGORITHM, 0);
            Schedule schedule = algorithm.execute(graph,2);
            System.out.println(schedule);
            assertEquals(schedule.getCost(), 28);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBBSchedule3() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_9_SeriesParallel.dot");
            BaseAlgorithm algorithm = AlgorithmFactory.getAlgorithm(AlgorithmType.BRANCH_AND_BOUND_ALGORITHM, 0);
            Schedule schedule = algorithm.execute(graph,4);
            System.out.println(schedule);
            assertEquals(schedule.getCost(), 55);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }



    private void testGraph(String graphPath, int numberOfProcessors, int expectedCost) {
        try {
            Graph graph = this.parser.parse(graphPath);
            BaseAlgorithm algorithm = AlgorithmFactory.getAlgorithm(AlgorithmType.BRANCH_AND_BOUND_ALGORITHM, 0);
            Schedule schedule = algorithm.execute(graph,numberOfProcessors);

            assertEquals(expectedCost, schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }


}
