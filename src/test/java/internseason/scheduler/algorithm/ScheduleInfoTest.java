package internseason.scheduler.algorithm;

import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.input.DOTParser;
import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Schedule;
import internseason.scheduler.model.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ScheduleInfoTest {
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
            //Map<String, Task> map = graph.getTaskIds();
            t0 = graph.getTask("0");
            t1 = graph.getTask("1");
            t2 = graph.getTask("2");
            t3 = graph.getTask("3");

        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetSchedule() {

        Schedule schedule = new Schedule(2, graph.getTasks());
        try {
            schedule.add(t0, 0);
            schedule.add(t1, 0);
            schedule.add(t2, 1);
            schedule.add(t3, 0);


            List<String> freeList = new ArrayList<String>();
            for (int i=0; i<5; i++) {
                freeList.add(String.valueOf(i));
            }

            ScheduleInfo si = new ScheduleInfo(schedule, 1, freeList, schedule.getCost());

            assertEquals(si.getSchedule().toString(), schedule.toString());

            //this second assertEquals checks the second predicate outcome of the getSchedule() method
            assertEquals(si.getSchedule().toString(), schedule.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
