package scheduler;

import internseason.scheduler.DOTParser;
import internseason.scheduler.algorithm.AStarAlgorithm;
import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

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
            AStarAlgorithm algorithm = new AStarAlgorithm(graph,2);
            Schedule schedule = algorithm.execute();
            System.out.println(schedule);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }
}
