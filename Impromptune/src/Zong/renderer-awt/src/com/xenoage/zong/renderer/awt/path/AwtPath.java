package com.xenoage.zong.renderer.awt.path;

import com.xenoage.zong.symbols.path.*;

import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Creates an AWT {@link Shape} from a {@link Path}.
 * 
 * @author Andreas Wenger
 */
public class AwtPath {
	
	public static Shape createShape(Path path) {
		GeneralPath shape = new GeneralPath();
		for (PathElement e : path.getElements()) {
			switch (e.getType()) {
				case ClosePath:
					shape.closePath();
					break;
				case CubicCurveTo:
					CubicCurveTo c = (CubicCurveTo) e;
					shape.curveTo(c.cp1.x, c.cp1.y, c.cp2.x, c.cp2.y, c.p.x, c.p.y);
					break;
				case LineTo:
					LineTo l = (LineTo) e;
					shape.lineTo(l.p.x, l.p.y);
					break;
				case MoveTo:
					MoveTo m = (MoveTo) e;
					shape.moveTo(m.p.x, m.p.y);
					break;
				case QuadraticCurveTo:
					QuadraticCurveTo q = (QuadraticCurveTo) e;
					shape.quadTo(q.cp.x, q.cp.y, q.p.x, q.p.y);
					break;
			}
		}
		return shape;
	}

}
