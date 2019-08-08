package internseason.scheduler.algorithm;

import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Schedule;
import internseason.scheduler.model.Task;

import java.util.*;

public class AStarAlgorithm extends BaseAlgorithm {
    Queue<Schedule> scheduleQueue;
    List<List<Task>> topologicalTasks;

    public AStarAlgorithm(Graph graphObj, int numberOfProcessors) {
        super(graphObj, numberOfProcessors);
        scheduleQueue = new PriorityQueue<>(new AStarHeuristic());
        topologicalTasks = new ArrayList<>();


        for (List<String> idLayer : graph.getTopologicalOrdering()) {
            topologicalTasks.add(getTasksFromIds(idLayer));
        }

    }


    @Override
    Schedule execute() {

        int totalTasks = graph.getTasks().size();
        Schedule initialSchedule = new Schedule(getNumberOfProcessors());
        scheduleQueue.add(initialSchedule);
        int currentLayer = 0;

        while (!scheduleQueue.isEmpty()) {
            Schedule head = scheduleQueue.poll();

            if (head.getNumberOfTasks() == totalTasks) {
                return head;
            }


            List<Schedule> combinations = generateAllCombinations(head, topologicalTasks.get(currentLayer));

            if (combinations == null) {
                currentLayer++;
                combinations = generateAllCombinations(head, topologicalTasks.get(currentLayer));
            }

            scheduleQueue.addAll(combinations);
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

    private List<Schedule> generateAllCombinations(Schedule schedule, List<Task> currentLayer) {

        for (int i = currentLayer.size() - 1; i >= 0; i--) {
            Task task = currentLayer.get(i);
            if (schedule.isTaskAssigned(task.getId())) {
                currentLayer.remove(i);
            }
        }

        if (currentLayer.size() == 0) {
            return null;
        }
        List<Schedule> out = new ArrayList<>();

        for (int i = 0; i < currentLayer.size(); i++) {
            Task node = currentLayer.get(i);

            for (int processId = 0; processId < numberOfProcessors; processId++) {
                Schedule newSchedule = new Schedule(schedule);
                newSchedule.add(node, processId);
                out.add(newSchedule);
            }
        }

        return out;
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
                } else if (o1.getNumberOfTasks() == o2.getNumberOfTasks()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        }

    }
}
