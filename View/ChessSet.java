
package View;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

// class that contains hashmaps of ImageIcons for each piece in the game, and of all possible tile colours

public class ChessSet {

	// constants
	private boolean WHITE = true;
	private boolean BLACK = false;
	private Integer TILE_SIZE;
	private double PIECE_PERCENTAGE_OF_TILE = 75.0;
	private String[] pieceNames = new String[] {"King", "Queen", "Pawn", "Rook", "Knight", "Bishop"};

	// tools
	private IconLoader iconLoader = new IconLoader();

	// contents
	private HashMap<String, ImageIcon> whitePieces = new HashMap<String, ImageIcon>();
	private HashMap<String, ImageIcon> blackPieces = new HashMap<String, ImageIcon>();
	private HashMap<String, Color> tileColors = new HashMap<String, Color>();


	public ChessSet(String whiteColor, String blackColor, int tileSize) {

		TILE_SIZE = tileSize;
		addTileColors();
		addPiecesFromFile(whiteColor, whitePieces);
		addPiecesFromFile(blackColor, blackPieces);

	}

	private void addTileColors() {
		tileColors.put("Black", Color.gray);
		tileColors.put("White", new Color(255,255,255));
		tileColors.put("Green", new Color(39,131,39));
		tileColors.put("Red", new Color(164,6,1));
		tileColors.put("Yellow", new Color(243,243,69));
		tileColors.put("Blue", new Color(69,75,243));
	}

	// clears the passed HashMap and loads it with a new set of pieces from file.
	// File location determined by the 'color' String argument
	private void addPiecesFromFile(String color, HashMap<String, ImageIcon> pieces) {
		pieces.clear();
		int iconSize = (int) ((double) TILE_SIZE * PIECE_PERCENTAGE_OF_TILE / 100.0);

		for(int i = 0; i < pieceNames.length; i++) {
			String path = "Piece_Images/"+color+"/"+pieceNames[i]+".png";
			ImageIcon icon = iconLoader.createImageIcon(path, iconSize);
			pieces.put(pieceNames[i], icon);
		}
	}

	// returns the icon for the passed piece name of the passed logical colour (WHITE or BLACK)
	public ImageIcon getPieceIcon(boolean color, String name) {
		if(color == WHITE) {
			return whitePieces.get(name);
		} else {
			return blackPieces.get(name);
		}
	}

	// switches the display color of the passed logical colour to represent the passed display colour
	public void updatePieceColors(boolean colorToUpdate, String newColor) {
		if(colorToUpdate == WHITE) {
			addPiecesFromFile(newColor, whitePieces);
		} else {
			addPiecesFromFile(newColor, blackPieces);
		}
	}

	public Color getTileColor(String tileColorName) {
		return tileColors.get(tileColorName);

	}

}
