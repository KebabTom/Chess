
package View;


import javax.swing.*;
import javax.swing.SwingConstants.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Component;
import java.awt.Container;
import java.util.*;
import Controller.*;

// The options panel, displaying all game options for the user
// constructor contains a reference to the ChessController, allowing listeners to update game settings

public class OptionsPanel extends JPanel implements ActionListener {

	// constants
	private int BORDER_SIZE = 3;
	private int PANEL_WIDTH = 150;
	private boolean WHITE = true;
	private boolean BLACK = false;
	private Color backgroundColor = new Color(28,162,28);

	// icons and possible colours
	private String[] colors = {"White", "Black", "Green", "Red", "Yellow", "Blue"};
	private ImageIcon restartIcon;
	private ImageIcon undoIcon;
	private ImageIcon guideIcon;
	private ImageIcon noGuideIcon;
	private ImageIcon noCheckIcon;
	private ImageIcon checkIcon;
	private ImageIcon checkmateIcon;

	// containers
	private Border containerBorder = BorderFactory.createMatteBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, Color.black);
	private JPanel whoseTurnContainer;
	private JPanel buttonsContainer;
	private JPanel colorOptionsContainer;
	private JPanel gameStatusContainer;

	// content
	private JLabel whoseTurnLabel;
	private JLabel gameOptionsLabel;
	private JLabel colorOptionsLabel;
	private JLabel gameStatusLabel;
	private JPanel turnColorPanel;
	private JLabel padLabel;
	private JButton restartButton;
	private JButton undoButton;
	private JButton guideToggleButton;
	private JComboBox<String> whiteColorBox;
	private JComboBox<String> blackColorBox;
	private JPanel checkStatusPanel;
	private JLabel checkPic;

	// tools
	private IconLoader iconLoader = new IconLoader();

	private ChessController controller;

	public OptionsPanel (ChessController c) {

		controller = c;
		loadIconsFromFile();
        
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBackground(backgroundColor);

        addWhoseTurn();

        addOptionButtons();

        addColorOptions();

        addGameStatusDisplay();

	}

	// loads images for all icons in the display panel
	private void loadIconsFromFile() {
		restartIcon = iconLoader.createImageIcon("Images/reset-icon.png", 40);
		undoIcon = iconLoader.createImageIcon("Images/undo-icon.png", 40);
		guideIcon = iconLoader.createImageIcon("Images/guides-icon.png", 40);
		noGuideIcon = iconLoader.createImageIcon("Images/no-guides-icon.png", 40);
		noCheckIcon = iconLoader.createImageIcon("Images/noCheck.png", 130);
		checkIcon = iconLoader.createImageIcon("Images/check.png", 130);
		checkmateIcon = iconLoader.createImageIcon("Images/checkmate.png", 130);
	}

	// adds the display of whose turn it is to the options panel
	private void addWhoseTurn() {
        whoseTurnContainer = makeContainer(155);
        addPadding(whoseTurnContainer, 10);

        whoseTurnLabel = headingLabel("Whose Turn");
        whoseTurnContainer.add(whoseTurnLabel);
        addPadding(whoseTurnContainer, 5);

        turnColorPanel = new JPanel();
        setAbsoluteSize(turnColorPanel, 100, 100);
        turnColorPanel.setBackground(Color.white);
        turnColorPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        turnColorPanel.setBorder(containerBorder);
        whoseTurnContainer.add(turnColorPanel);
        padLabel = headingLabel("Game Options");
        this.add(whoseTurnContainer);
    }

    // adds game option buttons
    private void addOptionButtons() {

        buttonsContainer = makeContainer(205);
        addPadding(buttonsContainer, 10);

        gameOptionsLabel = headingLabel("Game Options");
        buttonsContainer.add(gameOptionsLabel);

        addPadding(buttonsContainer, 5);
        restartButton = makeIconButton(restartIcon, "Restart the game");
        buttonsContainer.add(restartButton);

        addPadding(buttonsContainer, 5);
        undoButton = makeIconButton(undoIcon, "Undo the last move");
        buttonsContainer.add(undoButton);

        addPadding(buttonsContainer, 5);
        guideToggleButton = makeIconButton(guideIcon, "Toggle available move highlighting");
        buttonsContainer.add(guideToggleButton);

        this.add(buttonsContainer);
    }

    // returns a new JButton containing the passed icon and with the passed tool tip text
    // includes an action listener for the button
    private JButton makeIconButton(ImageIcon buttonIcon, String toolTipText) {
    	JButton b = new JButton(buttonIcon);
    	b.setToolTipText(toolTipText);
    	b.setVerticalTextPosition(AbstractButton.BOTTOM);
    	b.setHorizontalTextPosition(AbstractButton.CENTER);
    	b.setAlignmentX(Component.CENTER_ALIGNMENT);
    	b.addActionListener(this);
    	return b;
    }

    // adds combo boxes to the options panel to select piece colors
    // adds custom action listener to each JComboBox
    private void addColorOptions() {
        colorOptionsContainer = makeContainer(100);

        colorOptionsLabel = headingLabel("Piece Colours");

        addPadding(colorOptionsContainer, 10);
        colorOptionsContainer.add(colorOptionsLabel);

        blackColorBox = new JComboBox<String>(colors);
        blackColorBox.setMaximumSize(new Dimension(100,20));
        blackColorBox.setSelectedIndex(1);
        blackColorBox.addActionListener(new comboListener(BLACK, controller));

        addPadding(colorOptionsContainer, 5);
		colorOptionsContainer.add(blackColorBox);

		whiteColorBox = new JComboBox<String>(colors);
        whiteColorBox.setMaximumSize(new Dimension(100,20));
        whiteColorBox.setSelectedIndex(0);
        whiteColorBox.addActionListener(new comboListener(WHITE, controller));

        addPadding(colorOptionsContainer, 5);
		colorOptionsContainer.add(whiteColorBox);

        this.add(colorOptionsContainer);
    }

    // adds a game status display to the options panel, showing whether the player is in check or checkmate
    private void addGameStatusDisplay() {
        gameStatusContainer = makeContainer(185);
        gameStatusLabel = headingLabel("Game Status");
        addPadding(gameStatusContainer, 10);
        gameStatusContainer.add(gameStatusLabel);
        addPadding(gameStatusContainer, 5);

		checkStatusPanel = new JPanel();
		checkStatusPanel.setLayout(new GridBagLayout());
		setAbsoluteSize(checkStatusPanel, 130, 130);
        checkStatusPanel.setBackground(Color.black);

        checkPic = new JLabel();
        checkStatusPanel.add(checkPic);
        checkPic.setIcon(noCheckIcon);
        gameStatusContainer.add(checkStatusPanel);
        this.add(gameStatusContainer);
    }

	// makes a new container of the specified height and of the same width as the Display Panel
	private JPanel makeContainer(int height) {
		JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
        p.setBorder(containerBorder);
        setAbsoluteSize(p, PANEL_WIDTH, height);

        p.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.setBackground(backgroundColor);

        return p;
	}

	// adds blank padding to a panel for vertical positioning
	private void addPadding(JPanel container, int paddingHeight) {
		JPanel padding = new JPanel();
        padding.setOpaque(false);
        setAbsoluteSize(padding, PANEL_WIDTH, paddingHeight);
        container.add(padding);
	}

	private void setAbsoluteSize(JPanel p, int width, int height) {
        p.setPreferredSize(new Dimension(width, height));
        p.setMaximumSize(new Dimension(width, height));
        p.setMinimumSize(new Dimension(width, height));
	}

	// returns a new, centred label displaying the passed text
	private JLabel headingLabel(String content) {
        JLabel lbl = new JLabel(content);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setVerticalAlignment(SwingConstants.CENTER);
        return lbl;

	}

	public void updateTurnColorPanel(Color newColor){
		turnColorPanel.setBackground(newColor);
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

	// action listener for the game options buttons
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

	// Listener class for the piece colour combo boxes. Allows them to call updateChessSetColors() in the ChessController
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