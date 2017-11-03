package SpriteMe;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

public class Splotch extends JComponent {

	private static final long serialVersionUID = -2349012926208164404L;
	private static final int SIZE = SpriteMe.SPLOTCH_SIZE;
	private SpriteColor color;
	private boolean editable = true;
	private final Palette mommy;
	private final int index;
	/**
	 * 
	 * @param c
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

	public SpriteColor getColor() {
		return color;
	}

	/**
	 * 
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
	 * 
	 * @param c
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
	 * @param x
	 */
	public void setEditable(boolean x) {
		editable = x;
		setToolTip();
	}
	/**
	 * 
	 */
	private void setToolTip() {
		String changeable = editable ?
				"" :
				" - This color cannot be edited.";
		this.setToolTipText(this.toString() + changeable);
	}

	public String toString() {
		return color.toFullString();
	}

	public void paint(Graphics g) {
		g.fillRect(0, 0, SIZE, SIZE);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, SIZE, SIZE);
	}

	private void addMouse() {
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Color editing maybe?
				mommy.indexClicked(index);
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

			}

			@Override
			public void mouseExited(MouseEvent arg0) {}

			@Override
			public void mousePressed(MouseEvent arg0) {
				mommy.indexClicked(index);
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {}

		});
	}
}