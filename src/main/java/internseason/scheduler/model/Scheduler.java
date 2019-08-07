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
            System.out.println("added");
            scheduleList.add(schedule);
        } else {
            for (int i = start; i< graph.size(); i++) {
                System.out.println("i: " +i);
                //for every processor that can be scheduled
                for (int j = 0; j<schedule.numProcessors(); j++) {
                    System.out.println("j: " + j);
                    //can only add node if ALL its dependencies have been completed
                    if (schedule.getTasks().containsAll(graph.get(i).getDependencies())) {
                        System.out.println("contains dependencies");

                        //check if there will be a delay
                        if (schedule.isEmpty() || j == schedule.getLastProcessorId()) {
                            System.out.println("no delay");
                            schedule.add(graph.get(i), j);
                        } else {
                            System.out.println("delay");
                            schedule.addWithDelay(graph.get(i), j, schedule.getLastTask().getDelayTo(graph.get(i)));
                        }

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
