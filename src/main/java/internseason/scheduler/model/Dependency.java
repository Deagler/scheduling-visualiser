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

    public int getDependencyCost(){
        return dependencyCost;
    }

    public Task getSourceTask() {
        return sourceTask;
    }

    public Task getTargetTask(){
        return targetTask;
    }

    @Override
    public String toString(){
        return "Source:" + sourceTask.toString() + " Target:"+targetTask.toString()+" Weight:"+dependencyCost;
    }
}
