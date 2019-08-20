package internseason.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.javafx.FxGraphRenderer;

import java.net.URL;
import java.util.ResourceBundle;

public class MainScreen implements Initializable {
    private SingleGraph input_graph;
    private  SingleGraph schedule_graph;


    @FXML private Pane input_graph_pane;
    @FXML private Pane schedule_graph_pane;

    @FXML private Label runtime;
    @FXML private Label cores_for_execution;

    @FXML private Label available_processors;
    @FXML private Label max_memory;
    @FXML private Label used_memory;
    @FXML private Label free_memory;

    @FXML private Label schedules_explored;
    @FXML private Label schedules_in_queue;
    @FXML private Label schedules_in_array;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        input_graph = new SingleGraph("IG");
        schedule_graph = new SingleGraph("SG");
        setup_labels("4", "4", "24 gb");
        load_input_graph();
        load_schedule_graph();
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

    private void sample_generator_2(Graph g){
        BarabasiAlbertGenerator gen = new BarabasiAlbertGenerator();
        gen.addSink(g);
        gen.begin();
        for (int i = 0; i < 100; i++)
            gen.nextEvents();
        gen.end();
        gen.removeSink(g);

    }


    private void load_input_graph() {
        FxViewer v = new FxViewer(input_graph, FxViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        sample_generator_2(input_graph);

        input_graph.setAttribute("ui.antialias");
        input_graph.setAttribute("ui.quality");
        v.enableAutoLayout();
        FxViewPanel panel = (FxViewPanel) v.addDefaultView(false, new FxGraphRenderer());
        panel.setMaxSize(456, 219);

        input_graph_pane.getChildren().add(panel);
    }

    private void load_schedule_graph() {
        FxViewer v = new FxViewer(schedule_graph, FxViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        sample_generator(schedule_graph);

        schedule_graph.setAttribute("ui.antialias");
        schedule_graph.setAttribute("ui.quality");
        schedule_graph.setAttribute("ui.stylesheet", "url('internseason/gui/stylesheets/graph_css.css')");
        v.enableAutoLayout();

        FxViewPanel panel = (FxViewPanel) v.addDefaultView(false, new FxGraphRenderer());
        panel.getCamera().setViewPercent(0.3);
        panel.setMaxSize(456, 219);
        panel.setCenterShape(true);

        schedule_graph_pane.getChildren().add(panel);
    }

    @FXML
    public void playButtonPressed(){


    }

    @FXML
    public void stopButtonPressed(){


    }

    @FXML
    public void settingsButtonPressed(){


    }

    @FXML
    public void loadButtonPressed(){


    }

}
