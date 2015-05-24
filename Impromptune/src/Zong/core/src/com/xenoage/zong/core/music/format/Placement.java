package com.xenoage.zong.core.music.format;


import java.io.Serializable;

/**
 * {@link Positioning} information, where only the side
 * (above or below) is given.
 * 
 * @author Andreas Wenger
 */
public enum Placement
	implements Positioning, Serializable {
	Above,
	Below;
}
