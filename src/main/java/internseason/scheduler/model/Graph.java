package internseason.scheduler.model;

import java.util.*;

public class Graph {
    Map<String, Task> tasks;
    List<Dependency> dependencies;
    Map<String, List<String>> adjacencyList;
    List<List<String>> topologicalOrdering;

    List<String> topological;

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

    private Map<String, List<String>> getAdjacencyList() {
        if (this.adjacencyList != null) {
            return this.adjacencyList;
        }
        this.adjacencyList = new HashMap<>();

        for(Dependency dependency : this.dependencies) {
            String sourceId = dependency.getSourceTask().getId();
            String targetId = dependency.getTargetTask().getId();

            List<String> list = adjacencyList.containsKey(sourceId) ? adjacencyList.get(sourceId) : new ArrayList<>();
            list.add(targetId);

            adjacencyList.put(sourceId, list);
        }

        return adjacencyList;
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

        // Store the in degree of array
        int[] inDegrees = getInDegrees();

        // Initialize a queue with all in-degree zero vertices
        LinkedList<Integer> zeroDegrees = new LinkedList<>();
        for (int i=0; i<inDegrees.length;i++){
            if (inDegrees[i] == 0){
                zeroDegrees.add(i);
            }
        }
        //while there are vertices remaining in the queue
        while (!zeroDegrees.isEmpty()){
            //dequeue and output a vertex
            topological.add(String.valueOf(zeroDegrees.getFirst()));
            zeroDegrees.remove();
            //




        }
    }

    private int[] getInDegrees() {
        int[] inDegrees = new int[tasks.size()];
        for (List<String> value : adjacencyList.values()){
            for (String s : value){
                inDegrees[Integer.parseInt(s)]++;
            }
        }
        System.out.println(inDegrees);
        return inDegrees;
    }
}
