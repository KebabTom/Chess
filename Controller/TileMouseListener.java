
package Controller;


import java.awt.*;
import java.awt.event.*;
import View.*;

// used to highlight tiles when hovered and process user clicks on each Tile

public class TileMouseListener extends MouseAdapter {

	private Tile tile;
	private ChessController controller;

	public TileMouseListener(ChessController c, Tile t) {
		tile = t;
		controller = c;
		t.addMouseListener(this);
	}

    public void mouseClicked(MouseEvent e) {
    	controller.processTileClick(tile.getRow(), tile.getCol());
    }

    public void mouseEntered(MouseEvent e) {
    	tile.hoverBorder();
    }
    public void mouseExited(MouseEvent e) {
    	tile.normalBorder();
   	}

    public void mousePressed(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }
}