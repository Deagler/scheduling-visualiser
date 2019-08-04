package internseason.scheduler.model;

public class Allocation {
    int startTime;
    String processId;

    public Allocation(int startTime, String processId) {
        this.startTime = startTime;
        this.processId = processId;
    }
}
