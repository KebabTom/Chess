package Model;

import java.io.*;
import java.util.*;
import java.util.Stack.*;

// the principal class of the chess model.
// contains a 2D array of Piece objects to represent the game pieces, and moves them around to update the game state

public class Game {

	private boolean WHITE = true;
	private boolean BLACK = false;
	private boolean whoseMove = WHITE;

	public static final int BOARD_HEIGHT = 8;
	public static final int BOARD_WIDTH = 8;

	private boolean whiteKingMoved = false;
	private boolean blackKingMoved = false;

	private Piece[][] board;
	private Stack<Piece[][]> boardStack = new Stack<Piece[][]>();

	public Game() {
		board = new Piece[8][8];
		setUpGame();
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	// Setup methods
	////////////////////////////////////////////////////////////////////////////////////////////

	private void setUpGame() {
		addPieces();
		addPawns();
		addBlankSpaces();
	}

	private void addPieces() {
		board[0][0] = new Rook(BLACK, 0, 0);
		board[0][7] = new Rook(BLACK, 0, 7);
		board[0][1] = new Knight(BLACK, 0, 1);
		board[0][6] = new Knight(BLACK, 0, 6);
		board[0][2] = new Bishop(BLACK, 0, 2);
		board[0][5] = new Bishop(BLACK, 0, 5);
		board[0][3] = new King(BLACK, 0, 3);
		board[0][4] = new Queen(BLACK, 0, 4);

		board[7][0] = new Rook(WHITE, 7, 0);
		board[7][7] = new Rook(WHITE, 7, 7);
		board[7][1] = new Knight(WHITE, 7, 1);
		board[7][6] = new Knight(WHITE, 7, 6);
		board[7][2] = new Bishop(WHITE, 7, 2);
		board[7][5] = new Bishop(WHITE, 7, 5);
		board[7][3] = new King(WHITE, 7, 3);
		board[7][4] = new Queen(WHITE, 7, 4);
	}

	private void addPawns() {
		for(int i = 0; i < BOARD_WIDTH; i++) {
			board[1][i] = new Pawn(BLACK, 1, i);
			board[6][i] = new Pawn(WHITE, 6, i);
		}
	}

	private void addBlankSpaces() {
		for(int i = 0; i < BOARD_WIDTH; i++) {
			board[2][i] = new BlankSquare();
			board[3][i] = new BlankSquare();
			board[4][i] = new BlankSquare();
			board[5][i] = new BlankSquare();
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	// Game information returning methods
	///////////////////////////////////////////////////////////////////////////////////////////////

	public boolean outOfBounds(int r, int c) {
		if(r < 0 || c < 0 || r > 7 || c > 7) {
			return true;
		}
		return false;
	}

	public boolean spaceOccupied(int row, int col) {
		if(board[row][col] instanceof BlankSquare) {
			return false;
		}
		return true;
	}

	public boolean getColorInSpace(int row, int col) {
		return board[row][col].getColor();
	}

	public String whatPieceInSpace(int row, int col) {
		return board[row][col].getName();
	}

	public boolean whoseMove() {
		return whoseMove;
	}

	private boolean getOppositeColor(boolean color) {
		return !color;
	}

	public ArrayList<Integer[]> getAvailableMoves(int row, int col) {
		return board[row][col].getAvailableMoves(this);
	}

	public Integer[] getKingCoords(boolean color) {

		Integer[] coords = new Integer[2];

		for(int i = 0; i < BOARD_HEIGHT; i++) {
			for(int j = 0; j < BOARD_WIDTH; j++) {
				Piece p = board[i][j];
				if(p instanceof King && p.getColor() == color) {
					coords[0] = p.getRow();
					coords[1] = p.getCol();
				}
			}
		}
		return coords;
	}


	public boolean hasKingMoved(boolean color) {
		if((color == BLACK && blackKingMoved) || (color == WHITE && whiteKingMoved)) {
			return true;
		}
		return false;
	}

	// returns true if the space indicated by targetRow and targetColumn is threatened
	// by any piece on the opposing side
	public boolean isThreatened(int targetRow, int targetCol, boolean colorUnderThreat) {
		boolean searchColor = !colorUnderThreat;

		for(int r = 0; r < BOARD_HEIGHT; r++) {
			for(int c = 0; c < BOARD_WIDTH; c++) {
				if(board[r][c].getColor() == searchColor) {
					if(threatensLocation(board[r][c], targetRow, targetCol)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	// returns true if the target row and column are threatened by the piece p
	private boolean threatensLocation(Piece p, int targetRow, int targetCol) {
		ArrayList<Integer[]> coords = p.getThreats(this);
		Iterator itr = coords.iterator();
		while(itr.hasNext() ) {
			Integer[] chckCoords = (Integer[]) itr.next();
			if(chckCoords[0] == targetRow && chckCoords[1] == targetCol) {
				return true;
			}
		}
		return false;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// Game state altering methods (moves)
	//////////////////////////////////////////////////////////////////////////////////////////////////////

	// moves a piece from one position to another.
	// if move is legal, adds the last board position to the boardStack
	// if move is illegal (results in own king going into check) throws IOException and leaves the board unchanged
	public void move(Integer[] moveFrom, Integer[] moveTo) throws IOException {

		Piece[][] copy = copyBoard();
		Piece[][] tmp = board;
		board = copy;

		makeMove(moveTo[0],moveTo[1], moveFrom[0],moveFrom[1]);


		Integer kingR = getKingCoords(whoseMove)[0];
		Integer kingC = getKingCoords(whoseMove)[1];

		if(isThreatened(kingR, kingC, whoseMove)) {
			board = tmp;
			throw new IOException("Moved into check");
		}

		boardStack.push(tmp);
		switchTurn();

	}


	// pops the previous board from the stack and switches whose move it is
	public void undoMove() {
		if(!boardStack.empty()) {
			board = boardStack.pop();
			switchTurn();
		}
	}


	// move the piece to the required location, update its row and column attributes and adds a new BlankSquare where it was.
	// if the piece is a king, also moves rook if castling
	// if the piece is a pawn, switches it to a queen if it is on the last row
	public void makeMove(int rowTo, int colTo, int rowFrom, int colFrom) {
		Piece p = board[rowFrom][colFrom];

		board[rowTo][colTo] = p;
		board[rowFrom][colFrom] = new BlankSquare();
		p.setRow(rowTo);
		p.setCol(colTo);

		if(p.getName().equals("King")) {
			if(colFrom - colTo == 2) {
				moveRookInLeftCastle(rowFrom);
			}
			if(colTo - colFrom == 2) {
				moveRookInRightCastle(rowFrom);
			}
		}

		switchPieceIfLastRowPawn(p);

	}

	// if the passed piece is a pawn, switches it to a queen if it is either:
	// black and on the bottom row or white and on the top row
	private void switchPieceIfLastRowPawn(Piece p) {
		if(p.getName().equals("Pawn")) {
			if( (p.getRow() == 0 && p.getColor() == WHITE) ||
				(p.getRow() == 7 && p.getColor() == BLACK) ) {
				board[p.getRow()][p.getCol()] = new Queen(p.getColor(), p.getRow(), p.getCol());
			}
		}
	}

	// moves rook from column 0 to column 2 in the case of a left castle
	private void moveRookInLeftCastle(int row) {
		board[row][2] = board[row][0];
		board[row][0] = new BlankSquare();
		board[row][2].setCol(2);
	}

	// moves rook from column 7 to column 4 in the case of a right castle
	private void moveRookInRightCastle(int row) {
		board[row][4] = board[row][7];
		board[row][7] = new BlankSquare();
		board[row][4].setCol(4);
	}

	public void switchTurn() {
		whoseMove = getOppositeColor(whoseMove);
	}


	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// Check/checkmate calculating methods
	//////////////////////////////////////////////////////////////////////////////////////////////////////

	// returns true if the player who's turn it is is in check
	// i.e. their king is threatened
	public boolean checkForCheck() {
		boolean colorToCheck = whoseMove;
		Integer[] threatenedKingCoords = getKingCoords(colorToCheck);

		if(isThreatened(threatenedKingCoords[0], threatenedKingCoords[1], colorToCheck)) {
			return true;
		} else {
			return false;
		}
	}

	// returns true if the player who's move it is is in checkmate
	// first checks if their king can move. If it can move, returns false
	// if their king can't move, performs brute force check on all their pieces. If a single valid move can be made, returns false
	// if there can be no moves made without the king being in check, returns true
	public boolean checkForCheckMate() {
		if(checkForCheck()) {
			if(checkForPossibleKingMoves()) {
				return false;
			} else {
				if(bruteForceCheckMateEscape()) {
					return false;
				}
			}
		}

		return true;
	}

	// returns true if the king of the player whose turn it is can move.
	private boolean checkForPossibleKingMoves() {
		boolean kingColor = whoseMove;
		Integer[] threatenedKingCoords = getKingCoords(kingColor);
		Piece threatenedKing = board[threatenedKingCoords[0]][threatenedKingCoords[1]];
		ArrayList<Integer[]> kingMoves = threatenedKing.getAvailableMoves(this);
		if(kingMoves.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	// checks all moves for all pieces of the current player's colour.
	// returns true if any of them is a valid move (can be made without leaving the king in checkmate)
	private boolean bruteForceCheckMateEscape() {
		for(int row = 0; row < BOARD_HEIGHT; row++) {
			for(int col = 0; col < BOARD_WIDTH; col++) {
				if(spaceOccupied(row, col) && getColorInSpace(row, col) == whoseMove()) {
					ArrayList<Integer[]> moves = board[row][col].getAvailableMoves(this);
					Iterator itr = moves.iterator();
					Integer[] coordsFrom = new Integer[] {row, col};
					while(itr.hasNext()) {
						Integer[] coordsTo = (Integer[]) itr.next();
						try {
							move(coordsFrom, coordsTo);
							undoMove();
							return true;
						} catch (IOException e) {
							// do nothing
						}
					}
				}
			}
		}

		return false;
	}


	////////////////////////////////////////////////////////////////////////////////////////////
	// Internal methods
	////////////////////////////////////////////////////////////////////////////////////////////
	
	// returns true if the two integer arrays contain the same two elements in the same order
	private boolean sameCoords(Integer[] coordA, Integer[] coordB) {
		if(coordA.length != 2 || coordB.length != 2) {
			return false;
		}
		if(coordA[0] == coordB[0] && coordA[1] == coordB[1]) {
			return true;
		} else {
			return false;
		}
	}

	// creates a full, deep copy of the 2D Piece array that makes up the board
	private Piece[][] copyBoard() {
		Piece[][] copy = new Piece[BOARD_HEIGHT][BOARD_WIDTH];
		for(int i = 0; i < BOARD_HEIGHT; i++) {
			for(int j = 0; j < BOARD_WIDTH; j++) {
				Piece p = board[i][j];
				if(p instanceof BlankSquare) { copy[i][j] = new BlankSquare(); }
				if(board[i][j] instanceof Pawn)   { copy[i][j] = new Pawn(p.getColor(), p.getRow(), p.getCol()); }
				if(board[i][j] instanceof Rook)   { copy[i][j] = new Rook(p.getColor(), p.getRow(), p.getCol()); }
				if(board[i][j] instanceof Knight) { copy[i][j] = new Knight(p.getColor(), p.getRow(), p.getCol()); }
				if(board[i][j] instanceof Bishop) { copy[i][j] = new Bishop(p.getColor(), p.getRow(), p.getCol()); }
				if(board[i][j] instanceof King)   { copy[i][j] = new King(p.getColor(), p.getRow(), p.getCol()); }
				if(board[i][j] instanceof Queen)  { copy[i][j] = new Queen(p.getColor(), p.getRow(), p.getCol()); }
			}
		}
		return copy;
	}

}
