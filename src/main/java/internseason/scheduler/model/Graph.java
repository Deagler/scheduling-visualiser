package internseason.scheduler.model;

import java.util.*;

public class Graph {
    Map<String, Task> tasks;
    List<Dependency> dependencies;
    Map<String, List<String>> adjacencyList;
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

    public Task getTask(String id) {
        return tasks.get(id);
    }

    public Map<String, Task> getTasks() {
        return this.tasks;
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
        // Store the in degree of array
        adjacencyList = getAdjacencyList();
        int[] inDegrees = getInDegrees(adjacencyList);

        Set<Integer> visitedSet = new HashSet<>();
        topologicalOrdering = new ArrayList<>();

        // Initialize a queue with all in-degree zero vertices
        LinkedList<Integer> zeroDegrees = new LinkedList<>();
        for (int i=0; i<inDegrees.length;i++){
            if (inDegrees[i] == 0){
                zeroDegrees.add(i);
            }
        }
        List<List<String>> layers = new ArrayList<>();
        //while there are vertices remaining in the queue
        while (!zeroDegrees.isEmpty()){


            visitedSet.addAll(zeroDegrees);

            List<String> layer  = new ArrayList<>();
            for (Integer vertex : zeroDegrees) {
                adjacencyList.remove(String.valueOf(vertex));
                layer.add(String.valueOf(vertex));
            }
            layers.add(layer);

            zeroDegrees.clear();


            //enqueue any vertice whose in degree became zero
            inDegrees = getInDegrees(adjacencyList);
            for (int i=0;i<inDegrees.length;i++){
                if (inDegrees[i] == 0 && !visitedSet.contains(i) && !zeroDegrees.contains(i)){
                    zeroDegrees.add(i);
                }
            }
        }

        topologicalOrdering = layers;
    }

    private int[] getInDegrees(Map<String,List<String>> adj) {
        int[] inDegrees = new int[tasks.size()];
        for (List<String> value : adj.values()){
            for (String s : value){
                inDegrees[Integer.parseInt(s)]++;
            }
        }
        return inDegrees;
    }

}
