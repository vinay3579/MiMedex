package com.pyramidconsulting.util;

import java.io.IOException;
import java.io.InputStream;

public class IOUtils extends org.apache.commons.io.IOUtils {

	public static String readFileContentFromClasspath(final String fileName)
			throws IOException {
		final InputStream templateFileIS = IOUtils.class.getClassLoader()
				.getResourceAsStream(fileName);
		if (templateFileIS == null) {
			throw new RuntimeException("Could not read file:" + fileName
					+ " from classpath.");
		}
		return IOUtils.toString(templateFileIS);
	}
}
