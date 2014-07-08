package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;

/**
 * Abstract class with most of the functionality of a minesweeper button.
 * Concrete subclasses must simply define the styles of the button based on the contents.
 * 
 * @author Gustavo Hidalgo
 */
public abstract class Abstract2DMSButton implements ChangeListener<MSController.Tile> {

    protected Button button = new Button("  ");

    MSController.Tile observed;
    MSController controller;

    public Abstract2DMSButton(MSController.Tile observed, MSController controller) {
        this.controller = controller;
        this.observed = observed;
        observed.addListener(this);
        button.setOnMousePressed((mouse) -> setPressedStyle());
        button.setOnMouseReleased((mouse) -> setOpenStyle());
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

    public void flag() {
        if (observed.isFlagged()) {
            setFlaggedStyle();
        } else {
            setClosedStyle();
        }
    }

    public void open() {
        if(!observed.isBomb()) {
            setPressedStyle();
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

    public Node getButton() {
        return button;
    }

    protected abstract void setClosedStyle();
    protected abstract void setFlaggedStyle();
    protected abstract void setOpenStyle();
    protected abstract void setBombStyle();
    protected abstract void setPressedStyle();

    
}
