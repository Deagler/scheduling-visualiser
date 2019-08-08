package internseason.scheduler.model;

import java.util.*;

public class Schedule {


    private Map<Integer, Processor> processorMap;
    private int numOfProcessors;
    private int cost;
    private Stack<Integer> processorOrder;

    public Schedule(int numOfProcessors) {
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

        checkIncreasedCost(processor.getCost());
    }

    public void addWithDelay(Task task, int processorId, int delay) throws Exception {
        if (!processorMap.containsKey(processorId)) {
            processorMap.put(processorId, new Processor(processorId));
        }
        //task.setDelay(delay);
        Processor processor = processorMap.get(processorId);
        //processor.addTaskWithDelay(task, delay);
        processor.addTaskAt(task, processor.getCost() + delay);

        processorOrder.push(processor.getId());

        checkIncreasedCost(processor.getCost());
    }

    public void removeLastTask() {
        //clear up space on processor
        Processor processor = processorMap.get(processorOrder.peek());
        //Task task = processor.getLastTask();
        //scheduleMap.remove(task);

        boolean costChanged = false;

        if (processor.getCost() == this.cost) {
            costChanged = true;
        }

        processor.removeLastTask();

        processorOrder.pop();

        if (costChanged) {
            //recalculateCost();
        }
    }

    private void checkIncreasedCost(int cost) {
        if (cost > this.cost) {
            this.cost = cost;
        }
    }

//    public void checkDecreasedCost(int cost) {
//        if (cost < this.cost) {
//            this.cost = cost;
//        }
//    }

    private void recalculateCost() {
        int max = 0;
        for (Processor p: processorMap.values()) {
            if (p.getCost() > max) {
                max = p.getCost();
            }
        }
        this.cost = max;
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

    public int getNumberOfTasks() {
        return this.getTasks().size();
    }

    public int getLastProcessorId() {
        return processorOrder.peek();
    }

    public Task getLastTask() {
        return processorMap.get(processorOrder.peek()).getLastTask();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i=0; i<numOfProcessors; i++) {
            sb.append(processorMap.get(i).toString());
            System.out.println("HERE:" + processorMap.get(i).toString());
//            sb.append("p");
//            sb.append(i);
//            sb.append(": cost: ");
//            sb.append(processorMap.get(i).getCost());
            sb.append("\n");
        }

        return sb.toString();
    }
}
