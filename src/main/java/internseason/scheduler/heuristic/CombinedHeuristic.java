package internseason.scheduler.heuristic;

import internseason.scheduler.model.Schedule;

import java.util.List;

public class CombinedHeuristic implements BaseHeuristic {

    private List<BaseHeuristic> heuristics;

    public CombinedHeuristic(List<BaseHeuristic> heuristics){
        this.heuristics = heuristics;
    }

    @Override
    public int calculateCostFunction(Schedule partialSchedule) {
        int max = Integer.MIN_VALUE;
        for (BaseHeuristic heuristic : heuristics){
            max = Math.max(max,heuristic.calculateCostFunction(partialSchedule));
        }
        return max;
    }
}
