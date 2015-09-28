package com.pyramidconsulting.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertyUtil {
	private static final Logger logger = Logger.getLogger(PropertyUtil.class);
	private Properties resProperty = null;

	public PropertyUtil(final String propertyFile) {
		init(propertyFile);
	}

	private void init(final String propertyFile) {
		final InputStream inputStream = PropertyUtil.class
				.getClassLoader().getResourceAsStream(
						propertyFile);
		if (inputStream == null) {
			logger.error(propertyFile
					+ " File Not Found in classpath");
			throw new RuntimeException(
					"property file ["+propertyFile+"] not found in the classpath");
		}
		resProperty = new Properties();
		try {
			resProperty.load(inputStream);
		} catch (final IOException e) {
			logger.error("Could not load property file ["+propertyFile+"] from classpath");
			throw new RuntimeException(
					"Could not load property file ["+propertyFile+"] from classpath");
		}
		logger.debug("property file loaded:" + propertyFile);
	}

	public String getProperty(final String property) {
		String value = resProperty.getProperty(property);
		if(value!=null) {
			value = value.replace("\\", "//");
		}
		return value;
	}
}