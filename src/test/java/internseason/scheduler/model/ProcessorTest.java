package internseason.scheduler.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ProcessorTest {

    private Processor p0;
    private Processor p1;
    private Task t0;
    private Task t1;
    private Task t2;

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
        p0.addTaskAt(t2, 6);
        assertEquals(11, p0.getCost());
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
        p0.addTaskAt(t2, 6);

        assertEquals(6, p0.getTaskStartTime("2"));
        assertEquals(0, p1.getTaskStartTime("2"));

    }

    @Test
    public void testGetTaskIds() {
        ArrayList<String> expected = new ArrayList<String>();
        expected.add("0");
        expected.add("1");

        assertEquals(expected, p0.getTaskIds());

        p0.addTaskAt(t2, 5);
        expected.add("2");

        assertEquals(expected, p0.getTaskIds());
    }

    @Test
    public void testAddTaskAtException0() {
        try {
            p0.addTaskAt(t1, 10);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Processor has already scheduled this task", e.getMessage());
        }
    }

    @Test
    public void testAddTaskAtException1() {
        try {
            p0.addTaskAt(t2, 4);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Illegal time to add Task", e.getMessage());
        }
    }

    @Test
    public void testToString() {

        assertEquals("t0 scheduled at: 0\n" +
                "t1 scheduled at: 2\n", p0.toString());

        p0.addTaskAt(t2, 8);
        assertEquals("t0 scheduled at: 0\n" +
                "t1 scheduled at: 2\n" +
                "t2 scheduled at: 8\n", p0.toString());

    }

    @Test
    public void testHashCodeEquivalence() {
        Processor p2 = new Processor(0);

        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p0.hashCode(), p2.hashCode());

        p2.addTaskAt(t0, 0);
        p2.addTaskAt(t1, 2);
        assertEquals(p0.hashCode(), p2.hashCode());
    }

    @Test
    public void testHashCodeInequivalence() {
        Processor p2 = new Processor(2);

        assertNotEquals(p0.hashCode(), p1.hashCode());
        assertNotEquals(p0.hashCode(), p2.hashCode());
    }

}
