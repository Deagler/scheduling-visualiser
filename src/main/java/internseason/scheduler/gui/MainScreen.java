package internseason.scheduler.gui;

import internseason.scheduler.Main;
import internseason.scheduler.algorithm.SystemInformation;
import internseason.scheduler.input.Config;
import internseason.scheduler.input.DOTParser;
import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.model.Graph;
import internseason.scheduler.model.Processor;
import internseason.scheduler.model.Schedule;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.javafx.FxGraphRenderer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainScreen implements Initializable {
    private SingleGraph input_graph;
    private SingleGraph schedule_graph;
    private File graph_path;
    private SystemInformation sysInfo;

    private long startTime;
    private Timer timer;
    HashMap<String, String> parentMap;
    private Config config;
    private Service<Pair<Schedule, Graph>> algorithmService;


    @FXML
    private Pane input_graph_pane;
    @FXML
    private Pane schedule_graph_pane;
    @FXML
    private Pane optimalSchedule;

    @FXML
    private Label loaded_graph_label;
    @FXML
    private Label runtime;
    @FXML
    private Label cores_for_execution;

    @FXML
    private Label available_processors;
    @FXML
    private Label max_memory;
    @FXML
    private Label used_memory;
    @FXML
    private Label free_memory;

    @FXML
    private Label schedules_explored;
    @FXML
    private Label schedules_in_queue;
    @FXML
    private Label schedules_in_array;


    public MainScreen(Config config) {
        this.config = config;

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        input_graph = new SingleGraph("IG");
        schedule_graph = new SingleGraph("SG");
        parentMap = new HashMap<>();

        setup_labels(String.valueOf(config.getNumberOfCores()), String.valueOf(config.getNumberOfProcessors()), "24 gb");
        loadInputGraph(this.config.getInputDotFile());
        this.loaded_graph_label.setText(this.config.getInputDotFile());
        initialiseScheduleGraph();
        this.sysInfo = new SystemInformation();
        this.bindLabel(sysInfo.schedulesQueuedProperty(), schedules_in_queue);
        this.bindLabel(sysInfo.schedulesExploredProperty(), schedules_explored);
        System.out.println("initialised");
        sysInfo.addListener(this::buildScheduleGraph);

    }

    private void resetVisualisedGraphs(String inputDotFile) {

    }


    private void bindLabel(IntegerProperty systemProperty, Label label) {
        systemProperty.addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> label.setText(String.valueOf(newVal)));
        });
    }

    public void setup_labels(String cores, String processors, String max_mem) {
        runtime.setText("00:00:000 (s)");

        cores_for_execution.setText(cores);
        available_processors.setText(processors);

        max_memory.setText(max_mem);
        used_memory.setText("0 MB");
        free_memory.setText(max_mem);

        schedules_explored.setText("0 K");
        schedules_in_queue.setText("0 K");
        schedules_in_array.setText("0 K");
    }

    public void updateOptimalSchedule(Schedule schedule, Graph graph){
        final NumberAxis xAxis = new NumberAxis();
        final CategoryAxis yAxis = new CategoryAxis();

        final ScheduleVisulisation<Number,String> chart = new ScheduleVisulisation<Number,String>(xAxis,yAxis);
        xAxis.setLabel("");
        xAxis.setTickLabelFill(Color.CHOCOLATE);
        xAxis.setMinorTickCount(4);

        Map<Integer, Processor> processorMap = schedule.getProcessorIdMap();
        String[] processors = new String[processorMap.size()];
        int count = 0;
        for (Map.Entry<Integer,Processor> entry: processorMap.entrySet()){
            Integer proc = entry.getKey() +1;
            processors[count] = "CPU " + (proc).toString();
            count +=1;
        }

        yAxis.setLabel("");
        yAxis.setTickLabelFill(Color.CHOCOLATE);
        yAxis.setTickLabelGap(10);
        yAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(processors)));

