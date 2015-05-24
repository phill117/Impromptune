package com.xenoage.zong.musiclayout.layouter.verticalframefilling;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.musiclayout.FrameArrangement;

import java.io.Serializable;

/**
 * This vertical frame filling strategy changes nothing.
 * It simply returns the given frame arrangement.
 * 
 * @author Andreas Wenger
 */
public class NoVerticalFrameFillingStrategy
	implements VerticalFrameFillingStrategy, Serializable {

	public static final NoVerticalFrameFillingStrategy instance = new NoVerticalFrameFillingStrategy();


	@Override public FrameArrangement computeFrameArrangement(FrameArrangement frameArr, Score score) {
		return frameArr;
	}

}
