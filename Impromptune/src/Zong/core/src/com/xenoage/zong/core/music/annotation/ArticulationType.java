package com.xenoage.zong.core.music.annotation;

import java.io.Serializable;

/**
 * Type of an {@link Articulation}, like staccato or tenuto.
 * 
 * @author Andreas Wenger
 */
public enum ArticulationType implements Serializable {
	Accent,
	Staccato,
	Staccatissimo,
	/** Marcato. In MusicXML called strong accent. */
	Marcato,
	Tenuto
}
