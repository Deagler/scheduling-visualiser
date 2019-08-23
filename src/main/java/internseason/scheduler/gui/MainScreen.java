package internseason.scheduler.gui;

import internseason.scheduler.Main;
import internseason.scheduler.algorithm.SystemInformation;
import internseason.scheduler.algorithm.event.AlgorithmEventListener;
import internseason.scheduler.input.Config;
import internseason.scheduler.input.DOTParser;
import internseason.scheduler.exceptions.InputException;
import internseason.scheduler.model.Schedule;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.javafx.FxGraphRenderer;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainScreen implements Initializable {
    private SingleGraph input_graph;
    private  SingleGraph schedule_graph;
    private File graph_path;
    private SystemInformation sysInfo;

    private long startTime;
    private Timer timer;
    HashMap<Integer, Integer> parentMap;
    private Config config;


    @FXML private Pane input_graph_pane;
    @FXML private Pane schedule_graph_pane;

    @FXML private Label loaded_graph_label;
    @FXML private Label runtime;
    @FXML private Label cores_for_execution;

    @FXML private Label available_processors;
    @FXML private Label max_memory;
    @FXML private Label used_memory;
    @FXML private Label free_memory;

    @FXML private Label schedules_explored;
    @FXML private Label schedules_in_queue;
    @FXML private Label schedules_in_array;


    public MainScreen(Config config) {
        this.config = config;
        this.sysInfo = new SystemInformation();

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        input_graph = new SingleGraph("IG");
        schedule_graph = new SingleGraph("SG");
        parentMap = new HashMap<>();

        setup_labels("4", "4", "24 gb");
        load_input_graph(this.config.getInputDotFile());
        this.loaded_graph_label.setText(this.config.getInputDotFile());

        this.bindLabel(sysInfo.schedulesQueuedProperty(), schedules_in_queue);
        this.bindLabel(sysInfo.schedulesExploredProperty(), schedules_explored);

        sysInfo.addListener(new AlgorithmEventListener() {
            @Override
            public void schedulesGenerated(Integer parentHashcode, List<Integer> childHashcodes) {
                buildScheduleGraph(parentHashcode, childHashcodes);
            }
        });

    }


    private void bindLabel(IntegerProperty systemProperty, Label label) {
        systemProperty.addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> label.setText(String.valueOf(newVal)));
        });
    }

    public void setup_labels(String cores, String processors, String max_mem){
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




    public void add_to_graph(){

    }

    private void sample_generator(Graph g){
        BarabasiAlbertGenerator gen = new BarabasiAlbertGenerator();
        gen.addSink(g);
        gen.begin();
        for (int i = 0; i < 1000; i++)
            gen.nextEvents();
        gen.end();
        gen.removeSink(g);

    }

    private void load_input_graph(String path) {
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

    private void load_schedule_graph() {
        FxViewer v = new FxViewer(schedule_graph, FxViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
//        sample_generator(schedule_graph);
//        schedule_graph.addNode("0" );
//        schedule_graph.addNode("1" );
//        schedule_graph.addNode("2" );
//        schedule_graph.addNode("3" );
//        schedule_graph.addNode("4" );
//        schedule_graph.addNode("5" );
//        schedule_graph.addNode("6" );
//        schedule_graph.addEdge("01", "0", "1");
//        schedule_graph.addEdge("02", "0", "2");
//        schedule_graph.addEdge("03", "0", "3");
//        schedule_graph.addEdge("14", "1", "4");
//        schedule_graph.addEdge("15", "1", "5");
//        schedule_graph.addEdge("16", "1", "6");


        schedule_graph.setAttribute("ui.antialias");
        schedule_graph.setAttribute("ui.quality");
        schedule_graph.setAttribute("ui.stylesheet", "url('internseason/scheduler/gui/stylesheets/graph_css.css')");

//        schedule_graph.getEdge("01").setAttribute("ui.class", "visited");
        v.enableAutoLayout();

        FxViewPanel panel = (FxViewPanel) v.addDefaultView(false, new FxGraphRenderer());
        //panel.getCamera().setViewPercent(0.3);
        panel.setMaxSize(456, 219);
        panel.setCenterShape(true);
        schedule_graph_pane.getChildren().add(panel);
    }

    private void buildScheduleGraph(Integer node, List<Integer> children){
        if (parentMap.containsKey(node)){
            Integer parentNode = parentMap.get(node);
            schedule_graph.getEdge(parentNode.toString() + node.toString()).setAttribute("ui.class", "visited");
        }else{
            schedule_graph.addNode(node.toString());
            schedule_graph.getNode(node.toString()).setAttribute("ui.class", "root");
        }

  
        for (Integer n :children) {
            parentMap.put(n, node);
            System.out.println(n);
            schedule_graph.addNode(n.toString());
            schedule_graph.addEdge(node.toString() + n.toString(), node.toString(), n.toString());
        }

    }

    private void startTimer(){
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
    public void playButtonPressed(){

        startTimer();
        Service<Schedule> service = new Service<Schedule>() {

            @Override
            protected Task<Schedule> createTask() {
                return new Task<Schedule>() {
                    @Override
                    protected Schedule call() throws IOException, InterruptedException {
                       return  Main.startAlgorithm(config, sysInfo);
                    }
                };
            }
        };


        service.setOnSucceeded((e) -> {
            System.out.println("Algorithm Finished");
            this.stopTimer();
        });

        service.setOnFailed((t) -> {
            System.out.println("Algorithm Failed");
            t.getSource().getException().printStackTrace();
            this.stopTimer();
        });

        service.start();
    }

    @FXML
    public void stopButtonPressed(){



    }

    @FXML
    public void settingsButtonPressed(){


    }

    @FXML
    public void loadButtonPressed()  {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Graph Resource File");
        File file = fileChooser.showOpenDialog(input_graph_pane.getScene().getWindow());
        if(file!=null) {
            graph_path = file;
            loaded_graph_label.setText(file.toString());
            config.setInputDotFile(file.toString());
            input_graph = new SingleGraph(graph_path.toString());

            load_input_graph(graph_path.toString());
        }
    }

}

