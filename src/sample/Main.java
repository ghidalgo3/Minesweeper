package sample;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    int rows = 10;
    int cols = 10;
    int bombs = 10;
    Scene mainScene;
    MSGridPane game;
    TextField columnsField = new TextField();
    TextField rowsField = new TextField();
    TextField bombsField = new TextField();
    StringProperty gameStatus = new SimpleStringProperty("");
    StringProperty bombsFlagged = new SimpleStringProperty("");
    Text status = new Text();
    Text bombsStatus = new Text();
    ScrollPane gameView;

    @Override
    public void start(Stage primaryStage) throws Exception{
        game = makeGame();
        VBox root = new VBox();
        gameView = new ScrollPane(game);
        VBox.setVgrow(gameView, Priority.ALWAYS);
        //gameView.setMinSize(100,100);
        status.textProperty().bind(gameStatus);
        bombsStatus.textProperty().bind(bombsFlagged);
        root.getChildren().addAll(controlButtons(), statusBar(), gameView);
        mainScene = new Scene(root);
        primaryStage.setTitle("Minesweeper");
        primaryStage.setScene(mainScene);
        root.setPrefSize(400,400);
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
        game = makeGame();
        gameView.setContent(game);
        
        //root.setPrefSize(game.getPrefWidth(), game.getPrefHeight());
        //stage.sizeToScene();
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
        Button editButton = new Button("Change");
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
