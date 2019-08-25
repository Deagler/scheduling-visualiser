package internseason.scheduler.heuristic;

import internseason.scheduler.model.Schedule;
import internseason.scheduler.model.Scheduler;
import internseason.scheduler.model.Task;

import java.util.List;


/** Calculates the DRT cost of a given partial schedule by looking at its max data ready time and bottom level
 */
public class DataReadyTimeHeuristic implements BaseHeuristic {
    private List<Task> freeTasks;
    private Scheduler scheduler;

    public DataReadyTimeHeuristic(List<Task> freeTasks, Scheduler scheduler){
        this.freeTasks = freeTasks;
        this.scheduler = scheduler;
    }
    @Override
    public int calculateCostFunction(Schedule partialSchedule) {
        int maxDRT = Integer.MIN_VALUE;
        for (Task task : freeTasks){
            int drt = this.scheduler.calculateDRT(partialSchedule, task);
            int bottomLevel = task.getBottomLevel();
            maxDRT = Math.max(maxDRT, (drt+bottomLevel));
        }
        return maxDRT;
    }
}
