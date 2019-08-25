package internseason.scheduler.algorithm;

import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.input.DOTParser;
import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Schedule;
import internseason.scheduler.model.Scheduler;
import internseason.scheduler.model.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AstarScheduleInfoTest {
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
    public void testGetSchedule() {

        Schedule schedule = new Schedule(2);
        try {
            scheduler.addTask(schedule, t0, 0);
            scheduler.addTask(schedule, t1, 0);
            scheduler.addTask(schedule, t2, 1);
            scheduler.addTask(schedule, t3, 0);


            List<String> freeList = new ArrayList<String>();
            for (int i=0; i<5; i++) {
                freeList.add(String.valueOf(i));
            }

            AstarScheduleInfo si = new AstarScheduleInfo(schedule, 1, freeList, schedule.getCost());

            assertEquals(si.getSchedule().toString(), schedule.toString());

            //this second assertEquals checks the second predicate outcome of the getSchedule() method
            assertEquals(si.getSchedule().toString(), schedule.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
