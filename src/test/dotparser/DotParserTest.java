package dotparser;

import internseason.scheduler.DOTParser;
import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.model.Graph;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

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

            for (String id : graph.getTasks().keySet()) {
                HashMap<String, Integer> dependencyCount = buildNode8DependencyCountMap();
                // explicit type due to weird ambiguous assert method call due to autoboxing
                assertEquals(Integer.valueOf(dependencyCount.get(id)), Integer.valueOf(graph.getTask(id).getNumDependencies()));
            }

        } catch (InputException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testInvalidParse() {
        try {
            Graph graph = this.parser.parse("src/test/sources/invalid.dot");
            fail();
        } catch (InputException e) {
            assertEquals("Invalid input dot files", e.getMessage());
        }
    }

    public HashMap<String, Integer> buildNode8DependencyCountMap() {
        HashMap<String, Integer> map = new HashMap<String, Integer>() {{
            put("0", 0);
            put("1", 1);
            put("2", 1);
            put("3", 1);
            put("4", 3);
            put("5", 2);
            put("6", 3);
            put("7", 5);
        }};

        return map;
    }

}