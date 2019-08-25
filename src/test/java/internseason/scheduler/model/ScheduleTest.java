package internseason.scheduler.model;

import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.input.DOTParser;
import org.junit.Before;

public class ScheduleTest {

    private Schedule s0;
    private Schedule s1;
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
            t0 = graph.getTask("0");
            t1 = graph.getTask("1");
            t2 = graph.getTask("2");
            t3 = graph.getTask("3");

        } catch (InputException e) {
            e.printStackTrace();
        }
    }


}
