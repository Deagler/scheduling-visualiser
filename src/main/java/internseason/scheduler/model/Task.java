package internseason.scheduler.model;

import java.util.HashMap;
import java.util.Map;

public class Task {
        private Map<Task, Integer> children;
        private int cost;
        private int delay;
        private String id;

        public Task(int cost, String id) {
            this.cost = cost;
            this.id = id;
            children = new HashMap<>();
            this.delay = 0;
        }

        public Task(int cost, int delay, String id) {
            this.cost = cost;
            this.id = id;
            children = new HashMap<>();
            this.delay = delay;
        }

        public int getCost() {
            return cost;
        }

        public boolean hasDelay() {
            if (delay > 0) {
                return true;
            }
            return false;
        }

        public int getDelay() {
            return delay;
        }

        public void setDelay(int delay) {
            this.delay = delay;
        }
}

