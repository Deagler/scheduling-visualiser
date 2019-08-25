package internseason.scheduler.model;

import javafx.util.Pair;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Processor implements Serializable {
    private int cost;
    private int processorId;

    private ArrayList<Pair<String, Integer>> taskScheduleList; // task along with when its scheduled
    private HashMap<String, Integer> taskIdScheduleMap; // map from task to time scheduled

    public Processor(int processorId) {
        this.cost = 0;
        this.processorId = processorId;
        this.taskIdScheduleMap = new HashMap<>();
        this.taskScheduleList = new ArrayList<>();
    }

    public int getId() {
        return processorId;
    }

    public int getCost() {
        return cost;
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
        for (Pair<String, Integer> pair: taskScheduleList) {
            result.add(pair.getKey());
        }

        return result;

    }

    //ToDo: make exception class

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
        this.taskScheduleList.add(new Pair(task.getId(), time));

        this.cost = time + task.getCost();

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Pair<String, Integer> t: this.taskScheduleList) {
            sb.append("t" + t.getKey() + " scheduled at: " + t.getValue() + "\n");
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(taskIdScheduleMap.hashCode());
        return builder.hashCode();
    }
}
