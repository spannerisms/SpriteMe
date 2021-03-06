package spriteme;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import spritemanipulator.SpriteManipulator;

public class SplotchEditor extends Container {
	private static final long serialVersionUID = -5665184823715239064L;

	// dimension constants
	private static final Dimension sliderD = new Dimension(120, 10);
	private static final Dimension textD = new Dimension(40, 20);
	private static final Dimension labelD = new Dimension(20, 20);

	// local vars
	// color and splotch information
	private final Splotch victim;
	private final SpriteColor originalColor;
	private String colorName = "Custom color";
	private final int[] RGB;
	private boolean enabled;
	private boolean isAllFour = false;
	private Splotch[] allFourVictims;

	// GUI vars
	private final GridBagLayout w = new GridBagLayout();

	// sliders
	private final JSlider[] sliders;
	private final JSlider red;
	private final JSlider green;
	private final JSlider blue;

	// text input
	private final JFormattedTextField[] vals;
	private final JFormattedTextField redT;
	private final JFormattedTextField greenT;
	private final JFormattedTextField blueT;

	// buttons
	private final JButton apply = new JButton("Apply");
	private final JButton reset = new JButton("Reset");
	private final JButton darker = new JButton("Darken");
	private final JButton lighter = new JButton("Lighten");
	private final JButton presets = new JButton("Choose a preset color");

	// color preview
	private final ColorPreview p = new ColorPreview();
	private final PresetSplotchChooser PRESETS;

	// for location
	private final SplotchBlob mommy;

	/**
	 *
	 * @param c - Splotch to apply color edits to.
	 * @param e - Enable/Disabled
	 */
	public SplotchEditor(PresetSplotchChooser chooser, SplotchBlob parent, Splotch c, boolean e) {
		PRESETS = chooser;
		mommy = parent;
		victim = c;
		enabled = e;
		RGB = c.getColorVals();
		originalColor = new SpriteColor(c.getColorName(), RGB[0], RGB[1], RGB[2]); // new object so it never changes
		sliders = new JSlider[] {
				new JSlider(JSlider.HORIZONTAL, 0, 31, RGB[0]/8),
				new JSlider(JSlider.HORIZONTAL, 0, 31, RGB[1]/8),
				new JSlider(JSlider.HORIZONTAL, 0, 31, RGB[2]/8)
			};

		vals = new JFormattedTextField[3];
		NumberFormat rgbFormat = NumberFormat.getNumberInstance();
		rgbFormat.setMaximumFractionDigits(3);

		vals[0] = new JFormattedTextField(rgbFormat);
		vals[1] = new JFormattedTextField(rgbFormat);
		vals[2] = new JFormattedTextField(rgbFormat);

		red = sliders[0];
		green = sliders[1];
		blue = sliders[2];

		redT = vals[0];
		greenT = vals[1];
		blueT = vals[2];

		redT.setValue(RGB[0]);
		greenT.setValue(RGB[1]);
		blueT.setValue(RGB[2]);

		p.setColor(RGB);
		initializeDisplay();
		addListeners();

		reset.doClick();
	}

