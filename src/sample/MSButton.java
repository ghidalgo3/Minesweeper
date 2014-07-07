package sample;

import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

/**
 * Created by Gustavo on 7/6/14.
 */
public class MSButton extends AbstractMSButton{


    
    public MSButton(MSController.Tile observed, MSController controller) {
        super(observed, controller);
        button = new Button();
        button.setMaxHeight(Double.MAX_VALUE);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnMouseClicked((mouse) -> { //LAMBDAS <3
                    if (mouse.getButton() == MouseButton.PRIMARY) {
                        controller.choose(observed.getRow(), observed.getCol());
                    } else if (mouse.getButton() == MouseButton.SECONDARY) {
                        controller.flag(observed.getRow(), observed.getCol());
                    }
                }
        );
        setClosedStyle();
    }

    @Override
    protected void setClosedStyle() {
        button.setText("  ");
        button.setStyle("-fx-background-color: deepskyblue;" +
                "      -fx-border-color: darkblue");
    }

    @Override
    protected void setFlaggedStyle() {
        button.setText("F"); button.setStyle("-fx-background-color: yellow;");
        button.textFillProperty().setValue(Color.BLACK);
    }

    @Override
    protected void setOpenStyle() {
        int bombNeighbors = observed.getBombNeighbors();
        if(!(bombNeighbors == 0)) {
            switch(bombNeighbors) {
                case 1: button.setText(""+bombNeighbors); button.textFillProperty().setValue(Color.RED);    break;
                case 2: button.setText(""+bombNeighbors); button.textFillProperty().setValue(Color.ORANGE); break;
                case 3: button.setText(""+bombNeighbors); button.textFillProperty().setValue(Color.YELLOW); break;
                case 4: button.setText(""+bombNeighbors); button.textFillProperty().setValue(Color.GREEN);  break;
                case 5: button.setText(""+bombNeighbors); button.textFillProperty().setValue(Color.BLACK);  break;
                case 6: button.setText(""+bombNeighbors); button.textFillProperty().setValue(Color.PINK);   break;
                case 7: button.setText(""+bombNeighbors); button.textFillProperty().setValue(Color.WHITE);  break;
                case 8: button.setText(""+bombNeighbors); button.textFillProperty().setValue(Color.PURPLE); break;
            }
        } else {
            button.setStyle("-fx-background-color: white;");
            button.setDisable(true);
        }
    }

    @Override
    protected void setBombStyle() {
        button.setText("B");
    }



}
