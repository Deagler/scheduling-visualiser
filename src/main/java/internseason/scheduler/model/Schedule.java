package internseason.scheduler.model;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;

public class Schedule {

    private HashMap<Integer, Processor> processorIdMap; //map from processId to processor
    private HashMap<String, Integer> taskIdProcessorMap; // map from task to process id
    private int numOfProcessors;
    private int cost;
    private int maxBottomLevel;
    private int idleTime;

    public Schedule(int numOfProcessors) {
        this.numOfProcessors = numOfProcessors;
        this.cost = 0;
        this.taskIdProcessorMap = new HashMap<>();
        this.idleTime =0;

        this.initializeProcessMap(numOfProcessors);
    }

    public void initializeProcessMap(int numberOfProcesses) {
        this.processorIdMap = new HashMap<>();

        for (int i = 0; i < numberOfProcesses; i++) {
            this.processorIdMap.put(i, new Processor(i));
        }
    }

    public int getNumOfProcessors() {
        return numOfProcessors;
    }

    public Schedule(Schedule schedule) {
        this.processorIdMap = SerializationUtils.clone(schedule.processorIdMap);
        this.numOfProcessors = schedule.numOfProcessors;
        this.cost = schedule.cost;
        this.taskIdProcessorMap = SerializationUtils.clone(schedule.taskIdProcessorMap);
        this.maxBottomLevel = schedule.maxBottomLevel;
        this.idleTime = schedule.idleTime;
    }

    public int numProcessors() {
        return numOfProcessors;
    }

    public void add(Task task, int processorId) {

        if (!processorIdMap.containsKey(processorId)) {
            processorIdMap.put(processorId, new Processor(processorId));
        }

        Processor processor = processorIdMap.get(processorId);
        Integer processorCost = processor.getCost();
        Integer startTime = findNextAvailableTimeInProcessor(task, processorId);
        processor.addTaskAt(task, startTime);

        this.taskIdProcessorMap.put(task.getId(), processorId);

        checkIncreasedCost(processor.getCost());

        Integer slack = startTime - processorCost;

        this.idleTime += slack;

        this.maxBottomLevel = Math.max(this.maxBottomLevel, startTime + task.getBottomLevel());
    }

    public int getMaxBottomLevel() {
        return maxBottomLevel;
    }

    public int getIdleTime() {
        return idleTime;
    }

    private int findNextAvailableTimeInProcessor(Task task, int processorId) {
        List<Task> parentTasks = task.getParentTasks();

        int result = processorIdMap.get(processorId).getCost();

        for (Task parent: parentTasks) {


            Integer sourceProcess = this.taskIdProcessorMap.get(parent.getId());

            if (sourceProcess == processorId) {
                continue;
            }

            Processor process = this.processorIdMap.get(sourceProcess);
            int parentFinishTime = process.getTaskStartTime(parent) + parent.getCost();
            int communicationCost = parent.getCostToChild(task);

            int newScheduleTime = parentFinishTime + communicationCost;

            result = Math.max(result, newScheduleTime);
        }

        return result;
    }

    public boolean isTaskAssigned(Task task) {
        return this.taskIdProcessorMap.containsKey(task.getId());
    }

    private void checkIncreasedCost(int cost) {
        if (cost > this.cost) {
            this.cost = cost;
        }
    }

    private void recalculateCost() {
        int max = 0;
        for (Processor p: processorIdMap.values()) {
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
        List<Task> result = new ArrayList<>();

        for (Processor processor: processorIdMap.values()) {
            result.addAll(processor.getTasks());
        }

        return result;
    }

    public int getNumberOfTasks() {
        return this.getTasks().size();
    }

    public int getTaskStartTime(Task task) {
        int processId = taskIdProcessorMap.get(task.getId());
        Processor processor = processorIdMap.get(processId);
        return processor.getTaskStartTime(task);
    }

    public int getProcessorIdForTask(Task task) {
        return this.taskIdProcessorMap.get(task.getId());
    }

    //finishing time of parent task + edge cost from parent to task
    public int calculateDRT(Task task) {

        int min = Integer.MAX_VALUE;

        for (int processorId : processorIdMap.keySet()) {
            int max = 0;

            for (int i=0; i<task.getParentTasks().size(); i++) {
                Task parent = task.getParentTasks().get(i);

                //finish time of parent
                Processor parentProcessor = processorIdMap.get(taskIdProcessorMap.get(parent.getId()));
                int finTime = parentProcessor.getTaskStartTime(parent) + parent.getCost();
                int communicationCost = 0;

                if (parentProcessor.getId() != processorId) {
                    communicationCost = parent.getCostToChild(task);
                }

                if (finTime + communicationCost > max) {
                    max = finTime + cost;
                }
            }

            if (max < min ) {
                min = max;
            }
        }

        return min;
    }





    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i=0; i<numOfProcessors; i++) {
            //sb.append("p" + processorIdMap.get(i));
            sb.append("Processor " + i + "\n");
            sb.append(processorIdMap.get(i));
            sb.append("Cost of Processor " + i + ": " + processorIdMap.get(i).getCost() + "\n");
        }

        sb.append("Total schedule cost is: " + this.cost);

        return sb.toString();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();

        List<Integer> hashCodes = new ArrayList<>();

        for (Processor process : processorIdMap.values()) {
             hashCodes.add(process.hashCode());
        }

        Collections.sort(hashCodes);
        builder.append(hashCodes);
        return builder.hashCode();
    }
}
