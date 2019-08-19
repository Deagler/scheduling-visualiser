package internseason.scheduler.algorithm;

import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Schedule;
import internseason.scheduler.model.Task;

import java.util.*;


/**
 * Temporary Data-structure to store a schedule and the given topological layer the algorithm should attempt to generate
 * new schedules from.
 */
class ScheduleInfo {
    public Schedule schedule;
    public Integer layer;

    public ScheduleInfo(Schedule schedule, Integer layer) {
        this.schedule = schedule;
        this.layer = layer;
    }

    @Override
    public String toString() {
        return "ScheduleInfo{" +
                ", layer=" + layer +
                '}';
    }
}

/**
 * PoC AStar Algorithm that uses Topological layering and
 * a heuristic that factors in the cost of the schedule and number of tasks.
 *
 * Possible Schedules are generated layer-by-layer as to avoid generating schedules that have dependency violations.
 */
public class AStarAlgorithm extends BaseAlgorithm {
    Queue<ScheduleInfo> scheduleQueue;
    List<List<Task>> topologicalTasks;


    public AStarAlgorithm(Graph graphObj, int numberOfProcessors) {
        super(graphObj, numberOfProcessors);
        scheduleQueue = new PriorityQueue<>(new AStarHeuristic());
        topologicalTasks = new ArrayList<>();


        for (List<String> idLayer : graph.getTopologicalOrdering()) {
            topologicalTasks.add(getTasksFromIds(idLayer));
        }

    }

    /**
     * Basic Implementation of the AStar Algorithm without duplicate detection or any pruning.
     * Reference: https://researchspace.auckland.ac.nz/handle/2292/30213
     * @return An optimal schedule
     */
    @Override
    public Schedule execute() {

        int totalTasks = graph.getTasks().size();
        Schedule initialSchedule = new Schedule(getNumberOfProcessors());

        scheduleQueue.add(new ScheduleInfo(initialSchedule, 0)); // Add the empty schedule to the queue.

        Set<Integer> visited = new HashSet<>();
        visited.add(initialSchedule.hashCode());
    int counter = 0;
        while (!scheduleQueue.isEmpty()) {
            ScheduleInfo head = scheduleQueue.poll();

            // Return the optimal schedule (First complete schedule, orchestrated by AStar Heuristic)
            if (head.schedule.getNumberOfTasks() == totalTasks) {
                System.out.println(counter);
                return head.schedule;
            }


            // Extending the polled schedule to generate all possible "next" states.
            List<ScheduleInfo> combinations = generateAllCombinations(head, topologicalTasks.get(head.layer));


            if (combinations == null) { // Move to next topological layer if no possible schedules on current layer.
                head.layer = head.layer +1;
                combinations = generateAllCombinations(head, topologicalTasks.get(head.layer));
            }

            for (ScheduleInfo possibleCombination : combinations) {

                if (visited.contains(possibleCombination.schedule.hashCode())) {
                    System.out.println(counter);
                    continue;
                }
                scheduleQueue.add(possibleCombination);
                visited.add(possibleCombination.schedule.hashCode());
            }


             counter++;
        }

        return null;
    }

    private List<Task> getTasksFromIds(List<String> taskIds) {
        List<Task> out = new ArrayList<>();
        for (String taskId : taskIds) {
            out.add(graph.getTask(taskId));
        }

        return out;
    }

    /**
     * Generates all possible schedules if a node from the current topological layering
     * has not been assigned in the schedule.
     * @param scheduleinfo
     * @param currentLayer
     * @return
     */
    private List<ScheduleInfo> generateAllCombinations(ScheduleInfo scheduleinfo, List<Task> currentLayer) {
        Schedule schedule = scheduleinfo.schedule;

        currentLayer = new ArrayList<>(currentLayer);
        for (int i = currentLayer.size() - 1; i >= 0; i--) {
            Task task = currentLayer.get(i);
            if (schedule.isTaskAssigned(task)) {
                currentLayer.remove(i);
            }
        }


        if (currentLayer.size() == 0) {
            return null;
        }

        List<ScheduleInfo> out = new ArrayList<>();
        for (int i = 0; i < currentLayer.size(); i++) {
            Task node = currentLayer.get(i);

            for (int processId = 0; processId < numberOfProcessors; processId++) {
                Schedule newSchedule = new Schedule(schedule);
                newSchedule.add(node, processId);
                out.add(new ScheduleInfo(newSchedule, scheduleinfo.layer));
            }
        }


        return out;
    }

    /**
     * Heuristic that orders schedules in ascending order of cost. (Lowest cost first)
     * If costs are equal then the schedule with a higher number of tasks assigned comes first.
     */
    private class AStarHeuristic implements Comparator<ScheduleInfo> {

        @Override
        public int compare(ScheduleInfo o1Info, ScheduleInfo o2Info) {
            Schedule o1 = o1Info.schedule;
            Schedule o2 = o2Info.schedule;
            if (o1.getCost() < o2.getCost()) {
                return -1;
            } else if (o1.getCost() > o2.getCost()) {
                return 1;
            } else {
                if (o1.getNumberOfTasks() > o2.getNumberOfTasks()) {
                    return -1;
                } else if (o1.getNumberOfTasks() == o2.getNumberOfTasks()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        }

    }
}
