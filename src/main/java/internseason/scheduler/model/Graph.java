package internseason.scheduler.model;

import java.util.*;

public class Graph {
    private List<Task> tasks;
    private List<List<String>> topologicalOrdering;


    private LinkedList<Task> adj[];

    public Graph() {
        tasks = new ArrayList<Task>();
    }

    public Graph(List<Task> tasks) {
        this.tasks = new ArrayList<Task>(tasks);
    }

    public int size() {
        return tasks.size();
    }

    public Task get(int index) {
        return tasks.get(index);
    }


    public List<List<String>> getTopologicalOrdering(){
        if (topologicalOrdering == null){
            createTopologicalOrdering();
        }
        return topologicalOrdering;
    }

    private void createTopologicalOrdering() {
        //need adjacency list representation of the graph
        /*1. Store each vertex’s In-Degree in an array
2. Initialize a queue with all in-degree zero vertices
3. While there are vertices remaining in the queue:
➭ Dequeue and output a vertex
➭ Reduce In-Degree of all vertices adjacent to it by 1
➭ Enqueue any of these vertices whose In-Degree became
zero*/

        int[] inDegrees = new int[tasks.size()];
        inDegrees = getInDegrees();





    }

    private int[] getInDegrees() {



    }
}
