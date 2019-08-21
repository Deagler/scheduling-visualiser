package internseason.scheduler.algorithm;

import com.google.gson.Gson;
import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Processor;
import internseason.scheduler.model.Schedule;
import internseason.scheduler.model.Task;
import org.apache.commons.lang3.SerializationUtils;

import java.util.*;
import java.util.stream.Collectors;



/**
 * Temporary Data-structure to store a schedule and the given topological layer the internseason.scheduler.algorithm should attempt to generate
 * new schedules from.
 */
class ScheduleInfo {
    private byte[] serialisedSchedule;
    private Integer maxBottomLevel;
    private Integer totalCost;
    private Integer totalNumberOfTasks;
    private Integer hashCode;
    private Integer layer;
    private List<String> freeList;
    private Schedule schedule;

    public ScheduleInfo(Schedule schedule, Integer layer, List<String> freeList, int totalScheduleCost) {
        this.serialisedSchedule = SerializationUtils.serialize(schedule);
        this.maxBottomLevel = schedule.getMaxBottomLevel();
        this.hashCode = schedule.hashCode();
        this.totalCost = totalScheduleCost;
        this.totalNumberOfTasks = schedule.getNumberOfTasks();
        this.layer = layer;
        this.freeList = freeList;
        this.schedule = null;
    }

    public Integer getTotalNumberOfTasks() {
        return totalNumberOfTasks;
    }

    public Schedule getSchedule() {
        if (this.schedule == null) {
            this.schedule = (Schedule)SerializationUtils.deserialize(serialisedSchedule);
        }

        return this.schedule;
    }

    public Integer getLayer() {
        return layer;
    }

    public void incrementLayer() {
        this.layer++;
    }

    public Integer getTotalCost() {
        return totalCost;
    }

    public List<String> getFreeList() {
        return freeList;
    }


    @Override
    public String toString() {
        return "ScheduleInfo{" +
                ", layer=" + layer +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleInfo that = (ScheduleInfo) o;
        return Objects.equals(hashCode, that.hashCode);

    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}

/**
 * PoC AStar Algorithm that uses Topological layering and
 * a heuristic that factors in the cost of the schedule and number of tasks.
 *
 * Possible Schedules are generated layer-by-layer as to avoid generating schedules that have dependency violations.
 */
public class AStarAlgorithm extends BaseAlgorithm {
    private Queue<ScheduleInfo> scheduleQueue;
    private int totalTaskTime;
    private Graph graph;

    /**
     * Tepmorary constructor to test factory pattern
     * @return
     */
    public AStarAlgorithm() {
        super();
        scheduleQueue = new PriorityQueue<>(new AStarHeuristic());
    }


