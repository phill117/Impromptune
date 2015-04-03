package com.xenoage.zong.core.info;

import com.xenoage.utils.annotations.Const;
import lombok.Data;

import java.io.Serializable;

/**
 * Class for a creator.
 *
 * @author Andreas Wenger
 */
@Const @Data public final class Creator implements Serializable {

	private final String name;
	private final String type;

}
