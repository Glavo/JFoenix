package demos.components;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ListViewDemo extends Application {

    private static final String ITEM = "Item ";
    private int counter = 0;

    @Override
    public void start(Stage stage) throws Exception {

        JFXListView<Node> list = new JFXListView<>();
        for (int i = 0; i < 4; i++) {
            list.getItems().add(new Label(ITEM + i));
        }
        JFXListView<Label> sublist = new JFXListView<>();
        for (int i = 0; i < 4; i++) {
            sublist.getItems().add(new Label(ITEM + i));
        }
        sublist.getStyleClass().add("mylistview");
        list.getItems().add(sublist);
        list.getStyleClass().add("mylistview");
        list.setSelectionModel(new MultipleSelectionModel<Node>() {

            @Override
            public ObservableList<Integer> getSelectedIndices() {
                return FXCollections.emptyObservableList();
            }

            @Override
            public ObservableList<Node> getSelectedItems() {
                return FXCollections.emptyObservableList();
            }

            @Override
            public void selectIndices(int index, int... indices) {

            }

            @Override
            public void selectAll() {

            }

            @Override
            public void clearAndSelect(int index) {

            }

            @Override
            public void select(int index) {

            }

            @Override
            public void select(Node obj) {

            }

            @Override
            public void clearSelection(int index) {

            }

            @Override
            public void clearSelection() {

            }

            @Override
            public boolean isSelected(int index) {
                return false;
            }

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public void selectPrevious() {

            }

            @Override
            public void selectNext() {

            }

            @Override
            public void selectFirst() {

            }

            @Override
            public void selectLast() {

            }
        });

        ListView<Node> javaList = new ListView<>();
        for (int i = 0; i < 4; i++) {
            javaList.getItems().add(new Label(ITEM + i));
        }
        ListView<Label> javaSublist = new ListView<>();
        for (int i = 0; i < 4; i++) {
            javaSublist.getItems().add(new Label(ITEM + i));
        }
        javaList.getItems().add(javaSublist);

        FlowPane pane = new FlowPane();
        pane.setStyle("-fx-background-color:WHITE");

        JFXButton button3D = new JFXButton("3D");
        button3D.setOnMouseClicked(e -> list.depthProperty().set(++counter % 2));

        JFXButton buttonExpand = new JFXButton("EXPAND");
        buttonExpand.setOnMouseClicked(e -> {
            list.depthProperty().set(1);
            list.setExpanded(true);
        });

        JFXButton buttonCollapse = new JFXButton("COLLAPSE");
        buttonCollapse.setOnMouseClicked(e -> {
            list.depthProperty().set(1);
            list.setExpanded(false);
        });

        pane.getChildren().add(button3D);
        pane.getChildren().add(buttonExpand);
        pane.getChildren().add(buttonCollapse);

        /*AnchorPane listsPane = new AnchorPane();
        listsPane.getChildren().add(list);
        AnchorPane.setLeftAnchor(list, 20.0);
        listsPane.getChildren().add(javaList);
        AnchorPane.setLeftAnchor(javaList, 300.0);*/
        VBox stackPane = new VBox();
        stackPane.getChildren().add(list);

        VBox box = new VBox();
        box.getChildren().add(pane);
        box.getChildren().add(stackPane);
        box.setSpacing(40);

        StackPane main = new StackPane();
        main.getChildren().add(box);
        main.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        StackPane.setMargin(pane, new Insets(20, 0, 0, 20));

        final Scene scene = new Scene(main, 600, 600, Color.WHITE);
        stage.setTitle("JFX ListView Demo ");
        scene.getStylesheets().add(ListViewDemo.class.getResource("/css/jfoenix-components.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
