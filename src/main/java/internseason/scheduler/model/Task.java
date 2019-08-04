package internseason.scheduler.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task {


        private List<Edge> incomingEdge;
        private List<Edge> outgoingEdge;
        private int cost;
        private String id;

        public Task(int cost, String id) {
            this.cost = cost;
            this.id = id;
            incomingEdge = new ArrayList<>();
            outgoingEdge = new ArrayList<>();
        }

        public int getCost() {
            return cost;
        }

        public String getId(){
            return id;
        }
}

