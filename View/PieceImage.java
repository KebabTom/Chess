
package View;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

class PieceImage extends JLabel {

	private double PERCENTAGE_OF_TILE = 75;
	private Image image;
	private ImageIcon icon;


	public void addImageAsIcon() {
		resizeImage();
		icon = new ImageIcon(image);
		this.setIcon(icon);
	}

	public void updatePic(String path) {
		loadImage(path);
	}

	public void clearPic() {
		setIcon(null);
	}


	public void loadImage(String path) {
    	java.net.URL imgURL = getClass().getResource(path);
    	Image img;
    	try {
    		img = ImageIO.read(imgURL);
    	} catch (Exception e) {
    		System.out.println("***ERROR: Couldn't read piece image file at: '"+path+"'");
    		throw new Error(e);
    	}
    	image = img;
	}

	public void resizeImage() {
		Container p = this.getParent();
		int newWidth = (int) ((double) PERCENTAGE_OF_TILE*(double)(p.getPreferredSize().getWidth()/100.0));
		int newHeight = (int) ((double) PERCENTAGE_OF_TILE*(double)(p.getPreferredSize().getHeight()/100.0));
		image = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
	}



}