package internseason.scheduler.algorithm;

import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Schedule;
import internseason.scheduler.model.Task;

import java.util.List;


/**
 * Base Interface for ALgorithms
 */
public abstract class BaseAlgorithm {
    public BaseAlgorithm() {

    }

    public abstract Schedule execute(Graph graphl, int numberOfProcessors, int numberOfCores);

    public abstract String toString();

}
