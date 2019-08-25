package internseason.scheduler.algorithm;

import internseason.scheduler.model.Schedule;

import java.util.Set;

public class BBScheduleInfo {
    Schedule partialSchedule;
    int lowerBound;
    int upperBound;
    Set<String> freeTasks;

    BBScheduleInfo(Schedule partialSchedule, int lowerBound, int upperBound, Set<String> freeTasks) {
        this.partialSchedule = partialSchedule;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.freeTasks = freeTasks;
    }

    public Schedule getSchedule() {
        return partialSchedule;
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public int getUpperBound() {
        return this.upperBound;
    }

    public void setLowerBound(int lowerBound) {
        this.lowerBound = lowerBound;
    }

    public void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }

    public Set<String> getFreeTasks() {
        return this.freeTasks;
    }
}
