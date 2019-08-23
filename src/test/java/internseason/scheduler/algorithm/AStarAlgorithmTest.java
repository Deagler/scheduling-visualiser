package internseason.scheduler.algorithm;

import internseason.scheduler.input.DOTParser;
import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AStarAlgorithmTest {
    private Graph graph;
    private DOTParser parser;
    @Before
    public void setup() {
        this.parser = new DOTParser();
    }

    @Test
    public void testAStarSchedule() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Test_Diamond.dot");
            BaseAlgorithm algorithm = AlgorithmFactory.getAlgorithm(AlgorithmType.A_STAR_ALGORITHM, 0);
            Schedule schedule = algorithm.execute(graph, 2);

        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAStarScheduleBig() {
        try {
            Graph graph = this.parser.parse("src/test/resources/big_chungus_16p_30nodes.dot");
            BaseAlgorithm algorithm = AlgorithmFactory.getAlgorithm(AlgorithmType.A_STAR_ALGORITHM, 0);
            Schedule schedule = algorithm.execute(graph, 2);

        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAStarSchedule1() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Test_Diamond.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2);
            assertEquals(schedule.getCost(), 8);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAStarScheduleAlphabet() {
        try {
            Graph graph = this.parser.parse("src/test/resources/alphabet.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2);
            assertEquals(schedule.getCost(), 8);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testAStarSchedule2() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_7_OutTree.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2);
            System.out.println(schedule);
            assertEquals(schedule.getCost(), 28);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test11NodeGraphOnTwoProcessors() {
        testGraph("src/test/resources/Nodes_11_OutTree.dot", 2, 350);
    }

    @Test
    public void test21NodeGraphOnTwoProcessors() {
        testGraph("src/test/resources/Nodes_21_floating.dot", 2, 92);
    }


    @Test
    public void test11NodeGraphOnFourProcessors() {
        testGraph("src/test/resources/Nodes_11_OutTree.dot", 4, 227);
    }


    @Test
    public void testAStarSchedule4() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_9_SeriesParallel.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,4);
            System.out.println(schedule);
            assertEquals(schedule.getCost(), 55);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAStarSchedule5() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_10_Random.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2);

            assertEquals(schedule.getCost(), 50);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAStarSchedule6() {
        testGraph("src/test/resources/Nodes_8_Random.dot", 2, 581);

    }


    private void testGraph(String graphPath, int numberOfProcessors, int expectedCost) {
        try {
            Graph graph = this.parser.parse(graphPath);
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,numberOfProcessors);

            assertEquals(expectedCost, schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }


}
