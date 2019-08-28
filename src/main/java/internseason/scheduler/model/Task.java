package internseason.scheduler.model;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.*;

/** Task class represents abstraction of a task on the input DAG, which has a cost and edges associated with it
 *  contains getters and setters to change properties of the task object
 */
public class Task implements Serializable {
    private List<String> parentTasks;
    private Map<String, Integer> childCosts;
    private int cost;
    private String id;
    private int bottomLevel;

    public Task(int cost, String id) {
        this.cost = cost;
        this.id = id;
        parentTasks = new ArrayList<>();
        childCosts = new HashMap<>();
    }


    public void addParentTask(Task task) { this.parentTasks.add(task.getId());}

    public void addChildTask(Task task, int communicationCost) {
        this.childCosts.put(task.getId(), communicationCost);
    }

    public int getCostToChild(Task task) {
        return this.childCosts.get(task.getId());
    }

    public List<String> getParentTasks() {
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

    public int getNumberOfParents() {
        return this.parentTasks.size();
    }

    public int getNumberOfChildren() {
        return this.childCosts.size();
    }

    public List<String> getChildrenList() { return new ArrayList<String>(this.childCosts.keySet()); }

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
