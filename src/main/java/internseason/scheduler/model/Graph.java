package internseason.scheduler.model;

import java.util.*;

public class Graph {
    Map<String, Task> tasks;
    List<Dependency> dependencies;
    List<List<String>> topologicalOrdering;

    public Graph() {
        tasks = new HashMap<String, Task>();
    }

    public void setTasks(Map<String,Task> tasks) {
        this.tasks = tasks;
    }

    public void setDependencies(List<Dependency> dependencies) {
        this.dependencies = dependencies;
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
        //inDegrees = getInDegrees();





    }

    private void getInDegrees() {

    }
}
