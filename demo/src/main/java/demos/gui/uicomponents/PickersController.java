package demos.gui.uicomponents;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXDatePicker;
import io.datafx.controller.ViewController;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@ViewController(value = "/fxml/ui/Pickers.fxml", title = "Material Design Example")
public class PickersController {

    @FXML
    private StackPane root;
    @FXML
    private JFXDatePicker dateOverlay;
    @FXML
    private JFXColorPicker colorPicker;

    @PostConstruct
    public void init() {
        dateOverlay.setDialogParent(root);
    }
}
