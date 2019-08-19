package internseason.scheduler.dotparser;

import internseason.scheduler.DOTParser;
import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
//                HashMap<String, Integer> dependencyCount = buildNode8DependencyCountMap();
                // explicit type due to weird ambiguous assert method call due to autoboxing
//                assertEquals(Integer.valueOf(dependencyCount.get(id)), Integer.valueOf(graph.getTask(id).getNumDependencies()));
            }

        } catch (InputException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetParents() {
        try {
            Graph graph = (this.parser.parse("src/test/resources/Nodes_8_Random.dot"));
            Map<String, Task> tasks = graph.getTasks();
            Map<String, Integer> parentCountMap = this.getNode8ParentCountMap();

            for (Task task : tasks.values()) {
                List<Task> parents = task.getParentTasks();
                assertEquals(Integer.valueOf(parentCountMap.get(task.getId())), Integer.valueOf(parents.size()));
            }

        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> getNode8ParentCountMap() {
        Map<String, Integer> parentCountMap = new HashMap<String, Integer>() {
            {
                put("0", 0);
                put("1", 1);
                put("2", 1);
                put("3", 1);
                put("4", 3);
                put("5", 2);
                put("6", 3);
                put("7", 5);

            }
        };
        return parentCountMap;
    }

}