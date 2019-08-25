package internseason.scheduler.algorithm;

import internseason.scheduler.input.DOTParser;
import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AStarAlgorithmParallelTest {
    private Graph graph;
    private DOTParser parser;
    private SystemInformation sysInfo;
    @Before
    public void setup() {
        this.parser = new DOTParser();
        this.sysInfo = new SystemInformation();
    }

    @Test
    public void testDiamondTwoCore() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Test_Diamond.dot");
            BaseAlgorithm algorithm = AlgorithmFactory.getAlgorithm(AlgorithmType.A_STAR_ALGORITHM);
            Schedule schedule = algorithm.execute(graph, 2,2, sysInfo);
            assertEquals(8,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDiamondFourCore() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Test_Diamond.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2,4,sysInfo);
            assertEquals(8, schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSevenOutTreeTwoCore() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_7_OutTree.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2,2,sysInfo);
            assertEquals( 28,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSevenOutTreeFourCore() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_7_OutTree.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2,4,sysInfo);
            assertEquals( 28,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEightRandomTwoCore() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_8_Random.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2,2,sysInfo);
            assertEquals( 581,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEightRandomFourCore() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_8_Random.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2,4,sysInfo);
            assertEquals( 581,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNineParallelFourProcessorTwoCore() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_9_SeriesParallel.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,4,2,sysInfo);
            System.out.println(schedule);
            assertEquals(55,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNineParallelFourProcessorEightCore() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_9_SeriesParallel.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,4,8,sysInfo);
            System.out.println(schedule);
            assertEquals(55,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void testTenRandomTwoCore() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_10_Random.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2,2,sysInfo);

            assertEquals(50,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTenRandomFourCore() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_10_Random.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2,4,sysInfo);

            assertEquals(50,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testElevenOutTreeTwoProcessorTwoCore() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_11_OutTree.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2,2,sysInfo);
            System.out.println(schedule);
            assertEquals(350,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testElevenOutTreeTwoProcessorFourCore() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_11_OutTree.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2,4,sysInfo);
            System.out.println(schedule);
            assertEquals(350,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testElevenOutTreeFourProcessorTwoCore() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_11_OutTree.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,4,2,sysInfo);
            System.out.println(schedule);
            assertEquals(227,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testElevenOutTreeFourProcessorFourCore() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_11_OutTree.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,4,4,sysInfo);
            System.out.println(schedule);
            assertEquals(227,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testTwentyFiveNodeTwoProcessorTwoCore() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_21_floating.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2,2,sysInfo);
            System.out.println(schedule);
            assertEquals(92,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTwentyFiveNodeTwoProcessorEightCore() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_21_floating.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2,8,sysInfo);
            System.out.println(schedule);
            assertEquals(92,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAStarScheduleBigFourCore() {
        try {
            Graph graph = this.parser.parse("src/test/resources/big_chungus_16p_30nodes.dot");
            BaseAlgorithm algorithm = AlgorithmFactory.getAlgorithm(AlgorithmType.A_STAR_ALGORITHM);
            Schedule schedule = algorithm.execute(graph, 2,4, sysInfo);
            assertEquals(273,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAStarScheduleBigEightCore() {
        try {
            Graph graph = this.parser.parse("src/test/resources/big_chungus_16p_30nodes.dot");
            BaseAlgorithm algorithm = AlgorithmFactory.getAlgorithm(AlgorithmType.A_STAR_ALGORITHM);
            Schedule schedule = algorithm.execute(graph, 2,8, sysInfo);
            assertEquals(273,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAStarScheduleAlphabetTwoCore() {
        try {
            Graph graph = this.parser.parse("src/test/resources/alphabet.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2,2, sysInfo);
            assertEquals(8,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAStarScheduleAlphabetFourCore() {
        try {
            Graph graph = this.parser.parse("src/test/resources/alphabet.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm();
            Schedule schedule = algorithm.execute(graph,2,4, sysInfo);
            assertEquals(8,schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }
}
