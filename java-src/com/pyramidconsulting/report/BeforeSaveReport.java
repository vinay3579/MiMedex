package com.pyramidconsulting.report;
import java.io.*;
import com.thoughtworks.selenium.Selenium;

public interface BeforeSaveReport {
	

	byte[] doAction(Selenium selenium, byte[] fileContent);
	
}
