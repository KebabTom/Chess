package Model;


import java.io.*;
import java.util.*;

class Pawn extends Piece {

	private boolean WHITE = true;
	private boolean BLACK = false;

	Pawn(boolean pieceColor, int row, int col) {
		setName("Pawn");
		setColor(pieceColor);
		setRow(row);
		setCol(col);
	}

	public void printPiece() {
		System.out.print("P");
	}

	// adds pawn moves based on current position, colour and surrounding enemies
	public ArrayList<Integer[]> getMovesAndThreats(Game g, boolean includeThreats) {

		if (includeThreats) {
			return getPawnThreats(g);
		}
		ArrayList<Integer[]> moves = new ArrayList<Integer[]>();

		if(getColor() == WHITE && getRow() == 6 || getColor() == BLACK && getRow() == 1) {
			addStarterMoves(g, moves);
		} else {
			addInPlayMoves(g, moves);
		}

		addTakingMoves(g, moves);

		return moves;
		
	}

	// the two spaces above or below the pawn that it threatens, depending on colour.
	// used when moving the king or calculating checkmate
	public ArrayList<Integer[]> getPawnThreats(Game g) {

		ArrayList<Integer[]> threats = new ArrayList<Integer[]>();

		int modifier = getModifier();
		int moveRow = getRow()+modifier;

		if(!g.outOfBounds(moveRow, getCol()-1) ) {
			threats.add(new Integer[] {moveRow, getCol()-1});
		}
		if(!g.outOfBounds(moveRow, getCol()+1) ) {
			threats.add(new Integer[] {moveRow, getCol()+1});
		}

		return threats;
	}

	// adds the pawns move options to take enemy pieces if they are there
	private void addTakingMoves(Game g, ArrayList<Integer[]> moves) {
		int modifier = getModifier();
		int moveRow = getRow()+modifier;

		if(checkForEnemy(moveRow, getCol()-1, g) ) {
			moves.add(new Integer[] {moveRow, getCol()-1});
		}
		if(checkForEnemy(moveRow, getCol()+1, g) ) {
			moves.add(new Integer[] {moveRow, getCol()+1});
		}

	}

	// returns true if there is an enemy piece at the passed coordinates
	private boolean checkForEnemy(int r, int c, Game g) {
		if(g.outOfBounds(r, c) ) { return false; }
		if(!g.spaceOccupied(r, c) ) { return false; }
		if(g.getColorInSpace(r, c) == getColor() ) { return false; }
		return true;
	}

	// as pawns can only move in one direction, the modifier is used to alter potential move coordinates in 
	// getMoves() & getThreats() depending on piece colour
	private int getModifier() {
		if(getColor() == WHITE) {
			return -1;
		}
		return 1;
	}

	// adds a possible two positions for the pawn's initial move
	private void addStarterMoves(Game g, ArrayList<Integer[]> moves) {
		int modifier = getModifier();

		boolean canMove = true;
		for(int i = 1; i < 3 && canMove; i++) {
			int moveRow = getRow()+(i*modifier);
			if(g.spaceOccupied(moveRow, getCol()) ) {
				canMove = false;
			} else {
				moves.add(new Integer[] {moveRow, getCol()});
			}
		}
	}

	// adds one possible position for when the pawn has already been moved
	private void addInPlayMoves(Game g, ArrayList<Integer[]> moves) {
		int modifier = getModifier();

		int moveRow = getRow()+modifier;
		if(!g.outOfBounds(moveRow, getCol()) && !g.spaceOccupied(moveRow, getCol()) ) {
			moves.add(new Integer[] {moveRow, getCol()});
		}
	}

}