    /**
     * Basic Implementation of the AStar Algorithm with duplicate detection and process normalisation.
     * Reference: https://researchspace.auckland.ac.nz/handle/2292/30213
     * @return An optimal schedule
     */
    @Override
    public Schedule execute(Graph graph, int numberOfProcessors) {
        int totalTasks = graph.getTasks().size();
        totalTaskTime = 0;
        for (Task task: graph.getTasks().values()){
            totalTaskTime +=task.getCost();
        }

        this.graph = graph;

        List<List<Task>> topologicalTasks = graph.getTopologicalOrdering();

        Schedule initialSchedule = new Schedule(numberOfProcessors, graph.getTasks());

        scheduleQueue.add(new ScheduleInfo(initialSchedule, 0, new ArrayList<String>(), 0)); // Add the empty schedule to the queue.
        // Calculates the Bottom Level for each task.
        List<Task> leafs = graph.getTasks().values() //find all the leaf nodes
                .stream()
                .filter((Task task) -> task.getNumberOfChildren() == 0)
                .collect(Collectors.toList());


        for (Task leaf : leafs) { //Compute the bottom levels for the nodes
            leaf.setBottomLevel(leaf.getCost());
            getBottomLevels(graph.buildTaskListFromIds(leaf.getParentTasks()), leaf.getCost());
        }

        Task maxTask = null;
        for (Task task : graph.getTasks().values()) {
           if (maxTask == null) {
               maxTask = task;
           }

           if (task.getBottomLevel() > maxTask.getBottomLevel()) {
               maxTask = task;
           }
        }


        Set<Integer> visited = new HashSet<>();
        visited.add(initialSchedule.hashCode());
        int counter = 0;

        boolean knownFTO = false; //variable that tells us if we need to call isFTO()

        while (!scheduleQueue.isEmpty()) {
            //ScheduleInfo head = scheduleQueue.poll();
            ScheduleInfo head = scheduleQueue.peek();
            Schedule realSchedule = head.getSchedule();
            scheduleQueue.remove();
            // Return the optimal schedule (First complete schedule, orchestrated by AStar Heuristic)
            if (realSchedule.getNumberOfTasks() == totalTasks) {
                System.out.println(counter);
                return realSchedule;
            }


            List<Task> currentLayer = topologicalTasks.get(head.getLayer());
            Queue<Task> FTOList = null;

            if (!knownFTO) {
                FTOList = isFTO(head, currentLayer);
                knownFTO = true;
            }
            List<ScheduleInfo> combinations;

            if (FTOList != null) {
                //TODO generateFTOCombinations to use the queue?
                combinations = generateFTOCombinations(head, topologicalTasks.get(head.getLayer()), numberOfProcessors);

//                //Queue<Task> ftoTasks =  sortFTOTasks(FTOList, schedule);
//                boolean same = true;
//                while (same) {
//                    //Task head = ftoTasks.peek();
//                    ftoTasks.remove();
//                    List<ScheduleInfo> ftoCombinations = generateFTOCombinations(head, FTOList, numberOfProcessors);
//
//                    List<Task> originalFreeList = getMergedFreeList(scheduleinfo.getSchedule(), currentLayer, scheduleinfo.getFreeList());
//                    //compare ftotasks with new free list
//
//                    //if same
//                    scheduleQueue.remove();
//                }
//
//                generateCombinations();
            } else {
                // Extending the polled schedule to generate all possible "next" states.
                combinations = generateAllCombinations(head, topologicalTasks.get(head.getLayer()), numberOfProcessors);
            }


            if (combinations == null) { // Move to next topological layer if no possible schedules on current layer.
                head.incrementLayer();
                //scheduleQueue.
                currentLayer = topologicalTasks.get(head.getLayer());
                FTOList = isFTO(head, currentLayer);
                if (FTOList != null) {
                    combinations = generateFTOCombinations(head, topologicalTasks.get(head.getLayer()), numberOfProcessors);
                } else {
                    combinations = generateAllCombinations(head, topologicalTasks.get(head.getLayer()), numberOfProcessors);
                }
                //combinations = generateCombinations(head, topologicalTasks.get(head.getLayer()), numberOfProcessors, realSchedule);
            }

            for (ScheduleInfo possibleCombination : combinations) {

                if (visited.contains(possibleCombination.hashCode())) {
                    continue;
                }
                scheduleQueue.add(possibleCombination);
                visited.add(possibleCombination.hashCode());
            }

            //if was in FTO
            if (FTOList != null) {
                //if next schedule in queue has the same freelist
                List<String> nextFreeIdList = scheduleQueue.peek().getFreeList();
                List<Task> nextFreeList = getMergedFreeList(scheduleQueue.peek().getSchedule(), currentLayer, nextFreeIdList);
                FTOList.remove();

                if (!(nextFreeList.containsAll(FTOList) && FTOList.containsAll(nextFreeList))) {
                    knownFTO = false;
                    continue;
                }

//                HashSet<Task> set = new HashSet<>();
//                set.addAll(nextFreeList);
//
//                for (Task t: FTOList) {
//                    if (!set.contains(t)) {
//                        //NEED TO CHECK FTO
//                        knownFTO = false;
//                        counter++;
//                        continue;
//                    } else {
//                        set.remove(t);
//                    }
//                }
//
//                if (set.size() != 0) {
//                    knownFTO = false;
//                }
            }

            knownFTO = false;




            counter++;
        }

        return null;
    }

    private int calculateIdleHeuristic(Schedule schedule) {

        return (totalTaskTime + schedule.getIdleTime()-1) / schedule.getNumOfProcessors();
    }


    private int calculateDRTHeuristic(Schedule schedule, List<Task> freeTasks){
        int maxDRT = Integer.MIN_VALUE;
        for (Task task : freeTasks){
            int drt = schedule.calculateDRT(task);
            int bottomLevel = task.getBottomLevel();
            maxDRT = Math.max(maxDRT, (drt+bottomLevel));
        }
        return maxDRT;
    }

