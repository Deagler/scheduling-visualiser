package internseason.scheduler.model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Processor {
    private int cost;
    private int processorId;

    private List<Pair<Task, Integer>> taskList; //task - delay on the task
    private Map<Task, Integer> taskMap; //task - time at which task is scheduled

    public Processor(int processorId) {
        this.cost = 0;
        this.processorId = processorId;
        this.taskList = new ArrayList<>();
        //this.taskMap = new HashMap<Task, Integer>();
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
        //taskMap.put(task, 0);
        taskList.add(new Pair(task, 0));
        taskMap.put(task, this.cost);
        this.cost += task.getCost();
    }

    public Task getLastTask() {
        return taskList.get(taskList.size()-1).getKey();
    }

    public void removeLastTask() {
        Task task = getLastTask();
        this.cost -= taskList.get(taskList.size() -1).getValue();
        this.cost -= task.getCost();
        taskMap.remove(task);
        taskList.remove(taskList.size()-1);
    }

    public List<Task> getTasks() {
        ArrayList<Task> result = new ArrayList<>();
        for (Task t: taskMap.keySet()) {
            result.add(t);
        }

        return result;

    }

    //ToDo: make exception class
    public void addTaskAt(Task task,int time) throws Exception {
        if (time < this.cost){
            throw new Exception();
        }

        this.taskList.add(new Pair(task, time-this.cost));
        this.taskMap.put(task, time);
        this.cost = time + task.getCost();

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Task t: taskMap.keySet()) {
            sb.append("p" + t.getId() + " scheduled on: " + taskMap.get(t) + "\n");
        }
        return sb.toString();
    }


}
