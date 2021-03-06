package com.xenoage.zong.musiclayout.continued;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.direction.Wedge;
import lombok.AllArgsConstructor;

/**
 * Continued {@link Wedge}.
 * 
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor public final class ContinuedWedge
	implements ContinuedElement {

	public final Wedge wedge;
	public final int staffIndex;


	@Override public Wedge getMusicElement() {
		return wedge;
	}

	@Override public int getStaffIndex() {
		return staffIndex;
	}

}
