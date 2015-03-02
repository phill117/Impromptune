package com.xenoage.utils.jse.xml;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;

/**
 * Entity resolver that does just nothing.
 *
 * @author Andreas Wenger
 */
public class NoResolver
	implements EntityResolver {

	private static InputSource in = new InputSource(new ByteArrayInputStream(
		"<?xml version='1.0' encoding='UTF-8'?>".getBytes()));


	@Override public InputSource resolveEntity(String publicId, String systemId) {
		return in;
	}

}
