package internseason.scheduler.heuristic;

import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Schedule;
import internseason.scheduler.model.Scheduler;
import internseason.scheduler.model.Task;

import java.util.Comparator;

public class FTOComparator implements Comparator<Task> {

    private Schedule schedule;
    //private Graph graph;
    private Scheduler scheduler;

    public FTOComparator(Schedule schedule, Graph graph) {
        this.schedule = schedule;
        //this.graph = graph;
        this.scheduler = new Scheduler(graph);
    }

    @Override
    public int compare(Task t1, Task t2) {

        if (this.scheduler.calculateDRTSingle(schedule, t1) < this.scheduler.calculateDRTSingle(schedule, t2)) {
            return -1;
        }

        if (this.scheduler.calculateDRTSingle(schedule, t1) > this.scheduler.calculateDRTSingle(schedule, t2)) {
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

        int t1costToChildTask = t1.getCostToChild(this.scheduler.getTask(t1.getChildrenList().get(0)));
        int t2costToChildTask = t2.getCostToChild(this.scheduler.getTask(t2.getChildrenList().get(0)));

//        int t1costToChildTask = t1.getCostToChild(graph.getTask(t1.getChildrenList().get(0)));
//        int t2costToChildTask = t2.getCostToChild(graph.getTask(t2.getChildrenList().get(0)));

        if (t1costToChildTask > t2costToChildTask) {
            return -1;
        }

        if (t1costToChildTask < t2costToChildTask) {
            return 1;
        }
        return 0;
    }
}
