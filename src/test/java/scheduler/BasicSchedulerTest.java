package scheduler;

import internseason.scheduler.DOTParser;
import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.model.*;
import internseason.scheduler.model.schedulers.BasicScheduler;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BasicSchedulerTest {
    private Graph graph;
    private BasicScheduler scheduler;
    private DOTParser parser;
    @Before
    public void setup() {
        scheduler = new BasicScheduler();
        this.parser = new DOTParser();
    }

    @Test
    public void testBasicSchedule() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_8_Random.dot");
            Schedule schedule = this.scheduler.produceSchedule(graph, 1);
            assertEquals("Processor 0\n" +
                    "t0 scheduled at: 0\n" +
                    "t1 scheduled at: 35\n" +
                    "t2 scheduled at: 123\n" +
                    "t3 scheduled at: 299\n" +
                    "t4 scheduled at: 458\n" +
                    "t5 scheduled at: 634\n" +
                    "t6 scheduled at: 775\n" +
                    "t7 scheduled at: 916\n" +
                    "Cost of Processor 0: 969\n" +
                    "Total schedule cost is: 969", schedule.toString());
        } catch (InputException e) {
            e.printStackTrace();
        }
    }
}
