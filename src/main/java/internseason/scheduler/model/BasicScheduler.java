package internseason.scheduler.model;

import java.util.List;

public class BasicScheduler {
    public Schedule produceSchedule(Graph graph, int numOfProcessors) {

        Schedule schedule = new Schedule(numOfProcessors);
        List<String> topological = graph.getTopologicalOrdering();

        for (String taskId : topological) {
            Task task = graph.getTask(taskId);
            try {
                schedule.add(task, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return schedule;
    }
}
