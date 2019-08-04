package internseason.scheduler.model;

import java.util.ArrayList;
import java.util.List;

public class Processor {
    private int time;
    private int processorId;
    private List<Task> taskList;

    public Processor(int processorId) {
        this.time = 0;
        this.processorId = processorId;
        this.taskList = new ArrayList<Task>();
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void addTask(Task task) {
        taskList.add(task);
        this.time += task.getCost();
    }

    public Task getLastTask() {
        return taskList.get(taskList.size()-1);
    }

    public void removeLastTask() {
        Task task = getLastTask();
        taskList.remove(taskList.size()-1);
        this.time -= task.getCost();
    }


}