    private Integer calculateCost(Schedule schedule, List<Task> freeTasks) {
        //return calculateDRTHeuristic(schedule,freeTasks);
        //return schedule.getMaxBottomLevel();
        //return Math.max(schedule.getMaxBottomLevel(), calculateIdleHeuristic(schedule));
        //return Math.max(schedule.getMaxBottomLevel(), calculateDRTHeuristic(schedule,freeTasks));
        return Math.max(schedule.getMaxBottomLevel(), calculateIdleHeuristic(schedule));

    }


    private List<Task> getMergedFreeList(Schedule schedule, List<Task> layer, List<String> extraNodes) {
        List<Task> freeNodes = new ArrayList<>(layer);
        freeNodes.addAll(this.graph.buildTaskListFromIds(extraNodes));

        for (int i = layer.size() - 1; i >= 0; i--) {
            Task task = layer.get(i);
            if (schedule.isTaskAssigned(task.getId())) {
                freeNodes.remove(i);
            }
        }

        return freeNodes;
    }

    /**
     *
     * @param info
     * @param currentLayer
     * @return
     *      the list of all freenodes if it is in FTO,
     *      else returns null
     */
    private Queue<Task> isFTO(ScheduleInfo info, List<Task> currentLayer) {
        String commonChildId =  "";
        Integer commonParentProcessorId = null;

        List<Task> freeNodes = new ArrayList<>(currentLayer);
        freeNodes.addAll(this.graph.buildTaskListFromIds(info.getFreeList()));

        // check how many parents and childrens task has
        for (Task task : freeNodes) {
            if (task.getNumberOfParents() > 1 || task.getNumberOfChildren() > 1){
                return null;
            }

            if (task.getNumberOfChildren() == 1) {
                for (String childId : task.getChildrenList()) {
                    if (commonChildId.isEmpty()) {
                        commonChildId = childId;
                    } else {
                        if (!commonChildId.equals(childId)) {
                            return null;
                        }
                    }
                }
            }

            if (task.getNumberOfParents() == 1) {
                Schedule s = info.getSchedule();
                for (String parentId : task.getParentTasks()) {

                    int parentProcessorId = s.getProcessorIdForTask(parentId);
                    if (commonParentProcessorId == null) {
                        commonParentProcessorId = parentProcessorId;
                    } else {
                        if (commonParentProcessorId != parentProcessorId) {
                            return null;
                        }
                    }
                }
            }
        }

        return sortFTOTasks(freeNodes, info.getSchedule());
        //return freeNodes;
    }

//    private List<ScheduleInfo> generateFTOCombinations(ScheduleInfo scheduleinfo, List<Task> currentLayer) {
//
//    }

    private List<ScheduleInfo> generateAllCombinations(ScheduleInfo scheduleinfo, List<Task> currentLayer, int numberOfProcessors) {
        Schedule schedule = scheduleinfo.getSchedule();

        currentLayer = new ArrayList<>(currentLayer);
        for (int i = currentLayer.size() - 1; i >= 0; i--) {
            Task task = currentLayer.get(i);
            if (schedule.isTaskAssigned(task.getId())) {
                currentLayer.remove(i);
            }
        }


        if (currentLayer.size() == 0) {
            return null;
        }

        List<ScheduleInfo> out = new ArrayList<>();
        for (int i = 0; i < currentLayer.size(); i++) {
            Task node = currentLayer.get(i);

            for (int processId = 0; processId < numberOfProcessors; processId++) {
                Schedule newSchedule = new Schedule(schedule, graph.getTasks());
                newSchedule.add(node, processId);

                List<String> expandedFreeNodeIds = new ArrayList<>();
                List<Task> expandedFreeNodes = new ArrayList<>();

                for (String childId : node.getChildrenList()) {
                    Task child = this.graph.getTask(childId);
                    boolean isTaskFree = true;
                    for (String parentId : child.getParentTasks()) {
                        if (!schedule.isTaskAssigned(parentId)) {
                            isTaskFree = false;
                            break;
                        }
                    }
                    if (isTaskFree) {
                        expandedFreeNodeIds.add(childId);
                        expandedFreeNodes.add(child);
                    }
                }

                out.add(new ScheduleInfo(newSchedule, scheduleinfo.getLayer(), expandedFreeNodeIds, calculateCost(newSchedule, expandedFreeNodes)));
            }
        }

        return out;
    }

