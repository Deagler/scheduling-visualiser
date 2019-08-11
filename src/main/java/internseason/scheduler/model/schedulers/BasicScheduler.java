package internseason.scheduler.model.schedulers;

import internseason.scheduler.algorithm.BasicAlgorithm;
import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Schedule;
import internseason.scheduler.model.Task;

import java.util.List;

public class BasicScheduler {

    private BasicAlgorithm basicAlgorithm;

    public BasicScheduler(Graph graph, int numOfProcessors) {
        basicAlgorithm = new BasicAlgorithm(graph, numOfProcessors);
    }

    public Schedule produceScheduler() {
        return basicAlgorithm.execute();
    }
}
