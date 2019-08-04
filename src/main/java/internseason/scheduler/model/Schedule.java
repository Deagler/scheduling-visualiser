package internseason.scheduler.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Schedule {

    //List<Allocation> scheduleList;
    //Map<Task, Processor> scheduleMap;
    //Map<Processor, List<Task>> scheduleMap;
    private Map<Integer, Processor> processorMap;
    private int numOfProcessors;
    private int cost;

    public Schedule(int numOfProcessors) {
        //scheduleList = new ArrayList<>();
        //scheduleMap = new HashMap<>();
        processorMap = new HashMap<>();
        this.numOfProcessors = numOfProcessors;
        this.cost = 0;
    }

    public int numProcessors() {
        return numOfProcessors;
    }

    public int size() {
        return processorMap.size();
        //return scheduleMap.size();
        //return scheduleList.size();
    }

    public void add(Task task, int processorId) {
        //map.put(task, new Allocation(, process));
        //scheduleMap.put(task, processorMap.getOrDefault(processorId, new Processor(processorId)));
        //processorMap.put(processorId, scheduleMap.get(task));
        //Allocation allocation = new Allocation(processorMap.getOrDefault(processorId, new Processor(processorId)), task);
        //scheduleList.add(allocation);
        if (!processorMap.containsKey(processorId)) {
            processorMap.put(processorId, new Processor(processorId));
        }
        //processorMap.get(processorId).addTask(task);
        Processor processor = processorMap.get(processorId);
        processor.addTask(task);

        recalculateCost(processor.getTime());
    }

    public void addWithDelay(Task task, int processorId, int delay) {
        if (!processorMap.containsKey(processorId)) {
            processorMap.put(processorId, new Processor(processorId));
        }
        //task.setDelay(delay);
        Processor processor = processorMap.get(processorId);
        processor.addTaskWithDelay(task, delay);

        recalculateCost(processor.getTime());
    }

    public void removeLastTask(int processorId) {
        //clear up space on processor
        Processor processor = processorMap.get(processorId);
        //Task task = processor.getLastTask();
        //scheduleMap.remove(task);
        processor.removeLastTask();

        recalculateCost(processor.getTime());
    }

    public void recalculateCost(int cost) {
        if (cost > this.cost) {
            this.cost = cost;
        }
    }

//    public void getTaskAt() {
//
//    }
}
