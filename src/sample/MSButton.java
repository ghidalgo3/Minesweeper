package sample;

import javafx.scene.paint.Color;

/**
 * Created by Gustavo on 7/6/14.
 */
public class MSButton extends Abstract2DMSButton{



    public MSButton(MSController.Tile observed, MSController controller) {
        super(observed, controller);
        button.setMaxHeight(Double.MAX_VALUE);
        button.setMaxWidth(Double.MAX_VALUE);
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
            setClosedStyle();
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

    @Override
    protected void setPressedStyle() {
        button.setStyle("-fx-background-color: darkblue;");
    }
}
