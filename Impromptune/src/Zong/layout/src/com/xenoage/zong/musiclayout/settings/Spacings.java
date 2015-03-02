package com.xenoage.zong.musiclayout.settings;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.NonNull;
import lombok.AllArgsConstructor;

/**
 * Settings for spacings (distances) in IS.
 * 
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor public class Spacings {

	//chord spacings
	@NonNull public final ChordSpacings normalChordSpacings;
	@NonNull public final ChordSpacings graceChordSpacings;

	//distances
	public final float widthSharp, widthFlat, widthClef, widthMeasureEmpty, widthDistanceMin;

}
