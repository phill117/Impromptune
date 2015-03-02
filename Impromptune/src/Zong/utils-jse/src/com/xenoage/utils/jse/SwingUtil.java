package com.xenoage.utils.jse;

import java.awt.*;

/**
 * Useful methods to work with Swing.
 * 
 * @author Andreas Wenger
 */
public class SwingUtil {

	public static void setMinimumWidth(Component c, int width) {
		if (c.getPreferredSize().width < width)
			c.setPreferredSize(new Dimension(width, c.getPreferredSize().height));
	}

	public static void setPreferredWidth(Component c, int width) {
		c.setPreferredSize(new Dimension(width, c.getPreferredSize().height));
	}

}