	/**
	 * Sets up child {@code Components}.
	 */
	private void initializeDisplay() {
		this.setLayout(w);
		setEnabling();

		// sizes
		red.setMaximumSize(sliderD);
		red.setPreferredSize(sliderD);
		green.setMaximumSize(sliderD);
		green.setPreferredSize(sliderD);
		blue.setMaximumSize(sliderD);
		blue.setPreferredSize(sliderD);

		// red
		GridBagConstraints l = new GridBagConstraints();
		l.fill = GridBagConstraints.HORIZONTAL;

		// sizes
		// sliders
		red.setMaximumSize(sliderD);
		red.setPreferredSize(sliderD);
		green.setMaximumSize(sliderD);
		green.setPreferredSize(sliderD);
		blue.setMaximumSize(sliderD);
		blue.setPreferredSize(sliderD);

		// text fields
		redT.setMaximumSize(textD);
		redT.setPreferredSize(textD);
		greenT.setMaximumSize(textD);
		greenT.setPreferredSize(textD);
		blueT.setMaximumSize(textD);
		blueT.setPreferredSize(textD);

		// preview
		l.gridy = 0;
		l.gridx = 0;
		l.gridheight = 3;
		this.add(p, l);

		l.gridheight = 1;

		// red
		l.gridy = 0;
		final JLabel lr = new JLabel("R", SwingConstants.CENTER);
		lr.setPreferredSize(labelD);
		lr.setMinimumSize(labelD);

		l.gridx = 1;
		this.add(lr,l);
		l.gridx = 2;
		this.add(red, l);
		l.gridx = 3;
		this.add(redT, l);

		// apply button
		l.gridx = 4;
		this.add(apply, l);

		// green
		l.gridy = 1;
		final JLabel lg = new JLabel("G", SwingConstants.CENTER);
		lg.setPreferredSize(labelD);
		lg.setMinimumSize(labelD);

		l.gridx = 1;
		this.add(lg,l);
		l.gridx = 2;
		this.add(green, l);
		l.gridx = 3;
		this.add(greenT, l);

		l.gridx = 4;
		this.add(darker, l);

		// blue
		l.gridy = 2;
		final JLabel lb = new JLabel("B", SwingConstants.CENTER);
		lb.setPreferredSize(labelD);
		lb.setMinimumSize(labelD);

		l.gridx = 1;
		this.add(lb,l);
		l.gridx = 2;
		this.add(blue, l);
		l.gridx = 3;
		this.add(blueT, l);

		l.gridx = 4;
		this.add(lighter, l);

		// preset colors
		l.gridy = 3;
		l.gridwidth = 2;
		l.gridx = 2;
		this.add(presets,l);

		l.gridwidth = 1;
		l.gridx = 4;
		this.add(reset, l);
	} // end display initialization

	/**
	 * Adds various listeners to sliders, fields, and buttons.
	 */
	private void addListeners() {
		// sliders
		ChangeListener slideListener = slideListen();
		ChangeListener repaintListener = repaintListen();
		for (JSlider s : sliders) {
			s.addChangeListener(slideListener);
			s.addChangeListener(repaintListener);
		}

		// text fields
		PropertyChangeListener textListener = textListen();
		for (JFormattedTextField v : vals) {
			v.addPropertyChangeListener(textListener);
		}

		// buttons
		// apply color
		apply.addActionListener(
			arg0 -> {
				if (isAllFour && allFourVictims != null) {
					for (Splotch s : allFourVictims) {
						s.setColor(getColor());
					}
				} else {
					victim.setColor(getColor());
				}
			}); // end listener

		// darker button
		darker.addActionListener(
			arg0 -> {
				byte[] RGB2 = SpriteColor.darkerShade(new byte[] {
						(byte) RGB[0],
						(byte) RGB[1],
						(byte) RGB[2]
					});

				SplotchEditor.this.setColor(
						RGB2[0],RGB2[1],RGB2[2]
					);
			}); // end listener

		// lighter button
		lighter.addActionListener(
			arg0 -> {
				byte[] RGB2 = SpriteColor.lighterShade(new byte[] {
						(byte) RGB[0],
						(byte) RGB[1],
						(byte) RGB[2]
					});

				SplotchEditor.this.setColor(
						RGB2[0],
						RGB2[1],
						RGB2[2]
					);
			}); // end listener

		// preset color chooser
		presets.addActionListener(
			arg0 -> {
				PRESETS.setPartner(this);
				if (!PRESETS.isVisible()) {
					PRESETS.setVisible(true);
					Point t = mommy.getLocationOnScreen();
					PRESETS.setLocation((int) t.getX()+120, (int) t.getY());
				}
				PRESETS.requestFocus();
			}); // end listener

		// reset button
		reset.addActionListener(arg0 -> SplotchEditor.this.resetColor());
	} // end Listeners

	/**
	 * Clicks the apply button
	 */
	public void apply() {
		apply.doClick();
	}

	/**
	 * Sets color back to original before any editing.
	 */
	private void resetColor() {
		setColor(originalColor);
	}

	/**
	 * Copies a {@code SpriteColor} and adjusts GUI inputs.
	 * @param c - {@link SpriteColor} to copy
	 */
	public void setColor(SpriteColor c) {
		byte[] t = c.getRGB();

		RGB[0] = Byte.toUnsignedInt(t[0]);
		RGB[1] = Byte.toUnsignedInt(t[1]);
		RGB[2] = Byte.toUnsignedInt(t[2]);

		sliders[0].setValue(RGB[0]/8);
		sliders[1].setValue(RGB[1]/8);
		sliders[2].setValue(RGB[2]/8);

		colorName = c.toString();
	}

