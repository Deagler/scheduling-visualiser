package internseason.scheduler.heuristic;

import internseason.scheduler.model.Schedule;

public class IdleTimeHeuristic implements BaseHeuristic {
    @Override
    public int calculateCostFunction(Schedule partialSchedule) {
        return 0;
    }
}
