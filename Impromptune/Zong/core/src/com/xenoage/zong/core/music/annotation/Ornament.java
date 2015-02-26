package com.xenoage.zong.core.music.annotation;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.zong.core.music.format.Placement;
import lombok.Data;

/**
 * An ornament, like trill, turn or mordent.
 * 
 * @author Andreas Wenger
 */
@Const @Data public class Ornament
	implements Annotation {

	/** The type of the ornament, like trill, turn or mordent. */
	@NonNull private final OrnamentType type;

	/** The placement of the ornament: above, below or default (null). */
	@MaybeNull private Placement placement;

}