    private List<ScheduleInfo> generateFTOCombinations(ScheduleInfo scheduleInfo, List<Task> ftoList, int numberOfProcesses) {
        //given a schedule, ftolist and processor schedule top fto task to all processors
        Schedule schedule = scheduleInfo.getSchedule();

        List<ScheduleInfo> out = new ArrayList<>();
        for (int processId=0;processId< numberOfProcesses;processId++){
            Task head = ftoList.get(0);
            Schedule newSchedule = new Schedule(schedule, graph.getTasks());
            newSchedule.add(head, processId);

            List<String> expandedFreeNodeIds = new ArrayList<>();
            List<Task> expandedFreeNodes = new ArrayList<>();

            for (String childId : head.getChildrenList()) {
                Task child = this.graph.getTask(childId);
                boolean isTaskFree = true;
                for (String parentId : child.getParentTasks()) {
                    if (!schedule.isTaskAssigned(parentId)) {
                        isTaskFree = false;
                        break;
                    }
                }
                if (isTaskFree) {
                    expandedFreeNodeIds.add(childId);
                    expandedFreeNodes.add(child);
                }
            }

            out.add(new ScheduleInfo(newSchedule, scheduleInfo.getLayer(), expandedFreeNodeIds, calculateCost(newSchedule, expandedFreeNodes)));
        }
        ftoList.remove(0);
        return out;
    }


    private Queue<Task> sortFTOTasks(List<Task> tasks, Schedule schedule) {
        PriorityQueue<Task> result = new PriorityQueue<Task>(new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                Map<String, Task> tasks = graph.getTasks();
                if (schedule.calculateDRTSingle(t1) < schedule.calculateDRTSingle(t2)) {
                    return -1;
                }

                if (schedule.calculateDRTSingle(t1) > schedule.calculateDRTSingle(t2)) {
                    return 1;
                }

                //tie
                //sort by DESCENDING outgoing edge cost
                if (t1.getNumberOfChildren() == 0) {
                    return 1;
                }

                if (t2.getNumberOfChildren() == 0) {
                    return -1;
                }

                int t1costToChildTask = t1.getCostToChild(graph.getTask(t1.getChildrenList().get(0)));
                int t2costToChildTask = t2.getCostToChild(graph.getTask(t2.getChildrenList().get(0)));

                if (t1costToChildTask > t2costToChildTask) {
                    return -1;
                }

                if (t1costToChildTask < t2costToChildTask) {
                    return 1;
                }
                return 0;
            }
        });

        result.addAll(tasks);

        if (verifySortedFTOList(result)) {
            return result;
        } else {
            return null;
        }
    }




    private void getBottomLevels(List<Task> tasks, int currentBottomLevel) {
        for (Task node : tasks) {
            if (node.getCost() < currentBottomLevel + node.getCost()) {
                node.setBottomLevel(currentBottomLevel + node.getCost());
            }
            if (!node.getParentTasks().isEmpty()) {
                getBottomLevels(graph.buildTaskListFromIds(node.getParentTasks()),
                        node.getBottomLevel());
            }
        }
    }

    private boolean verifySortedFTOList(PriorityQueue<Task> ftoList) {

        Queue<Task> tempList = new PriorityQueue<Task>(ftoList.comparator());
        tempList.addAll(ftoList);

        int outCost = Integer.MIN_VALUE;

        while (!tempList.isEmpty()) {
            Task t = tempList.poll();
            int costToChildTask = t.getCostToChild(graph.getTask(t.getChildrenList().get(0)));

            if (costToChildTask >= outCost) {
                outCost = costToChildTask;
            } else {
                return false;
            }
        }

        return true;
    }

    /**
     * Heuristic that orders schedules in ascending order of cost. (Lowest cost first)
     * If costs are equal then the schedule with a higher number of tasks assigned comes first.
     */
    private class AStarHeuristic implements Comparator<ScheduleInfo> {

        @Override
        public int compare(ScheduleInfo o1Info, ScheduleInfo o2Info) {
            return o1Info.getTotalCost().compareTo(o2Info.getTotalCost());
        }

    }

    @Override
    public String toString() {
        return "A Star Algorithm";
    }
}
