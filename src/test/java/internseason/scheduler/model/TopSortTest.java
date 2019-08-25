package internseason.scheduler.model;

import internseason.scheduler.input.DOTParser;
import internseason.scheduler.exceptions.InputException;
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
    public void testNineSeriesParallelGraphOrder(){ testTopologicalOrder("src/test/resources/Nodes_9_SeriesParallel.dot"); }

    @Test
    public void testTenRandomGraphOrder(){
        testTopologicalOrder("src/test/resources/Nodes_10_Random.dot");
    }

    private void testTopologicalOrder(String path) {
        try {
            Graph graph = this.dotparser.parse(path);
            //get top ordering
            List<List<Task>> topOrder =  graph.getTopologicalOrdering();
            List<Task> visited = new ArrayList<>();
            //check that each task's outgoing edge isnt already visited, if it is then top ordering is violated
            for (List<Task> layer : topOrder){
                for (Task task : layer){
                    List<String> parentIds = task.getChildrenList();
                    for (String parentId : parentIds){
                        assertTrue(!layer.contains(graph.getTask(parentId)));
                    }
                }
            }
        } catch (InputException e) {
            e.printStackTrace();
            fail();
        }
    }

}
