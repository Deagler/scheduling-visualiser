package internseason.scheduler.algorithm;

import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Schedule;
import internseason.scheduler.model.Task;

import java.util.List;

public class BasicAlgorithm extends BaseAlgorithm {


    public BasicAlgorithm(Graph graphObj, int numOfProcessors) {
        super(graphObj, numOfProcessors);
    }

    @Override
    public Schedule execute() {
        Schedule schedule = new Schedule(numberOfProcessors);
        List<List<String>> topological = graph.getTopologicalOrdering();

        for (List<String> layer : topological) {
            for (String taskId : layer) {
                Task task = graph.getTask(taskId);

                try {
                    schedule.add(task, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return schedule;
    }
}
