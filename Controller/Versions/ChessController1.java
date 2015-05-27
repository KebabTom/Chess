
package Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import View.*;
import Model.*;

public class ChessController implements Runnable {

    private boolean ready = false;

    private boolean moveStarted = false;
    private ArrayList<Integer[]> availableMoves;
    private Integer[] moveFrom = new Integer[2];
    private Integer[] moveTo = new Integer[2];

	private Tile[][] board = new Tile[8][8];
	private GridLayout chessGrid = new GridLayout(8,8);
    private JPanel container = new JPanel(chessGrid);


	private boolean WHITE = true;
	private boolean BLACK = false;

    private Integer TILE_SIZE = 80;

    private Game g = new Game();
    private ChessSet chessSet;
    private JFrame w;

    private JLabel movePiece;

    // Settings
    private boolean highlightMoves = true;
    private String whiteColor = "White";
    private String blackColor = "Black";
    private Color whiteTileColor = Color.white;
    private Color blackTileColor = Color.gray;


    public static void main(String[] args) {
    }

    public void run() {
        w = new JFrame();
        initialSetup();

        System.out.println(w.getHeight());
        System.out.println(w.getContentPane().getHeight());
    }

    public Color getWhiteTileColor() {
        return whiteTileColor;
    }

    public Color getBlackTileColor() {
        return blackTileColor;
    }

    private void initialSetup() {
        w.setDefaultCloseOperation(w.EXIT_ON_CLOSE);
        w.setTitle("Chess Time");
        this.addTiles();
        this.addBoardToContainer();
        chessSet = new ChessSet(whiteColor, blackColor, TILE_SIZE);
        w.getContentPane().setLayout(new GridBagLayout());
        w.setContentPane(container);
        w.pack();
        w.setLocationByPlatform(true);
        w.setVisible(true);
        addPiecesFromGame();
    }

    private void addTiles() {
    	boolean color = BLACK;
    	for(int i = 0; i < 8; i++) {
    		for(int j = 0; j < 8; j++) {
                if(color == WHITE) {
                    board[i][j] = new Tile(this, whiteTileColor, i, j);
                } else {
                    board[i][j] = new Tile(this, blackTileColor, i, j);
                }
    			color = !color;
    		}
    		color = !color;
    	}
    }

    public boolean ready() {
        return ready;
    }


    private void addBoardToContainer() {
    	boolean color = WHITE;
    	for(int i = 0; i < 8; i++) {
    		for(int j = 0; j < 8; j++) {
    			container.add(board[i][j]);
    		}
    	}

    }

    private void addPiecesFromGame() {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(g.spaceOccupied(i, j)) {
                    String pieceName = g.whatPieceInSpace(i, j);
                    ImageIcon piece = chessSet.getPieceIcon(g.getColorInSpace(i,j), pieceName);
                    board[i][j].addPiece(piece);
                } else {
                    board[i][j].removePiece();
                }
            }
        }
    }


    private boolean checkForValidStartToMove(int row, int col) {
        if(g.spaceOccupied(row, col) && g.getColorInSpace(row, col) == g.whoseMove()) {
            return true;
        }
        return false;
    }

    private void highlightAvailableMoves() {
        for(int i = 0; i < availableMoves.size(); i++) {
            Integer[] coords = availableMoves.get(i);
            board[coords[0]][coords[1]].highlight();
        }
    }

    private void startMove(int row, int col) {
        moveStarted = true;
        moveFrom[0] = row;
        moveFrom[1] = col;
        markMoveFrom();
        availableMoves = g.getAvailableMoves(row, col);
        if(highlightMoves) {
            highlightAvailableMoves();
        }
    }

    private void markMoveFrom() {
        board[moveFrom[0]][moveFrom[1]].mark();
    }

    private void unmarkMoveFrom() {
        board[moveFrom[0]][moveFrom[1]].unmark();
    }

    private boolean checkForValidEndToMove(int row, int col) {
        Iterator itr = availableMoves.iterator();
        while(itr.hasNext()) {
            Integer[] coords = (Integer[]) itr.next();
            if(coords[0] == row && coords[1] == col) {
                return true;
            }
        }
        return false;
    }

    private void endMove(int row, int col) {
        moveStarted = false;
        moveTo[0] = row;
        moveTo[1] = col;
        try {
            g.move(moveFrom, moveTo);
            addPiecesFromGame();
        } catch (Exception e) {
            System.out.println("King would be in check");
        }
    }


    private void clearAllHighlightedTiles() {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                board[i][j].removeHighlight();
            }
        }
    }

    public void processTileClick(int row, int col) {
        if(!moveStarted) {
            if(checkForValidStartToMove(row, col)) {
                startMove(row, col);
            }
        } else {
            unmarkMoveFrom();
            if(!checkForValidEndToMove(row, col)) {
                moveStarted = false;
                clearAllHighlightedTiles();
            } else {
                endMove(row, col);
                clearAllHighlightedTiles();
            }
        }

    }
}