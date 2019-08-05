package internseason.scheduler;

import com.paypal.digraph.parser.GraphEdge;
import com.paypal.digraph.parser.GraphElement;
import com.paypal.digraph.parser.GraphNode;
import com.paypal.digraph.parser.GraphParser;
import internseason.scheduler.model.Dependency;
import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Task;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class DOTParser {
    private Map<String, GraphNode> nodes;
    private Map<String, GraphEdge> edges;

    public Graph parse(String path) {
        try {
            GraphParser parser = new GraphParser(new FileInputStream(path));
            this.nodes= parser.getNodes();
            this.edges = parser.getEdges();

            return this.createGraph();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Graph createGraph() {
        Graph graph = new Graph();

        HashMap<String, Task> tasks = this.createTasks();

        graph.setTasks(this.createTasks());
        graph.setDependencies(this.createDependencies(tasks));

        return graph;
    }

    private HashMap<String, Task> createTasks() {
        HashMap<String, Task> tasks = new HashMap<>();
        for(GraphNode node : this.nodes.values()) {
            Task task = new Task(getCostOfGraphElement(node), node.getId());
            tasks.put(task.getId(), task);
        }

        return tasks;
    }

    private List<Dependency> createDependencies(Map<String, Task> tasks) {
        List<Dependency> dependencies = new ArrayList<>();
        for (GraphEdge edge : this.edges.values()) {
            Dependency dependency = new Dependency(
                    tasks.get(edge.getNode1().getId()),
                    tasks.get(edge.getNode2().getId()),
                    getCostOfGraphElement(edge)
            );
        }
        return dependencies;
    }

    /**
     * 'Weight' is stored as a String object in the GraphElement class. Need to cast to String then convert to integer.
     * @param element
     * @return
     */
    private int getCostOfGraphElement(GraphElement element) {
        return Integer.parseInt(String.valueOf(element.getAttribute("Weight")));
    }
}
