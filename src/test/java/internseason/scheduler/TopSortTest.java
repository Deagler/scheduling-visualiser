package internseason.scheduler;

import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.model.Graph;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TopSortTest {
    DOTParser dotparser;

    @Before
    public void init() {
        this.dotparser = new DOTParser();
    }

    @Test
    public void testTopTest() {
        try {
            Graph graph = this.dotparser.parse("src/test/resources/Nodes_8_Random.dot");
            List<String> topOrder =  graph.getTopologicalOrdering();
            for (int i=0; i<8;i++){
                String currentNode = topOrder.get(i);
                assertEquals(String.valueOf(i),currentNode);
            }
        } catch (InputException e) {
            e.printStackTrace();
            fail();
        }
    }

}
