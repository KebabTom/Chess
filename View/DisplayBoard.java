
package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import Controller.*;

/*
An 8*8 Gridlayout JPanel, filled with Tile objects to graphically represent the game state
*/

public class DisplayBoard extends JPanel {

    private int TILE_SIZE;
	private boolean WHITE = true;
	private boolean BLACK = false;

	private ChessController controller;

	private Tile[][] board = new Tile[8][8];
	private GridLayout chessGrid = new GridLayout(8,8);

    public DisplayBoard(ChessController c, int tileSize) {

    	controller = c;
        TILE_SIZE = tileSize;
    	initialSetup();
    }

    private void initialSetup() {

    	this.setLayout(chessGrid);
        this.addTilesToBoard(controller.getWhiteTileColor(), controller.getBlackTileColor());
    }

    // fills the panel with Tile objects of the passed arguments for white and black color spaces
    private void addTilesToBoard(Color whiteTileColor, Color blackTileColor) {
    	boolean color = BLACK;
    	for(int i = 0; i < 8; i++) {
    		for(int j = 0; j < 8; j++) {
                if(color == WHITE) {
                    board[i][j] = new Tile(controller, whiteTileColor, i, j, TILE_SIZE);
                } else {
                    board[i][j] = new Tile(controller, blackTileColor, i, j, TILE_SIZE);
                }
    			this.add(board[i][j]);
    			color = !color;
    		}
    		color = !color;
    	}
    }

    // updates the background colours of the Tile objects in the board
    public void updateTileColors(Color whiteTileColor, Color blackTileColor) {
    	boolean color = BLACK;
    	for(int i = 0; i < 8; i++) {
    		for(int j = 0; j < 8; j++) {
                if(color == WHITE) {
                    board[i][j].updateBackgroundColor(whiteTileColor);
                } else {
                    board[i][j].updateBackgroundColor(blackTileColor);
                }
    			color = !color;
    		}
    		color = !color;
    	}
    }

    // adding and removing piece icons
    public void addPieceToBoard(int row, int col, ImageIcon pieceIcon) {
    	board[row][col].addPiece(pieceIcon);

    }
    public void removePieceFromBoard(int row, int col) {
    	board[row][col].removePiece();
    }

    // mark and unmark the selected piece Tile (at the start and end of moves respectively)
    public void markMoveFrom(Integer[] coords) {
        board[coords[0]][coords[1]].mark();
    }
    public void unmarkMoveFrom(Integer[] coords) {
        board[coords[0]][coords[1]].unmark();
    }

    // highlight all the Tiles on the board according to the passed ArrayList
    public void highlightAvailableMoves(ArrayList<Integer[]> availableMoves) {
        for(int i = 0; i < availableMoves.size(); i++) {
            Integer[] coords = availableMoves.get(i);
            board[coords[0]][coords[1]].highlight();
        }
    }

    // clear highlighting borders from all the Tile objects on the board
    public void clearAllHighlightedTiles() {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                board[i][j].removeHighlight();
            }
        }
    }
}