package internseason.scheduler;

import com.paypal.digraph.parser.GraphEdge;
import com.paypal.digraph.parser.GraphNode;
import com.paypal.digraph.parser.GraphParser;
import internseason.scheduler.model.Task;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DOTParser {
    public void parse(String path) {
        try {
            GraphParser parser = new GraphParser(new FileInputStream(path));
            Map<String, GraphNode> nodes= parser.getNodes();
            Map<String, GraphEdge> edges = parser.getEdges();

            this.createTasks(nodes.values());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<Task> createTasks(Collection<GraphNode> nodes) {
        List<Task> tasks = new ArrayList<>();
        for(GraphNode node : nodes) {
            Task task = new Task(Integer.parseInt(String.valueOf(node.getAttribute("Weight"))), node.getId());
            tasks.add(task);
        }

        return tasks;
    }
}
