package internseason.scheduler.algorithm;

import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Schedule;
import internseason.scheduler.model.Task;

import java.util.List;


/**
 * Base abstract class for algorithms that all concrete classes extend.
 */
public abstract class BaseAlgorithm {

    /** Hook method that all concrete classes have to implement, defines what the expected inputs and outputs
     *  to the algorithm is
     * @param graph
     * @param numberOfProcessors
     * @param numberOfCores
     * @return optimal schedule
     */
    public abstract Schedule execute(Graph graph, int numberOfProcessors, int numberOfCores, SystemInformation sysInfo);

   

    public abstract String toString();

}
