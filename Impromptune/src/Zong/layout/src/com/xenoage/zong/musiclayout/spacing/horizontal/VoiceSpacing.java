package com.xenoage.zong.musiclayout.spacing.horizontal;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.collections.IList;
import com.xenoage.zong.core.music.Voice;
import lombok.Data;

import java.io.Serializable;

/**
 * This class contains the spacing of one voice
 * of a measure of a single staff.
 *
 * @author Andreas Wenger
 */
@Const @Data public final class VoiceSpacing implements Serializable {

	/** The {@link Voice} this spacing belongs to. */
	private final Voice voice;
	/** The interline space in mm of this voice. */
	private final float interlineSpace;
	/** The {@link SpacingElement}s of this voice. */
	private final IList<SpacingElement> spacingElements;

}
