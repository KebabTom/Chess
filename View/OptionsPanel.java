
package View;


import javax.swing.*;
import javax.swing.SwingConstants.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import Controller.*;



public class OptionsPanel extends JPanel implements ActionListener {

	private boolean WHITE = true;
	private boolean BLACK = false;

	private String[] colors = {"White", "Black", "Green", "Red", "Yellow", "Blue"};
	private ImageIcon restartIcon;
	private ImageIcon undoIcon;
	private ImageIcon guideIcon;
	private ImageIcon noGuideIcon;
	private ImageIcon noCheckIcon;
	private ImageIcon checkIcon;
	private ImageIcon checkmateIcon;

	private JLabel whoseTurnLabel;
	private JPanel turnColorPanel;
	private JLabel padLabel;
	private JButton restartButton;
	private JButton undoButton;
	private JButton guideToggleButton;
	private JComboBox<String> whiteColorBox;
	private JComboBox<String> blackColorBox;
	private JPanel checkStatusPanel;
	private JLabel checkPic;

	private ChessController controller;

	public OptionsPanel (ChessController c) {

		controller = c;

		restartIcon = createImageIcon("Images/reset-icon.png", 40);
		undoIcon = createImageIcon("Images/undo-icon.png", 40);
		guideIcon = createImageIcon("Images/guides-icon.png", 40);
		noGuideIcon = createImageIcon("Images/no-guides-icon.png", 40);
		noCheckIcon = createImageIcon("Images/noCheck.png", 140);
		checkIcon = createImageIcon("Images/check.png", 140);
		checkmateIcon = createImageIcon("Images/checkmate.png", 140);



        this.setPreferredSize(new Dimension(150, 620));
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBackground(Color.gray);


        whoseTurnLabel = headingLabel("Turn");
        this.add(whoseTurnLabel);

        turnColorPanel = new JPanel();
        turnColorPanel.setPreferredSize(new Dimension(100,100));
        turnColorPanel.setMaximumSize(new Dimension(100,100));
        turnColorPanel.setBackground(Color.white);
        this.add(turnColorPanel, BorderLayout.CENTER);
        padLabel = headingLabel("Game Options");
        this.add(padLabel);

        restartButton = new JButton(restartIcon);
    	restartButton.setVerticalTextPosition(AbstractButton.BOTTOM);
    	restartButton.setHorizontalTextPosition(AbstractButton.CENTER);
    	restartButton.addActionListener(this);
        this.add(restartButton);


        undoButton = new JButton(undoIcon);
    	undoButton.setVerticalTextPosition(AbstractButton.BOTTOM);
    	undoButton.setHorizontalTextPosition(AbstractButton.CENTER);
    	undoButton.addActionListener(this);
        this.add(undoButton);

        guideToggleButton = new JButton(guideIcon);
    	guideToggleButton.setVerticalTextPosition(AbstractButton.BOTTOM);
    	guideToggleButton.setHorizontalTextPosition(AbstractButton.CENTER);
    	guideToggleButton.addActionListener(this);
        this.add(guideToggleButton);


		whiteColorBox = new JComboBox<String>(colors);
        whiteColorBox.setMaximumSize(new Dimension(100,20));
        whiteColorBox.setSelectedIndex(0);
        whiteColorBox.addActionListener(new comboListener(WHITE, controller));

		this.add(whiteColorBox);

        blackColorBox = new JComboBox<String>(colors);
        blackColorBox.setMaximumSize(new Dimension(100,20));
        blackColorBox.setSelectedIndex(1);
        blackColorBox.addActionListener(new comboListener(BLACK, controller));

		this.add(blackColorBox);

		checkStatusPanel = new JPanel();
		checkStatusPanel.setLayout(new GridBagLayout());
        checkStatusPanel.setPreferredSize(new Dimension(140,140));
        checkStatusPanel.setMaximumSize(new Dimension(140,140));
        checkStatusPanel.setBackground(Color.black);

        checkPic = new JLabel();
        checkStatusPanel.add(checkPic);
        checkPic.setIcon(noCheckIcon);
        this.add(checkStatusPanel, BorderLayout.CENTER);


	}

	private JLabel headingLabel(String content) {
        JLabel lbl = new JLabel(content);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        return lbl;

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

	private ImageIcon createImageIcon(String path, int imageSize) {
		Image img = loadImage(path, imageSize);
		img = resizeImage(img, imageSize);
		ImageIcon icon = new ImageIcon(img);
		return icon;
	}

	// resizes an image so that it corresponds to the percentage of tile size
	private Image resizeImage(Image imgToResize, int imageSize) {
		imgToResize = imgToResize.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
		return imgToResize;
	}


	public void updateTurnColorPanel(Color newColor){
		turnColorPanel.setBackground(newColor);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == restartButton) {
			controller.restartGame();
		}
		if(e.getSource() == undoButton) {
			controller.undoMove();
		}
		if(e.getSource() == guideToggleButton) {
			controller.toggleGuides();
			toggleGuideButton();
		}
	}

	private void toggleGuideButton() {
		if(controller.showingGuides()) {
			guideToggleButton.setIcon(guideIcon);
		} else {
			guideToggleButton.setIcon(noGuideIcon);
		}
	}

	public void displayCheck() {
		checkPic.setIcon(checkIcon);
	}

	public void displayCheckmate() {
		checkPic.setIcon(checkmateIcon);
	}
	
	public void clearCheck() {
		checkPic.setIcon(noCheckIcon);
	}


	// Listener class for the combo boxes. Allows them to call updateChessSetColors() in the ChessController
	class comboListener implements ActionListener {
		private boolean comboColor;
		private ChessController controller;

		public comboListener(boolean color, ChessController c) {
			comboColor = color;
			controller = c;
		}

		public void actionPerformed(ActionEvent e) {
        	JComboBox cb = (JComboBox) e.getSource();
			String newColor = (String) cb.getSelectedItem();

			controller.updateChessSetColors(comboColor, newColor);
		}
	}

}