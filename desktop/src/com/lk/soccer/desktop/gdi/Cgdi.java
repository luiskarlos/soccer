/**
 *  Desc:   A singleton class to help alleviate the tedium of using the
 *          GDI. Call each method using the #define for gdi->
 *          eg gdi->Line(10, 20, 300, 300)
 *          You must always call gdi->StartDrawing() prior to any 
 *          rendering, and isComplete any rendering with gdi->StopDrawing()
 *
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.soccer.desktop.gdi;

import static com.lk.engine.common.d2.Vector2D.add;
import static com.lk.engine.common.d2.Vector2D.mul;
import static com.lk.engine.common.d2.Vector2D.sub;
import static com.lk.engine.common.d2.Vector2D.vec2DNormalize;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.List;

import com.lk.engine.common.d2.UVector2D;
import com.lk.engine.common.d2.Vector2D;

public class Cgdi {
	// ------------------------------- define some colors
	final public Color[] colors = { new Color(255, 0, 0), new Color(0, 0, 255), new Color(0, 255, 0), new Color(0, 0, 0),
	    new Color(255, 200, 200),
	    new Color(200, 200, 200), // grey
	    new Color(255, 255, 0), new Color(255, 170, 0), new Color(255, 0, 170), new Color(133, 90, 0),
	    new Color(255, 255, 255), new Color(0, 100, 0), // dark
	                                                    // green
	    new Color(0, 255, 255), // light BLUE
	    new Color(200, 200, 200), // light grey
	    new Color(255, 230, 230) // light pink
	};
	public final int NumColors = colors.length;

	public int NumPenColors() {
		return NumColors;
	}

	private Font m_OldFont;
	// enumerate some colors
	public final static int red = 0;
	public final static int blue = 1;
	public final static int green = 2;
	public final static int black = 3;
	public final static int pink = 4;
	public final static int grey = 5;
	public final static int yellow = 6;
	public final static int orange = 7;
	public final static int purple = 8;
	public final static int brown = 9;
	public final static int white = 10;
	public final static int dark_green = 11;
	public final static int light_blue = 12;
	public final static int light_grey = 13;
	public final static int light_pink = 14;
	public final static int hollow = 15;
	private Color m_OldPen;
	// all the pens
	private final Color blackPen;
	private final Color whitePen;
	private final Color redPen;
	private final Color greenPen;
	private final Color bluePen;
	private final Color greyPen;
	private final Color m_PinkPen;
	private final Color m_OrangePen;
	private final Color m_YellowPen;
	private final Color m_PurplePen;
	private final Color m_BrownPen;
	private final Color m_DarkGreenPen;
	private final Color m_LightBluePen;
	private final Color m_LightGreyPen;
	private final Color m_LightPinkPen;
	private final Color m_ThickBlackPen;
	private final Color m_ThickWhitePen;
	private final Color m_ThickRedPen;
	private final Color m_ThickGreenPen;
	private final Color m_ThickBluePen;

	private Brush m_OldBrush;
	// all the brushes
	private final Brush m_RedBrush;
	private final Brush m_GreenBrush;
	private final Brush m_BlueBrush;
	private final Brush m_GreyBrush;
	private final Brush m_BrownBrush;
	private final Brush m_YellowBrush;
	private final Brush m_OrangeBrush;
	private final Brush m_LightBlueBrush;
	private final Brush m_DarkGreenBrush;
	private Graphics2D m_hdc;

	public void fillRect(final Color c, final int left, final int top, final int width, final int height) {
		final Color old = m_hdc.getColor();
		m_hdc.setColor(c);
		m_hdc.fillRect(left, top, width, height);
		m_hdc.setColor(old);
	}

	public int fontHeight() {
		if (m_hdc == null) {
			return 0;
		}
		return m_hdc.getFontMetrics().getHeight();
	}

	public class Brush extends Color {
		private static final long serialVersionUID = 1L;

		public Brush(final int rgb) {
			super(rgb);
		}

		public Brush(final Color c) {
			super(c.getRGB());
		}

		public Brush(final int r, final int g, final int b) {
			super(r, g, b);
		}
	}

	// constructor is private
	public Cgdi() {
		blackPen = colors[black];
		whitePen = colors[white];
		redPen = colors[red];
		greenPen = colors[green];
		bluePen = colors[blue];
		greyPen = colors[grey];
		m_PinkPen = colors[pink];
		m_YellowPen = colors[yellow];
		m_OrangePen = colors[orange];
		m_PurplePen = colors[purple];
		m_BrownPen = colors[brown];

		m_DarkGreenPen = colors[dark_green];

		m_LightBluePen = colors[light_blue];
		m_LightGreyPen = colors[light_grey];
		m_LightPinkPen = colors[light_pink];

		m_ThickBlackPen = colors[black];
		m_ThickWhitePen = colors[white];
		m_ThickRedPen = colors[red];
		m_ThickGreenPen = colors[green];
		m_ThickBluePen = colors[blue];

		m_GreenBrush = new Brush(colors[green]);
		m_RedBrush = new Brush(colors[red]);
		m_BlueBrush = new Brush(colors[blue]);
		m_GreyBrush = new Brush(colors[grey]);
		m_BrownBrush = new Brush(colors[brown]);
		m_YellowBrush = new Brush(colors[yellow]);
		m_LightBlueBrush = new Brush(0, 255, 255);
		m_DarkGreenBrush = new Brush(colors[dark_green]);
		m_OrangeBrush = new Brush(colors[orange]);

		m_hdc = null;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("Cloning not allowed");
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	public void blackPen() {
		if (m_hdc != null) {
			SelectObject(m_hdc, blackPen);
		}
	}

	public void whitePen() {
		if (m_hdc != null) {
			SelectObject(m_hdc, whitePen);
		}
	}

	public void redPen() {
		if (m_hdc != null) {
			SelectObject(m_hdc, redPen);
		}
	}

	public void greenPen() {
		if (m_hdc != null) {
			SelectObject(m_hdc, greenPen);
		}
	}

	public void bluePen() {
		if (m_hdc != null) {
			SelectObject(m_hdc, bluePen);
		}
	}

	public void greyPen() {
		if (m_hdc != null) {
			SelectObject(m_hdc, greyPen);
		}
	}

	public void pinkPen() {
		if (m_hdc != null) {
			SelectObject(m_hdc, m_PinkPen);
		}
	}

	public void yellowPen() {
		if (m_hdc != null) {
			SelectObject(m_hdc, m_YellowPen);
		}
	}

	public void orangePen() {
		if (m_hdc != null) {
			SelectObject(m_hdc, m_OrangePen);
		}
	}

	public void purplePen() {
		if (m_hdc != null) {
			SelectObject(m_hdc, m_PurplePen);
		}
	}

	public void brownPen() {
		if (m_hdc != null) {
			SelectObject(m_hdc, m_BrownPen);
		}
	}

	public void darkGreenPen() {
		if (m_hdc != null) {
			SelectObject(m_hdc, m_DarkGreenPen);
		}
	}

	public void lightBluePen() {
		if (m_hdc != null) {
			SelectObject(m_hdc, m_LightBluePen);
		}
	}

	public void lightGreyPen() {
		if (m_hdc != null) {
			SelectObject(m_hdc, m_LightGreyPen);
		}
	}

	public void lightPinkPen() {
		if (m_hdc != null) {
			SelectObject(m_hdc, m_LightPinkPen);
		}
	}

	public void thickBlackPen() {
		if (m_hdc != null) {
			SelectObject(m_hdc, m_ThickBlackPen);
		}
	}

	public void thickWhitePen() {
		if (m_hdc != null) {
			SelectObject(m_hdc, m_ThickWhitePen);
		}
	}

	public void thickRedPen() {
		if (m_hdc != null) {
			SelectObject(m_hdc, m_ThickRedPen);
		}
	}

	public void thickGreenPen() {
		if (m_hdc != null) {
			SelectObject(m_hdc, m_ThickGreenPen);
		}
	}

	public void thickBluePen() {
		if (m_hdc != null) {
			SelectObject(m_hdc, m_ThickBluePen);
		}
	}

	public void blackBrush() {
		if (m_hdc != null) {
			SelectObject(m_hdc, new Brush(Color.BLACK));
		}
	}

	public void whiteBrush() {
		if (m_hdc != null) {
			SelectObject(m_hdc, new Brush(Color.WHITE));
		}
	}

	public void hollowBrush() {
		if (m_hdc != null) {
			BrushColor = null;
		}
	}

	public void greenBrush() {
		if (m_hdc != null) {
			SelectObject(m_hdc, m_GreenBrush);
		}
	}

	public void redBrush() {
		if (m_hdc != null) {
			SelectObject(m_hdc, m_RedBrush);
		}
	}

	public void blueBrush() {
		if (m_hdc != null) {
			SelectObject(m_hdc, m_BlueBrush);
		}
	}

	public void greyBrush() {
		if (m_hdc != null) {
			SelectObject(m_hdc, m_GreyBrush);
		}
	}

	public void brownBrush() {
		if (m_hdc != null) {
			SelectObject(m_hdc, m_BrownBrush);
		}
	}

	public void yellowBrush() {
		if (m_hdc != null) {
			SelectObject(m_hdc, m_YellowBrush);
		}
	}

	public void lightBlueBrush() {
		if (m_hdc != null) {
			SelectObject(m_hdc, m_LightBlueBrush);
		}
	}

	public void darkGreenBrush() {
		if (m_hdc != null) {
			SelectObject(m_hdc, m_DarkGreenBrush);
		}
	}

	public void orangeBrush() {
		if (m_hdc != null) {
			SelectObject(m_hdc, m_OrangeBrush);
		}
	}

	private Color PenColor = Color.BLACK;
	private Color BrushColor = null;

	private void SelectObject(final Graphics2D m_hdc, final Color color) {
		if (color instanceof Brush) {
			BrushColor = color;
		} else {
			PenColor = color;
		}
	}

	// ALWAYS call this before drawing
	public void startDrawing(final Graphics2D hdc) {
		assert (m_hdc == null);

		m_hdc = hdc;

		// get the current pen
		m_OldPen = hdc.getColor();
		m_OldBrush = new Brush(hdc.getBackground());
		m_OldFont = hdc.getFont();
		hdc.setFont(new Font(m_OldFont.getFontName(), Font.BOLD, 12));
		m_hdc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	// ALWAYS call this after drawing
	public void stopDrawing(final Graphics2D hdc) {
		assert (hdc != null);

		hdc.setColor(m_OldPen);
		hdc.setBackground(m_OldBrush);
		hdc.setFont(m_OldFont);
		m_hdc = null;
	}

	// ---------------------------Text
	private boolean opaque = false;
	private Color textColor = Color.BLACK;
	private final Color bg = Color.WHITE;

	public void textAtPos(final int x, int y, final String s) {
		final Color back = m_hdc.getColor();
		y += fontHeight() - 2;
		if (opaque) {
			final FontMetrics fm = m_hdc.getFontMetrics();
			// final int lineHeight = fm.getHeight();
			m_hdc.setColor(bg);
			m_hdc.fillRect(x, y - fm.getAscent() + fm.getDescent(), fm.stringWidth(s), fm.getAscent());
		}
		m_hdc.setColor(textColor);
		m_hdc.drawString(s, x, y);
		m_hdc.setColor(back);
	}

	public void textAtPos(final double x, final double y, final String s) {
		textAtPos((int) x, (int) y, s);
	}

	public void textAtPos(final UVector2D pos, final String s) {
		textAtPos((int) pos.x(), (int) pos.y(), s);
	}

	public void transparentText() {
		opaque = false;
	}

	public void opaqueText() {
		opaque = true;
	}

	public void textColor(final int color) {
		assert (color < NumColors);
		textColor = colors[color];
	}

	public void textColor(final int r, final int g, final int b) {
		textColor = new Color(r, g, b);
	}

	// ----------------------------pixels
	public void drawDot(final Vector2D pos, final Color color) {
		drawDot((int) pos.x, (int) pos.y, color);
	}

	public void drawDot(final int x, final int y, final Color color) {
		m_hdc.setColor(BrushColor);
		m_hdc.fillRect(x, y, 0, 0);
	}

	// -------------------------Line Drawing
	public void line(final UVector2D from, final UVector2D to) {
		line(from.x(), from.y(), to.x(), to.y());
	}

	public void line(final int a, final int b, final int x, final int y) {
		m_hdc.setColor(PenColor);
		m_hdc.drawLine(a, b, x, y);
	}

	public void line(final double a, final double b, final double x, final double y) {
		line((int) a, (int) b, (int) x, (int) y);
	}

	public void polyLine(final List<Vector2D> points) {
		// make sure we have at least 2 points
		if (points.size() < 2) {
			return;
		}
		final Polygon p = new Polygon();

		for (final Vector2D v : points) {
			p.addPoint((int) v.x, (int) v.y);
		}
		m_hdc.setColor(PenColor);
		m_hdc.drawPolygon(p);
	}

	public void lineWithArrow(final Vector2D from, final Vector2D to, final double size) {
		final Vector2D norm = vec2DNormalize(sub(to, from));

		// calculate where the arrow is attached
		final Vector2D CrossingPoint = sub(to, mul(norm, size));

		// calculate the two extra points required to make the arrowhead
		final Vector2D ArrowPoint1 = add(CrossingPoint, (mul(norm.perp(), 0.4f * size)));
		final Vector2D ArrowPoint2 = add(CrossingPoint, (mul(norm.perp(), 0.4f * size)));

		// draw the line
		m_hdc.setColor(PenColor);
		m_hdc.drawLine((int) from.x, (int) from.y, (int) CrossingPoint.x, (int) CrossingPoint.y);

		// draw the arrowhead (filled with the currently selected brush)
		final Polygon p = new Polygon();

		p.addPoint((int) ArrowPoint1.x, (int) ArrowPoint1.y);
		p.addPoint((int) ArrowPoint2.x, (int) ArrowPoint2.y);
		p.addPoint((int) to.x, (int) to.y);

		// SetPolyFillMode(m_hdc, WINDING);
		if (BrushColor != null) {
			m_hdc.setColor(BrushColor);
			m_hdc.fillPolygon(p);
		}
	}

	public void cross(final Vector2D pos, final int diameter) {
		line((int) pos.x - diameter, (int) pos.y - diameter, (int) pos.x + diameter, (int) pos.y + diameter);
		line((int) pos.x - diameter, (int) pos.y + diameter, (int) pos.x + diameter, (int) pos.y - diameter);
	}

	// ---------------------Geometry drawing methods
	public void rect(int left, final int top, int right, final int bot) {
		if (left > right) {
			final int tmp = right;
			right = left;
			left = tmp;
		}
		m_hdc.setColor(PenColor);
		m_hdc.drawRect(left, top, right - left, bot - top);
		if (BrushColor != null) {
			m_hdc.setColor(BrushColor);
			m_hdc.fillRect(left, top, right - left, bot - top);
		}

	}

	public void rect(final double left, final double top, final double right, final double bot) {
		rect((int) left, (int) top, (int) right, (int) bot);
	}

	public void closedShape(final List<Vector2D> points) {
		final Polygon pol = new Polygon();

		for (final Vector2D p : points) {
			pol.addPoint((int) p.x, (int) p.y);
		}
		m_hdc.setColor(PenColor);
		m_hdc.drawPolygon(pol);
		if (BrushColor != null) {
			// m_hdc.setColor(BrushColor);
			// m_hdc.fillPolygon(pol);
		}
	}

	public void circle(final UVector2D pos, final double radius) {
		circle(pos.x(), pos.y(), radius);
	}

	public void circle(final double x, final double y, final double radius) {
		m_hdc.setColor(PenColor);
		m_hdc.drawOval((int) (x - radius), (int) (y - radius), (int) (radius * 2), (int) (radius * 2));
		if (BrushColor != null) {
			m_hdc.setColor(BrushColor);
			m_hdc.fillOval((int) (x - radius + 1), (int) (y - radius + 1), (int) (radius * 2 - 1), (int) (radius * 2 - 1));
		}
	}

	public void circle(final int x, final int y, final double radius) {
		circle((double) x, (double) y, radius);
	}

	/*
	 * public void SetPenColor(final int color) { assert (color < NumColors);
	 * 
	 * switch (color) { case black: BlackPen(); return;
	 * 
	 * case white: WhitePen(); return; case RED: redPen(); return; case green:
	 * greenPen(); return; case BLUE: BluePen(); return; case pink: PinkPen();
	 * return; case grey: GreyPen(); return; case yellow: YellowPen(); return;
	 * case orange: OrangePen(); return; case purple: PurplePen(); return; case
	 * brown: BrownPen(); return; case light_blue: LightBluePen(); return; case
	 * light_grey: LightGreyPen(); return; case light_pink: LightPinkPen();
	 * return; }// end switch }/*
	 */
}