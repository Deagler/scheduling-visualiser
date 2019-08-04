package internseason.scheduler.model;

import java.util.HashMap;
import java.util.Map;

public class Task {
        Map<Task, Integer> children;
        int cost;
        String id;

        public Task(int cost, String id) {
            this.cost = cost;
            this.id = id;
            children = new HashMap<>();
        }

        public int getCost() {
            return cost;
        }
}

