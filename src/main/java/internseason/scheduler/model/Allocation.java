package internseason.scheduler.model;

public class Allocation {
    int startTime;
    Processor processor;
    Task task;

    public Allocation(int startTime, Processor processor, Task task) {
        this.startTime = startTime;
        this.processor = processor;
        this.task = task;
    }

    public Allocation(Processor processor, Task task) {
        this.processor = processor;
        this.task = task;
        this.startTime = processor.getCost();
        this.processor.setCost(this.processor.getCost() + task.getCost());
    }
}
