package internseason.scheduler.algorithm;

import com.sun.javafx.scene.traversal.Algorithm;
import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.input.DOTParser;
import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Schedule;
import internseason.scheduler.model.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class BBAlgorithmTest {
    private Graph graph;
    private DOTParser parser;
    @Before
    public void setup() {
        this.parser = new DOTParser();
    }


    @Test
    public void testGreedyScheduler() throws InputException {
        Graph graph = this.parser.parse("src/test/resources/Nodes_7_OutTree.dot");

        Set<String> free = new HashSet<>();
        for(Task task : graph.getTasks().values()) {
            if (task.getId().equals("0")) {
                free.add(task.getId());

            }
        }
        Schedule schedule = Schedule.buildGreedySchedule(new BBScheduleInfo(new Schedule(2, graph.getTasks()), Integer.MIN_VALUE, Integer.MAX_VALUE, free),graph);
        System.out.println(schedule);



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

    @Test
    public void testBBSchedule4() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_8_Random.dot");
            BaseAlgorithm algorithm = AlgorithmFactory.getAlgorithm(AlgorithmType.BRANCH_AND_BOUND_ALGORITHM, 0);
            Schedule schedule = algorithm.execute(graph,4);
            System.out.println(schedule);
            assertEquals(schedule.getCost(), 581);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBBSchedule5() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_11_OutTree.dot");
            BaseAlgorithm algorithm = AlgorithmFactory.getAlgorithm(AlgorithmType.BRANCH_AND_BOUND_ALGORITHM, 0);
            Schedule schedule = algorithm.execute(graph,2);
            System.out.println(schedule);
            assertEquals(schedule.getCost(), 350);
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