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
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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

    private Integer LARGE_PANE_WIDTH = 454;
    private Integer LARGE_PANE_HEIGHT = 219;

    private SingleGraph input_graph;
    private SingleGraph schedule_graph;
    private File graph_path;
    private SystemInformation sysInfo;

    private long startTime;
    private Timer timer;
    HashMap<String, String> parentMap;
    private Config config;
    private Service<Pair<Schedule, Graph>> algorithmService;

    private Integer branchesChecked;
    private BarChart<String,Integer> barChart;
    private Integer numberOfBars;
    private String selectedTheme = "Sky Blue";
    private String cssPath = "internseason/scheduler/gui/stylesheets/SkyBlue.css";
    private XYChart.Series series ;

    @FXML
    private Label branchesCheckedLabel;

    @FXML
    private Pane input_graph_pane;
    @FXML
    private Pane schedule_graph_pane;
    @FXML
    private Pane optimalSchedule;

    @FXML
    private Label optimalScheduleCost;
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
    private BarChart<String,Long> performanceGraph;

    @FXML
    private Pane performancePane;

    @FXML
    private Button loadButton;


    @FXML
    private Button playButton;

    @FXML
    private Button settingsButton;



    public MainScreen(Config config) {
        this.config = config;
    }

    public void setCSS(String selectedTheme, String path){
        Parent root = null;
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
            loader.setController(new MainScreen(Main.config));
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
        MainScreen mainScreen = loader.getController();
        mainScreen.setCssPath(selectedTheme, path);
        root.getStylesheets().add(path);

        Scene currentScene = new Scene(root, 1280, 800);

        Stage window = (Stage) playButton.getScene().getWindow();
        window.setScene(currentScene);
        window.show();
    }

    public void setCssPath(String themeName, String cssPath) {
        selectedTheme = themeName;
        this.cssPath = cssPath;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        input_graph = new SingleGraph("IG");
        schedule_graph = new SingleGraph("SG");
        parentMap = new HashMap<>();
        graph_path = new File(config.getInputDotFile());

        String maxMem = (Runtime.getRuntime().maxMemory() / (1024 * 1024)) + " MB";
        setup_labels(String.valueOf(config.getNumberOfCores()), String.valueOf(config.getNumberOfProcessors()), maxMem);

        createBarChart();

        enableRuntimeTimeButtons();

        loadInputGraph(this.config.getInputDotFile());
        this.loaded_graph_label.setText(new File(this.config.getInputDotFile()).getName());
        initialiseScheduleGraph();
        this.sysInfo = new SystemInformation();
        this.bindLabel(sysInfo.schedulesQueuedProperty(), schedules_in_queue);
        this.bindLabel(sysInfo.schedulesExploredProperty(), schedules_explored);

        sysInfo.addListener(this::buildScheduleGraph);
    }

    private void resetScheduleGraph() {
        schedule_graph.clear();
        initialiseScheduleGraph();

    }
    private void addPerformance(String graphName, Integer runtime){

        if (numberOfBars >= 2){
            series.getData().clear();
            numberOfBars = 0;
        }
        numberOfBars +=1;
        series.getData().add(new XYChart.Data(graphName, runtime));
    }
    private void createBarChart(){
        numberOfBars = 0;
        performancePane.getChildren().clear();
        CategoryAxis xAxis    = new CategoryAxis();
        xAxis.setTickLabelFill(Color.CHOCOLATE);
        xAxis.setTickLabelGap(0);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Runtime (ms)");
        yAxis.setTickLabelFill(Color.CHOCOLATE);
        yAxis.setTickLabelGap(0);

        barChart = new BarChart(xAxis, yAxis);

        barChart.setAnimated(false);
        barChart.setLegendVisible(false);
        barChart.setMaxSize(LARGE_PANE_WIDTH,LARGE_PANE_HEIGHT);

        barChart.setMinSize(LARGE_PANE_HEIGHT,LARGE_PANE_HEIGHT);

        performancePane.getChildren().add(barChart);
        barChart.getData().clear();
        series = new XYChart.Series();
        barChart.getData().add(series);

    }

    private void bindLabel(IntegerProperty systemProperty, Label label) {
        systemProperty.addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> label.setText(String.valueOf(newVal)));
        });
    }

    public void updateMemoryLabels(){
        Runtime instance = Runtime.getRuntime();
        long maxMem = instance.maxMemory();
        long freeMem = instance.freeMemory();
        long usedMem = maxMem - freeMem;

        int toMb = 1024 * 1024;

        freeMem = freeMem / toMb;
        usedMem = usedMem / toMb;

        used_memory.setText(usedMem + " MB");
        free_memory.setText(freeMem + "MB");
    }

    public void setup_labels(String cores, String processors, String max_mem) {
        runtime.setText("00:00:00.00");

        cores_for_execution.setText(cores);
        available_processors.setText(processors);
        max_memory.setText(max_mem);

        schedules_explored.setText("0 K");
        schedules_in_queue.setText("0 K");
    }

    public void disableRuntimeButtons(){
        loadButton.setDisable(true);
        playButton.setDisable(true);
        settingsButton.setDisable(true);
    }

    public void enableRuntimeTimeButtons(){
        loadButton.setDisable(false);
        playButton.setDisable(false);
        settingsButton.setDisable(false);
    }


    public void updateOptimalSchedule(Schedule schedule, Graph graph){
        final NumberAxis xAxis = new NumberAxis();
        final CategoryAxis yAxis = new CategoryAxis();
        // setting up chart
        final ScheduleVisulisation<Number,String> chart = new ScheduleVisulisation<Number,String>(xAxis,yAxis);
        xAxis.setLabel("Time");
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

        yAxis.setTickLabelFill(Color.CHOCOLATE);
        yAxis.setTickLabelGap(0);
        yAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(processors)));


        chart.setLegendVisible(false);
        chart.setBlockHeight(10);
        // adding information to chart
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
            }
            count +=1;
            chart.getData().addAll(series);
        }
        chart.getStylesheets().add("internseason/scheduler/gui/stylesheets/ScheduleVisualisation.css");

        Platform.runLater(()->{
            optimalSchedule.getChildren().clear();
            chart.setMaxSize(LARGE_PANE_WIDTH,LARGE_PANE_HEIGHT);
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

            input_graph.setAttribute("ui.antialias");
            input_graph.setAttribute("ui.quality");
            v.enableAutoLayout();
            FxViewPanel panel = (FxViewPanel) v.addDefaultView(false, new FxGraphRenderer());
            panel.setMaxSize(LARGE_PANE_WIDTH, LARGE_PANE_HEIGHT);

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
        panel.setMaxSize(LARGE_PANE_WIDTH, LARGE_PANE_HEIGHT);
        panel.setCenterShape(true);
        schedule_graph_pane.getChildren().add(panel);
    }


    private void buildScheduleGraph(Integer scheduleHashCode, Set<Integer> children) {
        branchesChecked +=1;
        Platform.runLater(() -> {
            branchesCheckedLabel.setText(branchesChecked.toString());
            String node = String.valueOf(scheduleHashCode);
            String parentNode = parentMap.get(node);
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
                long millis = (durationInMillis % 1000)/ 10;
                long second = (durationInMillis / 1000) % 60;
                long minute = (durationInMillis / (1000 * 60)) % 60;
                long hour = (durationInMillis / (1000 * 60 * 60)) % 24;
                Platform.runLater(() -> {
                    runtime.setText(String.format("%02d:%02d:%02d.%d", hour, minute, second, millis));
                    updateMemoryLabels();
                });
            }
        }, 0, 5);
    }

    private void stopTimer() {
        this.timer.cancel();
    }

    @FXML
    public void playButtonPressed()        {

            startTimer();
            resetScheduleGraph();
            disableRuntimeButtons();
            branchesChecked = 0;
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

            Pair<Schedule, Graph> results = (Pair<Schedule, Graph>) e.getSource().getValue();
            Schedule optimal = results.getKey();
            Graph graph = results.getValue();

            String optimalNode = String.valueOf(optimal.hashCode());
            String optimalNodeParent = parentMap.get(optimalNode);

            schedule_graph.addNode(optimalNode).setAttribute("ui.class", "optimal");
            schedule_graph.addEdge(optimalNodeParent + optimalNode, optimalNodeParent, optimalNode);
            while (optimalNodeParent != null) {
                schedule_graph
                        .getEdge(optimalNodeParent.toString() + optimalNode.toString())
                        .setAttribute("ui.class", "visited");
                optimalNode = optimalNodeParent;
                optimalNodeParent = parentMap.get(optimalNodeParent);
            }

            optimalScheduleCost.setText(Integer.toString(optimal.getCost()));
            updateOptimalSchedule(optimal, graph);
            enableRuntimeTimeButtons();


            int durationInMillis = (int)(System.currentTimeMillis() - startTime);

            this.stopTimer();
            addPerformance(graph_path.getName(), durationInMillis);
        });

        algorithmService.setOnFailed((t) -> {

            t.getSource().getException().printStackTrace();
            this.stopTimer();
        });

        algorithmService.start();
    }


    @FXML
    public void settingsButtonPressed() {
        Parent root = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SettingsScreen.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage settingsStage = new Stage();
        settingsStage.setResizable(false);
        settingsStage.setAlwaysOnTop(true);
        settingsStage.setTitle("Settings");

        root.getStylesheets().clear();

        root.getStylesheets().add(cssPath);

        Scene scene = new Scene(root,600,400);

        settingsStage.setScene(scene);

        settingsStage.initStyle(StageStyle.UNDECORATED);
        settingsStage.show();

        SettingsScreen settingsScreen = loader.getController();

        Platform.runLater(()->settingsScreen.getGuiColor().setValue(selectedTheme));
        settingsScreen.setDefaultValues(config.getNumberOfCores(),config.getNumberOfProcessors());
        settingsScreen.setConfig(config);

        settingsStage.setOnCloseRequest(event ->{
            Platform.runLater(()->{
                cores_for_execution.setText(Integer.toString(config.getNumberOfCores()));
                available_processors.setText(Integer.toString(config.getNumberOfProcessors()));
                ComboBox guiColor = settingsScreen.getGuiColor();
                selectedTheme = (String) guiColor.getSelectionModel().getSelectedItem();
                if (selectedTheme != null) {
                    switch (selectedTheme) {
                        case "Apple Green":
                            cssPath = "internseason/scheduler/gui/stylesheets/AppleGreen.css";
                            break;
                        case "Midnight Purple":
                            cssPath = "internseason/scheduler/gui/stylesheets/MidnightPurple.css";
                            break;
                        case "Sky Blue":
                            cssPath = "internseason/scheduler/gui/stylesheets/SkyBlue.css";
                            break;
                        case "Ruby Red":
                            cssPath = "internseason/scheduler/gui/stylesheets/RubyRed.css";
                            break;
                        case "Lemon Yellow":
                            cssPath = "internseason/scheduler/gui/stylesheets/LemonYellow.css";
                            break;

                    }
                    setCSS(selectedTheme, cssPath);
                }

            });
        });

//        settingsScreen.getSaveButton().setOnAction(event ->{
//
//        });

    }

    @FXML
    public void loadButtonPressed() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Graph Resource File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File file = fileChooser.showOpenDialog(input_graph_pane.getScene().getWindow());
        if (file != null) {
            graph_path = file;

            loaded_graph_label.setText(file.getName());
            config.setInputDotFile(file.toString());
            resetScheduleGraph();
            input_graph.clear();
            loadInputGraph(graph_path.toString());
        }
    }

}

