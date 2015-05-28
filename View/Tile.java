
package View;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragGestureListener;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSourceListener;
import Controller.*;

// the display for each tile on the chess board
// instantiated with a reference to the ChessController in order to process listener events

public class Tile extends JPanel {

	// constants
	private Integer TILE_SIZE;
	private Integer BORDER_SIZE = 3;
	private boolean WHITE = true;
	private boolean BLACK = false;
	private Integer row, col;

	private MouseListener listener;
	private Color tileColor;
	private JLabel pic = new JLabel();

	// borders used for highlighting
	private Border highlightBorder;
	private Border normalBorder;
	private Border hoverBorder;
	private Border markedBorder;

	private boolean highlighted = false;
	private boolean marked = false;



	// set the row and column values and iniial background and border colours
	// also sets up the mouse listener to pass requests back to the ChessController
	public Tile(ChessController c, Color bgColor, int startRow, int startCol, int tileSize) {

		TILE_SIZE = tileSize;
		row = startRow;
		col = startCol;
		listener = new TileMouseListener(c, this);

		setLayout(new GridBagLayout());
		setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));

		tileColor = bgColor;
		highlightBorder = BorderFactory.createMatteBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, Color.green);
		normalBorder = BorderFactory.createMatteBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, tileColor);
		hoverBorder = BorderFactory.createMatteBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, Color.black);
		markedBorder = BorderFactory.createMatteBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, Color.black);
    	
    	setBackground(tileColor);
    	setBorder(normalBorder);
    	this.add(pic);


	}

	// called when the user hovers over the tile
	public void hoverBorder() {
    	setBorder(hoverBorder);
	}

	// sets the Tile back to its usual border state depending on whether it is currently marked or highlighted
	// used when the user stops hovering over the Tile
	public void normalBorder() {
		if(marked) {
			markedBorder();
		} else if(highlighted) {
			highlightBorder();
			} else {
	    		setBorder(normalBorder);
			}
	}

	private void highlightBorder() {
		setBorder(highlightBorder);
	}

	public void highlight() {
		highlighted = true;
		setBorder(highlightBorder);
	}

	public void removeHighlight() {
		highlighted = false;
		normalBorder();
	}

	public void updateBackgroundColor(Color c) {
		tileColor = c;
    	setBackground(tileColor);
    	matchBorderToTile();
	}

	private void matchBorderToTile() {
		normalBorder = BorderFactory.createMatteBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, tileColor);
		setBorder(normalBorder);
	}

	private void markedBorder() {
		setBorder(markedBorder);
	}

	public void mark() {
		marked = true;
		markedBorder();
	}

	public void unmark() {
		marked = false;
		normalBorder();
	}

	public void removePiece() {
		pic.setIcon(null);
		repaint();
	}

	// getters and setters
	public void addPiece(ImageIcon icon) {
		pic.setIcon(icon);
		repaint();
	}

	public Icon getPiece() {
		return pic.getIcon();
	}

	public Integer getRow() {
		return row;
	}

	public Integer getCol() {
		return col;
	}


}