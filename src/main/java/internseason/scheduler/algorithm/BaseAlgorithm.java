package internseason.scheduler.algorithm;

import internseason.scheduler.model.Schedule;
import internseason.scheduler.model.Task;

import java.util.List;

public abstract class BaseAlgorithm {
    protected int numberOfProcessors;

    public BaseAlgorithm(int numberOfProcessors) {
        this.numberOfProcessors = numberOfProcessors;
    }

    abstract Schedule execute(List<Task> tasks);

    public int getNumberOfProcessors() {
        return numberOfProcessors;
    }

    public void setNumberOfProcessors(int numberOfProcessors) {
        this.numberOfProcessors = numberOfProcessors;
    }
}
