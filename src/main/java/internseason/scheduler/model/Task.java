package internseason.scheduler.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task {


    private List<Dependency> incomingDep;
    private List<Dependency> outgoingDep;
    private int cost;
    private String id;

    public Task(int cost, String id) {
        this.cost = cost;
        this.id = id;
        incomingDep = new ArrayList<>();
        outgoingDep = new ArrayList<>();
    }

    public int getCost() {
            return cost;
    }

    public String getId(){
        return id;
    }

    @Override
    public String toString() {
        return "Task " + this.id + ", Cost: " + this.cost;
    }

}

