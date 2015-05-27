
package View;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class ChessSet {

	private boolean WHITE = true;
	private boolean BLACK = false;

	private Integer TILE_SIZE;
	private double PERCENTAGE_OF_TILE = 75.0;

	private HashMap<String, ImageIcon> whitePieces = new HashMap<String, ImageIcon>();
	private HashMap<String, ImageIcon> blackPieces = new HashMap<String, ImageIcon>();
	private String[] pieceNames = new String[] {"King", "Queen", "Pawn", "Rook", "Knight", "Bishop"};

	private HashMap<String, Color> tileColors = new HashMap<String, Color>();


	public ChessSet(String whiteColor, String blackColor, int tileSize) {

		TILE_SIZE = tileSize;
		addTileColors();
		addPiecesFromFile(whiteColor, whitePieces);
		addPiecesFromFile(blackColor, blackPieces);

	}

	// clears the passed HashMap and loads it with a new set of pieces from file.
	// File location determined by the 'color' String argument
	private void addPiecesFromFile(String color, HashMap<String, ImageIcon> pieces) {
		pieces.clear();
		Image img;
		for(int i = 0; i < pieceNames.length; i++) {
			String path = "Piece_Images/"+color+"/"+pieceNames[i]+".png";
			img = loadImage(path);
			img = resizeImage(img);
			ImageIcon icon = new ImageIcon(img);
			pieces.put(pieceNames[i], icon);
		}
	}

	private Image loadImage(String path) {
    	java.net.URL imgURL = getClass().getResource(path);
    	Image img;
    	try {
    		img = ImageIO.read(imgURL);
    	} catch (Exception e) {
    		System.out.println("***ERROR: Couldn't read piece image file at: '"+path+"'");
    		throw new Error(e);
    	}
    	return img;
	}

	// resizes an image so that it corresponds to the percentage of tile size
	private Image resizeImage(Image imgToResize) {
		int newSize = (int) ((double) TILE_SIZE*PERCENTAGE_OF_TILE /100.0);
		imgToResize = imgToResize.getScaledInstance(newSize, newSize, Image.SCALE_SMOOTH);
		return imgToResize;
	}

	public ImageIcon getPieceIcon(boolean color, String name) {
		if(color == WHITE) {
			return whitePieces.get(name);
		} else {
			return blackPieces.get(name);
		}
	}

	public void updatePieceColors(boolean colorToUpdate, String newColor) {
		if(colorToUpdate == WHITE) {
			addPiecesFromFile(newColor, whitePieces);
		} else {
			addPiecesFromFile(newColor, blackPieces);
		}
	}

	private void addTileColors() {
		tileColors.put("Black", Color.gray);
		tileColors.put("White", new Color(255,255,255));
		tileColors.put("Green", new Color(39,131,39));
		tileColors.put("Red", new Color(164,6,1));
		tileColors.put("Yellow", new Color(243,243,69));
		tileColors.put("Blue", new Color(69,75,243));
	}

	public Color getTileColor(String tileColorName) {
		return tileColors.get(tileColorName);

	}

}
