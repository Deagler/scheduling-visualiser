package internseason.scheduler.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TaskTest {

    private Task t0;
    private Task t1;
    private Task t2;
    private Task t3;

    @Before
    public void setUp() {
        Task t0 = new Task(5, "0");
        Task t1 = new Task( 4, "1");
        Task t2 = new Task(3, "2");
        Task t3 = new Task(2, "3");
    }

    @Test
    public void testCostToChild() {
        t0.addChildTask(t1, 2);
        assertEquals(1, t0.getNumberOfChildren());
        t0.addChildTask(t2, 3);
        assertEquals(2, t0.getNumberOfChildren());
        t0.addChildTask(t3, 4);
        assertEquals(3, t0.getNumberOfChildren());

        assertEquals(2, t0.getCostToChild(t1));
        assertEquals(3, t0.getCostToChild(t2));
        assertEquals(4, t0.getCostToChild(t3));

        t0.addChildTask(t3, 5);
        assertEquals(5, t0.getCostToChild(t3));
    }

    @Test
    public void testGetParentTasks() {
        t0.addParentTask(t1);
        assertEquals(1, t0.getNumberOfParents());
        t0.addParentTask(t2);
        assertEquals(2, t0.getNumberOfParents());
        t0.addParentTask(t3);
        assertEquals(3, t0.getNumberOfParents());

        List<String> expected = new ArrayList<String>();
        expected.add(t1.getId());
        expected.add(t2.getId());
        expected.add(t3.getId());

        assertEquals(expected, t0.getParentTasks());
    }

    @Test
    public void testGetBottomLevel() {
        assertEquals(0, t0.getBottomLevel());

        t0.setBottomLevel(3);
        assertEquals(3, t0.getBottomLevel());

        t0.setBottomLevel(2);
        assertEquals(3, t0.getBottomLevel());

        t0.setBottomLevel(5);
        assertEquals(5, t0.getBottomLevel());
    }

    @Test
    public void testGetChildrenList() {
        t0.addChildTask(t1, 2);
        t0.addChildTask(t2, 3);
        t0.addChildTask(t3, 4);

        List<String> expected = new ArrayList<>();
        expected.add(t1.getId());
        expected.add(t2.getId());
        expected.add(t3.getId());

        assertEquals(true, expected.containsAll(t0.getChildrenList()) && t0.getChildrenList().containsAll(expected));
    }

    @Test
    public void testToString() {
        assertEquals("Task 0, Cost: 5", t0.toString());
        assertEquals("Task 1, Cost: 4", t1.toString());
        assertEquals("Task 2, Cost: 3", t2.toString());
        assertEquals("Task 3, Cost: 2", t3.toString());
    }

    @Test
    public void testHashCodeEquivalence() {
        Task t4 = new Task(5, "0");

        assertEquals(t4.hashCode(), t0.hashCode());
    }

    @Test
    public void testHashCodeInequivalence() {
        assertNotEquals(t0.hashCode(), t1.hashCode());
        assertNotEquals(t0.hashCode(), t2.hashCode());
        assertNotEquals(t0.hashCode(), t3.hashCode());
    }



}
