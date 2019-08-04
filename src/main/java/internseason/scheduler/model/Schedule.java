package internseason.scheduler.model;


import java.util.HashMap;
import java.util.Map;

public class Schedule {

    Map<Task, Processor> scheduleMap;
    Map<Integer, Processor> processorMap;
    int numOfProcessors;
    int cost;

    public Schedule(int numOfProcessors) {
        scheduleMap = new HashMap<>();
        processorMap = new HashMap<>();
        this.numOfProcessors = numOfProcessors;
        this.cost = 0;
    }

    public int numProcessors() {
        return numOfProcessors;
    }

    public int size() {
        return scheduleMap.size();
    }

    public void add(Task task, int processorId) {
        //map.put(task, new Allocation(, process));
        scheduleMap.put(task, processorMap.getOrDefault(processorId, new Processor(processorId)));
        processorMap.put(processorId, scheduleMap.get(task));
        processorMap.get(processorId).addTask(task);
    }

    public void removeLastTask(int processorId) {
        //clear up space on processor
        Processor processor = processorMap.get(processorId);
        Task task = processor.getLastTask();
        scheduleMap.remove(task);
        processor.removeLastTask();
    }

//    public void getTaskAt() {
//
//    }
}
