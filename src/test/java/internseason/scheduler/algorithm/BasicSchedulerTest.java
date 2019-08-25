package internseason.scheduler.algorithm;

import internseason.scheduler.input.DOTParser;
import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BasicSchedulerTest {
    private Graph graph;
    private DOTParser parser;
    @Before
    public void setup() {
        this.parser = new DOTParser();
    }

    @Test
    public void testBasicScheduleOneProcessor() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_8_Random.dot");
            BaseAlgorithm algorithm = AlgorithmFactory.getAlgorithm(AlgorithmType.BASIC_ALGORITHM, 0);
            Schedule schedule = algorithm.execute(graph, 1,1);
            assertEquals("Processor 0\n" +
                    "t0 scheduled at: 0\n" +
                    "t1 scheduled at: 35\n" +
                    "t2 scheduled at: 123\n" +
                    "t3 scheduled at: 299\n" +
                    "t4 scheduled at: 458\n" +
                    "t5 scheduled at: 634\n" +
                    "t6 scheduled at: 775\n" +
                    "t7 scheduled at: 916\n" +
                    "Cost of Processor 0: 969\n" +
                    "Total schedule cost is: 969", schedule.toString());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBasicScheduleTwoProcessor() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_8_Random.dot");
            BaseAlgorithm algorithm = AlgorithmFactory.getAlgorithm(AlgorithmType.BASIC_ALGORITHM, 0);
            Schedule schedule = algorithm.execute(graph, 2,1);
            assertEquals("Processor 0\n" +
                    "t0 scheduled at: 0\n" +
                    "t1 scheduled at: 35\n" +
                    "t2 scheduled at: 123\n" +
                    "t3 scheduled at: 299\n" +
                    "t4 scheduled at: 458\n" +
                    "t5 scheduled at: 634\n" +
                    "t6 scheduled at: 775\n" +
                    "t7 scheduled at: 916\n" +
                    "Cost of Processor 0: 969\n" +
                    "Processor 1\n" +
                    "Cost of Processor 1: 0\n" +
                    "Total schedule cost is: 969", schedule.toString());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }
}
