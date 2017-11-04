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

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

public class SpriteMe {
	private static final String VERSION = "v0.0.0";
	public static final int SPLOTCH_SIZE = 16;
	public static final Dimension SPLOTCH_DIMENSION = new Dimension(SPLOTCH_SIZE, SPLOTCH_SIZE);
	private static final Border rightPad = BorderFactory.createEmptyBorder(0,0,0,5);
	/*
	 * Combo box constants
	 */
	public static final ColorPair[] SKINCOLORS = new ColorPair[] {
			ColorPair.WHITE_SKIN,
			ColorPair.PALE_SKIN,
			ColorPair.HISPANIC_SKIN,
			ColorPair.BROWN_SKIN,
			ColorPair.BLACK_SKIN
	};

	public static final String[] MAIL_NAMES = {
		"Green mail", "Blue mail", "Red mail", "Bunny"
	};

	public static final SpritePart[] ACCESSORIES = new SpritePart[] {
		SpritePart.NOTHING, SpritePart.GLASSES,	
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
		final Dimension d = new Dimension(1000,700);
		final JFrame frame = new JFrame("Sprite Me " + VERSION);
		final JPanel controls = new JPanel(new GridBagLayout());
		GridBagConstraints w = new GridBagConstraints();

		w.gridy = -1;
		w.fill = GridBagConstraints.HORIZONTAL;

		// mail preview
		final JLabel mailLbl = new JLabel("Mail preview", SwingConstants.RIGHT);
		final JComboBox<String> mailPick = new JComboBox<String>(MAIL_NAMES);
		mailLbl.setBorder(rightPad);
		w.gridy++;
		w.gridx = 0;
		controls.add(mailLbl,w);
		w.gridx = 1;
		controls.add(mailPick, w);
		
		// skin color
		final JLabel skinLbl = new JLabel("Skin color", SwingConstants.RIGHT);
		final JComboBox<ColorPair> skinPick = new JComboBox<ColorPair>(SKINCOLORS);
		skinLbl.setBorder(rightPad);
		w.gridy++;
		w.gridx = 0;
		controls.add(skinLbl,w);
		w.gridx = 1;
		controls.add(skinPick, w);

		// accessories
		final JLabel acc1Lbl = new JLabel("Accessory 1", SwingConstants.RIGHT);
		final JComboBox<SpritePart> acc1Pick = new JComboBox<SpritePart>(ACCESSORIES);
		final JButton acc1Edit = new JButton("Edit");
		acc1Lbl.setBorder(rightPad);
		w.gridy++;
		w.gridx = 0;
		controls.add(acc1Lbl,w);
		w.gridx = 1;
		controls.add(acc1Pick, w);
		w.gridx = 2;
		controls.add(acc1Edit, w);

		// format frame
		final Container framesWrap = frame.getContentPane();
		SpringLayout f = new SpringLayout();
		framesWrap.setLayout(f);

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

		// palette
		Palette pal = new Palette();
		f.putConstraint(SpringLayout.NORTH, pal, 0,
				SpringLayout.NORTH, framesWrap);
		f.putConstraint(SpringLayout.EAST, pal, 0,
				SpringLayout.EAST, framesWrap);
		framesWrap.add(pal);
		
		// sprite appearance
		IndexedSprite mySprite = new IndexedSprite(pal);
		l.putConstraint(SpringLayout.NORTH, mySprite, 5,
				SpringLayout.NORTH, fullWrap);
		l.putConstraint(SpringLayout.WEST, mySprite, 5,
				SpringLayout.WEST, fullWrap);
		fullWrap.add(mySprite);

		// color changer
		ColorEditor colorEditor = new ColorEditor(pal);
		SpritePartEditor indexMapEditor = new SpritePartEditor(pal);
		pal.attachEditor(colorEditor);
		
		// wrapper frame
		f.putConstraint(SpringLayout.NORTH, fullWrap, 2,
				SpringLayout.NORTH, framesWrap);
		f.putConstraint(SpringLayout.SOUTH, fullWrap, -2,
				SpringLayout.SOUTH, framesWrap);
		f.putConstraint(SpringLayout.WEST, fullWrap, 2,
				SpringLayout.WEST, framesWrap);
		framesWrap.add(fullWrap);
		
		// color editor
		f.putConstraint(SpringLayout.NORTH, colorEditor, 2,
				SpringLayout.SOUTH, pal);
		f.putConstraint(SpringLayout.SOUTH, colorEditor, 2,
				SpringLayout.SOUTH, framesWrap);
		f.putConstraint(SpringLayout.EAST, colorEditor, -2,
				SpringLayout.EAST, framesWrap);
		framesWrap.add(colorEditor);
		
		// index map
		l.putConstraint(SpringLayout.SOUTH, indexMapEditor, -2,
				SpringLayout.SOUTH, fullWrap);
		l.putConstraint(SpringLayout.EAST, indexMapEditor, -2,
				SpringLayout.EAST, fullWrap);
		fullWrap.add(indexMapEditor);

		// TODO : Credits
		// bazly + fish for images
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
		frame.setLocation(200, 100);
		frame.setVisible(true);
		
		// repainting on all sprite changes
		SpriteChangeListener repainter =
				arg0 -> {
					indexMapEditor.refreshPalette();
					frame.repaint();
				};
		
		indexMapEditor.addSpriteChangeListener(repainter);
		pal.addSpriteChangeListener(repainter);
		mySprite.addSpriteChangeListener(repainter);

		// Action listeners for controls
		skinPick.addActionListener(
				arg0 -> pal.setSkinColor((ColorPair) skinPick.getSelectedItem())
			);

		mailPick.addActionListener(
				arg0 -> mySprite.setMail(mailPick.getSelectedIndex())
			);

		acc1Pick.addActionListener(
				arg0 -> mySprite.setAccessory((SpritePart) acc1Pick.getSelectedItem(), 1)
			);

		acc1Edit.addActionListener(
			arg0 ->	{
				SpritePart picked = (SpritePart) acc1Pick.getSelectedItem();
				if (picked == SpritePart.NOTHING) {
					indexMapEditor.editNewPart(null);
				} else {
					indexMapEditor.editNewPart(picked);
				}
			});
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