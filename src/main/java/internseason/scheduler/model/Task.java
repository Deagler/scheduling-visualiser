package internseason.scheduler.model;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private List<Dependency> incomingEdges;
    private List<Dependency> outgoingEdges;
    private int cost;
    private String id;

    public Task(int cost, String id) {
        this.cost = cost;
        this.id = id;
        incomingEdges = new ArrayList<>();
        outgoingEdges = new ArrayList<>();
    }

    public int getCost() {
        return cost;
    }

    public String getId(){
        return id;
    }

    public int getNumDependencies() {
        return this.incomingEdges.size();
    }

    public int getNumDependants() {
        return this.outgoingEdges.size();
    }

    public void addIncoming(Dependency edge) {
            this.incomingEdges.add(edge);
        }

    public void addOutgoing(Dependency edge) {
        this.outgoingEdges.add(edge);
    }

        public List<Dependency> getOutgoingEdges(){
            return outgoingEdges;
        }

        public List<Dependency> getIncomingEdges(){
            return incomingEdges;
        }

        @Override
        public String toString() {
            return "Task " + this.id + ", Cost: " + this.cost;
        }
}

