package internseason.scheduler.model;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task implements Serializable {


    private List<Dependency> incomingEdges;
    private List<Dependency> outgoingEdges;
    private List<Task> parentTasks;
    private Map<Task, Integer> childCosts;
    private int cost;
    private String id;
    private int bottomLevel;

    public Task(int cost, String id) {
        this.cost = cost;
        this.id = id;
        incomingEdges = new ArrayList<>();
        outgoingEdges = new ArrayList<>();
        parentTasks = new ArrayList<>();
        childCosts = new HashMap<>();
    }

    public void addIncoming(Dependency edge) {
        this.incomingEdges.add(edge);
    }

    public void addOutgoing(Dependency edge) {
        this.outgoingEdges.add(edge);
    }

    public void addParentTask(Task task) { this.parentTasks.add(task);}

    public void addChildTask(Task task, int communicationCost) {
        this.childCosts.put(task, communicationCost);
    }

    public int getCostToChild(Task task) {
        return this.childCosts.get(task);
    }

    public List<Task> getParentTasks() {
        return this.parentTasks;
    }

    public int getCost() {
        return cost;
    }

    public String getId(){
        return id;
    }


    public int getBottomLevel() {
        return bottomLevel;
    }

    public void setBottomLevel(int bottomLevel) {
        this.bottomLevel = Math.max(bottomLevel, this.bottomLevel);
    }

    public List<Dependency> getOutgoingEdges(){
        return outgoingEdges;
    }

    public List<Dependency> getIncomingEdges(){
        return incomingEdges;
    }

    public int getDelayTo(Task task) {
        //check if task depends on this

        for (Dependency dependency : outgoingEdges) {
            if (dependency.getTargetTask().equals(task)) {
                return dependency.getDependencyCost();
            }
        }

        //TODO throw exception
        return 0;
    }

    @Override
    public String toString() {
        return "Task " + this.id + ", Cost: " + this.cost;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getId());
        return builder.hashCode();
    }
}