	/**
	 * Copies color and adjusts GUI inputs.
	 */
	private void setColor(byte r, byte g, byte b) {
		RGB[0] = Byte.toUnsignedInt(r);
		RGB[1] = Byte.toUnsignedInt(g);
		RGB[2] = Byte.toUnsignedInt(b);

		sliders[0].setValue(RGB[0]/8);
		sliders[1].setValue(RGB[1]/8);
		sliders[2].setValue(RGB[2]/8);

		colorName = "Custom color";
	}

	public SpriteColor getColor() {
		return new SpriteColor(colorName, RGB[0], RGB[1], RGB[2]);
	}

	/**
	 *
	 */
	public void setEnabled(boolean e) {
		enabled = e;
		// hide the Preset Chooser if disabled
		if (!e && PRESETS.checkPartner(this)) {
			PRESETS.setVisible(false);
		}
		setEnabling();
	}

	/**
	 * Adjusts child components based on enable status.
	 */
	private void setEnabling() {
		for (Component c : this.getComponents()) {
			c.setEnabled(enabled);
		}
	}

	/**
	 * Determines if this editor applies its color to all mails or just 1.
	 * @param b
	 */
	public void editAllMails(boolean b) {
		isAllFour = b;
	}

	/**
	 * Sets object references to all four splotches at this color's index.
	 * @param allFourVictims
	 */
	public void setFourVictims(Splotch[] allFourVictims) {
		this.allFourVictims = allFourVictims;
	}

	/**
	 * Creates a {@link ChangeListener} that adjusts the respective {@code JFormattedTextField}.
	 */
	private ChangeListener slideListen() {
		return new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				int val = source.getValue();
				val *= 8;
				JFormattedTextField t = null;

				if (source == red) {
					RGB[0] = val;
					t = redT;
				} else if (source == green) {
					RGB[1] = val;
					t = greenT;
				} else if (source == blue) {
					RGB[2] = val;
					t = blueT;
				}
				t.setValue(val);
				t.setText(val+"");
			}
		};
	}

	/**
	 * Creates a {@link ChangeListener} that tells the {@link ColorPreview} to update itself.
	 */
	private ChangeListener repaintListen() {
		return new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				p.setColor(RGB);
				p.repaint();
			}
		};
	}

	/**
	 * Creates a {@link PropertyChangeListener} that validates the text field and
	 * adjusts the respective {@code JSlider}.
	 */
	private PropertyChangeListener textListen() {
		return new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				JFormattedTextField source = (JFormattedTextField) e.getSource();

				int val = ((Number) source.getValue()).intValue();
				if (val < 0) {
					val = 0;
				} else if (val > 248) {
					val = 248;
				}
				val = SpriteManipulator.roundVal(val);

				JSlider s = null;
				if (source == redT) {
					RGB[0] = val;
					s = red;
				} else if (source == greenT) {
					RGB[1] = val;
					s = green;
				} else if (source == blueT) {
					RGB[2] = val;
					s = blue;
				}
				s.setValue(val/8);
			}
		};
	}

	// preview class for drawing squares
	@SuppressWarnings("serial")
	private static class ColorPreview extends Component {
		public static final int SIZE = 40;
		static final Dimension D = new Dimension(SIZE,SIZE);

		private static final Color BLACK = Color.BLACK;
		private static final Color BLACK_OFF = new Color(0,0,0,50);

		// locals
		private boolean enabled = true;
		private Color c = new Color(0,0,0);
		private Color cOff = new Color(0,0,0,50);
		private Color curColor;
		private Color curOutline;

		public ColorPreview() {
			this.setSize(D);
			this.setPreferredSize(D);
			this.setMinimumSize(D);
			this.setMaximumSize(D);
		}

		/**
		 * Sets a new color with RGB values.
		 * @param rgb
		 */
		public void setColor(int[] rgb) {
			c = new Color(rgb[0], rgb[1], rgb[2]);
			cOff = new Color(rgb[0], rgb[1], rgb[2], 50);
			curColor = c;
			curOutline = BLACK;
		}

		public void setEnabled(boolean b) {
			enabled = b;
			if (enabled) {
				curColor = c;
				curOutline = BLACK;
			} else {
				curColor = cOff;
				curOutline = BLACK_OFF;
			}
		}

		public void paint(Graphics g) {
			g.setColor(curColor);
			g.fillRect(0, 0, SIZE, SIZE);
			g.setColor(curOutline);
			g.drawRect(0, 0, SIZE-1, SIZE-1);
		}
	} // end ColorPreview
}