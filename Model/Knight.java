package Model;

import java.io.*;
import java.util.*;

class Knight extends Piece {

	Knight(boolean pieceColor, int row, int col) {
		setName("Knight");
		setColor(pieceColor);
		setRow(row);
		setCol(col);
	}

	// checks all 8 possible positions the knight can move to
	public ArrayList<Integer[]> getMovesAndThreats(Game g, boolean includeThreats) {

		ArrayList<Integer[]> moves = new ArrayList<Integer[]>();

		int r = getRow();
		int c = getCol();

		checkSpace(moves, g, r-1, c-2, includeThreats);
		checkSpace(moves, g, r-1, c+2, includeThreats);
		checkSpace(moves, g, r+1, c+2, includeThreats);
		checkSpace(moves, g, r+1, c-2, includeThreats);
		checkSpace(moves, g, r-2, c-1, includeThreats);
		checkSpace(moves, g, r-2, c+1, includeThreats);
		checkSpace(moves, g, r+2, c+1, includeThreats);
		checkSpace(moves, g, r+2, c-1, includeThreats);

		return moves;


	}

}