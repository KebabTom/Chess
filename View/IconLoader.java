
package View;

import javax.swing.*;
import javax.swing.SwingConstants.*;
import javax.swing.border.*;
import javax.imageio.*;
import java.awt.*;

// class with only one public method.
// Used to load images from file as icons, in order to insert them into JPanels

class IconLoader {

	// returns an image icon from the passed file path, scaled to the passed size
	public ImageIcon createImageIcon(String path, int imageSize) {
		Image img = loadImage(path, imageSize);
		img = resizeImage(img, imageSize);
		ImageIcon icon = new ImageIcon(img);
		return icon;
	}
	
	private Image loadImage(String path, int imageSize) {
    	java.net.URL imgURL = getClass().getResource(path);
    	Image img;
    	try {
    		img = ImageIO.read(imgURL);
    	} catch (Exception e) {
    		System.out.println("***ERROR: Couldn't load image at: '"+path+"'");
    		throw new Error(e);
    	}
    	return img;
	}

	// resizes an image so that it corresponds to the passed size
	private Image resizeImage(Image imgToResize, int imageSize) {
		imgToResize = imgToResize.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
		return imgToResize;
	}

}

