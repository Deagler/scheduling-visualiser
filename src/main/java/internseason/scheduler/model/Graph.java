package internseason.scheduler.model;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {
    Map<String, Task> tasks;
    List<Dependency> dependencies;
    Map<String, List<String>> adjacencyList;
    List<List<Task>> topologicalOrdering;


    public Graph() {
        tasks = new HashMap<>();
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

    public List<List<Task>> getTopologicalOrdering(){
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

        Map<String, Integer> inDegrees = buildInDegrees(adjacencyList);
        inDegrees = getInDegrees(adjacencyList, inDegrees);

        Set<Integer> visitedSet = new HashSet<>();
        topologicalOrdering = new ArrayList<>();

        // Initialize a queue with all in-degree zero vertices
        LinkedList<Integer> zeroDegrees = new LinkedList<>();
//        for (int i=0; i<inDegrees.length;i++) {
//            if (inDegrees[i] == 0) {
//                zeroDegrees.add(i);
//            }
//        }
        for (String task : inDegrees.keySet()){
            if (inDegrees.get(task) == 0){
                zeroDegrees.add(Integer.parseInt(task));
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
            inDegrees = getInDegrees(adjacencyList, inDegrees);
            for (String task : inDegrees.keySet()){
                if (inDegrees.get(task) == 0 && !visitedSet.contains(Integer.parseInt(task)) && !zeroDegrees.contains(Integer.parseInt(task))) {
                    zeroDegrees.add(Integer.parseInt(task));
                }

            }
//            for (int i=0;i<inDegrees.length;i++){
//                if (inDegrees[i] == 0 && !visitedSet.contains(i) && !zeroDegrees.contains(i)){
//                    zeroDegrees.add(i);
//                }
//            }
        }
        // convert to task objects
        for (List<String> layer : layers) {
            topologicalOrdering.add(buildTaskListFromIds(layer));
        }
    }

    private Map<String,Integer> buildInDegrees(Map<String, List<String>> adj){
        HashMap<String, Integer> inDegrees = new HashMap<>();
        for (String task : adj.keySet()){
            inDegrees.put(task,0);
        }
        for (List<String> tasks : adj.values()){
            for (String task : tasks){
                if (!inDegrees.containsKey(task)){
                    inDegrees.put(task,0);
                }
            }
        }
        return inDegrees;
    }

    private Map<String,Integer> getInDegrees(Map<String,List<String>> adj, Map<String, Integer> inDegrees) {
        //int[] inDegrees = new int[tasks.size()];
        for (String task : inDegrees.keySet()){
            inDegrees.put(task,0);
        }
        for (List<String> value : adj.values()){
            for (String s : value){
                if (inDegrees.containsKey(s)){
                    int count = inDegrees.get(s);
                    count++;
                    inDegrees.put(s, count);
                } else {
                    inDegrees.put(s,1);
                }
            }
        }

//        int[] inDegrees = new int[tasks.size()];
//        for (List<String> value : adj.values()){
//            for (String s : value){
//                inDegrees[Integer.parseInt(s)]++;
//            }
//        }
//        return inDegrees;
        return inDegrees;
    }

    public List<Task> buildTaskListFromIds(List<String> taskIds) {
        List<Task> result = taskIds.stream()
                .map(t -> tasks.get(t))
                .collect(Collectors.toList());

        return result;
    }

    public void buildBottomLevels(){
        List<Task> leafs = this.getTasks().values() //find all the leaf nodes
                .stream()
                .filter((Task task) -> task.getNumberOfChildren() == 0)
                .collect(Collectors.toList());


        for (Task leaf : leafs) { //Compute the bottom levels for the nodes
            leaf.setBottomLevel(leaf.getCost());
            getBottomLevels(this.buildTaskListFromIds(leaf.getParentTasks()), leaf.getCost());
        }

    }


    private void getBottomLevels(List<Task> tasks, int currentBottomLevel) {
        for (Task node : tasks) {
            if (node.getCost() < currentBottomLevel + node.getCost()) {
                node.setBottomLevel(currentBottomLevel + node.getCost());
            }
            if (!node.getParentTasks().isEmpty()) {
                getBottomLevels(this.buildTaskListFromIds(node.getParentTasks()),
                        node.getBottomLevel());
            }
        }
    }
}
