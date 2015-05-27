package Model;


import java.io.*;
import java.util.*;

class Queen extends Piece {

	Queen(boolean pieceColor, int row, int col) {
		setName("Queen");
		setColor(pieceColor);
		setRow(row);
		setCol(col);
	}

	public void printPiece() {
		System.out.print("Q");
	}

	public ArrayList<Integer[]> getMovesAndThreats(Game g, boolean includeThreats) {

		ArrayList<Integer[]> moves = new ArrayList<Integer[]>();

		moves.addAll(getStraightMoves(g, includeThreats));
		moves.addAll(getDiagonalMoves(g, includeThreats));

		return moves;
		
	}

}