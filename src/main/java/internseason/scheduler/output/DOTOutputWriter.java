package internseason.scheduler.output;

import internseason.scheduler.model.Processor;
import internseason.scheduler.model.Schedule;
import internseason.scheduler.model.Task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DOTOutputWriter {
    private BufferedWriter writer;
    private Schedule schedule;
    private Map<String, Task> taskMap;

    public void write(String fileName, Schedule finalSchedule, Map<String, Task> taskMap) {
        try {
            FileWriter fw = new FileWriter(fileName);
            this.writer = new BufferedWriter(fw);
            this.writer.write("digraph \"Optimal-Schedule-For-" + this.stripExtension(fileName) + "\" {");
            this.writer.newLine();

            this.schedule = finalSchedule;
            this.taskMap = taskMap;

            List<Task> tasks = this.schedule.getTasks();

            // nodes
            for (Task task : tasks) {
                int startTime = this.schedule.getTaskStartTime(task);
                int processorId = this.schedule.getProcessorIdForTask(task.getId());
                this.writeNode(task, startTime, processorId);

            }

            // edges
            for (Task child : tasks) {
                for (String parentId: child.getParentTasks()) {
                    Task parent = taskMap.get(parentId);
                    int cost = parent.getCostToChild(child);
                    this.writeEdge(parent, child, cost);
                }
            }


            this.writer.write("}");

            this.writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeNode(Task task, int startTime, int processorId) throws IOException {
        this.writer.write("\t" + task.getId() + "\t" + "[" + "Weight=" + task.getCost() + ", Processor=" + processorId + ", Start_time=" + startTime + "];");
        this.writer.newLine();
    }

    private void writeEdge(Task parent, Task child, int cost) throws IOException {
        this.writer.write("\t"+parent.getId()+" -> "+child.getId()+"\t"+"["+"Weight="+cost+"];");
        this.writer.newLine();
    }

    private String stripExtension(String fileName) {
        int lastIndexDot = fileName.lastIndexOf('.');
        return fileName.substring(0, lastIndexDot);
    }



}
