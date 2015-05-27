package Model;


import java.io.*;
import java.util.*;

class King extends Piece {

	King(boolean pieceColor, int row, int col) {
		setName("King");
		setColor(pieceColor);
		setRow(row);
		setCol(col);
	}

	public void printPiece() {
		System.out.print("K");
	}


	public ArrayList<Integer[]> getMovesAndThreats(Game g, boolean includeThreats) {

		ArrayList<Integer[]> moves = new ArrayList<Integer[]>();

		for(int i = -1; i < 2; i++) {
			for(int j = -1; j <2; j++) {
				if(i != 0 || j != 0) {
					if(includeThreats || spaceAvailable(g, getRow()+i, getCol()+j) ) {
						moves.add(new Integer[] {getRow()+i, getCol()+j});
					}
				}
			}
		}

		if(!includeThreats) {
			moves.addAll(getCastleMoves(g, getColor()));
		}

		return moves;
	}

	// returns false if the space indicated is out of bounds, occupied by a friendly piece or threatened by an enemy piece
	// returns true if the space can be moved into
	private boolean spaceAvailable(Game g, int r, int c) {
		if(g.outOfBounds(r, c) ) { return false; }

		if(g.spaceOccupied(r, c) ) {
			if(g.getColorInSpace(r, c) == getColor() ) { return false; }
		}

		if(g.isThreatened(r, c, getColor())) { return false; }
		
		return true;

	}

	// returns empty ArrayList if king has moved or is unable to castle.
	// else returns ArrayList of one or two spaces depending on whether the king can castle left and/or right
	public ArrayList<Integer[]> getCastleMoves(Game g, boolean color) {
		ArrayList<Integer[]> castleMoves = new ArrayList<Integer[]>();

		if(g.hasKingMoved(color) || g.isThreatened(getRow(), getCol(), getColor())) {
			return castleMoves;
		}

		int row = getRow();
		int col = getCol();
		boolean canCastleLeft = true;
		boolean canCastleRight = true;

		for(int i = 1; i < 3; i++) {
			if(g.outOfBounds(row, col-i) || g.spaceOccupied(row, col-i) || g.isThreatened(row, col-i, color)) {
				canCastleLeft = false;
			}
			if(g.outOfBounds(row, col+i) || g.spaceOccupied(row, col+i) || g.isThreatened(row, col+i, color)) {
				canCastleRight = false;
			}
		}

		if(canCastleLeft) {
			canCastleLeft = checkCastleForRook(g, row, col-3, color);
		}
		if(canCastleRight) {
			canCastleRight = checkCastleForRook(g, row, col+4, color);
		}

		if(canCastleLeft && !g.isThreatened(row, col-2, color)) {
			castleMoves.add(new Integer[] {row, col-2});
		}
		if(canCastleRight && !g.isThreatened(row, col+2, color)) {
			castleMoves.add(new Integer[] {row, col+2});
		}
		return castleMoves;
	}

	private boolean checkCastleForRook(Game g, int row, int col, boolean kingColor) {
		if(g.outOfBounds(row, col) ) { return false; }
		if(g.whatPieceInSpace(row, col).equals("Rook")) {
			if(g.getColorInSpace(row, col) == kingColor) {
				return true;
			}
		}
		return false;
	}

}