package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Gustavo on 7/6/14.
 */
public class MSController {

    private Tile[][] tileGrid;
    public final int rows, columns, bombs;
    private int bombsFlagged;
    private int tilesFlagged;
    private int tilesOpened;
    private boolean bombOpened;
    private final StringProperty gameStatus;
    private final StringProperty bombsFlaggedProperty;
    private boolean gameLost;

    /**
     * Initializes a minesweeper game with row, columns and a number of bombs.
     * @param rows Rows
     * @param columns Columns
     * @param bombs Bombs
     * @param gameStatusDisplay StringProperty hooks up to a JavaFX GUI to display game Status
     */
    public MSController(int rows, int columns, int bombs, StringProperty gameStatusDisplay, StringProperty bombsFlagged) {
        this.rows = rows;
        this.columns = columns;
        this.bombs = bombs;
        initTiles();
        generateGrid();
        calcBombNeighbors();
        this.bombsFlaggedProperty = bombsFlagged;
        gameStatus = gameStatusDisplay;
        gameStatus.setValue("Playing");
        bombsFlaggedProperty.setValue("Bombs flagged: 0" + "/"+bombs);
    }

    /**
     * Helper method for accessing tiles.
     * @param row Tile row
     * @param col Tile col
     * @return Tile at a coordinate.
     */
    public Tile tileAt(int row, int col) {
        return tileGrid[rClamp(row)][cClamp(col)];
    }

    /**
     * Game is won when this evaluates to true.
     * ((bombsFlagged == bombs) &&  (tilesFlagged == bombs)) && (tilesOpened == ((rows*columns)-bombs));
     * All of the Grid has been uncovered properly.
     * @return True if game is won.
     */
    public boolean gameWon() {
        return ((bombsFlagged == bombs) && (tilesFlagged == bombs)) && (tilesOpened == ((rows*columns)-bombs));
    }

    /**
     * Player loses if he opens a Tile with a bomb.
     * @return True if he opened a Tile with a bomb.
     */
    public boolean gameLost() {
        return bombOpened;
    }

    /**
     * Counts the number of flagged tiles around a tile
     * @param row Tile row
     * @param col Tile col
     * @return Count of neighbor flagged tiles.
     */
    private int flagsAround(int row, int col) {
        int count = 0;
        for(Tile n : neighbors(row,col)) {
            if(n.isFlagged()) {
                count++;
            }
        }
        return count;
    }

    private void moveBomb(Tile selection) {
        selection.isBomb = false;
        Random r = new Random();
        for (int i = 0; i < tileGrid.length; i++) {
            for (int j = 0; j < tileGrid[i].length; j++) {
                if(r.nextDouble() < 0.5 && !tileGrid[i][j].isBomb) {
                    tileGrid[i][j].isBomb = true;
                    calcBombNeighbors();
                    return;
                }
            }
        }
    }
    /**
     * A tile is chosen when a player clicks on the grid. Could cause a loss.
     * @param row Tile row
     * @param col Tile col
     */
    public void choose(int row, int col) {
        if(gameLost) return;
        Tile selection = tileGrid[row][col];
        if(!selection.isFlagged() && selection.isBomb()) {
            if(tilesOpened == 0) {
                System.out.println("Bomb moved");
                moveBomb(selection);
            } else {
                selection.open();
                bombOpened = true; //lost the game
                gameLost = true;
            }

        } else { //if you're opening a normal one
            if(!selection.isOpen()) {
                recurChoose(row, col);
            } else {
                openSelected(row, col);
            }

        }
        //gameStatus.setValue("BF: "+bombsFlagged+" TO: "+tilesOpened+" TF: "+tilesFlagged);
        if(gameLost()) {
            gameStatus.setValue("You lost!");
        }
        if(gameWon()) {
            gameStatus.setValue("You win!");
        }
    }

