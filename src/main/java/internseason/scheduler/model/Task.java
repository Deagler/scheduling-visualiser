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

        public void addIncoming(Dependency edge) {
            this.incomingEdges.add(edge);
        }

        public void addOutgoing(Dependency edge) {
            this.outgoingEdges.add(edge);
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

        public List<Dependency> getOutgoingEdges(){
            return outgoingEdges;
        }

        public List<Dependency> getIncomingEdges(){
            return incomingEdges;
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

