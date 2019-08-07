package internseason.scheduler.model;

import javafx.util.Pair;

import java.util.*;

public class Schedule {

    //List<Allocation> scheduleList;
    //Map<Task, Processor> scheduleMap;
    //Map<Processor, List<Task>> scheduleMap;
    private Map<Integer, Processor> processorMap;
    private int numOfProcessors;
    private int cost;
    private Stack<Integer> processorOrder;

    public Schedule(int numOfProcessors) {
        //scheduleList = new ArrayList<>();
        //scheduleMap = new HashMap<>();
        processorMap = new HashMap<>();
        this.numOfProcessors = numOfProcessors;
        this.cost = 0;
        this.processorOrder = new Stack<>();
    }

    public int numProcessors() {
        return numOfProcessors;
    }

    public int size() {
        return processorOrder.size();
        //return scheduleMap.size();
        //return scheduleList.size();
    }

    public boolean isEmpty() {
        return processorOrder.isEmpty();
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

        processorOrder.push(processor.getId());

        recalculateCost(processor.getTime());
    }

    public void addWithDelay(Task task, int processorId, int delay) {
        if (!processorMap.containsKey(processorId)) {
            processorMap.put(processorId, new Processor(processorId));
        }
        //task.setDelay(delay);
        Processor processor = processorMap.get(processorId);
        processor.addTaskWithDelay(task, delay);

        processorOrder.push(processor.getId());

        recalculateCost(processor.getTime());
    }

    public void removeLastTask(int processorId) {
        //clear up space on processor
        Processor processor = processorMap.get(processorId);
        //Task task = processor.getLastTask();
        //scheduleMap.remove(task);
        processor.removeLastTask();

        processorOrder.pop();

        recalculateCost(processor.getTime());
    }

    public void recalculateCost(int cost) {
        if (cost > this.cost) {
            this.cost = cost;
        }
    }

    public int getCost() {
        return cost;
    }

//    public void getTaskAt() {
//
//    }

    //get all tasks in all processors of this schedule
    public List<Task> getTasks() {
        ArrayList<Task> result = new ArrayList<>();

        for (Processor processor: processorMap.values()) {
            result.addAll(processor.getTasks());
        }

        return result;
    }

    public int getLastProcessorId() {
        return processorOrder.peek();
    }

    public Task getLastTask() {
        return processorMap.get(processorOrder.peek()).getLastTask();
    }
}
