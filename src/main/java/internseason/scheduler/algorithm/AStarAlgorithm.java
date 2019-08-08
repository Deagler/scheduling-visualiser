package internseason.scheduler.algorithm;

import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Schedule;
import internseason.scheduler.model.Task;

import java.util.*;



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


    @Override
    public Schedule execute() {

        int totalTasks = graph.getTasks().size();
        Schedule initialSchedule = new Schedule(getNumberOfProcessors());

        scheduleQueue.add(new ScheduleInfo(initialSchedule, 0));


        while (!scheduleQueue.isEmpty()) {
            ScheduleInfo head = scheduleQueue.poll();

            if (head.schedule.getNumberOfTasks() == totalTasks) {
                return head.schedule;
            }


            List<ScheduleInfo> combinations = generateAllCombinations(head, topologicalTasks.get(head.layer));

            if (combinations == null) {

                head.layer = head.layer +1;
                combinations = generateAllCombinations(head, topologicalTasks.get(head.layer));
            }

            scheduleQueue.addAll(combinations);

            System.out.println(scheduleQueue);
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
