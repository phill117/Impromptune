package com.xenoage.zong.desktop.utils.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static com.xenoage.utils.jse.JsePlatformUtils.io;

/**
 * Loads a {@link BufferedImage}, whose format is optimal for the system.
 * 
 * @author Andreas Wenger
 */
public class PlatformImageLoader {

	public enum Alpha {
		/** No alpha component. */
		Opaque,
		/** Use alpha component. Choose this value if the image content is unknown
		 * and transparency might be needed. */
		Translucent;
	}


	/**
	 * Returns a device compatible image with the given image data.
	 * @param imagePath  the relative path to the image file
	 * @param alpha      opaque or translucent. This is important for better performance.
	 */
	public static BufferedImage read(String imagePath, Alpha alpha)
		throws IOException {
		return read(io().openFile(imagePath), alpha);
	}

	/**
	 * Returns a device compatible image with the given image data.
	 * @param imageData  the image data stream, like PNG data
	 * @param alpha      opaque or translucent. This is important for better performance.
	 */
	public static BufferedImage read(InputStream imageData, Alpha alpha)
		throws IOException {
		//load source image
		BufferedImage img = ImageIO.read(imageData);
		//create a compatible image
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getDefaultScreenDevice().getDefaultConfiguration();
		BufferedImage ret = gc.createCompatibleImage(img.getWidth(), img.getHeight(),
			(alpha == Alpha.Translucent ? Transparency.TRANSLUCENT : Transparency.OPAQUE));
		//copy source image to compatible image
		Graphics2D g = ret.createGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();
		return ret;
	}

}
