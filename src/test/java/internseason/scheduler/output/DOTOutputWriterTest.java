package internseason.scheduler.output;

import com.paypal.digraph.parser.GraphEdge;
import com.paypal.digraph.parser.GraphNode;
import com.paypal.digraph.parser.GraphParser;
import internseason.scheduler.input.DOTParser;
import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Schedule;
import internseason.scheduler.model.Scheduler;
import internseason.scheduler.model.Task;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DOTOutputWriterTest {
    DOTOutputWriter writer;
    DOTParser parser;

    @Before
    public void init() {
        this.writer = new DOTOutputWriter();
        this.parser = new DOTParser();
    }

    @Test
    public void testBasicWrite() {
        try {
            Graph graph = this.parser.parse("src/test/resources/Test_Diamond.dot");
            Map<String, Task> tasks = graph.getTasks();
            Scheduler scheduler = new Scheduler(graph);
            Schedule schedule = new Schedule(4);

            scheduler.addTask(schedule, tasks.get("0"), 0);
            scheduler.addTask(schedule, tasks.get("1"),  1);
            scheduler.addTask(schedule, tasks.get("2"),  2);
            scheduler.addTask(schedule, tasks.get("3"),  3);

            this.writer.write("bruh.txt", schedule, tasks);
            validateOutputParser("bruh.txt", schedule, tasks);
        } catch (InputException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void validateOutputParser(String path, Schedule schedule, Map<String, Task> taskMap) throws FileNotFoundException {
        Map<String, GraphNode> nodes;
        Map<String, GraphEdge> edges;

        GraphParser parser = new GraphParser(new FileInputStream(path));

        nodes = parser.getNodes();
        edges = parser.getEdges();

        for (GraphNode node : nodes.values()){
            Map<String, Object> attributes = node.getAttributes();
            Task task = taskMap.get(node.getId());
            assertEquals(String.valueOf(taskMap.get(node.getId()).getCost()),attributes.get("Weight"));
            assertEquals(String.valueOf(schedule.getProcessorIdForTask(task.getId())), attributes.get("Processor"));
            assertEquals(String.valueOf(schedule.getTaskStartTime(task)), attributes.get("Start_time"));
        }

        for (GraphEdge edge : edges.values()){
            Task parent = taskMap.get(edge.getNode1().getId());
            Task child = taskMap.get(edge.getNode2().getId());
            int expectedCost = parent.getCostToChild(child);
            assertEquals(String.valueOf(expectedCost), edge.getAttribute("Weight"));
        }

    }
}
