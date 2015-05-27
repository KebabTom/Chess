
package Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import View.*;
import Model.*;

public class ChessController implements Runnable {


    // temporary game information
    private boolean moveStarted = false;
    private ArrayList<Integer[]> availableMoves;
    private Integer[] moveFrom = new Integer[2];
    private Integer[] moveTo = new Integer[2];
    private boolean check = false;
    private boolean checkmate = false;

    // view assets
    private DisplayBoard chessBoard;
    private ChessSet chessSet;
    private OptionsPanel options;
    private JFrame w;

    // model assets
    private Game g = new Game();

    // constants
	private boolean WHITE = true;
	private boolean BLACK = false;
    private Integer TILE_SIZE = 80;

    // settings
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
    }

    public Color getWhiteTileColor() {
        return whiteTileColor;
    }

    public Color getBlackTileColor() {
        return blackTileColor;
    }

    public boolean showingGuides() {
        return highlightMoves;
    }

    public void toggleGuides() {
        highlightMoves = !highlightMoves;
        if(!highlightMoves) {
            chessBoard.clearAllHighlightedTiles();
        } else {
            if(moveStarted) {
                chessBoard.highlightAvailableMoves(availableMoves);
            }
        }
    }

    private void initialSetup() {
        w.setDefaultCloseOperation(w.EXIT_ON_CLOSE);
        w.setTitle("Chess Time");

        chessBoard = new DisplayBoard(this);
        chessSet = new ChessSet(whiteColor, blackColor, TILE_SIZE);
        options = new OptionsPanel(this);

        JPanel container = new JPanel();
        container.setBackground(Color.black);
        w.getContentPane().add(container);

        container.add(chessBoard);
        container.add(options);

        w.pack();
        updateBoardDisplayFromGame();
        w.setLocationByPlatform(true);
        w.setVisible(true);
    }


    private void updateBoardDisplayFromGame() {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(g.spaceOccupied(i, j)) {
                    String pieceName = g.whatPieceInSpace(i, j);
                    ImageIcon piece = chessSet.getPieceIcon(g.getColorInSpace(i,j), pieceName);
                    chessBoard.addPieceToBoard(i, j, piece);
                } else {
                    chessBoard.removePieceFromBoard(i, j);
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


    private void startMove(int row, int col) {
        moveStarted = true;
        moveFrom[0] = row;
        moveFrom[1] = col;
        chessBoard.markMoveFrom(moveFrom);
        availableMoves = g.getAvailableMoves(row, col);
        if(highlightMoves) {
            chessBoard.highlightAvailableMoves(availableMoves);
        }
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
        chessBoard.unmarkMoveFrom(moveFrom);
        chessBoard.clearAllHighlightedTiles();

        if(!sameCoords(moveFrom, moveTo)) {
            if(!checkForValidEndToMove(row, col)) {
                JOptionPane.showMessageDialog(chessBoard, "That piece can't move there");
            } else {
                try {
                    carryOutMove();
                } catch (Exception e) {
                JOptionPane.showMessageDialog(chessBoard, "Your king would be in check");
                }
            }
        }

    }

    // switches the pieces in the game model, updates the board display and carries out check & checkmate checks
    private void carryOutMove() throws Exception {
        g.move(moveFrom, moveTo);
        updateBoardDisplayFromGame();
        updateWhoseTurnDisplay();
        updateCheckDisplay();
    }

    private void updateCheckDisplay() {
        check = g.checkForCheck();
        checkmate = g.checkForCheckMate();
        if(check) {
            if(checkmate) {
                options.displayCheckmate();
            } else {
                options.displayCheck();
            }
        } else {
            options.clearCheck();
        }
    }

    // called by each Tile's MouseListener object with the row and column values of that tile.
    public void processTileClick(int row, int col) {
        if(!moveStarted) {
            if(checkForValidStartToMove(row, col)) {
                startMove(row, col);
            }
        } else {
            endMove(row, col);
        }

    }

    // returns true if the two integer arrays contain the same two elements in the same order
    private boolean sameCoords(Integer[] coordA, Integer[] coordB) {
        if(coordA[0] == coordB[0] && coordA[1] == coordB[1]) {
            return true;
        } else {
            return false;
        }
    }

    public void updateChessSetColors(boolean colorToUpdate, String newColor) {
        chessSet.updatePieceColors(colorToUpdate, newColor);
        if(colorToUpdate == WHITE) {
            whiteTileColor = chessSet.getTileColor(newColor);
        } else {
            blackTileColor = chessSet.getTileColor(newColor);
        }

        chessBoard.updateTileColors(whiteTileColor, blackTileColor);
        updateBoardDisplayFromGame();
        updateWhoseTurnDisplay();

    }

    public void restartGame() {
        g = new Game();
        resetToStartOfTurn();
    }

    public void undoMove() {
        g.undoMove();
        resetToStartOfTurn();
    }

    // called after game restart/undo. Resets moveStarted flag, updates whose turn/check display and clears move highlighting
    private void resetToStartOfTurn() {
        moveStarted = false;
        chessBoard.clearAllHighlightedTiles();
        updateBoardDisplayFromGame();
        updateWhoseTurnDisplay();
        updateCheckDisplay();
    }

    // updates the color of the indicator in the options panel to reflect the current turn/colours
    private void updateWhoseTurnDisplay() {
        if(g.whoseMove() == WHITE) {
            options.updateTurnColorPanel(whiteTileColor);
        } else {
            options.updateTurnColorPanel(blackTileColor);
        }
    }
}