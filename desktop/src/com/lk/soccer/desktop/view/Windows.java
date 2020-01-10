/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.soccer.desktop.view;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

/**
 * 
 * @author Petr
 */
final public class Windows {
	public static final long MF_CHECKED = 0x00000008L;
	public static final long MF_UNCHECKED = 0x00000000L;
	public static final long MFS_CHECKED = MF_CHECKED;
	public static final long MFS_UNCHECKED = MF_UNCHECKED;

	public static Image LoadIcon(final String file) {
		final URL icoURL = Windows.class.getResource(file);
		final Image img = Toolkit.getDefaultToolkit().createImage(icoURL);
		return img;
	}
}