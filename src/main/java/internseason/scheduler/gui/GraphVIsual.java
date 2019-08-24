package internseason.scheduler.gui;
import internseason.scheduler.algorithm.SystemInformation;
import internseason.scheduler.input.DOTParser;
import internseason.scheduler.algorithm.AStarAlgorithm;
import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.model.Processor;
import internseason.scheduler.model.Schedule;
import javafx.util.Pair;

import java.util.Arrays;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class GraphVIsual extends Application {
    @Override public void start(Stage stage) {

        DOTParser parser = new DOTParser();
        internseason.scheduler.model.Graph graph = null;
        try {
            graph = parser.parse("src/test/resources/Test_Diamond.dot");
        } catch (InputException e) {
            e.printStackTrace();
        }
        AStarAlgorithm algorithm = new AStarAlgorithm();
        Schedule schedule = algorithm.execute(graph, 2,new SystemInformation());

        Map<Integer, Processor> processorMap = schedule.getProcessorIdMap();
        String[] processors = new String[processorMap.size()];
        int count = 0;
        for (Map.Entry<Integer,Processor> entry: processorMap.entrySet()){
            processors[count] = "Processor " + entry.getKey().toString();
            count +=1;
        }
        stage.setTitle("Processor map");



        final NumberAxis xAxis = new NumberAxis();
        final CategoryAxis yAxis = new CategoryAxis();

        final ScheduleVisulisation<Number,String> chart = new ScheduleVisulisation<Number,String>(xAxis,yAxis);
        xAxis.setLabel("");
        xAxis.setTickLabelFill(Color.CHOCOLATE);
        xAxis.setMinorTickCount(4);

        yAxis.setLabel("");
        yAxis.setTickLabelFill(Color.CHOCOLATE);
        yAxis.setTickLabelGap(10);
        yAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(processors)));

        chart.setTitle("Process Visualisation");
        chart.setLegendVisible(false);
        chart.setBlockHeight(20);
        String machine;

        count = 0;
        for (Map.Entry<Integer,Processor> entry: processorMap.entrySet()){
            Integer processorNum = entry.getKey();
            Processor processor = entry.getValue();
            ArrayList<Pair<String, Integer>> pairs = processor.getTaskScheduleList();
            XYChart.Series series = new XYChart.Series();
            String proc = processors[count];
            for (Pair<String, Integer> pair : pairs){

                String taskID = pair.getKey();
                Integer startTime = pair.getValue();
                int cost = graph.getTask(taskID).getCost();
                XYChart.Data chartData = new XYChart.Data(startTime, proc, new ScheduleVisulisation.ExtraData(cost, "status-blue"));
                series.getData().add(chartData);
                System.out.println("cost:" + cost);
                System.out.println("start Time:" + startTime);
                System.out.println("Proc"+processors[count]);
            }
            count +=1;
            chart.getData().addAll(series);
        }
        chart.getStylesheets().add("internseason/scheduler/gui/stylesheets/ScheduleVisualisation.css");

        Scene scene  = new Scene(chart,620,350);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
