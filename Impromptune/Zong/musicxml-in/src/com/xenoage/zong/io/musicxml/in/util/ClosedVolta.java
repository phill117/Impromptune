package com.xenoage.zong.io.musicxml.in.util;

import com.xenoage.zong.core.music.volta.Volta;
import lombok.AllArgsConstructor;

/**
 * A {@link Volta} and its start measure.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class ClosedVolta {
	public Volta volta;
	public int measure;
}
