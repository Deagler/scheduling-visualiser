package internseason.scheduler.model;

import internseason.scheduler.input.DOTParser;
import internseason.scheduler.exceptions.InputException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            Set<String> visited = new HashSet<>();

            // check that each task's outgoing edge isnt already visited, if it is then top ordering is violated

            for (List<Task> topologicalLayer : topOrder) {
                for (Task task : topologicalLayer){
                    visited.add(task.getId());

                    List<String> outgoingEdges = task.getChildrenList();
                    for (String childNodeId : outgoingEdges){
                        boolean childNodeSeen = visited.contains(childNodeId);
                        assertFalse(childNodeSeen);
                    }
                }
            }



        } catch (InputException e) {
            e.printStackTrace();
            fail();
        }
    }

}
