package com.xenoage.zong.core.music.chord;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.Fraction;
import lombok.Data;

import java.io.Serializable;


/**
 * Grace chord with slash (acciaccatura) or without (appoggiatura).
 * 
 * @author Andreas Wenger
 */
@Const @Data public final class Grace implements Serializable {

	/** True for acciaccatura, false for appoggiatura. */
	private final boolean slash;
	/** Duration of the grace, like 1/8 for a grace chord that looks like an eighth. */
	private final Fraction graceDuration;

}
