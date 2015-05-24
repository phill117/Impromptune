package com.xenoage.zong.symbols.path;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.geom.Point2f;
import lombok.Data;

import java.io.Serializable;

/**
 * Move command to a given position.
 * 
 * @author Andreas Wenger
 */
@Const @Data
public class MoveTo
	implements PathElement, Serializable {

	private final PathElementType type = PathElementType.MoveTo;
	public final Point2f p;

	@Override public Point2f getTarget() {
		return p;
	}
	
}
