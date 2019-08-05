package internseason.scheduler.model;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    List<Task> tasks;

    public Graph() {
        tasks = new ArrayList<Task>();
    }

    public Graph(List<Task> tasks) {
        this.tasks = new ArrayList<Task>(tasks);
    }

    public int size() {
        return tasks.size();
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public void setTasks(List<Task> list) {
        tasks = list;
    }
}
