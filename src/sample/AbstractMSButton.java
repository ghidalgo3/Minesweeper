package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Labeled;

/**
 * Created by Gustavo on 7/7/14.
 */
public abstract class AbstractMSButton implements ChangeListener<MSController.Tile> {

    protected Labeled button;

    MSController.Tile observed;
    MSController controller;

    public AbstractMSButton(MSController.Tile observed, MSController controller) {
        this.controller = controller;
        this.observed = observed;
        observed.addListener(this);
    }

    public void flag() {
        if (observed.isFlagged()) {
            setFlaggedStyle();
        } else {
            setClosedStyle();
//            this.setText("");
//            this.setStyle("-fx-background-color: deepskyblue;" +
//                    "      -fx-border-color: darkblue");
        }
    }

    public void open() {
        if(!observed.isBomb()) {
            setOpenStyle();
        } else {
            setBombStyle();
        }
    }

    @Override
    public void changed(ObservableValue<? extends MSController.Tile> observed, MSController.Tile ignored, MSController.Tile tile) {
        if(tile.isOpen()) {
            open();
        } else {
            flag();
        }
    }

    public Labeled getButton() {
        return button;
    }

    protected abstract void setClosedStyle();
    protected abstract void setFlaggedStyle();
    protected abstract void setOpenStyle();
    protected abstract void setBombStyle();
    //protected abstract void setOnMouseClicked(MouseEvent event);

}
