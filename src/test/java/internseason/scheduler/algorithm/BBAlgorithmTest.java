package internseason.scheduler.algorithm;

import com.sun.javafx.scene.traversal.Algorithm;
import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.input.DOTParser;
import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Schedule;
import internseason.scheduler.model.Scheduler;
import internseason.scheduler.model.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class BBAlgorithmTest {
    private Graph graph;
    private DOTParser parser;
    private SystemInformation sysInfo;

    @Before
    public void setup() {
        this.sysInfo = new SystemInformation();
        this.parser = new DOTParser();
    }

    @Test
    public void testBBSchedule1() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Test_Diamond.dot");
            BaseAlgorithm algorithm = AlgorithmFactory.getAlgorithm(AlgorithmType.BRANCH_AND_BOUND_ALGORITHM, 0);
            Schedule schedule = algorithm.execute(graph, 2, 1, sysInfo);
            assertEquals(8, schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBBSchedule2() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_7_OutTree.dot");
            BaseAlgorithm algorithm = AlgorithmFactory.getAlgorithm(AlgorithmType.BRANCH_AND_BOUND_ALGORITHM, 0);
            Schedule schedule = algorithm.execute(graph, 2, 1, sysInfo);
            assertEquals(28, schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBBSchedule3() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_9_SeriesParallel.dot");
            BaseAlgorithm algorithm = AlgorithmFactory.getAlgorithm(AlgorithmType.BRANCH_AND_BOUND_ALGORITHM, 0);
            Schedule schedule = algorithm.execute(graph, 4, 1, sysInfo);
            assertEquals(55, schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBBSchedule4() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_8_Random.dot");
            BaseAlgorithm algorithm = AlgorithmFactory.getAlgorithm(AlgorithmType.BRANCH_AND_BOUND_ALGORITHM, 0);
            Schedule schedule = algorithm.execute(graph, 4, 1, sysInfo);
            assertEquals(581, schedule.getCost());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGreedyScheduler() throws InputException {
        Graph graph = this.parser.parse("src/test/resources/Nodes_7_OutTree.dot");

        Set<String> free = new HashSet<>();
        for (Task task : graph.getTasks().values()) {
            if (task.getId().equals("0")) {
                free.add(task.getId());

            }
        }

        Scheduler scheduler = new Scheduler(graph);
        Schedule schedule = scheduler.buildGreedySchedule(new BBScheduleInfo(new Schedule(2), Integer.MIN_VALUE, Integer.MAX_VALUE, free), graph);
        System.out.println(schedule);

    }


}
