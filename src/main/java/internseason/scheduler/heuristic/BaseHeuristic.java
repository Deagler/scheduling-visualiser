package internseason.scheduler.heuristic;

import internseason.scheduler.model.Schedule;

public interface BaseHeuristic {

    int calculateCostFunction(Schedule partialSchedule);

}
