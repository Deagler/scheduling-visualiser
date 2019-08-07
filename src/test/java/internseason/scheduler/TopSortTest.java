package internseason.scheduler;

import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.model.Dependency;
import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TopSortTest {
    DOTParser dotparser;

    @Before
    public void init() {
        this.dotparser = new DOTParser();
    }

    @Test
    public void testSevenTreeGraphOrder(){
        testTopologicalOrder("src/test/resources/Nodes_7_OutTree.dot");
    }

    @Test
    public void testEightRandomGraphOrder(){
        testTopologicalOrder("src/test/resources/Nodes_8_Random.dot");
    }

    @Test
    public void testNineSeriesParallelGraphOrder(){
        testTopologicalOrder("src/test/resources/Nodes_9_SeriesParallel.dot");
    }

    @Test
    public void testTenRandomGraphOrder(){
        testTopologicalOrder("src/test/resources/Nodes_10_Random.dot");
    }

    private void testTopologicalOrder(String path) {
        try {
            Graph graph = this.dotparser.parse(path);
            //get top ordering
            List<String> topOrder =  graph.getTopologicalOrdering();
            List<Task> visited = new ArrayList<>();
            //check that each task's outgoing edge isnt already visited, if it is then top ordering is violated
            for (String s : topOrder){
                Task task = graph.getTask(s);
                visited.add(task);
                List<Dependency> outgoingEdges = task.getOutgoingEdges();
                for (Dependency dependency : outgoingEdges){
                    assertTrue(!visited.contains(dependency.getTargetTask()));
                }
            }
        } catch (InputException e) {
            e.printStackTrace();
            fail();
        }
    }

}
