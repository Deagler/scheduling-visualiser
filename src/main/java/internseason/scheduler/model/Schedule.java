package internseason.scheduler.model;

import internseason.scheduler.algorithm.BBScheduleInfo;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.*;

public class Schedule implements Serializable {

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

    public Schedule(Schedule schedule) {
        this.processorIdMap = SerializationUtils.clone(schedule.processorIdMap);
        this.taskIdProcessorMap = SerializationUtils.clone(schedule.taskIdProcessorMap);
        this.numOfProcessors = schedule.numOfProcessors;
        this.cost = schedule.cost;
        this.maxBottomLevel = schedule.maxBottomLevel;
        this.idleTime = schedule.idleTime;
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

    public Map<Integer, Processor> getProcessorIdMap() {
        return this.processorIdMap;
    }

    public Map<String, Integer> getTaskIdProcessorMap() {
        return this.taskIdProcessorMap;
    }

    public int numProcessors() {
        return numOfProcessors;
    }

    /**
     * Schedules a task on a particular process at some time.
     * Updates the total cost of the schedule if it increases.
     * @param task
     * @param processorId
     * @param time
     */
    public void add(Task task, int processorId, int time) {

        if (!processorIdMap.containsKey(processorId)) {
            processorIdMap.put(processorId, new Processor(processorId));
        }

        Processor processor = processorIdMap.get(processorId);
        Integer processorCost = processor.getCost();
        processor.addTaskAt(task, time);
        this.taskIdProcessorMap.put(task.getId(), processorId);
        setCostIfIncreased(processor.getCost());

        Integer slack = time - processorCost;
        this.idleTime += slack;
        this.maxBottomLevel = Math.max(this.maxBottomLevel, time + task.getBottomLevel());
    }


    public int getMaxBottomLevel() {
        return maxBottomLevel;
    }

    public int getIdleTime() {

        return idleTime;
    }

    public boolean isTaskAssigned(String taskId) {
        return this.taskIdProcessorMap.containsKey(taskId);
    }

    /**
     * Checks if a task is free- such that all its predecessors are scheduled already
     * @param task
     * @return boolean
     */
    public boolean isTaskFree(Task task) {
        boolean isTaskFree = true;
        for (String parentId : task.getParentTasks()) {
            if (!isTaskAssigned(parentId)) {
                isTaskFree = false;
                break;
            }

        }
        return isTaskFree;
    }

    /**
     * Updates the cost of the schedule if it has increased
     * @param cost
     */
    private void setCostIfIncreased(int cost) {
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

    /**
     * Get all tasks currently scheduled (on all processors)
     * @return a list of task ids
     */
    public List<String> getTasks() {
        List<String> result = new ArrayList<>();

        for (Processor processor: processorIdMap.values()) {
            result.addAll(processor.getTaskIds());
        }

        return result;
    }

    public int getNumberOfTasks() {
        return this.getTasks().size();
    }

    /**
     * @param task
     * @return The scheduled start time for a given task
     */
    public int getTaskStartTime(Task task) {
        int processId = taskIdProcessorMap.get(task.getId());
        Processor processor = processorIdMap.get(processId);
        return processor.getTaskStartTime(task.getId());
    }

    public int getProcessorIdForTask(String taskId) {
        return this.taskIdProcessorMap.get(taskId);
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
