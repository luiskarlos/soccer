/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.soccer.desktop.view;

import static com.lk.soccer.desktop.view.Windows.*;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.MenuElement;

public class Script1 {
	public static class MyMenuBar extends JMenuBar {
		private static final long serialVersionUID = 1L;

		// final private ActionListener al;
		private Map<Integer, MyCheckBoxMenuItem> items = new HashMap<Integer, MyCheckBoxMenuItem>();

		public MyMenuBar() {
			super();
			// al = new ActionListener()
			// {
			//
			// @Override
			// public void actionPerformed(final ActionEvent e)
			// {
			// final MyMenuItem source = (MyMenuItem) e.getSource();
			// Main.HandleMenuItems(source.getID(), MyMenuBar.this);
			// }
			// };
		}

		@Override
		public JMenu add(final JMenu c) {
			for (final MenuElement elm : c.getSubElements()) {
				for (final MenuElement comp : elm.getSubElements()) {
					if (comp instanceof MyCheckBoxMenuItem) {
						final MyCheckBoxMenuItem myelm = (MyCheckBoxMenuItem) comp;
						this.items.put(myelm.getID(), myelm);
					}
				}
			}
			return super.add(c);
		}

		/*
		 * private ActionListener getActionListener() { return al; }/*
		 */

		/**
		 * Swap menu state and do call actionEvent
		 * 
		 * @param MenuItem
		 *          ID of MyCheckBoxMenuItem
		 */
		public void changeMenuState(final int MenuItem) {
			final MyCheckBoxMenuItem item = this.items.get(MenuItem);
			if (item != null) {
				item.doClick();
			}
		}

		/**
		 * Set menu state and do not call actionEvent
		 * 
		 * @param MenuItem
		 *          ID of MyCheckBoxMenuItem
		 * @param state
		 *          New state (MFS_CHECKED or MFS_UNCHECKED)
		 */
		public void setMenuState(final int MenuItem, final long state) {
			final MyCheckBoxMenuItem item = this.items.get(MenuItem);
			if (item == null) {
				return;
			}
			if (state == MFS_CHECKED) {
				item.setSelected(true);
			} else if (state == MFS_UNCHECKED) {
				item.setSelected(false);
			} else {
				throw new UnsupportedOperationException("Not yet implemented");
			}
		}
	}

	public static interface MyMenuItem {

		public int getID();
	}

	public static class MyButtonMenuItem extends JMenuItem implements MyMenuItem {
		private static final long serialVersionUID = 1L;

		private final int id;

		public MyButtonMenuItem(final String title, final int id, final ActionListener al) {
			super(title);
			this.id = id;
			this.addActionListener(al);
		}

		@Override
		public int getID() {
			return id;
		}
	}

	public static class MyCheckBoxMenuItem extends JCheckBoxMenuItem implements MyMenuItem {
		private static final long serialVersionUID = 1L;

		private final int id;

		public MyCheckBoxMenuItem(final String title, final int id, final ActionListener al) {
			this(title, id, al, false);
		}

		public MyCheckBoxMenuItem(final String title, final int id, final ActionListener al, final boolean checked) {
			super(title, checked);
			this.id = id;
			this.addActionListener(al);
		}

		@Override
		public int getID() {
			return id;
		}
	}

	public static MyMenuBar createMenu(final int id_menu) {
		final MyMenuBar menu = new MyMenuBar();
		// final ActionListener al = menu.getActionListener();
		final JMenu debug = new JMenu("Debug Aids");
		// final JMenuItem noAids = new MyButtonMenuItem("No Aids", ID_AIDS_NOAIDS,
		// al);
		// final JMenuItem ids = new MyCheckBoxMenuItem("Show IDs", IDM_SHOW_IDS,
		// al);
		// final JMenuItem states = new MyCheckBoxMenuItem("Show States",
		// IDM_SHOW_STATES, al);
		// final JMenuItem regions = new MyCheckBoxMenuItem("Show Regions",
		// IDM_SHOW_REGIONS, al);
		// final JMenuItem spots = new MyCheckBoxMenuItem("Show Support Spots",
		// IDM_AIDS_SUPPORTSPOTS, al);
		// final JMenuItem targets = new MyCheckBoxMenuItem("Show Targets",
		// ID_AIDS_SHOWTARGETS, al);
		// final JMenuItem threat = new
		// MyCheckBoxMenuItem("Highlight if Threatened", IDM_AIDS_HIGHLITE, al);
		// debug.add(noAids);
		// debug.add(ids);
		// debug.add(states);
		// debug.add(regions);
		// debug.add(spots);
		// debug.add(targets);
		// debug.add(threat);

		menu.add(debug);

		return menu;
	}
}