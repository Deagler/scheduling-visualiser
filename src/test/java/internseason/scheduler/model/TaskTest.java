package internseason.scheduler.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
        t0.addChildTask( t1, 3);

        assertEquals(3, t0.getCostToChild(t1));
    }

    @Test
    public void testGetParentTasks() {
        t0.addParentTask(t1);
        t0.addParentTask(t2);
        t0.addParentTask(t3);

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





}
