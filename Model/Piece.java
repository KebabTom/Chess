package Model;

import java.io.*;
import java.util.*;

// parent class for all pieces.
// all subclasses (except BlankSquare) contain their own getMovesAndThreats() methods to override the method in this class

class Piece {
	private boolean WHITE = true;
	private boolean BLACK = false;
	private int row;
	private int col;
	private String name;
	private boolean color;


	// getters and setters
	public void setColor(boolean newColor) {
		color = newColor;
	}

	public boolean getColor() {
		return color;
	}
	
	public void setName(String newName) {
		name = newName;
	}

	public String getName() {
		return name;
	}

	public void setRow(int newRow) {
		row = newRow;
	}

	public void setCol(int newCol) {
		col = newCol;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	// wrapper for getMovesAndThreats. Used when moving a player's pieces
	public ArrayList<Integer[]> getAvailableMoves(Game g) {
		return getMovesAndThreats(g, false);
	}

	// wrapper for getMovesAndThreats. Used when looking for check/checkmate and when finding spaces to move the king
	public ArrayList<Integer[]> getThreats(Game g) {
		return getMovesAndThreats(g, true);
	}

	// prototype for individual function for each piece.
	// If includeThreats is true, returns ArrayList of coordinates of all spaces on the board that the piece currently threatens
	// If includeThreats is false, returns ArrayList of coordinates of all spaces on the board that the piece can move to
	// Returns empty array list is called on blank space
	public ArrayList<Integer[]> getMovesAndThreats(Game g, boolean includeThreats) {
		return new ArrayList<Integer[]>();
	}

	//////////////////////////////////////////////////////////////
	/* General move methods (Applied to more than one piece) */
	//////////////////////////////////////////////////////////////

	// checks a space on the board to see if a piece can move there. If it can, it is added to the coords ArrayList
	// return value used for straight line pieces (Rook, Queen, Bishop) to determine whether to keep looking for moves
	// returns false if the piece definitely can't make any moves past this space. Returns true if more moves are possible.
	public boolean checkSpace(ArrayList<Integer[]> coords, Game g, int r, int c, boolean includeThreats) {

		if(g.outOfBounds(r, c)) {
			return false;
		}

		if(!g.spaceOccupied(r, c)) {
			coords.add(new Integer[] {r, c});
			return true;
		}

		if(g.getColorInSpace(r, c) != this.getColor()) {
			coords.add(new Integer[] {r, c});
			// if looking for threats, keep looking past the enemy king (Used when calculating where the king can move)
			if(includeThreats && g.whatPieceInSpace(r,c).equals("King")) {
				return true;
			}
		} else {
			if(includeThreats) {
				coords.add(new Integer[] {r, c});
			}
		}
		return false;
	}

	// returns an arraylist of integer[2] arrays with all coordinates left, right, up and down that a piece can move to
	// checks each space outwards until that space either contains another piece or is out of bounds
	public ArrayList<Integer[]> getStraightMoves(Game g, boolean includeThreats) {

		ArrayList<Integer[]> coords = new ArrayList<Integer[]>();

		boolean up, right, down, left;
		up = right = down = left = true;

		int i = 1;
		while(up||right||down||left) {
			if(up) {
				up = checkSpace(coords, g, row-i, col, includeThreats);
			}

			if(right) {
				right = checkSpace(coords, g, row, col+i, includeThreats);
			}

			if(down) {
				down = checkSpace(coords, g, row+i, col, includeThreats);
			}

			if(left) {
				left = checkSpace(coords, g, row, col-i, includeThreats);
			}

			i++;
		}
		return coords;
	}

	// returns arraylist of integer[2] arrays with all coordinates NorthWest, NorthEast, SouthEast and SouthWest
	// that a piece can move to
	// checks each space outwards until that space either contains another piece or is out of bounds
	public ArrayList<Integer[]> getDiagonalMoves(Game g, boolean includeThreats) {

		ArrayList<Integer[]> coords = new ArrayList<Integer[]>();

		boolean nw, ne, se, sw;
		nw = ne = se = sw = true;

		int i = 1;
		while(nw||ne||se||sw) {
			if(nw) {
				nw = checkSpace(coords, g, row-i, col-i, includeThreats);
			}

			if(ne) {
				ne = checkSpace(coords, g, row-i, col+i, includeThreats);
			}

			if(se) {
				se = checkSpace(coords, g, row+i, col+i, includeThreats);
			}

			if(sw) {
				sw = checkSpace(coords, g, row+i, col-i, includeThreats);
			}

			i++;
		}
		return coords;
	}





}