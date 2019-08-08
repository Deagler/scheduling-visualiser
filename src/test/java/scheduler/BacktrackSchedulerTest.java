package scheduler;

import internseason.scheduler.model.BacktrackScheduler;
import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Task;
import org.junit.Before;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;


public class BacktrackSchedulerTest {
    private Graph graph;
    private BacktrackScheduler scheduler;
    private Task task1;
    private Task task2;
    private Task task3;
    private Task task4;

    @Before
    public void setup() {
        graph = new Graph();
        scheduler = new BacktrackScheduler();
        task1 = new Task(1, "test1");
        task2 = new Task(2, "test2");
        task3 = new Task(3, "test3");
        task4 = new Task(4, "test4");
        HashMap<String, Task> taskMap = new HashMap<>();
        taskMap.put("test1", task1);
        taskMap.put("test2", task2);
        taskMap.put("test3", task3);
        taskMap.put("test4", task4);
        //graph.setTasks(new ArrayList<Task>(Arrays.asList(task1, task2, task3, task4)));
    }

//    @Test
//    public void testNoDependenciesOneProcessor() {
//        scheduler.createSchedules(graph, 1);
//        System.out.println(scheduler.findBestSchedule().toString());
//        assertEquals(scheduler.findBestSchedule().getCost(), 10);
//    }

//    @Test
//    public void testNoDependenciesTwoProcessors() {
//        scheduler.createSchedules(graph, 2);
//        //System.out.println(scheduler.findBestSchedule().getCost());
//        System.out.println(scheduler.findBestSchedule().toString());
//        assertEquals(scheduler.findBestSchedule().getCost(), 5);
//    }


}
