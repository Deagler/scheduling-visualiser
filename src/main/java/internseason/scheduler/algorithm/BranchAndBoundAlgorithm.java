package internseason.scheduler.algorithm;

import com.google.common.collect.MinMaxPriorityQueue;
import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Schedule;
import internseason.scheduler.model.Scheduler;
import internseason.scheduler.model.Task;

import java.util.*;
import java.util.stream.Collectors;

public class BranchAndBoundAlgorithm extends BaseAlgorithm {
    Graph graph;
    private int bestUpperBound;
    private Scheduler scheduler;
    int counter = 0;

    @Override
    public Schedule execute(Graph graph, int numberOfProcessors, int numberOfCores, SystemInformation systemInformation) {
        this.graph = graph;
        this.scheduler = new Scheduler(graph);
        bestUpperBound = Integer.MAX_VALUE;

        MinMaxPriorityQueue<BBScheduleInfo> queue = MinMaxPriorityQueue.orderedBy(new Comparator<BBScheduleInfo>() {
            @Override
            public int compare(BBScheduleInfo o1, BBScheduleInfo o2) {
                return o1.lowerBound - o2.lowerBound;
            }
        }).create();


        Schedule rootSchedule = new Schedule(numberOfProcessors);
        Set<String> freeList = new HashSet<>();

        // build free list in one pass
        for (Task task : graph.getTasks().values()) {
            if (rootSchedule.isTaskFree(task)) {
                freeList.add(task.getId());
            }
        }

        BBScheduleInfo scheduleInfo = new BBScheduleInfo(
                rootSchedule,
                Integer.MIN_VALUE,
                Integer.MAX_VALUE,
                freeList
        );



        queue.add(scheduleInfo);

        while(!queue.isEmpty()){
            BBScheduleInfo currentSchedule = queue.poll();
            if (currentSchedule.getLowerBound() == currentSchedule.getUpperBound() && currentSchedule.freeTasks.isEmpty()){
                return currentSchedule.getSchedule();
            }

            List<BBScheduleInfo> candSchedules = generateNewPartialSchedules(currentSchedule);
            List<BBScheduleInfo> finalSchedules = new ArrayList<>();
            //compute LB and UB
            for (BBScheduleInfo schedule : candSchedules){
                int upperBound = this.scheduler.buildGreedySchedule(schedule,graph).getCost();
                int lowerBound = this.calculateLowerBound(schedule);
                schedule.setUpperBound(upperBound);
                schedule.setLowerBound(lowerBound);
                if (upperBound < bestUpperBound){
                    bestUpperBound = upperBound;
                    while (queue.peekLast() != null && queue.peekLast().getLowerBound() > bestUpperBound){
                        queue.pollLast();
                    }
                }
                if (lowerBound <= upperBound){
                    finalSchedules.add(schedule);
                }
            }
            counter+=finalSchedules.size();
            queue.addAll(finalSchedules);

        }

        return null;
    }

    private int calculateLowerBound(BBScheduleInfo scheduleInfo) {
        Set<String> freelist = scheduleInfo.freeTasks;
        Schedule schedule = scheduleInfo.getSchedule();
        int maxPath = Integer.MIN_VALUE;
        for (String taskId : freelist){
            Task task = this.graph.getTask(taskId);
            int cPath = this.scheduler.getEarliestStartTime(schedule, task) + task.getBottomLevel();
            maxPath = Math.max(cPath, maxPath);
        }
        if (maxPath < schedule.getCost()){
            List<Task> unscheduled = new ArrayList<>(this.graph.getTasks().values());
            // remove all scheduled tasks
            unscheduled.removeAll(schedule.getTasks().stream().map(id -> this.graph.getTask(id)).collect(Collectors.toList()));
            int sum = 0;
            for (Task task : unscheduled){
                sum += task.getCost();
            }
            maxPath = sum/schedule.getNumOfProcessors()+schedule.getCost();
        }
        return maxPath;
    }

    public List<BBScheduleInfo> generateNewPartialSchedules(BBScheduleInfo scheduleInfo) {
        Schedule schedule = scheduleInfo.getSchedule();
        Set<String> freeList = scheduleInfo.getFreeTasks();
        List<BBScheduleInfo> out = new ArrayList<>();

        for (String taskId : freeList) {
            Task task = this.graph.getTask(taskId);

            for (int processId = 0; processId < schedule.getNumOfProcessors(); processId++) {
                Schedule newSchedule = new Schedule(schedule);
                this.scheduler.addTask(newSchedule, task, processId);

                Set<String> expandedFreeList = new HashSet<>();

                // get new free nodes
                for (String childId : task.getChildrenList()) {
                    Task child = this.graph.getTask(childId);
                    if (newSchedule.isTaskFree(child)) {
                        expandedFreeList.add(child.getId());
                    }
                }

                List<String> newFreeList = freeList.stream().filter(id -> !id.equals(taskId)).collect(Collectors.toList());

                for (String id : newFreeList) {
                    expandedFreeList.add(id);
                }
                BBScheduleInfo info = new BBScheduleInfo(newSchedule, scheduleInfo.lowerBound, scheduleInfo.upperBound, expandedFreeList);
                out.add(info);

            }
        }
        return out;
    }

    @Override
    public String toString() {
        return null;
    }

}
