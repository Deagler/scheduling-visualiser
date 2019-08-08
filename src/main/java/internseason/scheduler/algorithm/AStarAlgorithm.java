package internseason.scheduler.algorithm;

import internseason.scheduler.model.Schedule;
import internseason.scheduler.model.Task;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class AStarAlgorithm extends BaseAlgorithm {
    Queue<Schedule> scheduleQueue;

    public AStarAlgorithm(int numberOfProcessors) {
        super(numberOfProcessors);
        scheduleQueue = new PriorityQueue<>(new AStarHeuristic());
    }

    @Override
    Schedule execute(List<Task> tasks) {
        return null;
    }


    private class AStarHeuristic implements Comparator<Schedule> {

        @Override
        public int compare(Schedule o1, Schedule o2) {
            if (o1.getCost() < o2.getCost()) {
                return -1;
            } else if (o1.getCost() > o2.getCost()) {
                return 1;
            } else {
                if (o1.getNumberOfTasks() > o2.getNumberOfTasks()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }

    }
}
