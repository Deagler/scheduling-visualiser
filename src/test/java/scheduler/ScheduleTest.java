package scheduler;

import internseason.scheduler.DOTParser;
import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ScheduleTest {

    private Graph graph;
    private DOTParser parser;
    private Task t0;
    private Task t1;
    private Task t2;
    private Task t3;

    @Before
    public void setup() {
        this.parser = new DOTParser();

        try {
            graph = this.parser.parse("src/test/resources/Test_Diamond.dot");
            //Map<String, Task> map = graph.getTasks();
            t0 = graph.getTask("0");
            t1 = graph.getTask("1");
            t2 = graph.getTask("2");
            t3 = graph.getTask("3");

        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOneDependencySameProcessor() {
        Schedule schedule = new Schedule(1);
        try {
            schedule.add(t0, 0);
            schedule.add(t1, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("Processor 0\n" +
                "t0 scheduled at: 0\n" +
                "t1 scheduled at: 2\n" +
                "Cost of Processor 0: 4\n" +
                "Total schedule cost is: 4", schedule.toString());
        //System.out.println(schedule.toString());
    }

    @Test
    public void testOneDependencyDifferentProcessor() {
        Schedule schedule = new Schedule(2);

        try {

            schedule.add(t0, 0);
            schedule.add(t1, 1);

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
        //System.out.println(schedule.toString());
    }

    @Test
    public void testTwoDependencyProcessorZero() {
        Schedule schedule = new Schedule(2);
        try {
            schedule.add(t0, 0);
            schedule.add(t1, 0);
            schedule.add(t2, 1);
            schedule.add(t3, 0);
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
        //System.out.println(schedule.toString());
    }

    @Test
    public void testTwoDependencyProcessorOne() {
        Schedule schedule = new Schedule(2);
        try {
            schedule.add(t0, 0);
            schedule.add(t1, 0);
            schedule.add(t2, 1);
            schedule.add(t3, 1);
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
        //System.out.println(schedule.toString());
    }

    @Test
    public void testTwoDependencyProcessorTwo() {
        Schedule schedule = new Schedule(3);
        try {
            schedule.add(t0, 0);
            schedule.add(t1, 0);
            schedule.add(t2, 1);
            schedule.add(t3, 2);
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
        //System.out.println(schedule.toString());
    }

}
