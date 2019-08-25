package internseason.scheduler.heuristic;

import internseason.scheduler.model.Schedule;


/** Base interface for heuristics that all concrete heuristic classes implement, defines one function that returns
 *  the cost associated with the heuristic
 */
public interface BaseHeuristic {

    int calculateCostFunction(Schedule partialSchedule);

}
