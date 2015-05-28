package Model;

import java.io.*;
import java.util.*;

class Bishop extends Piece {

	Bishop(boolean pieceColor, int row, int col) {
		setName("Bishop");
		setColor(pieceColor);
		setRow(row);
		setCol(col);
	}

	public ArrayList<Integer[]> getMovesAndThreats(Game g, boolean includeThreats) {

		ArrayList<Integer[]> moves = new ArrayList<Integer[]>();

		moves.addAll(getDiagonalMoves(g, includeThreats));

		return moves;
		
	}

}