//        chart.setTitle("Process Visualisation");
        chart.setLegendVisible(false);
        chart.setBlockHeight( 20);
        String machine;

        count = 0;
        for (Map.Entry<Integer, Processor> entry: processorMap.entrySet()){
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

        Platform.runLater(()->{
            chart.setMaxSize(454,219);
            optimalSchedule.getChildren().add(chart);
        });
    }

    private void loadInputGraph(String path) {
        DOTParser parser = new DOTParser();
        try {
            FxViewer v = new FxViewer(input_graph, FxViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
            internseason.scheduler.model.Graph graph = parser.parse(path);
            GraphAdapter graphAdapter = new GraphAdapter(graph, input_graph, "test");
            input_graph = graphAdapter.getFrontEndGraph();
            //sample_generator_2(input_graph);

            input_graph.setAttribute("ui.antialias");
            input_graph.setAttribute("ui.quality");
            v.enableAutoLayout();
            FxViewPanel panel = (FxViewPanel) v.addDefaultView(false, new FxGraphRenderer());
            panel.setMaxSize(456, 219);

            input_graph_pane.getChildren().add(panel);

        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    private void initialiseScheduleGraph() {
        FxViewer v = new FxViewer(schedule_graph, FxViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);


        schedule_graph.setAttribute("ui.antialias");
        schedule_graph.setAttribute("ui.quality");
        schedule_graph.setAttribute("ui.stylesheet", "url('internseason/scheduler/gui/stylesheets/graph_css.css')");

        v.enableAutoLayout();

        FxViewPanel panel = (FxViewPanel) v.addDefaultView(false, new FxGraphRenderer());
        panel.setMaxSize(456, 219);
        panel.setCenterShape(true);
        schedule_graph_pane.getChildren().add(panel);
    }


    private void buildScheduleGraph(Integer scheduleHashCode, Set<Integer> children) {

        Platform.runLater(() -> {
            String node = String.valueOf(scheduleHashCode);
            String parentNode = parentMap.get(node);
            System.out.println(parentNode + " : " + node + " :" + children);

            schedule_graph.addNode(node);

            if (parentNode != null) {
                schedule_graph.addEdge(parentNode + node, parentNode, node);
            } else {
                schedule_graph.getNode(node).setAttribute("ui.class", "root");
            }


            for (Integer n : children) {
                parentMap.put(String.valueOf(n), node);
            }
        });


    }

    private void startTimer() {
        startTime = System.currentTimeMillis();
        timer = new Timer();


        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                long durationInMillis = System.currentTimeMillis() - startTime;
                long millis = durationInMillis % 1000;
                long second = (durationInMillis / 1000) % 60;
                long minute = (durationInMillis / (1000 * 60)) % 60;
                long hour = (durationInMillis / (1000 * 60 * 60)) % 24;
                Platform.runLater(() -> runtime.setText(String.format("%02d:%02d:%02d.%d", hour, minute, second, millis)));
            }
        }, 0, 5);
    }

    private void stopTimer() {
        this.timer.cancel();
    }

    @FXML
    public void playButtonPressed() {

        startTimer();
        algorithmService = new Service<Pair<Schedule, Graph>>() {

            @Override
            protected Task<Pair<Schedule, Graph>> createTask() {
                return new Task<Pair<Schedule, Graph>>() {
                    @Override
                    protected Pair<Schedule, Graph> call() throws IOException, InterruptedException {
                        return Main.startAlgorithm(config, sysInfo);
                    }
                };
            }
        };


        algorithmService.setOnSucceeded((e) -> {
            System.out.println("Algorithm Finished");
            Pair<Schedule, Graph> results = (Pair<Schedule, Graph>) e.getSource().getValue();
            Schedule optimal = results.getKey();
            Graph graph = results.getValue();

            String optimalNode = String.valueOf(optimal.hashCode());
            String optimalNodeParent = parentMap.get(optimalNode);

            schedule_graph.addNode(optimalNode).setAttribute("ui.class", "root");
            schedule_graph
                    .addEdge(optimalNodeParent + optimalNode, optimalNodeParent, optimalNode);
            while (optimalNodeParent != null) {
                schedule_graph
                        .getEdge(optimalNodeParent + optimalNode)
                        .setAttribute("ui.class", "visited");
                optimalNode = optimalNodeParent;
                optimalNodeParent = parentMap.get(optimalNodeParent);
            }

            updateOptimalSchedule(optimal, graph);


            this.stopTimer();
        });

        algorithmService.setOnFailed((t) -> {
            System.out.println("Algorithm Failed");
            t.getSource().getException().printStackTrace();
            this.stopTimer();
        });

        algorithmService.start();
    }

    @FXML
    public void stopButtonPressed() {


    }

    @FXML
    public void settingsButtonPressed() {


    }

    @FXML
    public void loadButtonPressed() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Graph Resource File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File file = fileChooser.showOpenDialog(input_graph_pane.getScene().getWindow());
        if (file != null) {
            graph_path = file;
            loaded_graph_label.setText(file.toString());
            config.setInputDotFile(file.toString());
            input_graph = new SingleGraph(graph_path.toString());
            schedule_graph = new SingleGraph("SG");

            loadInputGraph(graph_path.toString());
        }
    }

}

