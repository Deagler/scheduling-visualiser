package internseason.scheduler.model;

public class Edge {

    private Task sourceTask;

    private Task targetTask;

    private int edgeCost;

    public Edge(Task sourceTask, Task targetTask, int edgeCost){
        this.sourceTask = sourceTask;
        this.targetTask = targetTask;
        this.edgeCost = edgeCost;
    }

}
