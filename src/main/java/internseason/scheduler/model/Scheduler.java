package internseason.scheduler.model;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    List<Schedule> schedules = new ArrayList<Schedule>();

    public void createSchedules(Graph graph, int numOfProcessors) {

//        graph.sort();
        List<Schedule> scheduleList = new ArrayList<Schedule>();
//        Schedule schedule = new Schedule();
        backtrack(scheduleList, new Schedule(numOfProcessors), graph, 0);
        schedules = scheduleList;
    }

    public void backtrack(List<Schedule> scheduleList, Schedule schedule, Graph graph, int start) {

        if (/*schedule.size() or */start == graph.size()) {
            scheduleList.add(schedule);
        } else {
            for (int i = start; i< graph.size(); i++) {
                //for every processor that can be scheduled
                for (int j = 1; j<schedule.numProcessors(); j++) {
                    //TODO figure out if there will be a delay
                    //can only add node if ALL its dependencies have been completed
                    if (schedule.getTasks().containsAll(graph.get(i).getDependencies())) {
                        schedule.add(graph.get(i), j);
                        backtrack(scheduleList, schedule, graph, i+1);
                        schedule.removeLastTask(j);
                    }
//                    schedule.add(graph.get(i), j);
//                    backtrack(scheduleList, schedule, graph, i+1);
//                    schedule.removeLastTask(j);
                }
            }
        }
    }

    public Schedule findBestSchedule() {
        //TODO throw exception
        if (schedules.isEmpty()) {
            return null;
        }

        Schedule bestSchedule = schedules.get(0);

        for (int i=1; i<schedules.size(); i++) {
            if (schedules.get(i).getCost() < bestSchedule.getCost()) {
                bestSchedule = schedules.get(i);
            }
        }

        return bestSchedule;
    }
}
