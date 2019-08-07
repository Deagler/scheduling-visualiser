package internseason.scheduler.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task {

        private int cost;
        //private int delay;
        private List<Dependency> incomingEdge;
        private List<Dependency> outgoingEdge;
        private String id;

        public Task(int cost, String id) {
            this.cost = cost;
            this.id = id;
            //this.delay = 0;
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

            for (Dependency dependency : outgoingEdge) {
                if (dependency.getTargetTask().equals(task)) {
                    return dependency.getDependencyCost();
                }
            }

            //TODO throw exception
            return 0;
        }

        @Override
        public String toString() {
            return "Task " + this.id + ", Cost: " + this.cost;
        }
}

