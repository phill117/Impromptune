package com.xenoage.zong.io.musicxml.in.util;

import com.xenoage.utils.kernel.Range;
import com.xenoage.zong.core.music.volta.Volta;
import lombok.AllArgsConstructor;

/**
 * An unclosed {@link Volta}, needed during MusicXML import.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class OpenVolta {

	public int startMeasure;
	public Range numbers;
	public String caption;

}