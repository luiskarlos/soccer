/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.soccer.desktop.view;

import static com.lk.soccer.desktop.view.Windows.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import com.lk.soccer.desktop.view.Script1.MyMenuBar;

public class WindowUtils {
	// JFrame + Menu

	public static class Window extends JDialog {
		private static final long serialVersionUID = 1L;

		// private JMenuBar menu;

		public Window(final String title) {
			super((JFrame) null, title);
			this.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosing(final WindowEvent e) {
					System.exit(0);
				}
			});
		}

		public MyMenuBar getMenu() {
			final JMenuBar bar = this.getJMenuBar();
			if (bar == null) {
				return null;
			}
			return (MyMenuBar) bar;
		}

		@Override
		public void setJMenuBar(final JMenuBar menu) {
			assert (menu instanceof MyMenuBar);
			super.setJMenuBar(menu);
		}
	}

	/**
	 * Changes the state of a menu item given the item identifier, the desired
	 * state and the HWND of the menu owner
	 */
	public static void changeMenuState(final Script1.MyMenuBar hwnd, final int MenuItem, final long state) {
		// hwnd.setMenuState(MenuItem,state);
		hwnd.setMenuState(MenuItem, state);
	}

	/**
	 * Instead of SendMessage(hwnd, WM_COMMAND, MenuItem, NULL);
	 */
	public static void SendChangeMenuMessage(final Script1.MyMenuBar hwnd, final int MenuItem) {
		hwnd.changeMenuState(MenuItem);
	}

	/**
	 * if b is true MenuItem is checked, otherwise it is unchecked
	 */
	public static void checkMenuItemAppropriately(final Script1.MyMenuBar hwnd, final int MenuItem, final boolean b) {
		if (b) {
			changeMenuState(hwnd, MenuItem, MFS_CHECKED);
		} else {
			changeMenuState(hwnd, MenuItem, MFS_UNCHECKED);
		}
	}

	/**
	 * this is a replacement for the StringCchLength function found in the
	 * platform SDK. See MSDN for details. Only ever used for checking tool bar
	 * strings
	 */
	public static boolean checkBufferLength(final String buff, final int MaxLength, final int BufferLength) {
		return true;
	}

	public static void errorBox(final String msg) {
		JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
