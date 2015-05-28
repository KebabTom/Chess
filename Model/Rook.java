package Model;

import java.io.*;
import java.util.*;

class Rook extends Piece {

	Rook(boolean pieceColor, int row, int col) {
		setName("Rook");
		setColor(pieceColor);
		setRow(row);
		setCol(col);
	}

	public ArrayList<Integer[]> getMovesAndThreats(Game g, boolean includeThreats) {

		ArrayList<Integer[]> moves = new ArrayList<Integer[]>();

		moves.addAll(getStraightMoves(g, includeThreats));

		return moves;
		
	}

}