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
    private Map<String, Task> allTasks;

    public Schedule(int numOfProcessors, Map<String, Task> allTasks) {
        this.numOfProcessors = numOfProcessors;
        this.cost = 0;
        this.taskIdProcessorMap = new HashMap<>();
        this.idleTime =0;
        this.allTasks = allTasks;

        this.initializeProcessMap(numOfProcessors);
    }

    public Schedule(Schedule schedule, Map<String, Task> allTasks) {
        this.processorIdMap = SerializationUtils.clone(schedule.processorIdMap);
        this.taskIdProcessorMap = SerializationUtils.clone(schedule.taskIdProcessorMap);
        this.numOfProcessors = schedule.numOfProcessors;
        this.cost = schedule.cost;
        this.maxBottomLevel = schedule.maxBottomLevel;
        this.allTasks = allTasks;
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
        setCostIfIncreased(processor.getCost());



        Integer slack = startTime - processorCost;
        this.idleTime += slack;
        this.maxBottomLevel = Math.max(this.maxBottomLevel, startTime + task.getBottomLevel());
    }


    /**
     * Given a task t, schedule it on a processor p such that t's start time is minimised
     */
    public void addWithLowestStartTime(Task task) {
        int earliest = Integer.MAX_VALUE;
        int targetProcessorId = 0;
        for (int processorId = 0; processorId < numOfProcessors; processorId++) {
            int time = findNextAvailableTimeInProcessor(task, processorId);
            if (time < earliest) {
                earliest = time;
                targetProcessorId = processorId;
            }
        }

        this.add(task, targetProcessorId);
    }


    public int getEarliestStartTime(Task task){
        int earliest = Integer.MAX_VALUE;
        for (int processorId = 0; processorId < numOfProcessors; processorId++) {
            int time = findNextAvailableTimeInProcessor(task, processorId);
            if (time < earliest) {
                earliest = time;
            }
        }
        return earliest;
    }

    public int getMaxBottomLevel() {
        return maxBottomLevel;
    }

    public int getIdleTime() {

        return idleTime;
    }

    private int findNextAvailableTimeInProcessor(Task task, int processorId) {
        if (task == null) {
            System.out.println("chad mode");
        }
        List<String> parentTasks = task.getParentTasks();

        int result = processorIdMap.get(processorId).getCost();

        for (String parentId: parentTasks) {


            Integer sourceProcess = this.taskIdProcessorMap.get(parentId);

            if (sourceProcess == processorId) {
                continue;
            }

            Processor process = this.processorIdMap.get(sourceProcess);
            Task parentTask = allTasks.get(parentId);
            int parentFinishTime = process.getTaskStartTime(parentId) + parentTask.getCost();
            int communicationCost = parentTask.getCostToChild(task);

            int newScheduleTime = parentFinishTime + communicationCost;

            result = Math.max(result, newScheduleTime);
        }

        return result;
    }

    public boolean isTaskAssigned(String taskId) {
        return this.taskIdProcessorMap.containsKey(taskId);
    }

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

    //get all tasks in all processors of this schedule
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

    public int getTaskStartTime(Task task) {
        int processId = taskIdProcessorMap.get(task.getId());
        Processor processor = processorIdMap.get(processId);
        return processor.getTaskStartTime(task.getId());
    }

    public int getProcessorIdForTask(String taskId) {
        return this.taskIdProcessorMap.get(taskId);
    }

    //finishing time of parent task + edge cost from parent to task
    public int calculateDRT(Task task) {

        int min = Integer.MAX_VALUE;

        for (int processorId : processorIdMap.keySet()) {
            int max = 0;

            for (String parentId : task.getParentTasks()) {
                Task parent = allTasks.get(parentId);

                //finish time of parent
                Processor parentProcessor = processorIdMap.get(taskIdProcessorMap.get(parent.getId()));
                int finTime = parentProcessor.getTaskStartTime(parentId) + parent.getCost();
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

    //TODO throw exception
    //finishing time of parent task + edge cost from parent to task
    //input task should have a maximum of 1 parent
    public int calculateDRTSingle(Task task) {
        //if no parent return 0
        if (task.getNumberOfParents() == 0) {
            return 0;
        }

        //should only have 1 parent
        if (task.getNumberOfParents() == 1) {
            String parentTaskId = task.getParentTasks().get(0);
            Task parent = allTasks.get(parentTaskId);

            //finish time of parent
            int finTime = this.getTaskStartTime(parent) + parent.getCost();
            int cost = parent.getCostToChild(task);

            return finTime + cost;

        }

        return -1;
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

    public static Schedule buildGreedySchedule(BBScheduleInfo scheduleInfo, Graph graph) {
        Schedule cloneSchedule = new Schedule(scheduleInfo.getSchedule(),graph.getTasks());

        Set<String> freeList = scheduleInfo.getFreeTasks();
        System.out.println("original free list"+freeList);
        int counter = 0;
        while (freeList.size() > 0) {

            Set<String> temp = new HashSet<>();
            for (String taskId : freeList) {
                Task task = graph.getTask(taskId);
                cloneSchedule.addWithLowestStartTime(graph.getTask(taskId));
                for (String childId : task.getChildrenList()) {
                    Task child = graph.getTask(childId);
                    if (cloneSchedule.isTaskFree(child)) {
                        temp.add(child.getId());
                    }
                }
            }
            freeList = temp;
            System.out.println(" new free list "+ freeList+ " iteration: "+ counter);
            counter++;
        }
        return cloneSchedule;
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
