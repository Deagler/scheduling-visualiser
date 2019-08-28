package internseason.scheduler.algorithm;

import internseason.scheduler.model.Schedule;
import org.apache.commons.lang3.SerializationUtils;

import java.util.List;
import java.util.Objects;

/** Class stores information about a partial schedule that currently exists in the A star algorithm,
 *  contains a serialised version of a full schedule object, scheduleInfo objeccts are stored in the priority queue
 *  in our A star algorithm.
 */
public class AstarScheduleInfo {
        private byte[] serialisedSchedule;
        private Integer maxBottomLevel;
        private Integer totalCost;
        private Integer totalNumberOfTasks;
        private Integer hashCode;
        private Integer layer;
        private List<String> freeList;
        private Schedule schedule;

        public AstarScheduleInfo(Schedule schedule, Integer layer, List<String> freeList, int totalScheduleCost) {
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
            AstarScheduleInfo that = (AstarScheduleInfo) o;
            return Objects.equals(hashCode, that.hashCode);

        }

        @Override
        public int hashCode() {
            return hashCode;
        }
    }

