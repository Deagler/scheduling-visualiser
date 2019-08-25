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
    private SystemInformation sysInfo;
    @Before
    public void setup() {
        this.parser = new DOTParser();
        this.sysInfo = new SystemInformation();
    }

    @Test
    public void testAStarSchedule() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Test_Diamond.dot");
            BaseAlgorithm algorithm = AlgorithmFactory.getAlgorithm(AlgorithmType.A_STAR_ALGORITHM);
            Schedule schedule = algorithm.execute(graph, 2,1, sysInfo);

        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAStarSchedule1() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Test_Diamond.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2,1,sysInfo);
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
            Schedule schedule = algorithm.execute(graph,2,1,sysInfo);

            assertEquals(schedule.getCost(), 28);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test11NodeGraphOnTwoProcessorsParallel() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_11_OutTree.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2,4,sysInfo);

            assertEquals(schedule.getCost(), 350);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test11NodeGraphOnTwoProcessorsParallel2() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_11_OutTree.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,4,8,sysInfo);

            assertEquals(schedule.getCost(), 227);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test25NodeGraphOnTwoProcessorsParallel() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_21_floating.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2,8,sysInfo);

            assertEquals(schedule.getCost(), 92);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void test11NodeGraphOnTwoProcessors() {
        testGraph("src/test/resources/Nodes_11_OutTree.dot", 2, 350);
    }

    @Test
    public void test10NodeGraphOnTwoProcessors() {
        testGraph("src/test/resources/Nodes_10_Random.dot", 2, 50);
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
            Schedule schedule = algorithm.execute(graph,4,1,sysInfo);

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
            Schedule schedule = algorithm.execute(graph,2,1,sysInfo);

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
            Schedule schedule = algorithm.execute(graph,numberOfProcessors,1, sysInfo);

            assertEquals(expectedCost, schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testVerifySortedFTOList() {

    }

    @Test
    public void testAStarScheduleBig() {
        try {
            Graph graph = this.parser.parse("src/test/resources/big_chungus_16p_30nodes.dot");
            BaseAlgorithm algorithm = AlgorithmFactory.getAlgorithm(AlgorithmType.A_STAR_ALGORITHM);
            Schedule schedule = algorithm.execute(graph, 2,1, sysInfo);

        } catch (InputException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testAStarScheduleAlphabet() {
        try {
            Graph graph = this.parser.parse("src/test/resources/alphabet.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2,1, sysInfo);
            assertEquals(schedule.getCost(), 8);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }
}
