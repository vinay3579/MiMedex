package com.pyramidconsulting.report;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.thoughtworks.selenium.Selenium;

public class BeforeSaveReportImpl implements BeforeSaveReport {

	private static  Logger logger = Logger.getLogger(BeforeSaveReportImpl.class);

	@Override
	public byte[] doAction( Selenium selenium,  byte[] fileContent) {
		selenium.waitForPageToLoad("100000");
		logger.info("inside config. updating report content as per MiMedx requirement.");
		 StringBuilder replaceString = new StringBuilder("(Current) ");
		 String pageSource = selenium.getHtmlSource();
		int startIndex=-1;
		if((startIndex = pageSource.indexOf("hostname (cached)"))>=0) {
			 String cachedHost = pageSource.substring(startIndex+"hostname (cached) = ".length(),pageSource.indexOf("jvmname (cached)"));
			replaceString.append(cachedHost);
		}
		replaceString.append("</br>(Cached) ");

		if((startIndex = pageSource.indexOf("hostname (current) = "))>=0) {
			 String currentHost = pageSource.substring(startIndex+"hostname (current) = ".length(),pageSource.indexOf("jvmname (current)"));
			replaceString.append(currentHost);
		}
		 String updatedContent = StringUtils.replace(new String(fileContent), "__TEST_HOST_NAME__", replaceString.toString());
		return updatedContent.getBytes();
	}

}

