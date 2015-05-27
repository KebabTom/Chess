package Model;


import java.io.*;
import java.util.*;

class BlankSquare extends Piece {
	BlankSquare() {
		setName("BlankSquare");
	}

	public void printPiece() {
		System.out.print("O");
	}
}