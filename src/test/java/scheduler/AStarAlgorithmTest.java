package scheduler;

import internseason.scheduler.DOTParser;
import internseason.scheduler.algorithm.AStarAlgorithm;
import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AStarAlgorithmTest {
    private Graph graph;
    private DOTParser parser;
    @Before
    public void setup() {
        this.parser = new DOTParser();
    }

    @Test
    public void testAStarSchedule1() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Test_Diamond.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm(graph,3);
            Schedule schedule = algorithm.execute();

            assertEquals(schedule.getCost(), 8);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAStarSchedule2() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_7_OutTree.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm(graph,4);
            Schedule schedule = algorithm.execute();
            System.out.println(schedule);
            assertEquals(schedule.getCost(), 22);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAStarSchedule3() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_11_OutTree.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm(graph,2);
            Schedule schedule = algorithm.execute();

            assertEquals(schedule.getCost(), 350);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAStarSchedule4() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_9_SeriesParallel.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm(graph,4);
            Schedule schedule = algorithm.execute();
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
            AStarAlgorithm algorithm = new AStarAlgorithm(graph,2);
            Schedule schedule = algorithm.execute();

            assertEquals(schedule.getCost(), 50);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAStarSchedule6() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_8_Random.dot");
            AStarAlgorithm algorithm = new AStarAlgorithm(graph,4);
            Schedule schedule = algorithm.execute();

            assertEquals(schedule.getCost(), 581);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }


}
