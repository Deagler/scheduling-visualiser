package internseason.scheduler.model;

import java.io.Serializable;


/** Represents a dependency within the input DAG, this class is used to compute the
 * topological ordering within the graph object.
 */
public class Dependency implements Serializable {

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
