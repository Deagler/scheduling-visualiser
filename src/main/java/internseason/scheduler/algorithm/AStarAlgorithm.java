package internseason.scheduler.algorithm;

import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Schedule;
import internseason.scheduler.model.Task;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Temporary Data-structure to store a schedule and the given topological layer the internseason.scheduler.algorithm should attempt to generate
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

    /**
     * Tepmorary constructor to test factory pattern
     * @return
     */
    public AStarAlgorithm() {
        super();
        scheduleQueue = new PriorityQueue<>(new AStarHeuristic());
    }


    /**
     * Basic Implementation of the AStar Algorithm with duplicate detection and process normalisation.
     * Reference: https://researchspace.auckland.ac.nz/handle/2292/30213
     * @return An optimal schedule
     */
    @Override
    public Schedule execute(Graph graph, int numberOfProcessors) {
        int totalTasks = graph.getTasks().size();
        List<List<Task>> topologicalTasks = graph.getTopologicalOrdering();

        Schedule initialSchedule = new Schedule(numberOfProcessors);

        scheduleQueue.add(new ScheduleInfo(initialSchedule, 0)); // Add the empty schedule to the queue.
        // Calculates the Bottom Level for each task.
        List<Task> leafs = graph.getTasks().values() //find all the leaf nodes
                .stream()
                .filter((Task task) -> task.getOutgoingEdges().size() == 0)
                .collect(Collectors.toList());


        for (Task leaf : leafs) { //Compute the bottom levels for the nodes
            leaf.setBottomLevel(leaf.getCost());
            getBottomLevels(leaf.getParentTasks(), leaf.getCost());
        }

        Task maxTask = null;
        for (Task task : graph.getTasks().values()) {
           if (maxTask == null) {
               maxTask = task;
           }

           if (task.getBottomLevel() > maxTask.getBottomLevel()) {
               maxTask = task;
           }
        }


        Set<Integer> visited = new HashSet<>();
        visited.add(initialSchedule.hashCode());
        int counter = 0;
        while (!scheduleQueue.isEmpty()) {
            //ScheduleInfo head = scheduleQueue.poll();
            ScheduleInfo head = scheduleQueue.peek();
            scheduleQueue.remove();
            // Return the optimal schedule (First complete schedule, orchestrated by AStar Heuristic)
            if (head.schedule.getNumberOfTasks() == totalTasks) {
                System.out.println(counter);
                return head.schedule;
            }


            // Extending the polled schedule to generate all possible "next" states.
            List<ScheduleInfo> combinations = generateCombinations(head, topologicalTasks.get(head.layer), numberOfProcessors);


            if (combinations == null) { // Move to next topological layer if no possible schedules on current layer.
                head.layer = head.layer +1;
                combinations = generateCombinations(head, topologicalTasks.get(head.layer), numberOfProcessors);
                //scheduleQueue.clear();
            }

            for (ScheduleInfo possibleCombination : combinations) {

                if (visited.contains(possibleCombination.schedule.hashCode())) {
                    continue;
                }
                scheduleQueue.add(possibleCombination);
                visited.add(possibleCombination.schedule.hashCode());
            }


            counter++;
        }

        return null;
    }

    private int calculateIdleHeuristic(Schedule schedule) {
        int totalTaskTime = 0;
        for (Task task : schedule.getTasks()) {
            totalTaskTime += task.getCost();
        }

        return ((totalTaskTime + schedule.getIdleTime()) / schedule.getNumOfProcessors());

    }

    private Integer calculateCost(Schedule schedule) {
        return (Math.max(schedule.getMaxBottomLevel(), calculateIdleHeuristic(schedule)));

    }

    /**
     * Generates all possible schedules if a node from the current topological layering
     * has not been assigned in the schedule.
     * @param scheduleinfo
     * @param currentLayer
     * @return
     */
    private List<ScheduleInfo> generateCombinations(ScheduleInfo scheduleinfo, List<Task> currentLayer, int numberOfProcessors) {

//        if (tasksMatchFTOConditions(currentLayer)) {
//
//        } else {
            return generateAllCombinations(scheduleinfo, currentLayer, numberOfProcessors);
//        }

    }

//    private List<ScheduleInfo> generateFTOCombinations(ScheduleInfo scheduleinfo, List<Task> currentLayer) {
//
//    }

    private List<ScheduleInfo> generateAllCombinations(ScheduleInfo scheduleinfo, List<Task> currentLayer, int numberOfProcessors) {
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

    private void getBottomLevels(List<Task> tasks, int currentBottomLevel) {
        for (Task node : tasks) {
            if (node.getCost() < currentBottomLevel + node.getCost()) {
                node.setBottomLevel(currentBottomLevel + node.getCost());
            }
            if (!node.getParentTasks().isEmpty()) {
                getBottomLevels(node.getParentTasks(),
                        node.getBottomLevel());
            }
        }
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
            return calculateCost(o1).compareTo(calculateCost(o2));
        }

    }

    @Override
    public String toString() {
        return "A Star Algorithm";
    }
}
