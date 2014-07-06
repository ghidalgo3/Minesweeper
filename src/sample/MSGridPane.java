package sample;

import javafx.scene.layout.GridPane;

/**
 * Created by Gustavo on 7/6/14.
 */
public class MSGridPane extends GridPane {

    public MSGridPane(MSController game) {

        for(int row = 0; row < game.rows; row++) {
            for(int col = 0; col < game.columns; col++) {
                MSButton button = new MSButton(game.tileAt(row,col), row, col, game);
                this.add(button, row, col);
            }
        }
    }

}
