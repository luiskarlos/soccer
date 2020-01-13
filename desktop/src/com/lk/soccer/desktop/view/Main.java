/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.soccer.desktop.view;

import static com.lk.soccer.desktop.view.Windows.LoadIcon;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.lk.engine.soccer.Game;
import com.lk.engine.soccer.GameListener;
import com.lk.soccer.client.common.telegrams.TelegramBuilder;
import com.lk.soccer.desktop.gdi.Cgdi;
import com.lk.soccer.desktop.gdi.GdiCoach;
import com.lk.soccer.desktop.view.Script1.MyMenuBar;
import com.lk.soccer.desktop.view.WindowUtils.Window;

public class Main {
	// --------------------------------- Globals ------------------------------
	//
	// ------------------------------------------------------------------------
	final public static KeyCache keyCache = new KeyCache();

	private static final String APPLICATION_NAME = "Up Soccer";
	// bacause of game restart (soccerPitch could be null for a while)
	private static final Lock soccerPitchLock = new ReentrantLock();

	// create a timer

	public static void HandleMenuItems(final int wParam, final MyMenuBar hwnd) {
	}

	static BufferedImage buffer;
	static Graphics2D hdcBackBuffer;
	// these hold the dimensions of the client window area
	static int cxClient;
	static int cyClient;
	private static Game game;
	private static CgiRender render = new CgiRender();

	public static void main(final String[] args) {
		final Window window = new Window(APPLICATION_NAME);
		window.setIconImage(LoadIcon("/resources/icon1.png"));
		window.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		game = new Game();

		cxClient = game.getWidth();
		cyClient = game.getHeight();

		buffer = new BufferedImage(cxClient, cyClient, BufferedImage.TYPE_INT_RGB);
		hdcBackBuffer = buffer.createGraphics();
		// these hold the dimensions of the client window area
		// seed random number generator

		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		final Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		// Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

		window.setResizable(false);

		final int y = center.y - window.getHeight() / 2;
		window.setLocation(center.x - window.getWidth() / 2, y >= 0 ? y : 0);

		final JPanel panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paint(final Graphics g) {
				super.paint(g);
				final Cgdi gdi = render.getGdi();
				gdi.startDrawing(hdcBackBuffer);
				// fill our backbuffer with white
				gdi.fillRect(Color.WHITE, 0, 0, cxClient, cyClient);
				soccerPitchLock.lock();
				render.render();
				soccerPitchLock.unlock();
				gdi.stopDrawing(hdcBackBuffer);
				g.drawImage(buffer, 0, 0, null);
			}
		};

		panel.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				System.err.println("Add executable: " + e.toString());
				game.eval(String.format("pass red %d, %d", e.getX(), e.getY()));
			}
		});

		panel.setSize(cxClient, cyClient);
		panel.setPreferredSize(new Dimension(cxClient, cyClient));
		window.add(panel);
		window.pack();

		window.addComponentListener(new ComponentAdapter() {

			@Override
			// has the user resized the client area?
			public void componentResized(final ComponentEvent e) {
				// if so we need to update our variables so that any drawing
				// we do using cxClient and cyClient is scaled accordingly
				cxClient = e.getComponent().getBounds().width;
				cyClient = e.getComponent().getBounds().height;
				// now to resize the backbuffer accordingly.
				buffer = new BufferedImage(cxClient, cyClient, BufferedImage.TYPE_INT_RGB);
				hdcBackBuffer = buffer.createGraphics();
			}
		});

		// make the window visible
		window.setVisible(true);
		final Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				game.setListener(render.getListener());
				GdiCoach coach = new GdiCoach(render.getGdi());
				
				game.checkin(new TelegramBuilder(coach));
				game.setListener(new GameListener() {
					@Override
					public void onUpdateStart() {
					}

					@Override
					public void onUpdateEnd() {
						panel.repaint();
					}
				});

				game.eval(loadFile("./../scripts/spawn-players.script"));
				game.start();
				for (;;) {
					if (game.readForNextFrame())
						game.update();

					Thread.yield();
				}
			}
		});

		t.start();
	}

	public static String loadFile(final String name) {
		try {
			final File file = new File(name);
			System.out.println(file.getAbsolutePath());
			final InputStream stream = new FileInputStream(file);
			final byte[] data = new byte[stream.available()];
			stream.read(data);
			stream.close();
			return new String(data);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}