package internseason.gui;

import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Task;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.List;
import java.util.Map;

public class GraphAdapter {

    private Graph backEndGraph;
    private SingleGraph frontEndGraph;

    public GraphAdapter(Graph backEndGraph, String frontEndID){
        this.backEndGraph = backEndGraph;
        this.frontEndGraph = convert(backEndGraph, frontEndID);
    }

    public Graph getBackEndGraph() {
        return backEndGraph;
    }

    public SingleGraph getFrontEndGraph() {
        return frontEndGraph;
    }

    private SingleGraph convert(Graph backEndGraph, String graphID){

        SingleGraph graph = new SingleGraph(graphID);

        Map<String, Task> tasks = backEndGraph.getTasks();
        Map<String, List<String>> adjacencyList = backEndGraph.getAdjacencyList();

        // constructing nodes
        for (Map.Entry<String,Task> node : tasks.entrySet()) {
            String key = node.getKey();
            Task val = node.getValue();
            graph.addNode(key);
        }
        // constructing edges
        for (Map.Entry<String, List<String>> edge : adjacencyList.entrySet()) {
            String src = edge.getKey();
            List<String> destList = edge.getValue();
            for (String dest: destList){
                graph.addEdge(src+dest,  src,dest,true);
            }
        }

        return graph;
    }
}
