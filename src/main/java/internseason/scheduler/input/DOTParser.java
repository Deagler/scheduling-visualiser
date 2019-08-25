package internseason.scheduler.input;

import com.paypal.digraph.parser.GraphEdge;
import com.paypal.digraph.parser.GraphElement;
import com.paypal.digraph.parser.GraphNode;
import com.paypal.digraph.parser.GraphParser;
import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.model.Dependency;
import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Task;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class DOTParser {
    private Map<String, GraphNode> nodes;
    private Map<String, GraphEdge> edges;

    /**
     * Takes in the path for a dot file and returns an object encapsulating the input graph information
     * @param path
     * @return Graph object
     * @throws InputException
     */
    public Graph parse(String path) throws InputException {
        try {
            GraphParser parser = new GraphParser(new FileInputStream(path));
            this.nodes= parser.getNodes();
            this.edges = parser.getEdges();

            return this.createGraph();

        } catch (FileNotFoundException e) {
            throw new InputException("Invalid input dot files");
        }
    }

    private Graph createGraph() {
        Graph graph = new Graph();

        HashMap<String, Task> tasks = this.createTasks();

        graph.setTasks(tasks);
        graph.setDependencies(this.createDependencies(tasks));
        graph.buildBottomLevels();
        return graph;
    }

    /**
     * Adapter method to convert the PayPal digraph parser Node objects to Task objects
     * @return HashMap containing (TaskId: Task) KV pairs
     */
    private HashMap<String, Task> createTasks() {
        HashMap<String, Task> tasks = new HashMap<>();
        for(GraphNode node : this.nodes.values()) {
            Task task = new Task(getCostOfGraphElement(node), node.getId());
            tasks.put(task.getId(), task);
        }

        return tasks;
    }

    /**
     * Adapter method to convert the PayPal diagraph parser Edge objects to Dependency objects
     * @param tasks
     * @return a list of Dependency objects
     */
    private List<Dependency> createDependencies(Map<String, Task> tasks) {
        List<Dependency> dependencies = new ArrayList<>();
        for (GraphEdge edge : this.edges.values()) {
            Task sourceTask = tasks.get(edge.getNode1().getId());
            Task targetTask = tasks.get(edge.getNode2().getId());

            Dependency dependency = new Dependency(
                    sourceTask,
                    targetTask,
                    getCostOfGraphElement(edge)
            );

            sourceTask.addChildTask(targetTask, getCostOfGraphElement(edge));
            targetTask.addParentTask(sourceTask);

            dependencies.add(dependency);
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
