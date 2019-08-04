package internseason.scheduler;

import com.paypal.digraph.parser.GraphEdge;
import com.paypal.digraph.parser.GraphNode;
import com.paypal.digraph.parser.GraphParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

public class DOTParser {
    private Map<String, GraphNode> nodes;
    private Map<String, GraphEdge> edges;

    public void parse(String path) {
        try {
            GraphParser parser = new GraphParser(new FileInputStream(path));
            this.nodes = parser.getNodes();
            this.edges = parser.getEdges();

            for(GraphNode node : nodes.values()) {
                System.out.println(node.getId() + " " + node.getAttributes());
            }

            for(GraphEdge edge : edges.values()) {
                System.out.println(edge.getId() + " " + edge.getAttributes());
                System.out.println(edge.getId());
            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
