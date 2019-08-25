package internseason.scheduler.heuristic;

import internseason.scheduler.model.Schedule;

public class CriticalPathHeuristic implements BaseHeuristic {

    @Override
    public int calculateCostFunction(Schedule partialSchedule) {
        return partialSchedule.getMaxBottomLevel();
    }
}
