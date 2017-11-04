package SpriteMe;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

public class Splotch extends JComponent {
	private static final long serialVersionUID = -2349012926208164404L;
	private static final int SIZE = SpriteMe.SPLOTCH_SIZE;

	// locals
	private SpriteColor color;
	private boolean editable = true;
	private final Palette mommy;
	private final int index;

	/**
	 * Creates a new {@code Splotch} connected to
	 * a {@code Palette object},
	 * at index {@code i},
	 * and set to hold a {@code SpriteColor}.
	 * @param parent - {@code Palette} object
	 * @param i - index
	 * @param c - {@code SpriteColor} object
	 */
	public Splotch(Palette parent, int i, SpriteColor c) {
		mommy = parent;
		index = i;
		color = c;
		this.setForeground(color.toColor());
		this.setBackground(color.toColor());
		this.setSize(SpriteMe.SPLOTCH_DIMENSION);
		this.setMinimumSize(SpriteMe.SPLOTCH_DIMENSION);
		this.setPreferredSize(SpriteMe.SPLOTCH_DIMENSION);
		setToolTip();
		addMouse();
	}

	/**
	 * @return The {@code SpriteColor} object currently associated with this splotch.
	 */
	public SpriteColor getColor() {
		return color;
	}

	/**
	 * @return The RGB coordinates currently associated with this splotch.
	 */
	public int[] getColorVals() {
		byte[] t = color.getRGB();
		return new int[] {
				Byte.toUnsignedInt(t[0]),
				Byte.toUnsignedInt(t[1]),
				Byte.toUnsignedInt(t[2]),
		};
	}

	/**
	 * Sets the splotch's reference to a new color.
	 * @param c - new {@code SpriteColor
	 */
	public void setColor(SpriteColor c) {
		color = c;
		this.setForeground(color.toColor());
		this.setBackground(color.toColor());
		mommy.colorChanged(this);
		setToolTip();
	}

	/**
	 * 
	 */
	public void setEnabled(boolean e) {
		editable = e;
		setToolTip();
	}

	/**
	 * Self-use function to set a tooltip that may also indicate
	 * that the color index cannot be edited.
	 */
	private void setToolTip() {
		String changeable = editable ?
				"" :
				" - This color cannot be edited.";
		this.setToolTipText(this.toString() + changeable);
	}

	/**
	 * 
	 */
	public String toString() {
		return color.toFullString();
	}

	/**
	 * 
	 */
	public void paint(Graphics g) {
		g.fillRect(0, 0, SIZE, SIZE);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, SIZE, SIZE);
	}

	/**
	 * Used to add mouse listeners
	 */
	private void addMouse() {
		this.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent arg0) {
				mommy.indexClicked(index);
			}

			public void mouseEntered(MouseEvent arg0) {}

			public void mouseExited(MouseEvent arg0) {}

			public void mousePressed(MouseEvent arg0) {
				mommy.indexClicked(index);
			}

			public void mouseReleased(MouseEvent arg0) {}
		});
	} // end addMouse
}