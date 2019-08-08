package dotparser;

import internseason.scheduler.DOTParser;
import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.model.Graph;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DotParserTest {
    DOTParser parser;

    @Before
    public void init() {
        this.parser = new DOTParser();
    }

    @Test
    public void testRegularParse() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Nodes_8_Random.dot");

            assertEquals(8, graph.size());

            for (int i = 0; i < graph.size(); i++) {
                assertEquals(String.valueOf(i), graph.getTask(String.valueOf(i)).getId());
            }



        } catch (InputException e) {
            e.printStackTrace();
            fail();
        }
    }

}