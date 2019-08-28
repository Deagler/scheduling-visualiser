package internseason.scheduler.model;

import javafx.util.Pair;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/** Abstraction represents a processor on the optimal schedule,
 *  stores information about what tasks it stores and what time they are scheduled
 */
public class Processor implements Serializable {
    private int cost;
    private int processorId;


    private HashMap<String, Integer> taskIdScheduleMap; // map from task to time scheduled

    public Processor(int processorId) {
        this.cost = 0;
        this.processorId = processorId;
        this.taskIdScheduleMap = new HashMap<>();

    }

    public Map<String, Integer> getTaskScheduleMap() {
        return taskIdScheduleMap;
    }

    public int getId() {
        return processorId;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void addTask(Task task) {
        taskIdScheduleMap.put(task.getId(), this.cost);
        this.cost += task.getCost();
    }

    public int getTaskStartTime(String taskId) {
        return this.taskIdScheduleMap.get(taskId);
    }

    /**
     * Returns a list of ids for all the tasks scheduled on this processor
     * @return list of task ids
     */
    public List<String> getTaskIds() {
        ArrayList<String> result = new ArrayList<>();

        result.addAll(taskIdScheduleMap.keySet());


        return result;

    }

   

    /**
     * Schedules a task at some determined time 
     * @param task
     * @param time
     */
    public void addTaskAt(Task task,int time) {
        if (this.taskIdScheduleMap.containsKey(task.getId())) {
            throw new IllegalArgumentException("Processor has already scheduled this task");
        }

        if (time < this.cost){
            throw new IllegalArgumentException("Illegal time to add Task");
        }

        this.taskIdScheduleMap.put(task.getId(), time);
        this.cost = time + task.getCost();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Integer> entry : this.taskIdScheduleMap.entrySet()) {
            sb.append("t" + entry.getKey() + " scheduled at: " + entry.getValue() + "\n");
        }
        return sb.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Processor processor = (Processor) o;
        return this.hashCode() == o.hashCode();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(taskIdScheduleMap.hashCode());
        return builder.hashCode();
    }
}