    /**
     * Open a tile that has already been opened.
     *
     * @param row tile row
     * @param col tile col
     */
    private void openSelected(int row, int col) {
        Tile selection = tileGrid[row][col];
        if (flagsAround(row, col) == selection.getBombNeighbors()) { //tile open and clicked
            for(Tile n : neighbors(row, col)) {
                if(!n.isFlagged() && !n.isOpen()) {
                    recurChoose(n);
                }
            }
        }
    }

    /**
     * Toggles the flag state of a tile.
     *
     * @param row Tile row
     * @param col Tile col
     */
    public void flag(int row, int col) {
        if(gameLost) return;
        Tile selection = tileGrid[row][col];
        if(selection.isFlagged()) {
            tilesFlagged--;
            if(selection.isBomb()){
                bombsFlagged--;
            }
        } else {
            tilesFlagged++;
            if(selection.isBomb()) {
                bombsFlagged++;
            }
        }
        selection.flag();
        bombsFlaggedProperty.setValue("Bombs flagged: "+bombsFlagged + "/"+bombs);
        if(gameWon()) {
            gameStatus.setValue("You win!");
        }
    }

    /**
     * Recursively chooses tiles around the initial tile if it has no bomb neighbors.
     * The recursion stops when a tile has bombs around it.
     * @param row Tile row
     * @param col Tile col
     */
    private void recurChoose(int row, int col) {
        row = rClamp(row);
        col = cClamp(col);
        Tile selection = tileGrid[row][col];
        if(!selection.isBomb() && !selection.isOpen()) {
            selection.open();
            tilesOpened++;
            if(!hasNeighboringBombs(row, col)) {
                recurChoose(row - 1, col - 1); recurChoose(row - 1, col); recurChoose(row - 1, col + 1);
                recurChoose(row,     col - 1);                            recurChoose(row,     col + 1);
                recurChoose(row + 1, col - 1); recurChoose(row + 1, col); recurChoose(row + 1, col + 1);
            }
        }
    }

    /**
     * Helper method that delegates to the other neighbors(int, int)
     * @param t tile
     * @return ArrayList of neighboring tiles. Handles corners and edges.
     */
    private ArrayList<Tile> neighbors(Tile t) {
        return neighbors(t.row, t.col);
    }

    /**
     * Helper method that delegates to the other recurChoose(Tile)
     * @param selection Tile selected
     */
    private void recurChoose(Tile selection) {
        recurChoose(selection.row, selection.col);
    }

    /**
     * Clamps a row index to 0 <= r <= (rows in the grid)
     * @param n number
     * @return clamped number
     */
    private int rClamp(int n) {
        return Math.min((Math.max(n, 0)), rows-1);
    }

    /**
     * Clamps a column index to 0 <= c <= (columns in teh grid)
     * @param n number
     * @return clamped number
     */
    private int cClamp(int n) {
        return Math.min((Math.max(n, 0)), columns-1);
    }

    /**
     * Detects if a Tile has any bombs in the immediate grid around it.
     * @param row Tile row
     * @param col Tile col
     * @return True if bombs are present. False if not.
     */
    private boolean hasNeighboringBombs(int row, int col) {
        boolean bombs = false;
        for(Tile t : neighbors(row, col)) {
            bombs = bombs || t.isBomb();
        }
        return bombs;
    }

    /**
     * Helper method that creates an ArrayList of the surrounding tiles of a given tile.
     * Handles edges and corners.
     * @param row Tile row
     * @param col Tile col
     * @return ArrayList of neighboring tiles
     */
    private ArrayList<Tile> neighbors(int row, int col) {
        ArrayList<Tile> neighbors = new ArrayList<>();
        for (int r = row - 1; r <= row+1 ; r++) {
            for (int c = col-1; c <= col+1; c++) {
                if(r == row && c == col) continue;
                try{
                    neighbors.add(tileGrid[r][c]);
                } catch (ArrayIndexOutOfBoundsException a) {}
            }
        }
        //System.out.println(neighbors.size());
        return neighbors;
    }

