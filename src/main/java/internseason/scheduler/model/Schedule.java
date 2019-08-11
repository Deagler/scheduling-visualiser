package internseason.scheduler.model;

import org.apache.commons.lang3.SerializationUtils;

import java.util.*;

public class Schedule {

    private HashMap<Integer, Processor> processorMap; //map from processId to processor
    private HashMap<String, Integer> taskMap; // map from task to process id
    private int numOfProcessors;
    private int cost;

    public Schedule(int numOfProcessors) {
        this.numOfProcessors = numOfProcessors;
        this.cost = 0;
        this.taskMap = new HashMap<>();

        this.initializeProcessMap(numOfProcessors);
    }

    public void initializeProcessMap(int numberOfProcesses) {
        this.processorMap = new HashMap<>();

        for (int i = 0; i < numberOfProcesses; i++) {
            this.processorMap.put(i, new Processor(i));
        }
    }

    public Schedule(Schedule schedule) {
        this.processorMap = SerializationUtils.clone(schedule.processorMap);
        this.numOfProcessors = schedule.numOfProcessors;
        this.cost = schedule.cost;
        this.taskMap = SerializationUtils.clone(schedule.taskMap);
    }

    public int numProcessors() {
        return numOfProcessors;
    }

    public void add(Task task, int processorId) {

        if (!processorMap.containsKey(processorId)) {
            processorMap.put(processorId, new Processor(processorId));
        }

        Processor processor = processorMap.get(processorId);

        processor.addTaskAt(task, findNextAvailableTimeInProcessor(task, processorId));

        this.taskMap.put(task.getId(), processorId);

        checkIncreasedCost(processor.getCost());
    }


    private int findNextAvailableTimeInProcessor(Task task, int processorId) {
        List<Task> parentTasks = task.getParentTasks();

        int result = processorMap.get(processorId).getCost();

        for (Task parent: parentTasks) {


            Integer sourceProcess = this.taskMap.get(parent.getId());

            if (sourceProcess == processorId) {
                continue;
            }

            Processor process = this.processorMap.get(sourceProcess);
            int parentFinishTime = process.getTaskStartTime(parent) + parent.getCost();
            int communicationCost = parent.getCostToChild(task);

            int newScheduleTime = parentFinishTime + communicationCost;

            result = Math.max(result, newScheduleTime);
        }

        return result;
    }

    public boolean isTaskAssigned(Task task) {
        return this.taskMap.containsKey(task.getId());
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



    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i=0; i<numOfProcessors; i++) {
            //sb.append("p" + processorMap.get(i));
            sb.append("Processor " + i + "\n");
            sb.append(processorMap.get(i));
            sb.append("Cost of Processor " + i + ": " + processorMap.get(i).getCost() + "\n");
        }

        sb.append("Total schedule cost is: " + this.cost);

        return sb.toString();
    }


}
