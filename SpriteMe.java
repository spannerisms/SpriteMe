package SpriteMe;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class SpriteMe {
	private static final String VERSION = "v0.0.0";
	public static final int SPLOTCH_SIZE = 16;
	public static final Dimension SPLOTCH_DIMENSION = new Dimension(SPLOTCH_SIZE, SPLOTCH_SIZE);
	/*
	 * Combo box constants
	 */
	private static final ColorPair[] SKINCOLORS = new ColorPair[] {
			ColorPair.WHITE_SKIN,
			ColorPair.PALE_SKIN,
			ColorPair.HISPANIC_SKIN,
			ColorPair.BROWN_SKIN,
			ColorPair.BLACK_SKIN
	};

	public static final String[] MAIL_NAMES = {
		"Green mail", "Blue mail", "Red mail", "Bunny"
	};

	public static void main(String[] args) {
		//try to set LaF
		try {
			UIManager.setLookAndFeel("metal");
		} catch (UnsupportedLookAndFeelException
				| ClassNotFoundException
				| InstantiationException
				| IllegalAccessException e) {
			// try to set System default
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (UnsupportedLookAndFeelException
					| ClassNotFoundException
					| InstantiationException
					| IllegalAccessException e2) {
					// do nothing
			} //end System
		} // end Metal
		
		// main window
		final Dimension d = new Dimension(1000,500);
		final JFrame frame = new JFrame("Sprite Me " + VERSION);
		final JPanel controls = new JPanel(new GridBagLayout());
		GridBagConstraints w = new GridBagConstraints();
		final JComboBox<ColorPair> skinPick = new JComboBox<ColorPair>(SKINCOLORS);
		w.gridy = 0;
		controls.add(skinPick, w);

		// format main wrapper
		final Container fullWrap = new Container();
		final Dimension cd = new Dimension(600,500);
		SpringLayout l = new SpringLayout();
		fullWrap.setLayout(l);
		fullWrap.setPreferredSize(cd);
		fullWrap.setMinimumSize(cd);

		// add controls
		l.putConstraint(SpringLayout.EAST, controls, -5,
				SpringLayout.EAST, fullWrap);
		l.putConstraint(SpringLayout.NORTH, controls, 5,
				SpringLayout.NORTH, fullWrap);
		fullWrap.add(controls);

		// palette TODO: WORK THIS
		Palette pal = new Palette();
		l.putConstraint(SpringLayout.SOUTH, pal, 0,
				SpringLayout.SOUTH, fullWrap);
		l.putConstraint(SpringLayout.EAST, pal, 0,
				SpringLayout.EAST, fullWrap);
		fullWrap.add(pal);
		
		// sprite appearance
		IndexedSprite mySprite = new IndexedSprite(pal);
		l.putConstraint(SpringLayout.NORTH, mySprite, 5,
				SpringLayout.NORTH, fullWrap);
		l.putConstraint(SpringLayout.WEST, mySprite, 5,
				SpringLayout.WEST, fullWrap);
		fullWrap.add(mySprite);

		// color changer
		SpritePartEditor editor = new SpritePartEditor(pal);
		
		// format frame
		final Container framesWrap = frame.getContentPane();
		SpringLayout f = new SpringLayout();
		framesWrap.setLayout(f);
		
		f.putConstraint(SpringLayout.NORTH, fullWrap, 2,
				SpringLayout.NORTH, framesWrap);
		f.putConstraint(SpringLayout.SOUTH, fullWrap, -2,
				SpringLayout.SOUTH, framesWrap);
		f.putConstraint(SpringLayout.WEST, fullWrap, 2,
				SpringLayout.WEST, framesWrap);
		f.putConstraint(SpringLayout.EAST, fullWrap, -2,
				SpringLayout.WEST, editor);
		framesWrap.add(fullWrap);
		
		f.putConstraint(SpringLayout.NORTH, editor, 2,
				SpringLayout.NORTH, framesWrap);
		f.putConstraint(SpringLayout.SOUTH, editor, -2,
				SpringLayout.VERTICAL_CENTER, framesWrap);
		f.putConstraint(SpringLayout.EAST, editor, -2,
				SpringLayout.EAST, framesWrap);
		framesWrap.add(editor);
		
		
		// TODO : Credits
		// bazly + fish for images
		// But what if Ganon dabs back?
		ImageIcon ico = new ImageIcon(
				SpriteMe.class.getResource("/SpriteMe/Images/Link thinking small.png")
			);
		ImageIcon icoTask = new ImageIcon(
				SpriteMe.class.getResource("/SpriteMe/Images/Link thinking.png")
			);
		ArrayList<Image> icons = new ArrayList<Image>();
		icons.add(ico.getImage());
		icons.add(icoTask.getImage());
		frame.setIconImages(icons);

		// display frame
		frame.setPreferredSize(d);
		frame.setMinimumSize(d);
		setAllSizes(frame, d);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(200, 200);
		frame.setVisible(true);
		
		// Action listeners
		skinPick.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pal.setSkinColor((ColorPair) skinPick.getSelectedItem());
			}});
		
		// repainting on all sprite changes
		SpriteChangeListener repainter = new SpriteChangeListener() {

			@Override
			public void eventReceived(SpriteChangeEvent arg0) {
				editor.refreshPalette();
				frame.repaint();
			}
		};
		
		editor.addSpriteChangeListener(repainter);
		pal.addSpriteChangeListener(repainter);
	}
	
	/**
	 * Set min max and preferred size for a component
	 * @param c
	 * @param d
	 */
	private static void setAllSizes(Component c, Dimension d) {
		c.setPreferredSize(d);
		c.setMaximumSize(d);
		c.setMinimumSize(d);
	}
}