package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

/**
 * Created by Gustavo on 7/6/14.
 */
public class MSButton extends Button implements ChangeListener<MSController.Tile> {

    final private MSController controller;
    final private int row, col;
    MSController.Tile observed;

    public MSButton(MSController.Tile observed, int row, int col, MSController controller) {
        this.setMaxHeight(Double.MAX_VALUE);
        this.setMaxWidth(Double.MAX_VALUE);
        this.observed = observed;
        observed.addListener(this);
        this.setText("  ");
        this.controller = controller;
        this.row = row;
        this.col = col;
        this.setOnMouseClicked((mouse) -> { //LAMBDAS <3
                    if(mouse.getButton() == MouseButton.PRIMARY) {
                        controller.choose(row, col);
                    } else if (mouse.getButton() == MouseButton.SECONDARY) {
                        controller.flag(row, col);
                    }
                }
        );
        this.setStyle("-fx-background-color: deepskyblue;" +
                "      -fx-border-color: darkblue");
        //this.setTooltip(new Tooltip(observed.isBomb()?"Bomb":"Not a bomb"));
    }

    private void update() {
        if(observed.isOpen()) {
            if(!observed.isBomb()) {
                int bombNeighbors = observed.getBombNeighbors();
                if(!(bombNeighbors == 0)) {
                    switch(bombNeighbors) {
                        case 1: this.setText(""+bombNeighbors); this.textFillProperty().setValue(Color.RED);    break;
                        case 2: this.setText(""+bombNeighbors); this.textFillProperty().setValue(Color.ORANGE); break;
                        case 3: this.setText(""+bombNeighbors); this.textFillProperty().setValue(Color.YELLOW); break;
                        case 4: this.setText(""+bombNeighbors); this.textFillProperty().setValue(Color.GREEN);  break;
                        case 5: this.setText(""+bombNeighbors); this.textFillProperty().setValue(Color.BLACK);  break;
                        case 6: this.setText(""+bombNeighbors); this.textFillProperty().setValue(Color.PINK);   break;
                        case 7: this.setText(""+bombNeighbors); this.textFillProperty().setValue(Color.WHITE);  break;
                        case 8: this.setText(""+bombNeighbors); this.textFillProperty().setValue(Color.PURPLE); break;
                    }

                } else {
                    this.setStyle("-fx-background-color: white;");
                    this.setDisabled(true);
                }
            } else {
                this.setText("B");
            }
        } else if(observed.isFlagged()) {
            this.setText("F"); this.setStyle("-fx-background-color: yellow;");
            this.textFillProperty().setValue(Color.BLACK);
        } else {
            this.setText(" ");
            this.setStyle("-fx-background-color: deepskyblue;" +
                    "      -fx-border-color: darkblue");
        }
    }

    @Override
    public void changed(ObservableValue<? extends MSController.Tile> observableValue,
                        MSController.Tile tile,
                        MSController.Tile tile2) {
        update();
    }
}
