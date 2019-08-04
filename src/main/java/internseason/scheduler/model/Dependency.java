package internseason.scheduler.model;

public class Dependency {

    private Task sourceTask;

    private Task targetTask;

    private int dependencyCost;

    public Dependency(Task sourceTask, Task targetTask, int edgeCost){
        this.sourceTask = sourceTask;
        this.targetTask = targetTask;
        this.dependencyCost = edgeCost;
    }

    public int getEdgeCost(){
        return dependencyCost;
    }

    public Task getSourceTask() {
        return sourceTask;
    }

    public Task getTargetTask(){
        return targetTask;
    }
}
