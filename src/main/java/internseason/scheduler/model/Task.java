package internseason.scheduler.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task {

        private Map<Task, Integer> children;
        private int cost;
        //private int delay;
        private List<Dependency> incomingEdge;
        private List<Dependency> outgoingEdge;
        private String id;

        public Task(int cost, String id) {
            this.cost = cost;
            this.id = id;
            children = new HashMap<>();
            //this.delay = 0;
            incomingEdge = new ArrayList<>();
            outgoingEdge = new ArrayList<>();
        }

//        public Task(int cost, int delay, String id) {
//            this.cost = cost;
//            this.id = id;
//            children = new HashMap<>();
//            //this.delay = delay;
//            incomingEdge = new ArrayList<>();
//            outgoingEdge = new ArrayList<>();
//        }

        public int getCost() {
            return cost;
        }

//        public boolean hasDelay() {
//            if (delay > 0) {
//                return true;
//            }
//            return false;
//        }

//        public int getDelay() {
//            return delay;
//        }
//
//        public void setDelay(int delay) {
//            this.delay = delay;
//        }

        public String getId(){
            return id;
        }

        public List<Dependency> getDependencies() {
            return incomingEdge;
        }

        public int getDelayTo(Task task) {
            //check if task depends on this

            for (Dependency dependency: outgoingEdge) {
                if (dependency.getTargetTask().equals(task)) {
                    return dependency.getEdgeCost();
                }
            }

            //TODO throw exception
            return 0;
        }
}

