package internseason.scheduler.heuristic;

import internseason.scheduler.model.Schedule;

/** Calculates the total amount of idle time currently present in the partial schedule
 */
public class IdleTimeHeuristic implements BaseHeuristic {
    private int totalTaskTime;

    public IdleTimeHeuristic(int totalTasktime){
        this.totalTaskTime = totalTasktime;
    }

    @Override
    public int calculateCostFunction(Schedule partialSchedule) {
        return (totalTaskTime + partialSchedule.getIdleTime()-1) / partialSchedule.getNumOfProcessors();
    }
}
