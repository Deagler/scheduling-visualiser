package internseason.scheduler.heuristic;

import internseason.scheduler.model.Schedule;


/** Returns the critical path cost of a given partial schedule
 */
public class CriticalPathHeuristic implements BaseHeuristic {

    @Override
    public int calculateCostFunction(Schedule partialSchedule) {
        return partialSchedule.getMaxBottomLevel();
    }
}
