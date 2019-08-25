package internseason.scheduler.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProcessorTest {

    Processor p0;
    Processor p1;
    Task t0;
    Task t1;
    Task t2;

    @Before
    public void setup() {
        p0 = new Processor(0);
        p1 = new Processor(1);
        t0 = new Task(2,"0");
        t1 = new Task(3, "1");
        t2 = new Task(5, "2");

        p0.addTaskAt(t0, 0);
        p0.addTaskAt(t1, 2);
    }

    @Test
    public void testCost() {
        assertEquals(5, p0.getCost());
        p0.addTaskAt(t2, 5);
        assertEquals(10, p0.getCost());
    }

    @Test
    public void testGetId() {
        assertEquals(0, p0.getId());
        assertEquals(1, p1.getId());
    }

    @Test
    public void testGetTaskStartTime() {
        assertEquals(0, p0.getTaskStartTime("0"));
        assertEquals(2, p0.getTaskStartTime("1"));

        p1.addTaskAt(t2, 0);
        p0.addTaskAt(t2, 5);

        assertEquals(5, p0.getTaskStartTime("2"));
        assertEquals(0, p1.getTaskStartTime("2"));

    }

    @Test
    public void testGetTaskIds() {
        ArrayList<String> expected = new ArrayList<String>();
        expected.add("0");
        expected.add("1");

        assertTrue(expected.containsAll(p0.getTaskIds()) && p0.getTaskIds().containsAll(expected));

        p0.addTaskAt(t2, 5);
        expected.add("2");

        assertTrue(expected.containsAll(p0.getTaskIds()) && p0.getTaskIds().containsAll(expected));
    }


}
