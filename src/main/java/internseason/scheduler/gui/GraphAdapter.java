package internseason.scheduler.gui;

import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Task;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.List;
import java.util.Map;

public class GraphAdapter {

    private Graph backEndGraph;
    private SingleGraph frontEndGraph;

    public GraphAdapter(Graph backEndGraph, SingleGraph singleGraph, String frontEndID){
        this.backEndGraph = backEndGraph;
        this.frontEndGraph = convert(backEndGraph, singleGraph,frontEndID);
        //frontEndGraph.display();
    }

    public Graph getBackEndGraph() {
        return backEndGraph;
    }

    public SingleGraph getFrontEndGraph() {
        return frontEndGraph;
    }

    private SingleGraph convert(Graph backEndGraph, SingleGraph graph, String graphID){

//        graph.addNode("0" );
//        graph.addNode("1" );
//        graph.addNode("2" );
//        graph.addNode("3" );
//        graph.addNode("4" );
//        graph.addNode("5" );
//        graph.addNode("6" );
//        graph.addEdge("01", "0", "1");
//        graph.addEdge("02", "0", "2");
//        graph.addEdge("03", "0", "3");
//        graph.addEdge("14", "1", "4");
//        graph.addEdge("15", "1", "5");
//        graph.addEdge("16", "1", "6");

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
                System.out.println(src+dest);
                graph.addEdge(src+dest,  src,dest,true);
            }
        }

        return graph;
    }
}
