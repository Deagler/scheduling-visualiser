package internseason.scheduler.gui;


import internseason.scheduler.input.CLIException;
import internseason.scheduler.input.Config;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the settings screen
 */
public class SettingsScreen implements Initializable {

    Config config;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    HBox topBar;
    @FXML
    ComboBox guiColor;

    @FXML
    TextField coreNumber;

    @FXML
    TextField processorNumber;

    @FXML
    Button saveButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        topBarSetup();
        ObservableList themes = FXCollections.observableArrayList(
                "Sky Blue",
                "Apple Green",
                "Midnight Purple",
                "Ruby Red",
                "Lemon Yellow"
        );
        guiColor.getItems().addAll(themes);

        guiColor.setValue("Apple Green");

    }

    public ComboBox getGuiColor() {
        return guiColor;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public void setDefaultValues(Integer cores, Integer processors){
        Platform.runLater(()->{
            this.coreNumber.setText(cores.toString());
            this.processorNumber.setText(processors.toString());
        });
    }

    /**
     * fires an event called window close request which is listened to
     */
    public void onSaved(){
        String numCores = coreNumber.getText();
        String numProcessors = processorNumber.getText();

        try {
            config.setNumberOfCores(Integer.parseInt(numCores));
            config.setNumberOfProcessors(Integer.parseInt(numProcessors));

        } catch (CLIException e) {
            e.printStackTrace();
        }


        Stage stage = (Stage) coreNumber.getScene().getWindow();
        stage.fireEvent(
                new WindowEvent(
                        stage,
                        WindowEvent.WINDOW_CLOSE_REQUEST
                )
        );
        stage.close();

    }

    /**
     * sets up a draggable topbar
     */
    public void topBarSetup(){

        topBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });

        //move around here
        topBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = (Stage)topBar.getScene().getWindow();
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
    }

    /**
     * on cancel exit window
     */
    public void onCancel(){
        Stage stage = (Stage) coreNumber.getScene().getWindow();
        stage.close();
    }

    public Button getSaveButton() {
        return saveButton;
    }


}
