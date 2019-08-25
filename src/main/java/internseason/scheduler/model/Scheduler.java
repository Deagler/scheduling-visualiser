package internseason.scheduler.model;

import internseason.scheduler.algorithm.BBScheduleInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Scheduler {

    private Map<String, Task> allTasks;

    public Scheduler(Graph graph) {
        allTasks = graph.getTasks();
    }

    public void addTask(Schedule schedule, Task task, int processorId) {

        int startTime = findNextAvailableTimeInProcessor(schedule, task, processorId);
        schedule.add(task, processorId, startTime);

    }

    /**
     * Given a task t, schedule it on a processor p such that t's start time is minimised
     */
    public void addWithLowestStartTime(Schedule schedule, Task task) {
        int earliest = Integer.MAX_VALUE;
        int targetProcessorId = 0;
        for (int processorId = 0; processorId < schedule.getNumOfProcessors(); processorId++) {
            int time = findNextAvailableTimeInProcessor(schedule,task, processorId);
            if (time < earliest) {
                earliest = time;
                targetProcessorId = processorId;
            }
        }

        this.addTask(schedule, task, targetProcessorId);
    }

    public int getEarliestStartTime(Schedule schedule, Task task){
        int earliest = Integer.MAX_VALUE;
        for (int processorId = 0; processorId < schedule.getNumOfProcessors(); processorId++) {
            int time = findNextAvailableTimeInProcessor(schedule, task, processorId);
            if (time < earliest) {
                earliest = time;
            }
        }
        return earliest;
    }



    private int findNextAvailableTimeInProcessor(Schedule schedule, Task task, int processorId) {
        List<String> parentTasks = task.getParentTasks();


        Map<Integer, Processor> processorIdMap = schedule.getProcessorIdMap();
        Map<String, Integer> taskIdProcessorMap = schedule.getTaskIdProcessorMap();

        int result = processorIdMap.get(processorId).getCost();

        for (String parentId: parentTasks) {


            Integer sourceProcess = taskIdProcessorMap.get(parentId);

            if (sourceProcess == processorId) {
                continue;
            }

            Processor process = processorIdMap.get(sourceProcess);
            Task parentTask = this.allTasks.get(parentId);
            int parentFinishTime = process.getTaskStartTime(parentId) + parentTask.getCost();
            int communicationCost = parentTask.getCostToChild(task);

            int newScheduleTime = parentFinishTime + communicationCost;

            result = Math.max(result, newScheduleTime);
        }

        return result;
    }

    public Schedule buildGreedySchedule(BBScheduleInfo scheduleInfo, Graph graph) {
        Schedule cloneSchedule = new Schedule(scheduleInfo.getSchedule());

        Set<String> freeList = scheduleInfo.getFreeTasks();
        int counter = 0;
        while (freeList.size() > 0) {

            Set<String> temp = new HashSet<>();
            for (String taskId : freeList) {
                Task task = graph.getTask(taskId);
                this.addWithLowestStartTime(cloneSchedule, graph.getTask(taskId));
                for (String childId : task.getChildrenList()) {
                    Task child = graph.getTask(childId);
                    if (cloneSchedule.isTaskFree(child)) {
                        temp.add(child.getId());
                    }
                }
            }
            freeList = temp;
            counter++;
        }
        return cloneSchedule;
    }



    //finishing time of parent task + edge cost from parent to task
    public int calculateDRT(Schedule schedule, Task task) {

        int min = Integer.MAX_VALUE;

        Map<Integer, Processor> processorIdMap = schedule.getProcessorIdMap();
        Map<String, Integer> taskIdProcessorMap = schedule.getTaskIdProcessorMap();

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
                    max = finTime + communicationCost;
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
    public int calculateDRTSingle(Schedule schedule, Task task) {
        //if no parent return 0
        if (task.getNumberOfParents() == 0) {
            return 0;
        }

        //should only have 1 parent
        if (task.getNumberOfParents() == 1) {
            String parentTaskId = task.getParentTasks().get(0);
            Task parent = allTasks.get(parentTaskId);

            //finish time of parent
            int finTime = schedule.getTaskStartTime(parent) + parent.getCost();
            int cost = parent.getCostToChild(task);

            return finTime + cost;

        }

        return -1;
    }

    public Task getTask(String id) {
        return allTasks.get(id);
    }
}
