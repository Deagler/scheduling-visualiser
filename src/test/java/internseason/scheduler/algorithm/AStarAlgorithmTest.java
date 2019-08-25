package internseason.scheduler.algorithm;

import internseason.scheduler.input.DOTParser;
import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    public void testDiamondTwoProcessor() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Test_Diamond.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2,1,sysInfo);
            assertEquals(8,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSevenOutTreeTwoProcessor() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_7_OutTree.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2,1,sysInfo);
            System.out.println(schedule);
            assertEquals(schedule.getCost(), 28);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSevenOutTreeFourProcessor() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_7_OutTree.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,4,1,sysInfo);
            System.out.println(schedule);
            assertEquals(schedule.getCost(), 22);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testTenRandomOneProcessor() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_10_Random.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,1,1,sysInfo);

            assertEquals(63,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTenRandomTwoProcessor() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_10_Random.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2,1,sysInfo);

            assertEquals(50,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testElevenOutTreeOneProcessor() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_11_OutTree.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,1,1,sysInfo);
            System.out.println(schedule);
            assertEquals(640,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testElevenOutTreeTwoProcessor() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_11_OutTree.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2,1,sysInfo);
            System.out.println(schedule);
            assertEquals(350,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testElevenOutTreeFourProcessor() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_11_OutTree.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,4,1,sysInfo);
            System.out.println(schedule);
            assertEquals(227,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testTwentyFiveTwoProcessor() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_21_floating.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2,1,sysInfo);
            System.out.println(schedule);
            assertEquals(92,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBigTwoProcessor() {
        try {
            Graph graph = this.parser.parse("src/test/resources/big_chungus_16p_30nodes.dot");
            BaseAlgorithm algorithm = AlgorithmFactory.getAlgorithm(AlgorithmType.A_STAR_ALGORITHM);
            Schedule schedule = algorithm.execute(graph, 2,1, sysInfo);
            assertEquals(273, schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testAStarScheduleAlphabetTwoProcessor() {
        try {
            Graph graph = this.parser.parse("src/test/resources/alphabet.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2,1, sysInfo);
            assertEquals(8,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }
}
