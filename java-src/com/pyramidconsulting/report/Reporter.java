package com.pyramidconsulting.report;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Reporter {

	void ReportStep(String description, String expectedResult, String actualResult,
			String stepStatus) throws FileNotFoundException,
			IOException;

	public void saveReport( String fileLocation) throws FileNotFoundException, IOException;

	public void saveReport() throws FileNotFoundException, IOException;

	String getReportLocation();

	Boolean isTestPassed();
	void setIsFromQC(Boolean isFromQC);
	
	//@ Added by Kamlesh
	String HTMLSummaryReportPath();
	String getTimeDiff(long timeDuration);
	

	
}
