package sample;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    int rows = 10;
    int cols = 10;
    int bombs = 10;
    Scene mainScene;
    MSGridPane game;
    VBox layout = new VBox();
    TextField columnsField = new TextField();
    TextField rowsField = new TextField();
    TextField bombsField = new TextField();
    StringProperty gameStatus = new SimpleStringProperty("");
    StringProperty bombsFlagged = new SimpleStringProperty("");
    Text status = new Text();
    Text bombsStatus = new Text();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = makeGame();
        status.textProperty().bind(gameStatus);
        bombsStatus.textProperty().bind(bombsFlagged);
        layout.getChildren().addAll(controlButtons(), statusBar(), root);
        mainScene = new Scene(layout);
        primaryStage.setTitle("Minesweeper");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private HBox statusBar() {
        HBox bar = new HBox(status, bombsStatus);
        bar.setSpacing(10);
        return bar;
    }

    private MSGridPane makeGame() {
        return new MSGridPane(new MSController(rows, cols, bombs, gameStatus, bombsFlagged));
    }

    private void resetGame() {

        layout.getChildren().remove(2);
        game = makeGame();
        layout.getChildren().add(game);
    }

    private void edit() {
        try {
            rows = Integer.parseInt(rowsField.getText());
            cols = Integer.parseInt(columnsField.getText());
            bombs = Integer.parseInt(bombsField.getText());
        } catch (NumberFormatException l) {
            rows = 10; cols = 10; bombs = 10;
        }
        resetGame();
    }



    private HBox controlButtons() {
        Button resetButton = new Button("Reset");
        resetButton.setMaxWidth(Double.MAX_VALUE);
        Button editButton = new Button("Edit");
        editButton.setMaxWidth(Double.MAX_VALUE);
        columnsField.setMaxWidth(60);
        columnsField.promptTextProperty().setValue("columns");
        rowsField.setMaxWidth(60);
        rowsField.promptTextProperty().setValue("rows");
        bombsField.setMaxWidth(60);
        bombsField.promptTextProperty().setValue("bombs");
        HBox group = new HBox(resetButton, editButton, columnsField, rowsField, bombsField);
        resetButton.setOnMouseClicked((m) -> resetGame());
        editButton.setOnMouseClicked((m) -> edit());
        return group;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
