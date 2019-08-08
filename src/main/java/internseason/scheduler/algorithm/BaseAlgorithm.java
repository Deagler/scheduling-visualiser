package internseason.scheduler.algorithm;

import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Schedule;
import internseason.scheduler.model.Task;

import java.util.List;

public abstract class BaseAlgorithm {
    protected int numberOfProcessors;
    protected Graph graph;

    public BaseAlgorithm(Graph graphObj, int numberOfProcessors) {
        this.numberOfProcessors = numberOfProcessors;
        this.graph = graphObj;
    }

    public abstract Schedule execute();

    public int getNumberOfProcessors() {
        return numberOfProcessors;
    }

    public void setNumberOfProcessors(int numberOfProcessors) {
        this.numberOfProcessors = numberOfProcessors;
    }


}
