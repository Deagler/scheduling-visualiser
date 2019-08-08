package scheduler;

import internseason.scheduler.DOTParser;
import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

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
            System.out.println(schedule);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }
}
