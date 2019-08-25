package internseason.scheduler.algorithm;

import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Schedule;
import internseason.scheduler.model.Scheduler;
import internseason.scheduler.model.Task;

import java.util.List;


/** Trivial algorithm that allocates every task on a single processor
 */
public class BasicAlgorithm extends BaseAlgorithm {

    @Override
    public Schedule execute(Graph graph, int numberOfProcessors, int numOfCores) {

        Scheduler scheduler = new Scheduler(graph);
        Schedule schedule = new Schedule(numberOfProcessors);
        List<List<Task>> topological = graph.getTopologicalOrdering();

        for (List<Task> layer : topological) {
            for (Task task : layer) {
                try {
                    scheduler.addTask(schedule, task, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return schedule;
    }

    @Override
    public String toString() {
        return "Basic Algorithm";
    }
}
