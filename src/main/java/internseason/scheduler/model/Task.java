package internseason.scheduler.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task {


        private List<Dependency> incomingEdge;
        private List<Dependency> outgoingEdge;
        private int cost;
        private String id;

        public Task(int cost, String id) {
            this.cost = cost;
            this.id = id;
            incomingEdge = new ArrayList<>();
            outgoingEdge = new ArrayList<>();
        }

        public void addIncoming(Dependency edge) {
            this.incomingEdge.add(edge);
        }

        public void addOutgoing(Dependency edge) {
            this.outgoingEdge.add(edge);
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

