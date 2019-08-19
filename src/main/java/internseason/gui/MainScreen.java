package internseason.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.javafx.FxGraphRenderer;

import java.net.URL;
import java.util.ResourceBundle;

public class MainScreen implements Initializable {
    private SingleGraph input_graph;
    private  SingleGraph schedule_graph;


    @FXML private Pane pane_basic;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        input_graph = new SingleGraph("IG");
        schedule_graph = new SingleGraph("SG");
        load_input_graph();
    }

    public void add_to_graph(){



//        input_graph.addNode("0" );
//        input_graph.addNode("1" );
//        input_graph.addNode("2" );
//        input_graph.addNode("3" );
//        input_graph.addNode("4" );
//        input_graph.addNode("5" );
//        input_graph.addNode("6" );
//        input_graph.addEdge("01", "0", "1");
//        input_graph.addEdge("02", "0", "2");
//        input_graph.addEdge("03", "0", "3");
//        input_graph.addEdge("14", "1", "4");
//        input_graph.addEdge("15", "1", "5");
//        input_graph.addEdge("16", "1", "6");
    }


    private void load_input_graph() {
        FxViewer v = new FxViewer(input_graph, FxViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);

        DorogovtsevMendesGenerator gen = new DorogovtsevMendesGenerator();
        gen.addSink(input_graph);
        gen.begin();
        for (int i = 0; i < 100; i++)
            gen.nextEvents();
        gen.end();
        gen.removeSink(input_graph);


        input_graph.setAttribute("ui.antialias");
        input_graph.setAttribute("ui.quality");
        v.enableAutoLayout();
        FxViewPanel panel = (FxViewPanel) v.addDefaultView(false, new FxGraphRenderer());
        panel.setMaxSize(448, 249);

        pane_basic.getChildren().add(panel);
    }
}
