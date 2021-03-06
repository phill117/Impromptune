package com.xenoage.zong.core.music.format;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;


/**
 * Formatting of an endpoint of a bezier curve.
 * 
 * @author Andreas Wenger
 */
@Data @AllArgsConstructor public final class BezierPoint implements Serializable {

	/** The position of the end point relative to a reference point. */
	private SP point;
	/** The position of the control point relative to the end point. */
	private SP control;

}
