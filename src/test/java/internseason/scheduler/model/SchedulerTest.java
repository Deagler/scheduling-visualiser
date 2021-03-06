package internseason.scheduler.model;

import internseason.scheduler.input.DOTParser;
import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SchedulerTest {

    private Graph graph;
    private DOTParser parser;
    private Task t0;
    private Task t1;
    private Task t2;
    private Task t3;
    private Scheduler scheduler;

    @Before
    public void setup() {
        this.parser = new DOTParser();

        try {
            graph = this.parser.parse("src/test/resources/Test_Diamond.dot");
            //Map<String, Task> map = graph.getTaskIds();
            t0 = graph.getTask("0");
            t1 = graph.getTask("1");
            t2 = graph.getTask("2");
            t3 = graph.getTask("3");
            scheduler = new Scheduler(graph);

        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOneDependencySameProcessor() {
        Schedule schedule = new Schedule(1);
        try {
            scheduler.addTask(schedule, t0, 0);
            scheduler.addTask(schedule, t1, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("Processor 0\n" +
                "t0 scheduled at: 0\n" +
                "t1 scheduled at: 2\n" +
                "Cost of Processor 0: 4\n" +
                "Total schedule cost is: 4", schedule.toString());

    }

    @Test
    public void testOneDependencyDifferentProcessor() {
        Schedule schedule = new Schedule(2);

        try {

            scheduler.addTask(schedule, t0, 0);
            scheduler.addTask(schedule, t1, 1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("Processor 0\n" +
                "t0 scheduled at: 0\n" +
                "Cost of Processor 0: 2\n" +
                "Processor 1\n" +
                "t1 scheduled at: 4\n" +
                "Cost of Processor 1: 6\n" +
                "Total schedule cost is: 6", schedule.toString());

    }

    @Test
    public void testTwoDependencyProcessorZero() {
        Schedule schedule = new Schedule(2);
        try {
            scheduler.addTask(schedule, t0, 0);
            scheduler.addTask(schedule, t1, 0);
            scheduler.addTask(schedule, t2, 1);
            scheduler.addTask(schedule, t3, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("Processor 0\n" +
                "t0 scheduled at: 0\n" +
                "t1 scheduled at: 2\n" +
                "t3 scheduled at: 7\n" +
                "Cost of Processor 0: 9\n" +
                "Processor 1\n" +
                "t2 scheduled at: 3\n" +
                "Cost of Processor 1: 6\n" +
                "Total schedule cost is: 9", schedule.toString());

    }

    @Test
    public void testTwoDependencyProcessorOne() {
        Schedule schedule = new Schedule(2);
        try {

            scheduler.addTask(schedule, t0, 0);
            scheduler.addTask(schedule, t1, 0);
            scheduler.addTask(schedule, t2, 1);
            scheduler.addTask(schedule, t3, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("Processor 0\n" +
                "t0 scheduled at: 0\n" +
                "t1 scheduled at: 2\n" +
                "Cost of Processor 0: 4\n" +
                "Processor 1\n" +
                "t2 scheduled at: 3\n" +
                "t3 scheduled at: 6\n" +
                "Cost of Processor 1: 8\n" +
                "Total schedule cost is: 8", schedule.toString());

    }

    @Test
    public void testTwoDependencyProcessorTwo() {
        Schedule schedule = new Schedule(3);
        try {

            scheduler.addTask(schedule, t0, 0);
            scheduler.addTask(schedule, t1, 0);
            scheduler.addTask(schedule, t2, 1);
            scheduler.addTask(schedule, t3, 2);

        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("Processor 0\n" +
                "t0 scheduled at: 0\n" +
                "t1 scheduled at: 2\n" +
                "Cost of Processor 0: 4\n" +
                "Processor 1\n" +
                "t2 scheduled at: 3\n" +
                "Cost of Processor 1: 6\n" +
                "Processor 2\n" +
                "t3 scheduled at: 7\n" +
                "Cost of Processor 2: 9\n" +
                "Total schedule cost is: 9", schedule.toString());

    }

    @Test
    public void testHashCodeEquivalence() {
        Schedule one = new Schedule(2);

        scheduler.addTask(one, new Task(10, "1"), 1);
        scheduler.addTask(one, new Task(10, "2"), 1);

        Schedule two = new Schedule(2);

        scheduler.addTask(two, new Task(10, "1"), 1);
        scheduler.addTask(two, new Task(10, "2"), 1);

        assertEquals(one.hashCode(), two.hashCode());
    }

    @Test
    public void testHashCodeInequivalence() {
        Schedule one = new Schedule(2);
        scheduler.addTask(one, new Task(10, "1"), 1);
        scheduler.addTask(one, new Task(10, "2"), 1);

        Schedule two = new Schedule(2);
        scheduler.addTask(two, new Task(10, "1"), 1);
        scheduler.addTask(two, new Task(10, "3"), 1);

        assertNotEquals(one.hashCode(), two.hashCode());
    }


    @Test
    public void testProcessNormalisationHashCode() {
        Schedule one = new Schedule(2);
        scheduler.addTask(one, new Task(10, "1"), 1);
        scheduler.addTask(one, new Task(10, "2"), 1);

        Schedule two = new Schedule(2);
        scheduler.addTask(two, new Task(10, "1"), 0);
        scheduler.addTask(two, new Task(10, "2"), 0);

        assertEquals(one.hashCode(), two.hashCode());
    }



}