    /**
     * Counts the number of bombs around a Tile
     * @param row Tile row
     * @param col Tile col
     * @return Number of bombs around this tile
     */
    private int countNeighboringBombs(int row, int col) {
        int count = 0;
        for(Tile t : neighbors(row, col)) {
            count += t.isBomb() ? 1 : 0;
        }
        return count;
    }

    /**
     * Initializes all the Tile objects in the grid. None are bombs by default.
     */
    private void initTiles() {
        tileGrid = new Tile[rows][columns];
        for (int row = 0; row < tileGrid.length; row++) {
            for (int col = 0; col < tileGrid[row].length; col++) {
                tileGrid[row][col] = new Tile(false, row, col);
            }
        }
    }
    /**
     * Fills the grid with the correct amount of bombs
     */
    private void generateGrid() {
        Random rand = new Random();
        float bombProb = ((float)bombs)/(columns*rows);
        int bombsPlaced = 0;
        while(bombsPlaced < bombs) {
            for (int row = 0; row < tileGrid.length; row++) {
                for (int col = 0; col < tileGrid[row].length; col++) {
                    if (!tileGrid[row][col].isBomb() && (rand.nextFloat() < bombProb) && (bombsPlaced < bombs)) {
                        tileGrid[row][col].isBomb = true;
                        bombsPlaced++;
                        //System.out.printf("(%d, %d) ", row, col);
                        //System.out.println(bombsPlaced);
                    }
                }
            }
        }
    }

    /**
     * Each tile knows how many bombs surround it.
     */
    private void calcBombNeighbors() {
        for (int row = 0; row < tileGrid.length; row++) {
            for (int col = 0; col < tileGrid[row].length; col++) {
                if(!tileGrid[row][col].isBomb()) {
                    tileGrid[row][col].setBombNeighbors(countNeighboringBombs(row, col));
                }
            }
        }
    }


    /**
     * Represents an individual Tile in a Minesweeper game.
     * Contains information about its state as the game is played.
     */
    public class Tile implements ObservableValue<Tile> {

        private final BooleanProperty isFlagged = new SimpleBooleanProperty(false);
        private final BooleanProperty open = new SimpleBooleanProperty(false);
        private boolean isBomb;
        private ChangeListener<? super Tile> changeListener;
        private int bombNeighbors = -1;
        private final int row;
        private final int col;

        /**
         * Creates a tile at a position and declares if bomb or not.
         * @param isBomb
         * @param row
         * @param col
         */
        public Tile(boolean isBomb, int row, int col) {
            this.row = row;
            this.col = col;
        }

        /**
         * Tile may only be opened once. That is, opening it multiple times has no effect. Tile makes
         * no guarantee if its a bomb or not.
         */
        public void open() {
            if(!isFlagged.getValue()) {
                open.setValue(true); //don't open if flagged
                changeListener.changed(this, null, this);
            }
        }

        /**
         * Since the tile doesn't know about its surroundings, this much be set by the controller.
         * @param n Bomb neighbors.
         */
        public void setBombNeighbors(int n) {
            bombNeighbors = n;
        }

        /**
         * Bomb neighbors.
         * @return Number of bombs around this tile.
         */
        public int getBombNeighbors() {
            return bombNeighbors;
        }

        /**
         * Toggles the flagged state of this Tile.
         */
        public void flag() {
            if(!open.getValue()) {
                isFlagged.setValue(!isFlagged.getValue());
                changeListener.changed(this, null, this);
            }
        }

        public boolean isBomb() {
            return isBomb;
        }

        public boolean isFlagged() {
            return isFlagged.getValue();
        }

        public boolean isOpen() {
            return open.getValue();
        }
        @Override
        public Tile getValue() {
            return this;
        }

        @Override
        /**
         * Registers a ChangeListener for displaying changes to this tile in a GUI.
         */
        public void addListener(ChangeListener<? super Tile> changeListener) {
            this.changeListener = changeListener;
        }

        @Override
        public void removeListener(ChangeListener<? super Tile> changeListener) {}

        @Override
        public void addListener(InvalidationListener invalidationListener) {}

        @Override
        public void removeListener(InvalidationListener invalidationListener) {}
    }
}
