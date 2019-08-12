package internseason.scheduler.model;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Processor implements Serializable {
    private int cost;
    private int processorId;

    private ArrayList<Pair<Task, Integer>> tasks; // task along with when its scheduled
    private HashMap<String, Integer> taskMap; // map from task to time scheduled

    public Processor(int processorId) {
        this.cost = 0;
        this.processorId = processorId;
        this.taskMap = new HashMap<>();
        this.tasks = new ArrayList<>();
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
        taskMap.put(task.getId(), this.cost);
        tasks.add(new Pair(task, this.cost));
        this.cost += task.getCost();
    }

    public int getTaskStartTime(Task task) {
        return this.taskMap.get(task.getId());
    }

    public List<Task> getTasks() {
        ArrayList<Task> result = new ArrayList<>();
        for (Pair<Task, Integer> pair: tasks) {
            result.add(pair.getKey());
        }

        return result;

    }

    //ToDo: make exception class
    public void addTaskAt(Task task,int time) {
        if (time < this.cost){
            throw new IllegalArgumentException("Illegal time");
        }

        this.taskMap.put(task.getId(), time);
        this.tasks.add(new Pair(task, time));

        this.cost = time + task.getCost();

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Pair<Task, Integer> t: this.tasks) {
            sb.append("t" + t.getKey().getId() + " scheduled at: " + t.getValue() + "\n");
        }
        return sb.toString();
    }

}
