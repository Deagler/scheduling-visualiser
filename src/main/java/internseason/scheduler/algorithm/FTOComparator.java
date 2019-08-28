package internseason.scheduler.algorithm;

import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Schedule;
import internseason.scheduler.model.Scheduler;
import internseason.scheduler.model.Task;

import java.util.Comparator;

/** Comparator used in the Astar algorithm to build the Fixed Task Ordering of tasks that qualify for FTO
 */
public class FTOComparator implements Comparator<Task> {

    private Schedule schedule;
    private Scheduler scheduler;

    public FTOComparator(Schedule schedule, Graph graph) {
        this.schedule = schedule;
        this.scheduler = new Scheduler(graph);
    }

    /** Compare method that the queue for FTO uses to sort by data ready time of each free task
     * @param t1
     * @param t2
     * @return
     */
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
        int t2costToChildTask = t2.getCostToChild(this.scheduler.getTask(t2.getChildrenList().get(0)));;

        if (t1costToChildTask > t2costToChildTask) {
            return -1;
        }

        if (t1costToChildTask < t2costToChildTask) {
            return 1;
        }
        return 0;
    }
}
