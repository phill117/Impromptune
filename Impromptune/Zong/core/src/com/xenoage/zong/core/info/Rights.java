package com.xenoage.zong.core.info;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import lombok.Data;

/**
 * Class for rights.
 *
 * @author Andreas Wenger
 */
@Const @Data public final class Rights {

	@NonNull private final String text;
	@MaybeNull private final String type;

}
