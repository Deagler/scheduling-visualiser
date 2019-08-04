package internseason.scheduler.model;

public class Allocation {
    int startTime;
    int processId;

    public Allocation(int startTime, int processId) {
        this.startTime = startTime;
        this.processId = processId;
    }
}
