package internseason.scheduler.model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Processor {
    private int time;
    private int processorId;
    private List<Pair<Task, Integer>> taskList;
    //private Map<Task, Integer> taskMap;

    public Processor(int processorId) {
        this.time = 0;
        this.processorId = processorId;
        this.taskList = new ArrayList<>();
        //this.taskMap = new HashMap<Task, Integer>();
    }

    public int getId() {
        return processorId;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void addTask(Task task) {
        //taskMap.put(task, 0);
        taskList.add(new Pair(task, 0));
        this.time += task.getCost();
    }

    public void addTaskWithDelay(Task task, int delay) {
        //taskMap.put(task, delay);
        taskList.add(new Pair(task, delay));
        this.time += task.getCost();
        this.time += delay;
    }

    public Task getLastTask() {
        return taskList.get(taskList.size()-1).getKey();
    }

    public void removeLastTask() {
        Task task = getLastTask();
        this.time -= taskList.get(taskList.size() -1).getValue();
        this.time -= task.getCost();
        taskList.remove(taskList.size()-1);
        //this.time -= task.getDelay();
    }

    public List<Task> getTasks() {
        ArrayList<Task> result = new ArrayList<>();

        for (Pair<Task, Integer> pair: taskList) {
            result.add(pair.getKey());
        }

        return result;

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("p" + processorId + ": " + time);
        return sb.toString();
    }


}
