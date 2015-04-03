package com.xenoage.zong.symbols.path;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.geom.Point2f;
import lombok.Data;

import java.io.Serializable;

/**
 * Cubic Bezier curve segment.
 * 
 * @author Andreas Wenger
 */
@Const @Data
public final class CubicCurveTo
	implements PathElement, Serializable {

	private final PathElementType type = PathElementType.CubicCurveTo;
	public final Point2f cp1, cp2, p;

	@Override public Point2f getTarget() {
		return p;
	}
	
}
