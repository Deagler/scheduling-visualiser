package internseason.scheduler.test.scheduler;

import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Scheduler;
import internseason.scheduler.model.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;


public class TestScheduler {
    private Graph graph;
    private Scheduler scheduler;
    private Task task1;
    private Task task2;
    private Task task3;
    private Task task4;

    @Before
    public void setup() {
        graph = new Graph();
        scheduler = new Scheduler();
        task1 = new Task(1, "test1");
        task2 = new Task(2, "test2");
        task3 = new Task(3, "test3");
        task4 = new Task(4, "test4");
        graph.setTasks(new ArrayList<>(Arrays.asList(task1, task2, task3, task4)));
    }

    @Test
    public void testNoDependenciesOneProcessor() {
        scheduler.createSchedules(graph, 1);
        assertEquals(scheduler.findBestSchedule().getCost(), 10);
    }


}
