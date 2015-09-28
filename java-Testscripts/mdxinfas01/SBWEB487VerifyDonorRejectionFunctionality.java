package mdxinfas01;


import java.sql.Timestamp;
import org.apache.log4j.Logger;
import com.pyramidconsulting.runner.Script;
import com.utilTest.ExcelUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import com.gargoylesoftware.htmlunit.javascript.host.KeyboardEvent;

import java.io.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.KeyEvent;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Calendar;

import mdxinfas01.*;

public class SBWEB487VerifyDonorRejectionFunctionality extends Script {
	private static final Logger logger = Logger.getLogger(SBWEB487VerifyDonorRejectionFunctionality.class);
    	
    @Override
    public void run() throws Exception {
    
    	boolean blnFlag = false;
    	String strStepDesc="";
		String strExpResult="";		
		java.util.Date date= new java.util.Date();
   	    String timeStamp =new Timestamp(date.getTime()).toString().replace(" ","").replace(":","").replace("-", "").replace(".","");
	
		// ########################################################################################################
		// # Test Case ID: SBWEB-487
		// # Test Case Name: VerifyDonorRejectionFunctionality
		// #------------------------------------------------------------------------------------------------------
		// # Description: This scripts verifies reject button functionality on 'Recovered Tissue' page under 'Release Processing'
		// # and then successfully log out.
		// #------------------------------------------------------------------------------------------------------
		// # Pre-conditions: User is logged in into the MiMedx SBWeb application.
		// # 				 User is on "Release Processing" landing page.
		// #				 User is on "Recovered Tissue [Recovery ID]" page.
		// # Post-conditions: NA
		// # landing page.
		// # Limitations: NA
		// #------------------------------------------------------------------------------------------------------
		// # Owner: Aarti Suresh
		// # Created on: 02-10-2014
		// #------------------------------------------------------------------------------------------------------
		// # Reviewer: Himanshu Gosain
		// # Review Date:
		// #------------------------------------------------------------------------------------------------------
		// # History Notes:
		// ########################################################################################################

		//Getting data from config file  
		String strURL=Script.dicConfigValues.get("strApplicationURL");
		
		//Getting data from master test data file
		String strUserName = Script.dicTestData.get("strUserName");
		String strPassword = Script.dicTestData.get("StrPassword");
		String strRejectReason = Script.dicTestData.get("strRejectReason");
		String strExpectedText = Script.dicTestData.get("strExpectedText");

		//Test Data to be fetched for the current 'Release Processing' script from DB for reject functionality.
		String strRecoveryId = Script.dicTestData.get("RecoveryKey");

		// ########################################################################################################
		// Step 1 - Launch browser.
		// Open application URL.
		// ########################################################################################################
		strStepDesc = "Launch browser and open SBWEB application URL.";
		strExpResult = "Browser and SBWEB application URL should be opened.";
		blnFlag = LaunchApplicationUrl(strURL);
		if (blnFlag == true) 
		{
			reporter.ReportStep(strStepDesc, strExpResult, "SBWEB application url: '"+strURL+"' launched successfully.", "Pass");
		}
		else 
		{
			reporter.ReportStep(strStepDesc, strExpResult, Err.Description, "Fail");
			return;
		}

		// ########################################################################################################
		// Step 2 - Enter all the mandatory fields with
		// valid data and click on login button.
		// ########################################################################################################
		strStepDesc = "Enter all the mandatory fields with valid data and click on login button.";
		strExpResult = "User should be logged in into the application.";
		blnFlag = loginMiMedx(strUserName, strPassword);
		if (blnFlag == true) 
		{
			reporter.ReportStep(strStepDesc, strExpResult, "User: '" + strUserName+ "' signed in successfully.", "Pass");
		} 
		else 
		{
			reporter.ReportStep(strStepDesc, strExpResult, Err.Description, "Fail");
			return;
		}

		// ########################################################################################################
		// Step 3 - Click on 'Quality Assurance' link given on left navigation area.
		// ########################################################################################################
		strStepDesc = "Click on 'Quality Assurance' link given on left navigation area.";
		strExpResult = "'Quality Assurance' link should be clicked.";
		blnFlag=clickAndVerifyVisible("pgeAssembly_Lines", "lnkQuality_Assurance", "pgeAssembly_Lines", "lnkRelease_Processing");
		if (blnFlag == true) 
		{
			reporter.ReportStep(strStepDesc, strExpResult, "'Quality Assurance' link clicked successfully.", "Pass");
		} 
		else 
		{
			reporter.ReportStep(strStepDesc, strExpResult, Err.Description, "Fail");
		}

		// ########################################################################################################
		// Step 4 - Click on 'Release Processing' link and verify 'Release Processing' page is displayed.
		// ########################################################################################################
		strStepDesc = "Click on 'Release Processing' link and verify 'Release Processing' page is displayed.";
		strExpResult = "'Release Processing' page should be displayed.";
		blnFlag=clickAndVerify("pgeAssembly_Lines", "lnkRelease_Processing", "pgeRelease_Processing", "txtSerologyResult");
		if (blnFlag == true) 
		{
			reporter.ReportStep(strStepDesc, strExpResult, "'Release Processing' page is verified sucessfully.", "Pass");
		}
		else 
		{
			reporter.ReportStep(strStepDesc, strExpResult, Err.Description, "Fail");
		}
		
		//########################################################################################################
		//Step 5 - Verify that if search functionality working properly on Release Processing page.
		//########################################################################################################
		strStepDesc = "Verify search functionality working properly on 'Release Processing' page.";
		strExpResult = "Searched result should be displayed in 'Release Processing' grid.";
		blnFlag=verifySearch("pgeRelease_Processing","txtRecoveryId","lnkRecoveryId",strRecoveryId);
		if (blnFlag == true)
		{
			reporter.ReportStep(strStepDesc, strExpResult, "Search functionality working properly in Recovered Tissue grid.", "Pass");
		}
		else
		{
			reporter.ReportStep(strStepDesc, strExpResult, Err.Description, "Fail");
			return;
		}
		
		//########################################################################################################
		//Step 6 - Click on specified Recovery Id link on Release Processing page. 
		//########################################################################################################
		strStepDesc = "Click on specified 'Recovery Id' link on 'Release Processing' page.";
		strExpResult = "Recovery Id '"+strRecoveryId+"' link should be clicked on 'Release Processing' page.";
		blnFlag=clickLink(strRecoveryId,"Recovered Tissue");
		
		if (blnFlag == true)
		{
			reporter.ReportStep(strStepDesc, strExpResult, "Recovery Id link '"+strRecoveryId+"' clicked successfully.", "Pass");
		}
		else
		{
			reporter.ReportStep(strStepDesc, strExpResult, Err.Description, "Fail");
		}	
		
		//########################################################################################################
		//Step 7 - Click on reject button on Recovered Tissue page. 
		//########################################################################################################
		strStepDesc = "Click on 'Reject' button on 'Recovered Tissue' page.";
		strExpResult = "'Reject' button should be clicked on 'Recovered Tissue' page.";
		blnFlag=clickAndVerify("pgeRecovered_Tissue", "btnReject", "pgeRecovered_Tissue", "objRejectTissueWindow");
		
		if (blnFlag == true)
		{
			reporter.ReportStep(strStepDesc, strExpResult, "'Reject' button clicked successfully.", "Pass");
		}
		else
		{
			reporter.ReportStep(strStepDesc, strExpResult, Err.Description, "Fail");
		}
		
		//########################################################################################################
		//Step 8 - Verify rejected status on Recovered Tissue page. 
		//########################################################################################################
		strStepDesc = "Verify rejected status on 'Recovered Tissue' page.";
		strExpResult = "'Rejected' status should be displayed on 'Recovered Tissue' page.";
		blnFlag=verifyRejectedStatus(strRejectReason, strUserName, strPassword);
		
		if (blnFlag == true)
		{
			reporter.ReportStep(strStepDesc, strExpResult, "'Rejected' status displayed on 'Recovered Tissue' page.", "Pass");
		}
		else
		{
			reporter.ReportStep(strStepDesc, strExpResult, Err.Description, "Fail");
		}		
		
		//########################################################################################################
		//Step 9 - Verify the 'Donor Release for Processing Checklist' generation with Rejection comments.
		//########################################################################################################
		strStepDesc = " Verify the 'Donor Release for Processing Checklist' generation with Rejection comments.";
		strExpResult = "'Donor Release for Processing Checklist' PDF should be verified for the keywords '"+strRecoveryId+"','"+strExpectedText+"'.";
		blnFlag=clickReviewFormDonorReject(strRecoveryId, strExpectedText);
		if (blnFlag == true)
		{
			reporter.ReportStep(strStepDesc, strExpResult, "'Review form' link has been clicked and the PDF has been verified for the keywords '"+strRecoveryId+","+strExpectedText+"'.", "Pass");
		}
		else
		{
			reporter.ReportStep(strStepDesc, strExpResult, Err.Description, "Fail");
		}
		
		// ########################################################################################################
		// Step 10 - Logout from SBWEB test application.
		// ########################################################################################################
		strStepDesc = "Logout from SBWEB test application.";
		strExpResult = "User should be logged out successfully.";
		blnFlag = logoutMiMedx(strUserName);
		if (blnFlag == true)
		{
			reporter.ReportStep(strStepDesc, strExpResult, "User: '" + strUserName+ "' signed out successfully.", "Pass");
		} 
		else 
		{
			reporter.ReportStep(strStepDesc, strExpResult, Err.Description, "Fail");
		}
    }
    
    // Library methods
    
//Methods from :func_application.txt
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: LaunchApplicationUrl
        //''@Objective: This Function launches the application URL and verifies if launch is successful.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strURL - URL of the application that needs to be launched.        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= LaunchApplicationUrl("http://mdxinfas01:8080/SBWEB/")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[09-Sep-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean LaunchApplicationUrl(String strURL) 
		{
			boolean blnFlag = false;
			blnFlag = LaunchApplicationUrl();
			if (blnFlag) 
			{
				blnFlag = Page("pgeLogin").Element("btnLogin").Exist();
				if (!blnFlag) 
				{					
					Err.Description = "Unable to launch URL '"+strURL+"'.";
				}					
			} 
			else
				Err.Description = "Unable to launch browser.";
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: loginMiMedx
        //''@Objective: This Function login into the application.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strUserName - User Name that is used to login to MiMedx application.
        //''@	strPassword - Password that is used to login to MiMedx application.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= loginMiMedx("akhare","India@123")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[07-Jul-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean loginMiMedx(String strUserName, String strPassword)throws Exception 
		{
			boolean blnFlag = false;
			blnFlag = Page("pgeLogin").Element("txtUser").Type(strUserName);
			if (blnFlag == true) 
			{
				blnFlag = Page("pgeLogin").Element("txtPassword").Type(strPassword);
				if (blnFlag == true) 
				{
					blnFlag = Page("pgeLogin").Element("btnLogin").Click(20);
					if(blnFlag == true)
					{
						//waitForPageRefresh();
						blnFlag = Page("pgeAssembly_Lines").Element("lnkRecovery").Exist();
						if (!blnFlag)
							Err.Description = "Home page not displayed, Login failed.";
					}
					else
						Err.Description = "Unable to click login button.";
				} 
				else
					Err.Description = "Unable to type in Password.";
			} 
			else
				Err.Description = "Unable to type in UserId.";
			return blnFlag;
		}

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: logoutMiMedx
        //''@Objective: This Function logout from the application.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strUserName - User Name that was used to login to MiMedx application.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= logoutMiMedx("akhare")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[07-Jul-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean logoutMiMedx(String strUserName) throws Exception 
		{
			boolean blnFlag = false, blnExist=false;
			Page("pgeAssembly_Lines").Element("lnkLogout").Click(20);
			blnExist=Page("pgeLogin").Element("objPleaseLogin").Exist();
			if(blnExist==true)
			{
				blnFlag=true;
			}
			else
			{
				Err.Description="User: '" + strUserName+ "' not signed out from MiMedx application.";
				blnFlag=false;
			}
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickAndVerify
        //''@Objective: This function clicks on an element on page and verify existance of an element on the navigated page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	Page - Page name to mapping the object on the page.
        //''@	Element - object name on which click action needs to be performed.
		//''@	NewPage - New page which is navigated to, after click on Page->Element has been performed.
        //''@	NewElement - New object name on new page whose existence needs to be validated to ensure that the navigation is sucessful.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickAndVerify(Page, Element, NewPage, NewElement)        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[23-Jul-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean clickAndVerify(String PageName, String Element, String newPageName, String newElement)
		{
			boolean blnFlag = false;
			blnFlag=Page(PageName).Element(Element).Click(20);
			waitForPageRefresh(15);
			if(blnFlag==true)
			{
				//Page(newPageName).Element(newElement).WaitForElement(10);
				blnFlag = Page(newPageName).Element(newElement).Exist();
				if (!blnFlag)
						Err.Description = "Unable to verify navigated page.";
			} 
			else
					Err.Description = "Unable to perform click action.";
			
			return blnFlag;
		}		
/*
		public boolean loginErrorMessage() throws Exception 
		{
			// String errMsg = "";
			boolean blnFlag = false;
			String expError = Script.dicTestData.get("StrExpError");
			String errMsg = Page("pgeLogin").Element("InvalidLoginError").GetText();
			if (errMsg.equalsIgnoreCase(expError)) {
				blnFlag = true;
			} 
			else 
			{
				blnFlag = false;
			}
			return blnFlag;
		}
*/
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: getScreenTitle
        //''@Objective: This Function captures the screen title on the recovered tissue screen.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strUserName - User Name which is used to login to the application.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= getScreenTitle()        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[07-Jul-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean getScreenTitle(String strUserName) 
		{
			boolean blnFlag = false;
			String ScreenTitle = Page("pgeRecovered_Tissue").Element("objRecovered_Tissue").GetText();
			if (ScreenTitle.equalsIgnoreCase("Recovered Tissue")) 
			{
				blnFlag = true;
			} 
			else 
			{
				Err.Description = "User: '"+strUserName+"' is not on the 'Recovered Tissue' page and is on the '"+ ScreenTitle + "' page.";
				blnFlag = false;
			}
			return blnFlag;
		}

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: getPendingStatus
        //''@Objective: This Function captures 'Pending' status of newly added tissue on the recovered tissue page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	
        //''@---------------------------------------------------------------------------------------------------------------------------		
        //''@Example: blnStatus= getPendingStatus()        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[09-Jul-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean getPendingStatus() throws Exception 
		{
			boolean blnFlag = false;
			String status = Page("pgeRecovered_Tissue").Element("objStatus").GetText();
			if (status.equalsIgnoreCase("Pending")) 
			{
				blnFlag = true;
			} 
			else 
			{
				Err.Description="'Pending status not displayed for recently added tissue and the status shown is: '"+status;
				blnFlag = false;
			}
			return blnFlag;
		}

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickLink
        //''@Objective: This function clicks on specified recoveryid and calls the function getPageTitle() to verify user is on correct page or not.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strRecoveryId - The Recovery ID of the tissue which needs to be clicked.
		//''@	strExpText - The page title which needs to be validated after click of the Recovery ID.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickLink("WC14F036", "Recovered Tissue")       
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[09-Jul-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean clickLink(String strRecoveryId, String strExpText) 
		{
			boolean blnFlag = false;
			Page("pgeRecovered_Tissue").Element("//*[contains(text(),'" + strRecoveryId+ "')]").Click(20);
			blnFlag = getPageTitle(strExpText);
			return blnFlag;
		}

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: getPageTitle
        //''@Objective: This Function verifies that user is on correct page or not.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strExpText - The page title of the current page which needs to be validated.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= getPageTitle("Recovered Tissue")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[09-Jul-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean getPageTitle(String strExpText)
		{
			boolean blnFlag = false;
			String PageTitle = Page("pgeRecovered_Tissue").Element("objRecoveredTissuePage").GetText().trim();			
			if (PageTitle.contains(strExpText.trim())) 
			{
				blnFlag = true;
			}
			else
			{
				Err.Description = "'"+ PageTitle + "' page is displayed instead of '" + strExpText+ "' page.";
				blnFlag = false;				
			}
			return blnFlag;
		}

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: canvasSignature
        //''@Objective: This Function sign the digital signature area.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	driver - Selenium Driver instance.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= canvasSignature(driver)        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[09-Jul-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean canvasSignature(WebDriver driver) 
		{
			boolean blnFlag = false;
			
			try
			{
				WebElement element = driver.findElement(By.xpath("//canvas[@class='pad']"));
				
				Actions builder = new Actions(driver);
	
				builder.clickAndHold(element)
						.moveByOffset(0, 45)
						.moveByOffset(10, 20)
						.moveByOffset(20, 45)
						.moveByOffset(5, 37)
						.moveByOffset(17, 37)
						.moveByOffset(30, 15)
						.moveByOffset(17, 37)
						.moveByOffset(40, 45)
						.moveByOffset(40, 60)
						.moveByOffset(0, 60)
						.release()
						.perform();
				
				waitForSync(10);
				blnFlag = true;
			}
			catch(NoSuchElementException e)
			{
				blnFlag = false;
					Err.Description = "Unable to locate element.";
			}
			return blnFlag;
		}

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: canvasSignature
        //''@Objective: This is an override function to provide flexibility to sign the digital signature area.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	pageName - Page name on which canvas is presented.
		//''@	eleCanvas - Canvas pad xpath.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= canvasSignature(pageName, eleCanvas)        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[19-Feb-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean canvasSignature(String pgeName, String eleCanvas) 
		{
			boolean blnFlag = false;
			String tmpXpath= Page(pgeName).Element(eleCanvas).GetXpath();
			
			try
			{
				WebElement element = driver.findElement(By.xpath(tmpXpath));
								
				Actions builder = new Actions(driver);
	
				builder.clickAndHold(element)
						.moveByOffset(0, 45)
						.moveByOffset(10, 20)
						.moveByOffset(20, 45)
						.moveByOffset(5, 37)
						.moveByOffset(17, 37)
						.moveByOffset(30, 15)
						.moveByOffset(17, 37)
						.moveByOffset(40, 45)
						.moveByOffset(40, 60)
						.moveByOffset(0, 60)
						.release()
						.perform();
						
				blnFlag = true;
			}
			catch(NoSuchElementException e)
			{
				blnFlag = false;
					Err.Description = "Unable to locate element.";
			}
			return blnFlag;
		}				

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: addTissue
        //''@Objective: This Function fills the form to add the tissue and sign the digital signature area.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strCollectionAgent - The Collection Technician's name which needs to be selected in the 'Add Recovered Tissue' page.
		//''@	dteDate - The date which needs to be added in the 'Add Recovered Tissue' page.
        //''@	strSurgeon - Surgeon name associated with the hospital.
		//''@	strFirstName - First name of the patient.
		//''@	strLastName - Last name of the patient.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= addTissue("akhare", "India@123","Hanif Anum", "Jun 29, 2014 00:00", "Barbara Simmons, MD", "Jane","Doe")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[11-Jul-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean addTissue(String strCollectionAgent, String dteDate, String strSurgeonName, String strtFirstName, String strLastName)
		{
			boolean blnFlag = false;
			waitForSync(2);
			
			blnFlag=enterSchedule(strSurgeonName,strtFirstName,strLastName);
			waitForSync(1);
			//blnFlag = true;
			
			if (blnFlag == true) 
			{
				
				//blnFlag=Page("pgeNewRecoveredTissue").Element("dteSelectDate").Type(dteDate);
				// The date field needs javascript to set the value currently
				try
				{
					JavascriptExecutor js = (JavascriptExecutor) driver;
					js.executeScript("document.getElementById('rform:recovered_input').setAttribute('value', '"+dteDate+"')");
					blnFlag=true;
				}
				catch(Exception e)
				{										
				}
				 					
				waitForSync(2);
				if (blnFlag == true) 
				{
					blnFlag=Page("pgeNewRecoveredTissue").Element("lstCollectionTech").Select(strCollectionAgent);
					waitForSync(2);
					
					if (blnFlag == true) 
					{
						blnFlag = Page("pgeNewRecoveredTissue").Element("drpShippingContainer").Select("Yes");
						waitForSync(4);
						
						if (blnFlag == true) 
						{
							blnFlag = Page("pgeNewRecoveredTissue").Element("drpCoolent").Select("Yes");
							waitForSync(4);
							
							if (blnFlag == true) 
							{
								blnFlag = Page("pgeNewRecoveredTissue").Element("drpPaperwork").Select("Yes");
								waitForSync(4);
								
								if (blnFlag == true) 
								{
									blnFlag = Page("pgeNewRecoveredTissue").Element("drpTissueContainer").Select("Yes");
									waitForSync(4);
									
									if (blnFlag == true) 
									{
										blnFlag = Page("pgeNewRecoveredTissue").Element("drpTissueAccepted").Select("Yes");
										waitForSync(4);
										
										if (blnFlag == true) 
										{
											blnFlag = Page("pgeNewRecoveredTissue").Element("lstCollectionTech").Select(strCollectionAgent);
											if (blnFlag == true)
											{
												blnFlag = canvasSignature(driver); 
												if (blnFlag == true)
												{
													blnFlag=Page("pgeNewRecoveredTissue").Element("btnSave").Click(20);
													waitForSync(1);
													
													if (blnFlag == true)
													{
														blnFlag=!(Page("pgeNewRecoveredTissue").Element("objAddTissueError").Exist());
														if(!blnFlag)
															Err.Description = "Error exists while filling the details on 'Add Recovered Tissue' page.";		
													}
													else 
														Err.Description = "Unable to click 'Save' button.";
												}
												else
													Err.Description = "Unable to sign in canvas signature.";
											}
											else					
												Err.Description = "Unable to select 'Collection Agent' drop down.";
										}
										else 
											Err.Description = "Unable to select 'Tissue Accepted?' drop down.";
									} 
									else
										Err.Description = "Unable to select 'tissue container?' drop down.";
								} 
								else
									Err.Description = "Unable to select 'paperwork present?' drop down.";
							} 
							else
								Err.Description = "Unable to select 'contents cool?' drop down.";
						} 
						else
							Err.Description = "Unable to select 'sealed and intact?' drop down.";				
					}
					else
						Err.Description = "Unable to select 'Collection Agent' drop down.";			
				}
				else
					Err.Description = "Unable to eneter recovered date.";	
			}
			else
				Err.Description = Err.Description;		
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: enterSchedule
        //''@Objective: This Function fills the form to add the tissue and sign the digital signature area.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strSurgeon - Surgeon name associated with the hospital.
		//''@	strFirstName - First name of the patient.
		//''@	strLastName - Last name of the patient.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= enterSchedule("Barbara Simmons, MD", "Jane","Doe")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[09-10-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean enterSchedule(String strSurgeon,String strFirstName, String strLastName)
		{
			boolean blnFlag = false;
			
			blnFlag=Page("pgeNewRecoveredTissue").Element("txtSurgeon").Type(strSurgeon);
			waitForSync(2);
			
			if (blnFlag == true) 
			{
				String xpath=Page("pgeNewRecoveredTissue").Element("txtSurgeon").GetXpath();
				driver.findElement(By.xpath(xpath)).sendKeys(Keys.RETURN);
				waitForPageRefresh(180);
				blnFlag=Page("pgeNewRecoveredTissue").Element("btnRecruited").Exist();
				
				if (blnFlag == true) 
				{
					waitForSync(2);
					blnFlag=Page("pgeNewRecoveredTissue").Element("btnRecruited").Click(20);
					waitForSync(2);
					
					if (blnFlag == true) 
					{
						blnFlag = Page("pgeNewRecoveredTissue").Element("objPencil").Click(20);
						waitForSync(1);
						
						if (blnFlag == true) 
						{
							blnFlag = Page("pgeNewRecoveredTissue").Element("txtFirstName").Type(strFirstName);
							waitForSync(2);
							
							if (blnFlag == true) 
							{
								blnFlag = Page("pgeNewRecoveredTissue").Element("txtLastName").Type(strLastName);
								waitForSync(2);
								
								if (blnFlag == true) 
								{
									blnFlag = Page("pgeNewRecoveredTissue").Element("objCheck").Click(20);
									waitForSync(2);
									if(!blnFlag)
										Err.Description = "Unable to click 'Check' link in C-Section grid.";	
								}
								else 
									Err.Description = "Unable to type last name in C-Section grid.";
							}
							else
								Err.Description = "Unable to type first name in C-Section grid.";
						}
						else
							Err.Description = "Unable to click pencil icon in C-Section grid.";
					}
					else					
						Err.Description = "Unable to click recruited button.";													
				}
				else 
					Err.Description = "Recruited button not displayed on 'Add Recovered Tissue' page.";
			} 
			else
				Err.Description = "Unable to type surgeon name.";	

			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifySearch
        //''@Objective: This Function verifies the search functionality in a grid.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	PageName - The Page Name where the search box is present.
		//''@	ElementName - The Object name where the search text needs to be entered.
		//''@	loadedElementName - The First object name where appears in the grid after the search operation has been performed.
		//''@	text - The search text which needs to be entered in PageName->ElementName.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifySearch(PageName, ElementName, loadedElementName, "WC14F036")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[11-Jul-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean verifySearch(String Page, String Element, String newElement, String text) 
		{
			boolean blnFlag = false;
			text=text.trim();
			try
			{
				Page(Page).Element(Element).Type(text);
				final String tempSource = driver.getPageSource();
				(new WebDriverWait(driver, 180)).until(new ExpectedCondition<Boolean>() 
				{
					public Boolean apply(WebDriver driver) 
					{
						boolean flag = false;
						if (!tempSource.equals(driver.getPageSource())) 
						{
							flag = true;
						}
						return flag;
					}
				});
					blnFlag=true;
			}
			catch (TimeoutException e)
			{
				//Err.Description="Timed out after 180 seconds.Searched element not found or search functinality not working.";				
				blnFlag = true;				
			}
			if( blnFlag==true)
			{			
				String eleLink = Page(Page).Element(newElement).GetText();
				if (eleLink.trim().contains(text))
				{
					blnFlag = true;
				} 
				else 
				{
					blnFlag = false;
					Err.Description = "Either page refresh did not happen or searched text not displayed in grid.";
				}
			}
		return blnFlag;
		}

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickHistoryLink
        //''@Objective: This Function clicks on History link on recovered tissue page and verfies the tilte of the navigated page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strRecoveryId - The Recovery ID of the tissue.
		//''@	strExpText - The expected title of the navigated page.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickHistoryLink("WC14F036", "Donor Tissue")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[15-Jul-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickHistoryLink(String strRecoveryId, String strExpText)throws Exception 
		{
			boolean blnFlag = false;
			blnFlag = Page("pgeRecovered_Tissue").Element("//*[contains(text(),'" + strRecoveryId+ "')]/following::a[contains(text(),'History')]").Click(20);
			// driver.findElement(By.xpath("//*[contains(text(),'"+strRecoveryId+"')]/following::a[contains(text(),'History')]")).click();
			if (blnFlag==true)
			{
				blnFlag = getPageTitle(strExpText);
				if (blnFlag==true)
				{
					blnFlag=Page("pgeNewRecoveredTissue").Element("btnReturn").Click(20);
					if (!blnFlag)
						Err.Description = "Unable to click 'Return' button on 'Donor History' page.";
				}
				else
					Err.Description=Err.Description;			
			}
			else
			{
				Err.Description="Unable to click on 'History' Link";
			}
			
			return blnFlag;
		}

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickRecoveryFormLink
        //''@Objective: This Function clicks on Recovery Form link on recovered tissue page and verfies the tilte of the navigated page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strRecoveryId - The Recovery ID of the tissue.
		//''@	strExpText - The expected title of the navigated page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickRecoveryFormLink("WC14F036", "Incoming Tissue Form")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[15-Jul-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickRecoveryFormLink(String strRecoveryId, String strExpText) throws Exception
		{
			boolean blnFlag = false;
			waitForSync(2);
			strExpText=strExpText+","+strRecoveryId;
			blnFlag = Page("pgeRecovered_Tissue").Element("//*[contains(text(),'" + strRecoveryId+ "')]/following::a[contains(text(),'Recovery Form')]").Click(20);
			//driver.findElement(By.xpath("//*[contains(text(),'" + strRecoveryId+ "')]/following::a[contains(text(),'Recovery Form')]")).click();
			waitForSync(10);
			if (blnFlag==true)
			{
				blnFlag=verifyPDF(strExpText);
				if(!blnFlag)
					Err.Description=Err.Description;
			}
			else
			{
				Err.Description="Unable to click on 'Recovery Form' Link";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickRefrigeratorLogLink
        //''@Objective: This Function clicks on Refrigerator Log link on recovered tissue page and verfies the tilte of the navigated page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strRecoveryId - The Recovery ID of the tissue.
		//''@	strExpText - The expected title of the navigated page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickRefrigeratorLogLink("WC14F036", "Incoming Tissue Form")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[16-Jul-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################	
		public boolean clickRefrigeratorLogLink(String strRecoveryId,String strExpText) throws Exception 
		{
			boolean blnFlag = false;
			strExpText=strExpText+","+strRecoveryId;
			waitForSync(2);
			blnFlag = Page("pgeRecovered_Tissue").Element("//*[contains(text(),'" + strRecoveryId+ "')]/following::a[contains(text(),'Refrigerator Log')]").Click(20);
			//driver.findElement(By.xpath("//*[contains(text(),'"+ strRecoveryId+ "')]/following::a[contains(text(),'Refrigerator Log')]")).click();
			waitForSync(10);
			if (blnFlag==true)
			{
				blnFlag=verifyPDF(strExpText);
				if(!blnFlag)
					Err.Description=Err.Description;
			}
			else
			{
				Err.Description="Unable to click on 'Refrigerator Log' Link";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: searchSerologyResult
        //''@Objective: This Function searches serology results with status 'Yes' on Release Processing page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strSerologyResult - The value that needs to be searched for in the Serology Results.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= searchSerologyResult("Yes")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[17-Jul-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################	
		public boolean searchSerologyResult(String strSerologyResult) throws Exception 
		{
			boolean blnFlag = false;
			Page("pgeRelease_Processing").Element("txtSerologyResult").Type(strSerologyResult);
			waitForPageRefresh();
			String result = Page("pgeRelease_Processing").Element("objSerologyResult").GetText();
			if (result.equalsIgnoreCase(strSerologyResult)) 
			{
				blnFlag = true;
			}
			else 
			{
				Err.Description = "Searched tissue with serology results 'Yes' not displayed in grid.";
				blnFlag = false;
			}

			return blnFlag;
		}

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: waitForPageRefresh
        //''@Objective: This Function wait for grid to refresh.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= waitForPageRefresh()        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[17-Jul-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public void waitForPageRefresh() 
		{
			try 
			{
				final String tempSource = driver.getPageSource();
				(new WebDriverWait(driver, 90)).until(new ExpectedCondition<Boolean>() 
				{
					public Boolean apply(WebDriver driver) 
					{
						boolean flag = false;
						if (!tempSource.equals(driver.getPageSource())) 
						{
							flag = true;
						}
						return flag;		
					}
				});
			}
			catch (TimeoutException e)
			{
				Err.Description="Timed out after 90 seconds wait.";
			}
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickLoadReviewForm
        //''@Objective: This Function clicks on Load/Review link on 'Recovered Tissue [Recovery Id]' page and load 'pdf' file.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strFilePath - The file path of the sample PDF file that needs to be loaded.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickLoadReviewForm(Status)        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[07-22-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean clickLoadReviewForm(String strFilePath) 
		{
			boolean blnFlag=false;
			String Status="";
			String lnkName=Page("pgeRecovered_Tissue").Element("lnkLoad_Form").GetText();
			if (lnkName.equalsIgnoreCase("Load Form"))
			{
				blnFlag=Page("pgeRecovered_Tissue").Element("lnkLoad_Form").Click(20);
				if (blnFlag==true)
				{
					blnFlag=uploadFile(strFilePath);
					if (blnFlag==true)
					{
						if(!(Page("pgeRecovered_Tissue").Element("objLoadFormError").Exist()))
						{
						Status="'Load Form' link clicked and file uploaded successfully.";
						Script.dicCommonValue.put("Success", Status);
						Page("pgeRecovered_Tissue").Element("lnkLoad_Form").Click(20);
						}
						else 
						{
							blnFlag=false;
							Err.Description = "Unable to upload 'pdf' file.";
						}
					}
					else		
						Err.Description = Err.Description;					
				}
				else
					Err.Description = "Unable to click on 'Load Form' link.";
										
			}
			else if (lnkName.equalsIgnoreCase("Review Form"))
			{
				blnFlag=Page("pgeRecovered_Tissue").Element("lnkLoad_Form").Click(20);
				if (blnFlag==true)
				{
					Status="'Review Form' link clicked successfully.";
					Script.dicCommonValue.put("Sucess", Status);
				}
				else
					Err.Description = "Unable to click on 'Review Form' link.";
			}
			else
			{
				Err.Description="Link not visible or not clickable.";
				blnFlag=false;
			}
			
			return blnFlag;
		}
				
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: uploadFile
        //''@Objective: This Function upload the 'pdf' file 'Recovered Tissue Form Files' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strFilePath - The file path of the sample PDF file that needs to be loaded in 'Recovered Tissue Form Files' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= uploadFile("E:\\Workspace\\PDF_Files\\secondsdoc.pdf")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[07-22-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean uploadFile(String strFileName) 
		{
			boolean blnFlag=false;
			String dir=System.getProperty("user.dir");
			String strFilePath=dir+"\\PDF_Files\\"+strFileName;

			String strDialog="File Upload";
			if(Script.dicConfigValues.get("strBrowserType").equalsIgnoreCase("CHROME"))
			{
				strDialog="Open";
			}
			else if(Script.dicConfigValues.get("strBrowserType").equalsIgnoreCase("IE"))
			{
				strDialog="Choose File to Upload";
			}
			blnFlag=Page("pgeRelease_Processing").Element("//div[@class='ui-fileupload ui-widget']//span").Exist();
			if(blnFlag)
			{
				blnFlag=Page("pgeRelease_Processing").Element("//div[@class='ui-fileupload ui-widget']//span").Click(20);
				if(blnFlag)
				{
					waitForSync(1);
					try 
					{
						
						String[] dialog =  new String[]{"./AutoIt/Upload.exe",strFilePath, strDialog};
						Runtime.getRuntime().exec(dialog);
//						Process process = new ProcessBuilder("./AutoIt/Upload.exe",strFilePath, strDialog).start();
						blnFlag=true;
						if(blnFlag)
						{
							waitForSync(3);
							blnFlag=Page("pgeRelease_Processing").Element("//table[@class='ui-fileupload-files']//tr").isVisible();
							if(blnFlag)
							{
								blnFlag=Page("pgeRelease_Processing").Element("//span[text()='Upload']").Click(20);
								if(blnFlag)
								{
									waitForSync(3);
									blnFlag=Page("pgeRecovered_Tissue").Element("btnReturn").Click(20);
									if(!blnFlag)
										Err.Description = "Unable to click on 'Return' button.";	
								}
								else
									Err.Description = "Unable to click on 'Upload' button.";
							}
							else 
								Err.Description = "Selected file not displayed or selection not done properly.";	
						}
						else
							Err.Description = "AutoIt code not executed.";
					} 
					catch (IOException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
						blnFlag=false;
					}
				}
				else 
					Err.Description = "Unable to click on 'Choose' button.";
			}
			else 
				Err.Description = "'Choose' button not diaplyed on the page";
			
			return blnFlag;				
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyScan
        //''@Objective: This Function verifies uploaded 'pdf' file and pass 'pdf' file on 'Recovered Tissue Form Files' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strFollowUpComment - The Followup comment that needs to be entered on 'Edit Process Note' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyScan("Ok")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[08-07-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean verifyScan(String strFollowUpComment) 
		{
			boolean blnFlag = false;
			blnFlag=Page("pgeForms_Package").Element("lnkVerifyScan").Click(20);
			if (blnFlag=true)
			{
				blnFlag=Page("pgeForms_Package").Element("chkNeedActions").Click(20);
				if (blnFlag=true)
				{
					blnFlag=Page("pgeForms_Package").Element("txtFollowUp").Type(strFollowUpComment);
					if (blnFlag=true)
					{
						blnFlag=Page("pgeForms_Package").Element("btnSaveEditWindow").Click(20);
						if (blnFlag=true)
						{
							String button = Page("pgeForms_Package").Element("btnPass").GetText();
							if (button.equalsIgnoreCase("Pass")) 
							{
								blnFlag = true;
							} 
							else 
							{
								Err.Description = "'Pass' button not displayed on 'Forms Package' page.";
								blnFlag = false;
							}
						}
						else
							Err.Description = "Unable click on 'Save' button.";
					}
					else
						Err.Description = "Unable enter text in 'Follow Up' comment.";
				}
				else
					Err.Description = "Unable to uncheck 'Needs Action' check box.";
			}
			else
				Err.Description = "Unable to click on 'Verify Scan' link.";
			return blnFlag;
		}

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: releasePassForm
        //''@Objective: This function releases the verified scan 'pdf' on 'Forms Package' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strPassComment - The User comments that needs to be entered on 'Pass Release Form' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= releasePassForm("Testing")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[08-07-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean releasePassForm(String strPassComment) 
		{
			boolean blnFlag = false;
			waitForSync(1);
			blnFlag=Page("pgeForms_Package").Element("txtUserComments").Type(strPassComment);
			if (blnFlag==true)
			{
				blnFlag=Page("pgeForms_Package").Element("btnCommit").Click(20); 
				if (blnFlag==true)
				{
					String button=Page("pgeRecovered_Tissue").Element("btnMarkReview").GetText();
					if (button.equalsIgnoreCase("Mark for Review"))
					{
						blnFlag=true;
					}
					else
					{
						blnFlag=false;
						Err.Description = "'Mark for Review' button not displayed.";
					}
				}
				else
					Err.Description = "Unable to click 'Commit' button.";				
			}
			else
				Err.Description = "Unable to type in comment text area.";		
		return blnFlag;
	}
	
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: markForReview
        //''@Objective: This function clicks on 'Mark for Review' button and validate 'Review Form' link changed to 'Verify Eligibility' 
		//''@ link on 'Recovered Tissue' Page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= markForReview()        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[08-07-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################	
		public boolean markForReview() 
		{
			boolean blnFlag = false;
			blnFlag=Page("pgeRecovered_Tissue").Element("btnMarkReview").Click(20);
			if (blnFlag==true)
			{
				String status=Page("pgeRecovered_Tissue").Element("lnkVerifyEligibility").GetText();
				if (status.equalsIgnoreCase("Verify Eligibility"))
				{
					blnFlag=true;
				}
				else
				{
					blnFlag=false;
					Err.Description = "'Verify Eligibility' link not displayed on the page.";
				}
			}
			else
				Err.Description = "Unable to click 'Mark for Review' button.";	
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyTissueStatus
        //''@Objective: This function verifies tissue status on 'Recovered Tissue' Page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	Page - Reference to 'Recovered Tissue' Page.
		//''@	Element - The element that has the Status text in 'Recovered Tissue' page.
		//''@	expText - The expected status of the tissue that should be displayed in the Page->Element.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyTissueStatus("pgeRecovered_Tissue", "objTissueStatus", "Eligibility Review")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[08-07-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
		public boolean verifyTissueStatus(String Page, String Element, String expText) 
		{
			boolean blnFlag = false;
			String status=Page(Page).Element(Element).GetText();
			if (status.equalsIgnoreCase(expText))
			{
				blnFlag=true;
			}
			else
			{
				blnFlag=false;
				Err.Description = "Unable to validate '"+expText+"' status,'"+status+"' status displayed instead of '"+expText+"'.";
			}	
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyRejectedStatus
        //''@Objective: This Function verifies tissue rejected status on 'Recovered Tissue' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strReason - 'Reason for Rejection' that needs to be entered in 'Recovered Tissue' page.
		//''@	strUserId - User ID initials that needs to be entered in 'Recovered Tissue' page.
		//''@	strPassword - Password that needs to be entered in 'Recovered Tissue' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyRejectedStatus("Testing", "akhare", "India@123")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[07-23-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean verifyRejectedStatus(String strReason,String strUserId, String strPassword) 
		{
			boolean blnFlag = false;			
			blnFlag=Page("pgeRecovered_Tissue").Element("txtReasonRejectWindow").Type(strReason);
			if (blnFlag==true)
			{
				//blnFlag=Page("pgeRecovered_Tissue").Element("txtUserIdRejectWindow").Type(strUserId);
				//if (blnFlag==true)
				//{
					//blnFlag=Page("pgeRecovered_Tissue").Element("txtPasswordRejectWindow").Type(strPassword);
					//if (blnFlag==true)
					//{
					blnFlag=Page("pgeRecovered_Tissue").Element("btnCommitRejectWindow").Click(20);
					waitForSync(1);
						if (blnFlag==true)
						{
							String status=Page("pgeRecovered_Tissue").Element("objTissueStatus").GetText();
							if (status.contains("Rejected"))
							{
								blnFlag=true;
							}
							else
							{
								blnFlag=false;
								Err.Description = "Unable to validate rejected status.";
							}
						}
						else
							Err.Description = "Unable to click commit button."; 
					//}
					//else
						//Err.Description = "Unable to type in Password.";
				//}
				//else
					//Err.Description = "Unable to type in UserId.";
			}
			else
				Err.Description = "Unable to select Reason from dropdown.";
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifySaveFunctionality
        //''@Objective: This Function verifies QA notes save functionality on 'Recovered Tissue' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strQaComment - 'QA Notes' that needs to be entered in 'Recovered Tissue' page.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifySaveFunctionality("QA comment for testing")         
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[07-23-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean verifySaveFunctionality(String strQaComment) 
		{
			boolean blnFlag = false;
			blnFlag=Page("pgeRecovered_Tissue").Element("txtQANotes").Type(strQaComment);
			if (blnFlag==true)
			{
				blnFlag=Page("pgeRecovered_Tissue").Element("btnSave").Click(20);
				if (blnFlag==true)
				{
					String comment=Page("pgeRecovered_Tissue").Element("txtQANotes").GetValue();
					if (comment.contains(strQaComment))
					{
						blnFlag=true;
					}
					else
					{
						blnFlag=false;
						Err.Description = "Unable to validate entered text in QA notes text box.";
					}
				}
				else
					Err.Description = "Unable to click save button.";				
			}
			else
				Err.Description = "Unable to type in QA notes text area.";
			return blnFlag;
		}			

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillEntriesForNotEligible
        //''@Objective: This function fill entries in 'Tissue is NOT Eligible for Transplant' window and verfies 'Ineligible for Transplant'
		//''@ status on 'Recovered Tissue' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strNotEligibleComment - The Not eligible comment that needs to be entered in 'Tissue is NOT Eligible for Transplant' window.
		//''@	strUser - User ID initials that needs to be entered in 'Tissue is NOT Eligible for Transplant' window .
		//''@	strPassword - Password that needs to be entered in 'Tissue is NOT Eligible for Transplant' window .
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fillEntriesForNotEligible("comment for testing","akhare","India@123")         
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[08-05-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean fillEntriesForNotEligible(String strNotEligibleComment, String strUser, String strPassword) 
		{
			boolean blnFlag = false;
			waitForSync(2);
			blnFlag=Page("pgeForms_Package").Element("txtUserComments").Type(strNotEligibleComment);
			if (blnFlag==true)
			{
				blnFlag=Page("pgeForms_Package").Element("txtSignUser").Type(strUser);
				if (blnFlag==true)
				{
					blnFlag=Page("pgeForms_Package").Element("txtSignPassword").Type(strPassword);
					if (blnFlag==true)
					{
						// blnFlag=Page("pgeForms_Package").Element("btnCommit").Click(20);
						blnFlag=Page("pgeForms_Package").Element("btnClose").Click(20);
						if (blnFlag==true)
						{
							String status=Page("pgeRecovered_Tissue").Element("objTissueStatus").GetText();
							if (status.contains("Ineligible for Transplant"))
							{
								blnFlag=true;
							}
							else
							{
								blnFlag=false;
								Err.Description = "Unable to validate 'Ineligible for Transplant' status.";
							}
						}
						else
							Err.Description = "Unable to click commit button.";				
					}
					else
						Err.Description = "Unable to type in password text area.";
				}
				else
					Err.Description = "Unable to type in user name text area.";
			}	
			else
				Err.Description = "Unable to type in comment text area.";		
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillEntriesForIncomplete
        //''@Objective: This function fill entries in 'Tissue Eligibility Review is Incomplete' window and verfies 'Quarantine'
		//''@ status on 'Recovered Tissue' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strIncompleteComment - The 'Incomplete' comment that needs to be entered in 'Tissue Eligibility Review is Incomplete' window.
		//''@	strUser - User ID initials that needs to be entered in 'Tissue Eligibility Review is Incomplete' window.
		//''@	strPassword - Password that needs to be entered in 'Tissue Eligibility Review is Incomplete' window.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fillEntriesForIncomplete("comment for testing","akhare","India@123")         
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[08-05-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean fillEntriesForIncomplete(String strIncompleteComment, String strUser, String strPassword) 
		{
			boolean blnFlag = false;
			waitForSync(2);
			blnFlag=Page("pgeForms_Package").Element("txtUserComments").Type(strIncompleteComment);
			if (blnFlag==true)
			{
				// blnFlag=Page("pgeForms_Package").Element("btnCommit").Click(20);
				blnFlag=Page("pgeForms_Package").Element("btnClose").Click(20);
				if (blnFlag==true)
				{
					String status=Page("pgeRecovered_Tissue").Element("objTissueStatus").GetText();
					if (status.contains("Quarantine"))
					{
						blnFlag=true;
					}
					else
					{
						blnFlag=false;
						Err.Description = "Unable to validate 'Quarantine' status.";
					}
				}
				else
					Err.Description = "Unable to click commit button.";		
			}
			else
				Err.Description = "Unable to type in comment text area.";		
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillEntriesForEligible
        //''@Objective: This function fill entries in 'Tissue is Eligible for Transplant' window and verfies 'Eligible for Transplant'
		//''@ status on 'Recovered Tissue' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strEligibleComment - The 'Eligible' comment that needs to be entered in 'Tissue is Eligible for Transplant' window.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fillEntriesForEligible("comment for testing")         
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[08-05-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean fillEntriesForEligible(String strEligibleComment) 
		{
			boolean blnFlag = false;
			waitForSync(2);
			blnFlag=Page("pgeForms_Package").Element("txtUserComments").Type(strEligibleComment);
			if (blnFlag==true)
			{
				blnFlag=Page("pgeForms_Package").Element("btnCommit").Click(20);
				if (blnFlag==true)
				{
					String status=Page("pgeRecovered_Tissue").Element("objTissueStatus").GetText();
					if (status.contains("Eligible for Transplant"))
					{
						blnFlag=true;
					}
					else
					{
						blnFlag=false;
						Err.Description = "Unable to validate 'Eligible for Transplant' status.";
					}
				}
				else
					Err.Description = "Unable to click commit button.";				
			}
			else
				Err.Description = "Unable to type in comment text area.";		
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: validateOpenedWindow
        //''@Objective: This function validate opened window on 'Forms Package' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	Page - The Page which contains the Title of the opened window..
		//''@	Element - The Element which contains the Title of the opened window.
		//''@	Title - The title of the opened window.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= validateOpenedWindow(Page,Element,Title)         
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[08-06-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean validateOpenedWindow(String Page, String Element, String Title) 
		{
			boolean blnFlag = false;
			waitForSync(2);
			String window=Page(Page).Element(Element).GetText();
			if (window.contains(Title))
			{
				blnFlag=true;
				Page("pgeForms_Package").Element("btnClose").Click(20);
			}
			else
			{
				blnFlag=false;
				Err.Description = "Unable to validate '"+Title+"' window.";
			}
			return blnFlag;
		}	

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillEntriesAcceptAndRelease
        //''@Objective: This function fill entries in 'Accept and Release the Tissue for Processing' window and verfies 'Release OK'
		//''@ status on 'Recovered Tissue' page..
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strReleaseComment - The 'Release comments' which are to be entered in 'Accept and Release the Tissue for Processing' window.
		//''@	strUser - The User ID which needs to be entered in 'Accept and Release the Tissue for Processing' window.
		//''@	strPassword - The Password which needs to be entered in 'Accept and Release the Tissue for Processing' window.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fillEntriesAcceptAndRelease("comment for testing","akhare","India@123")         
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[08-06-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean fillEntriesAcceptAndRelease(String strReleaseComment, String strUser, String strPassword) 
		{
			boolean blnFlag = false;
			waitForSync(1);
			blnFlag=Page("pgeForms_Package").Element("txtUserComments").Type(strReleaseComment);
			if (blnFlag==true)
			{			
				//blnFlag=Page("pgeForms_Package").Element("txtSignUser").Type(strUser);
				//if (blnFlag==true)
				//{
					//blnFlag=Page("pgeForms_Package").Element("txtSignPassword").Type(strPassword);
					//if (blnFlag==true)
					//{
						blnFlag=Page("pgeForms_Package").Element("btnCommit").Click(20);
						if (blnFlag==true)
						{
							String status=Page("pgeRecovered_Tissue").Element("objTissueStatus").GetText();
							if (status.contains("Release OK"))
							{
								blnFlag=true;
							}
							else
							{
								blnFlag=false;
								Err.Description = "Unable to validate 'Release OK' status.";
							}
						}
						else
							Err.Description = "Unable to click 'Commit' button.";				
					//}
					//else
						//Err.Description = "Unable to type in password text area.";
				//}
				//else
					//Err.Description = "Unable to type in user name text area.";					
			}
			else
				Err.Description = "Unable to type in comment text area.";		
			return blnFlag;
		}		

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillEntriesTransferTissue
        //''@Objective: This function fill entries in 'Transfer the Tissue' window and verfies 'Released'
		//''@ status on 'Recovered Tissue' page..
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	lstRefrigerator - The Refrigerator that needs to be selected in 'Transfer the Tissue' window.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fillEntriesTransferTissue("RFI-15R")         
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[08-06-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean fillEntriesTransferTissue(String lstRefrigerator) 
		{
			boolean blnFlag = false;
			waitForSync(1);
			blnFlag=Page("pgeRecovered_Tissue").Element("lstRefrigerator_Utility").Type(lstRefrigerator);
			if (blnFlag==true)
			{
				blnFlag=Page("pgeRecovered_Tissue").Element("btnTransfer_Utility").Click(20);
				if (blnFlag==true)
				{
					String status=Page("pgeRecovered_Tissue").Element("objTissueStatus").GetText();
					if (status.contains("Released"))
					{
						blnFlag=true;
					}
					else
					{
						blnFlag=false;
						Err.Description = "Unable to validate 'Released' status.";
					}
				}
				else
					Err.Description = "Unable to click 'Transfer' button.";				
			}
			else
				Err.Description = "Unable to select 'Refrigerator' from drop down list.";
				
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickRecoveryIDCheckBox
        //''@Objective: This Function clicks on specified recoveryid checkbox 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strRecoveryId - The Recovery ID, against which the checkbox needs to be checked.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickRecoveryIDCheckBox("PA14G020")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[04-Aug-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean clickRecoveryIDCheckBox(String strRecoveryId)throws Exception 
		{
			boolean blnFlag = false;	
			String 	strDonorKey;
			String tempXpath = "//a[contains(text(),'"+strRecoveryId+"')]/../preceding-sibling::td//span/..";			
			//driver.findElement(By.xpath("//a[contains(text(),'"+strRecoveryId+"')]/../preceding-sibling::td//span/..")).sendKeys(Keys.SPACE);
			//Place the Donor Key in a variable for furture use
			strDonorKey = Page("pgeDonor_List").Element("objDonorKey").GetText();
			Script.dicTestData.put("strDonorKey", strDonorKey);
			//if (( driver.findElement(By.xpath("//a[contains(text(),'"+strRecoveryId+"')]/parent::*/preceding-sibling::td//input[@type='checkbox']")).isSelected()) == true)
			/*{				
				blnFlag = true;
			}
			else
			{
				blnFlag = false;
				Err.Description = "Recovery Checkbox was not checked successfully.";
			}
			*/
				
			blnFlag = Page("pgeDonor_List").Element(tempXpath).Click(20);
			
			if(!blnFlag)			
				Err.Description = "Recovery Checkbox was not clicked successfully.";
			
			return blnFlag;
		}
								
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyPrepopulatedData
        //''@Objective: This Function verifies if data is pre-populated in grid on 'Process Recovered Tissues' page.It verifies if the Recovery ID is 
		//''@	correct and also checks if the Donor ID and the Refrigerator are pre-populated.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strRecoveryId - The Recovery ID which needs to be verified in the prepopulated grid on 'Process Recovered Tissues' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyPrepopulatedData("PA14G020")         
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-04-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean verifyPrepopulatedData(String strRecoveryId)  
		{
			boolean blnFlag = false;
			String strRecoveryIDfromTable = Page("pge_ProcessRecoveredTissues").Element("objRecoveryIDGrid").GetText();
			String strDonorfromTable = Page("pge_ProcessRecoveredTissues").Element("objDonorGrid").GetText();
			String strRefrigeratorfromTable = Page("pge_ProcessRecoveredTissues").Element("objRefrigeratorGrid").GetText();
			
			blnFlag = (strRecoveryId).contains(strRecoveryIDfromTable);
			if (blnFlag == true)
			{	
				blnFlag = !strDonorfromTable.isEmpty();
				if(blnFlag == true)
				{
					blnFlag = !strRefrigeratorfromTable.isEmpty();
					if(blnFlag == false)
					{
						Err.Description = "Refrigerator is not present in the table on 'Processed Recovered Tissues' page.";
					}
				}
				else
				{
					Err.Description = "Donor is not present in the table on 'Processed Recovered Tissues' page.";
				}
			}
				else
			Err.Description = "Recovery ID is not present in the table on 'Processed Recovered Tissues' page.";
			
			return blnFlag;
		}	
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyManufacturingLineTechnicianObjects
        //''@Objective: This Function verifies if Manufacturing line and Technician objects are present on the 'Process Recovered Tissues' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strUserName - The Logged in username which needs to be verified in the prepopulated grid on 'Process Recovered Tissues' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyManufacturingLineTechnicianObjects("smoketest")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-05-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean verifyManufacturingLineTechnicianObjects(String strUserName) throws Exception 
		{
			boolean blnFlag = false;
			blnFlag=Page("pge_ProcessRecoveredTissues").Element("lstManufacturingLine").Exist();
					
			if (blnFlag)
			{	
				blnFlag = verifyExactText("pge_ProcessRecoveredTissues","objTechnician",strUserName);
				if(!blnFlag)
					Err.Description = "The Technician username '"+strUserName+"' does not display on the Technician area on 'Processed Recovered Tissues' page.";				
			}
			else
				Err.Description = "The Manufacturing line dropdown does not display on 'Processed Recovered Tissues' page.";
			
			return blnFlag;
		}

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: provideTechnicianSignature
        //''@Objective: This Function provides technician username and password in the 'Process Recovered Tissues' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strUserName - The Technician user name that needs to be entered in 'Process Recovered Tissues' page.
		//''@	strPassword - The Technician password that needs to be entered in 'Process Recovered Tissues' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= provideTechnicianSignature("akhare","India@123")    
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-05-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean provideTechnicianSignature(String strUserName, String strPassword)throws Exception 
		{
			boolean blnFlag = false;
			blnFlag = Page("pge_ProcessRecoveredTissues").Element("txtTechnicianUserName").Type(strUserName);
			if (blnFlag == true) 
			{
				blnFlag = Page("pge_ProcessRecoveredTissues").Element("txtTechnicianPassword").Type(strPassword);
				if (!blnFlag)
						Err.Description = "Unable to type in Technician Password.";				
			} 
			else
				Err.Description = "Unable to type in Technician UserId.";
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickandVerifyDonorIDLink
        //''@Objective: This Function clicks on a donor link corresponding to a specified recoveryid and verifies new page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strRecoveryId - The Recovery ID, corresponding to which the Donor ID needs to be clicked.
		//''@	strExpText - The Expected Title of the navugated page after the click on Donor ID has taken place.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickandVerifyDonorIDLink("WC14F036", "Recovered Tissue")  
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-05-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean clickandVerifyDonorIDLink(String strRecoveryId, String strExpText)throws Exception 
		{
			boolean blnFlag = false;
			//driver.findElement(By.xpath("//a[contains(text(),'" + strRecoveryId + "')]")).click();
			Page("pgeDonorProcessing").Element("//span[text()='" + strRecoveryId + "']/parent::*/preceding-sibling::td/a").Click(20);
			blnFlag = getPageTitle(strExpText);
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: addAmnionorChorion
        //''@Objective: This Function either adds and amnion or a Chorion on the 'Raw Tissue Assessment' page depending on the input parameter after filling //''@	in required details (strTissueType).
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strTissueType - The Tissue Type which needs to be added, dictates the flow of the function. Can be either 'Amnion' or 'Chorion'.
		//''@	strHeight - The height of the tissue which needs to be entered while adding the tissue.
		//''@	strWidth -  The width of the tissue which needs to be entered while adding the tissue.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= addAmnionorChorion("Amnion","10","15")    
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-06-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean addAmnionorChorion(String strTissueType,String strHeight, String strWidth) throws Exception 
		{
			boolean blnFlag = false;
			if(strTissueType.equalsIgnoreCase("Amnion"))
			{
				Page("pgeRawTissueAssessment").Element("txtTissueHeight").Type(strHeight);
				Page("pgeRawTissueAssessment").Element("txtTissueWidth").Type(strWidth);
				Page("pgeRawTissueAssessment").Element("btnAddAmnion").Click(20);
				blnFlag = Page("pgeRawTissueAssessment").Element("objAmnionAdded").Exist();
				if (!blnFlag)
						Err.Description = "The Amnion has not been added successfully.";
			}
			else if(strTissueType.equalsIgnoreCase("Chorion"))
			{
				Page("pgeRawTissueAssessment").Element("txtTissueHeight").Type(strHeight);
				Page("pgeRawTissueAssessment").Element("txtTissueWidth").Type(strWidth);
				Page("pgeRawTissueAssessment").Element("btnAddChorion").Click(20);	
				blnFlag = Page("pgeRawTissueAssessment").Element("objChorionAdded").Exist();
				if (!blnFlag)
						Err.Description = "The Chorion has not been added successfully.";
			}
			else
			{
				blnFlag=false;
				Err.Description = "Please pass correct input(either 'Amnion' or 'Chorion') to the function 'AddAmnionorChorion'."; 
			}
						
			return blnFlag;
		}
				
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: deleteAmnionorChorion
        //''@Objective: This Function either deletes and amnion or a Chorion on the 'Raw Tissue Assessment' page depending on the input parameter and verifies successful deletion.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strTissueType - The Tissue Type which needs to be deleted, dictates the flow of the function. Can be either 'Amnion' or 'Chorion'.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= deleteAmnionorChorion("Amnion")      
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-07-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean deleteAmnionorChorion(String strTissueType) throws Exception 
		{
			boolean blnFlag = false;
			if(strTissueType.equalsIgnoreCase("Amnion"))
			{
				Page("pgeRawTissueAssessment").Element("lnkAmnionDelete").Click(20);
				//After deleting Amnion, wait for one sec for the change to reflect
				waitForSync(1);
				blnFlag = !(Page("pgeRawTissueAssessment").Element("objAmnionAdded").Exist());
				if (!blnFlag)
						Err.Description = "The Amnion has not been deleted successfully.";
			}
			else if(strTissueType.equalsIgnoreCase("Chorion"))
			{
				Page("pgeRawTissueAssessment").Element("lnkChorionDelete").Click(20);
				//After deleting Chorion, wait for one sec for the change to reflect
				waitForSync(1);
				blnFlag = !(Page("pgeRawTissueAssessment").Element("objChorionAdded").Exist());
				if (!blnFlag)
						Err.Description = "The Chorion has not been deleted successfully.";
			}
			else
			{				
				Err.Description = "Please pass correct input(either 'Amnion' or 'Chorion') to the function 'DeleteAmnionorChorion'."; 
			}
						
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillRawTissueAssmt
        //''@Objective: This Function fills the raw tissue assessment form.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strAmnionHeight - The height of the Amnion which needs to be entered while filling raw tissue assesment form.
		//''@	strAmnionWidth - The width of the Amnion which needs to be entered while filling raw tissue assesment form.
		//''@	strChorionHeight -  The height of the Chorion which needs to be entered while filling raw tissue assesment form.
		//''@	strChorionWidth -  The width of the Chorion which needs to be entered while filling raw tissue assesment form.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fillRawTissueAssmt("10","12","10","15")      
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-07-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean fillRawTissueAssmt(String strAmnionHeight, String strAmnionWidth,String strChorionHeight, String strChorionWidth)
		{
			boolean blnFlag = false;
			try
			{
				blnFlag=addAmnionorChorion("Amnion", strAmnionHeight, strAmnionWidth);
				//Sync issue in this page - Wait for one sec
				waitForSync(1);
				blnFlag=addAmnionorChorion("Chorion", strChorionHeight, strChorionWidth);
				//Sync issue in this page - Wait for one sec
				waitForSync(1);
			}
			catch(Exception e)
			{
				blnFlag = false;			
			}
					
			blnFlag = Page("pgeRawTissueAssessment").Element("rdoChorionPresent").Click(20);
								
			if (blnFlag == true) 
			{
							
				blnFlag = Page("pgeRawTissueAssessment").Element("rdoProcessOk").Click(20);
										
				if (!blnFlag)
					Err.Description = "Unable to select 'Tissue Acceptable for processing' radio button.";
									
			}
			else
				Err.Description = "Unable to select 'Chorion present' radio button.";
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: electronicSignatureTissueAssessment
        //''@Objective: This Function signs the signs the canvas on Raw Tissue Assessment page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	txtUser - Initials of the user to fill in the Signature field in Raw Tissue Assesment page .
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= electronicSignatureTissueAssessment("akhare","India@123")     
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-07-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean electronicSignatureTissueAssessment(String txtUser, String txtPassword)
		{
			boolean blnFlag = false;
			/* - Commenting this as Canvas pad is no longer part of functionality
			blnFlag = canvasSignature(driver);
			if(blnFlag)
			{			
				blnFlag = Page("pgeRawTissueAssessment").Element("txtSignaturePadInput").Type(txtUser);
				if(!blnFlag)
					Err.Description = "Unable to type user ID.";							
			}
			else
			{
				blnFlag = false;
				Err.Description = "Unable to enter signature in canvas.";	
			}
			*/
			blnFlag = Page("pgeRawTissueAssessment").Element("txtUsername").Type(txtUser);
				if(blnFlag)
				{
					blnFlag = Page("pgeRawTissueAssessment").Element("txtPassword").Type(txtPassword);
					if(!blnFlag)
						Err.Description = "Unable to type in password.";
				}
				else
				{					
					Err.Description = "Unable to type in username.";	
				}
				
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillAmnionChorionPopup
        //''@Objective: This Function fills the Hold or Release Amnion or Chorion popup form (selects a valid refrigerator).
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strRefrigerator - The refrigerator name that is entered in the Hold/Release Amnion/Chorion popup.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fillAmnionChorionPopup("RFI-015R")     
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-07-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean fillAmnionChorionPopup(String strRefrigerator)
		{
			boolean blnFlag = false;			
					
			waitForSync(2);
			blnFlag = Page("pgeDonorProcessing").Element("lstRefrigerator").Select(strRefrigerator);
			/*				
			if (blnFlag == true) 
			{
				blnFlag = Page("pgeDonorProcessing").Element("txtSignatureUserName").Type(strUserName);
									
				if (blnFlag == true) 
				{
					blnFlag = Page("pgeDonorProcessing").Element("txtSignaturePassword").Type(strPassword);
			*/								
					if (!blnFlag)
					Err.Description = "Unable to select Refrigerator " +strRefrigerator+".";
				/*
				}
				else
				Err.Description = "Unable to enter Signature Username.";
			}
			else
			Err.Description = "Unable to select Refrigerator " +strRefrigerator+".";							
			*/
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyHoldReleaseRefrigeratorLog
        //''@Objective: This Function verifies the refrigerator log with Hold and Release details of Amnion or Chorion.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strTissue - The Tissue Type for which, the refrigerator log needs to be verified. Can be either 'Amnion' or 'Chorion'.
		//''@	strAction - The action that has been taken on the Amnion/Chorion, dictates the flow of the function. Can be either 'Hold' or 'Release'.
		//''@	strRecoveryId - The Recovery ID of the tissue which needs to be verified in the refrigerator log.
		//''@	strRefrigerator - The Refrigerator which needs to be verified in the refrigerator log.
		//''@	strFullName - The Full name of the user who performed the operation (Hold/Release) which needs to be verified in the refrigerator log.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyHoldReleaseRefrigeratorLog("Amnion","Hold","WC14F036","RFI-015R","Ashish Khare")   
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-07-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean verifyHoldReleaseRefrigeratorLog(String strTissue, String strAction, String strRecoveryId, String strRefrigerator, String strFullName)
		{
			boolean blnFlag = false;
			String tempVar;
			String strExpectedAction = "Hold" ; 
					
			blnFlag = Page("pgeDonorProcessing").Element("//tbody[@id='centerform:tblrfrglog_data']//*[text()='"+strTissue+"']").Exist();
							
			if (blnFlag == true) 
			{
				Page("pgeDonorProcessing").Element("//tbody[@id='centerform:tblrfrglog_data']//*[text()='"+strTissue+"']").MouseHover();
				tempVar= Page("pgeDonorProcessing").Element("//td[text()='"+strTissue+"']/preceding-sibling::td[1]").GetText();
				blnFlag = tempVar.equalsIgnoreCase(strRecoveryId);
									
				if (blnFlag == true) 
				{
				tempVar= Page("pgeDonorProcessing").Element("//td[text()='"+strTissue+"']/preceding-sibling::td[2]").GetText();
				blnFlag = tempVar.equalsIgnoreCase(strRefrigerator);
											
					if(blnFlag == true)
					{
						tempVar= Page("pgeDonorProcessing").Element("//td[text()='"+strTissue+"']/following-sibling::td[2]").GetText();
						blnFlag = tempVar.equalsIgnoreCase(strFullName);
						
						if (blnFlag == true)
						{
							tempVar= Page("pgeDonorProcessing").Element("//td[text()='"+strTissue+"']/following-sibling::td[4]").GetText();
							if(strExpectedAction.equalsIgnoreCase(strAction))
							{
								blnFlag = tempVar.isEmpty();
											
								if (!blnFlag)
								Err.Description = "There is an entry under 'Removed By', '"+tempVar+"' which is not expected for 'Hold " +strTissue+"'.";		
							}
							else
							{
								blnFlag = tempVar.equalsIgnoreCase(strFullName);
											
								if (!blnFlag)
								Err.Description = strFullName+" entry is not correct under 'Removed By' in the refrigerator log table.";	
							}						
						}
						else
						Err.Description = strFullName+" entry is not correct under 'Placed By' in the refrigerator log table.";						
					}
					else
					Err.Description = strRefrigerator+" entry is not correct in the refrigerator log table.";
				}
				else
				Err.Description = strRecoveryId+" entry is not correct in the refrigerator log table.";
			}
			else
			Err.Description = strTissue+" entry is not present in the refrigerator log table.";							
			
			return blnFlag;
		}

				
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillFixtureFields
        //''@Objective: This Function fills the Fixture and Load number fields.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strFixture - The Fixture that needs to be filled in for adding a new fixture.
		//''@	strLoadNumber - The Load number that needs to be filled for adding a new fixture.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fillFixtureFields("SNG-001","1234")      
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-11-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean fillFixtureFields(String strFixture, String strLoadNumber)
		{
			boolean blnFlag = false;					
			blnFlag = Page("pgeProductDehydration").Element("txtFixture").Type(strFixture);
							
			if (blnFlag) 
			{
				blnFlag = Page("pgeProductDehydration").Element("txtLoadNumber").Type(strLoadNumber);
															
				if (!blnFlag)
					Err.Description = "Unable to enter Load number" +strLoadNumber+".";						
			}
			else
				Err.Description = "Unable to enter Fixture" +strFixture+".";							
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyFixtureGridProductDehydration
        //''@Objective: This Function verifies the Fixture data grid that is updated on Product Dehydration page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strFixture - The Fixture that needs to be verified in the Fixture data grid after adding a new fixture.
		//''@	strLoadNumber - The Load number that needs to be verified in the Fixture data grid after adding a new fixture.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyFixtureGridProductDehydration("SNG-001","1234")     
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-11-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean verifyFixtureGridProductDehydration(String strFixture, String strLoadNumber)
		{
			boolean blnFlag = false;	
			String tempVar;			
								
			tempVar = Page("pgeProductDehydration").Element("//*[text()='Fixture']//ancestor::thead/following-sibling::tbody//td[1]").GetText();
			blnFlag = tempVar.equalsIgnoreCase(strFixture);
							
			if (blnFlag) 
			{
				tempVar = Page("pgeProductDehydration").Element("//*[text()='Fixture']//ancestor::thead/following-sibling::tbody//td[2]").GetText();
				blnFlag = tempVar.equalsIgnoreCase(strLoadNumber);
				
				if (!blnFlag)
					Err.Description = "The Load number is not updated correctly in the data grid. It is updated as '" +tempVar+"'.";		
				
			}
			else
				Err.Description = "The Fixture is not updated correctly in the data grid. It is updated as '" +tempVar+"'.";							
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickAndVerifyVisible
        //''@Objective: This Function clicks on the specified link and verifies if the object mentioned is visible or not.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	PageName - The Page Name which maps to the Element which needs to be clicked.
		//''@	Element - The Element which needs to be clicked.
		//''@	newPageName - The new Page Name which maps to the newElement which needs to be verified for visibility after Page->Element has been clicked.
		//''@	newElement - The newElement which needs to be verified for visibility after Page->Element has been clicked.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickAndVerifyVisible("pgeProductDehydration", "lnkNoEquipmentSpecified", "pgeProductDehydration","btnAddEquipment")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-11-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean clickAndVerifyVisible(String PageName, String Element, String newPageName, String newElement)
		{
			boolean blnFlag = false;
			blnFlag=Page(PageName).Element(Element).Click(20);
			waitForPageRefresh(5);
			if(blnFlag)
			{
				blnFlag = Page(newPageName).Element(newElement).isVisible();
				if (!blnFlag)
					Err.Description = "The object mentioned is not visible, the link has not expanded.";
			} 
			else
					Err.Description = "Unable to perform click action.";
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: provideSignatureDehydration
        //''@Objective: This Function signs the electronic canvas and inputs the textbox with the Signature based on the place of the function call.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strUserName - The Initials of the user which needs to be entered in the Equipment/ Sheet section.
		//''@	strPlaceofFuncCall - The place where this function is called in Dehydration page, dictates the object where strUserName 
		//''@                        is entered. Can be either 'Equipment' or 'Sheet'.	
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= provideSignatureDehydration("akhare","Equipment")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-11-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean provideSignatureDehydration(String strUserName,String strPlaceofFuncCall)
		{
			boolean blnFlag = false;
			String strElement = null;
			String strElementToClick = "";
			
			if (strPlaceofFuncCall.equalsIgnoreCase("Equipment"))
			{
				strElement = "txtSign_Equipment";
				strElementToClick ="objDateField";
				blnFlag = true;
			}
			else if (strPlaceofFuncCall.equalsIgnoreCase("Sheet"))
			{
				strElement = "txtInitials";
				blnFlag = true;
			}
			else
			{
				blnFlag = false;
				Err.Description = "Pass valid paramaters to the function 'ProvideSignatureDehydration'.";
			}		
			
			blnFlag=Page("pgeProductDehydration").Element(strElement).Type(strUserName);
			
			if (blnFlag)
			{					
				// Click on the 'Date' label so that the focus can be shifted from the calendar popup
				if(!strElementToClick.isEmpty())
					blnFlag = Page("pgeProductDehydration").Element(strElementToClick).Click();
				//Sync Issue at this page - Wait for one sec
				waitForSync(2);
				blnFlag = canvasSignature(driver);
				if(!blnFlag)
				{
					blnFlag = false;
					Err.Description = "Unable to enter signature in canvas.";	
				}				
				blnFlag = true;			
			}
			else
			Err.Description = "Unable to type the Username.";				
			
			return blnFlag;
		}
				
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: selectEquipmentTypeListBox
        //''@Objective: This Function selects the specified value from the Equipment Type list box.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strSelectionValue - The value of the Equipment type that needs to be selected from the 'Equipment Type' list box.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= selectEquipmentTypeListBox("Tissue Dryer")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-12-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean selectEquipmentTypeListBox(String strSelectionValue)
		{
			boolean blnFlag = false;
			try
			{	
				blnFlag = Page("pgeProductDehydration").Element("txtSign_Equipment").Click();
				boolean tempFlag = Page("pgeProductDehydration").Element("//div[contains(@id,'eqtype')]//span").Click(20);
				tempFlag = Page("pgeProductDehydration").Element("//li[@data-label='" + strSelectionValue + "']").Click(20);
				//Wait for one second to wait for selection value to show up in the list
				waitForSync(1);
				blnFlag = true;	
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Unable to select the option '" +strSelectionValue+ "' from the list box.";					
			}
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: selectEquipmentListBox
        //''@Objective: This Function selects the specified value from the Equipment list box based on the specific place where it is called.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strSelectionValue - The value of the Equipment that needs to be selected from the 'Equipment' list box.		
		//''@	strPlaceofFuncCall - The place where this function is called in Dehydration page, dictates the object where strSelectionValue 
		//''@                        is selected from. Can be either 'Equipment' or 'Sheet'.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= selectEquipmentListBox("TD-005","Equipment")     
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-12-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean selectEquipmentListBox(String strSelectionValue,String strPlaceofFuncCall)
		{
			boolean blnFlag = false;
			String strElement = null;
			
			if (strPlaceofFuncCall.equalsIgnoreCase("Equipment"))
			{
				strElement = "lstEquipment";
				blnFlag = true;
			}
			else if(strPlaceofFuncCall.equalsIgnoreCase("Sheet"))
			{
				strElement = "lstEquipment_Sheet";
				blnFlag = true;
			}
			else
			{
				blnFlag = false;
				Err.Description = "Pass valid paramaters to the function 'SelectEquipmentListBox'.";	
			}
			/*
			switch(strPlaceofFuncCall)
			{
				case "Equipment": strElement = "lstEquipment";
					blnFlag = true;
					break;
				case "Sheet": strElement = "lstEquipment_Sheet";
					blnFlag = true;
					break; 
				default: blnFlag = false;
					Err.Description = "Pass valid paramaters to the function 'SelectEquipmentListBox'.";	
					break;
			}
			*/
			if(blnFlag)
			{			
				Page("pgeProductDehydration").Element(strElement).Type(strSelectionValue);
				//Wait for one second for the Selection value to show up on the list box
				waitForSync(1);
				boolean tempflag = Page("pgeProductDehydration").Element("//li[@data-item-value='" + strSelectionValue + "']").Click(20);
				//Wait for one second for the page to refresh with the pre-populated values
				waitForPageRefresh();				
			}
			else
			{
				blnFlag = false;
				Err.Description = "Unable to select the option '" +strSelectionValue+ "' from the list box.";					
			}		
					
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: selectConfirmTempCheckBox
        //''@Objective: This Function selects the specified check box.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= selectConfirmTempCheckBox()      
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-12-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean selectConfirmTempCheckBox()
		{
			boolean blnFlag = false;
			try
			{			
				driver.findElement(By.xpath("//input[contains(@name,'ovntempconfirm')]")).click();				
				blnFlag = true;
			}
			catch(Exception e)
			{
				blnFlag = false;
			}
			
			if(!blnFlag)
				Err.Description = "Unable to select the Confirm Oven Temperature check box.";					
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyEquipmentGridProductDehydration
        //''@Objective: This Function verifies the Equipment data grid that is updated on Product Dehydration page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strEquipmentType - The value of the Equipment Type that needs to be verified from the Equipment grid on Product Dehydration page.		
		//''@	strEquipment - The value of the Equipment that needs to be verified from the Equipment grid on Product Dehydration page.		
		//''@	strFullName - The user's full name that needs to be verified from the Equipment grid on Product Dehydration page.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyEquipmentGridProductDehydration("Tissue Dryer","TD-0005","akhare")  
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-12-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean verifyEquipmentGridProductDehydration(String strEquipmentType, String strEquipment,String strFullName)
		{
			boolean blnFlag = false;	
			String tempVar;			
					
			Page("pgeProductDehydration").Element("//*[contains(@id,'tbleqplist_data')]//td[1]").MouseHover();
			tempVar = Page("pgeProductDehydration").Element("//*[contains(@id,'tbleqplist_data')]//td[1]").GetText();
			blnFlag = tempVar.equalsIgnoreCase(strEquipmentType);
							
			if (blnFlag) 
			{
				tempVar = Page("pgeProductDehydration").Element("//*[contains(@id,'tbleqplist_data')]//td[2]").GetText();
				blnFlag = tempVar.equalsIgnoreCase(strEquipment);
				
				if(blnFlag)
				{
					tempVar = Page("pgeProductDehydration").Element("//*[contains(@id,'tbleqplist_data')]//td[6]").GetText();
					blnFlag = tempVar.equalsIgnoreCase(strFullName);
				
					if (!blnFlag)
						Err.Description = "The Full Name is not updated correctly in the data grid. It is updated as '" +tempVar+"'.";		
				}
				else
					Err.Description = "The Equipment is not updated correctly in the data grid. It is updated as '" +tempVar+"'.";		
			}
			else
				Err.Description = "The Equipment Type is not updated correctly in the data grid. It is updated as '" +tempVar+"'.";							
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyStartsStopsTable
        //''@Objective: This Function verifies the 'Starts and Stops' log with Start and Stop details of the Sheet.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strAction - The place where this function is called(in Start or Stop Sheet functionality).It dictates the data that is being 
		//''@	verified in the Starts and Stop Table. Can be either 'Start' or 'Stop'.			
		//''@	strEquipment - The value of the Equipment that needs to be verified from the Starts and Stops grid on Product Dehydration page.		
		//''@	strFullName - The user's full name that needs to be verified from the Starts and Stops grid on Product Dehydration page.		
		//''@	strUserName - The user's initials that needs to be verified from the Starts and Stops grid on Product Dehydration page.				
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyStartsStopsTable("Start","TD-005","Ashish Khare","akhare")      
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-13-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean verifyStartsStopsTable(String strAction,String strEquipment, String strFullName, String strUserName)
		{
			boolean blnFlag = false;
			String tempVar;
			String strExpectedAction = "Start";
								
			Page("pgeProductDehydration").Element("//*[contains(@id,'tblsheetlist')]/td").MouseHover();					
			tempVar = Page("pgeProductDehydration").Element("//*[contains(@id,'tblsheetlist')]/td/a").GetText();
			blnFlag = tempVar.equalsIgnoreCase(strEquipment);
							
			if (blnFlag == true) 
			{
				tempVar= Page("pgeProductDehydration").Element("//*[contains(@id,'tblsheetlist')]/td[3]").GetText();
				blnFlag = tempVar.equalsIgnoreCase(strFullName);
									
				if (blnFlag == true) 
				{
					tempVar= Page("pgeProductDehydration").Element("//*[contains(@id,'tblsheetlist')]/td[6]").GetText();
											
					if(strExpectedAction.equalsIgnoreCase(strAction))
					{
						blnFlag = tempVar.isEmpty();
									
						if (!blnFlag)
							Err.Description = "There is an entry under 'Name', '"+tempVar+"' which is not expected for 'Start Sheet." ;		
					}
					else
					{
						blnFlag = tempVar.equalsIgnoreCase(strFullName);
									
						if (!blnFlag)
							Err.Description = strFullName+" entry is not correct under 'Name' in the Starts and Stops table in the Stops section.";	
					}										
					
				}
				else
					Err.Description = strFullName+" entry is not correct in the under 'Name' in the Starts and Stops table in the Starts section.";
			}
			else
				Err.Description = strEquipment+" entry is not correct in the under 'Sheet' in the Starts and Stops table.";							
			
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyPouchesTable
        //''@Objective: This Function verifies the 'Pouches' log on Product Dehydration page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strLoadNumber - The value of the Load number that needs to be verified from the Pouches log on Product Dehydration page.		
		//''@	strEquipmentPouch - The value of the Equipment that needs to be verified from the Pouches log on Product Dehydration page.		
		//''@	strSealedAtTemp - The value of the Sealed At temperature that needs to be verified from the Pouches log on Product Dehydration page.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyPouchesTable("1234","HS-004","250")    
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-13-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean verifyPouchesTable(String strLoadNumber,String strEquipmentPouch, String strSealedAtTemp)
		{
			boolean blnFlag = false;
			String tempVar;
			Page("pgeProductDehydration").Element("//*[contains(@id,'tblpouchlist_data')]//td").MouseHover();				
											
			tempVar = Page("pgeProductDehydration").Element("//*[contains(@id,'tblpouchlist_data')]//td").GetText();
			blnFlag = tempVar.equalsIgnoreCase(strLoadNumber);
							
			if (blnFlag == true) 
			{
				tempVar = Page("pgeProductDehydration").Element("//*[contains(@id,'tblpouchlist_data')]//td[3]").GetText();
				blnFlag = tempVar.equalsIgnoreCase(strEquipmentPouch);
									
				if (blnFlag == true) 
				{
					tempVar = Page("pgeProductDehydration").Element("//*[contains(@id,'tblpouchlist_data')]//td[5]").GetText();
					blnFlag = tempVar.equalsIgnoreCase(strSealedAtTemp);
												
					if (!blnFlag)
						Err.Description = "There is an entry under 'Sealed At', '"+tempVar+"' which is not expected in Pouches Table.";					
					
				}
				else
					Err.Description = strEquipmentPouch+" entry is not correct under 'Equipment' in Pouches table.";
			}
			else
				Err.Description = strLoadNumber+" entry is not correct in the under 'Lot #' in the Pouches table.";							
			
			return blnFlag;
		}	
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickAndVerifyNotExist
        //''@Objective: This Function clicks on a link and verifies that the new object does not exist.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	PageName - The Page Name which maps to the Element which needs to be clicked.
		//''@	Element - The Element which needs to be clicked.
		//''@	newPageName - The new Page Name which maps to the newElement which needs to be verified for non-existence after Page->Element has been 
		//''@   clicked.
		//''@	newElement - The newElement which needs to be verified for non-existence after Page->Element has been clicked.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickAndVerifyNotExist("pgeDonorProcessing", "lnkDelete", "pgeDonorProcessing", "lnkAddCut_SecondRow")      
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-13-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean clickAndVerifyNotExist(String PageName, String Element, String newPageName, String newElement)
		{
			boolean blnFlag = false;
			blnFlag=Page(PageName).Element(Element).Click(20);
			if(blnFlag==true)
			{
				//Wait for the Page  to Refresh
				waitForPageRefresh();
				blnFlag = !(Page(newPageName).Element(newElement).Exist());
				if (!blnFlag)
						Err.Description = "The element mentioned is still existing.";
			} 
			else
					Err.Description = "Unable to perform click action.";
			
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickAndVerify
        //''@Objective: This function clicks on a link on page and calls the function getPageTitle() to verify that user is navigated on 
		//''@ correct page or not.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	PageName - The Page Name which maps to the Element which needs to be clicked.
		//''@	Element - The Element which needs to be clicked.
		//''@	strExpText - The new navigated page title which needs to be verified after Page->Element has been clicked.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickAndVerify(Page, Element, "Recovered Tissue")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[08-11-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean clickAndVerify(String Page, String Element, String strExpText)
		{
			boolean blnFlag = false;
			blnFlag=Page(Page).Element(Element).Click(20);
			if(blnFlag==true)
			{
				blnFlag = getPageTitle(strExpText);
			}
			else
			Err.Description = "Unable to perform click action.";	
			return blnFlag;
		}

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickVendorName
        //''@Objective: This function clicks specified vendor name on Sterilization page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	PageName - The Page Name which maps to the Element which needs to be clicked.
		//''@	strVendorName - The Vendor name which needs to be clicked.
		//''@	newPageName - The new navigated page which needs to be verified after Page->strVendorName has been clicked.
		//''@	newElement - An element in the new newPageName which needs to be validated for existance after Page->strVendorName has been clicked.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickVendorName(Page, Element, "Recovered Tissue")      
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[08-11-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean clickVendorName(String PageName, String strVendorName, String newPageName, String newElement)
		{
			boolean blnFlag = false;
			blnFlag=Page(PageName).Element("//*[text()='"+strVendorName+"']").Click(20);
			if(blnFlag==true)
			{
				blnFlag = Page(newPageName).Element(newElement).Exist();
				if (!blnFlag)
						Err.Description = "Unable to verify navigated page.";
			} 
			else
					Err.Description = "Unable to perform click action.";
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: selectTissueForSterilization
        //''@Objective: This function selects a particular tissue for sterilization based on the donor key. 		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strDonorKey - The donor key which maps to the tissue whose check box needs to be selected for Sterlization.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= selectTissueForSterilization("20131944")      
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[08-11-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean selectTissueForSterilization(String strDonorKey)
		{
			boolean blnFlag = false;
			blnFlag=Page("pgeSterilization").Element("//*[text()='"+strDonorKey+"']/../..//input").Click(20);
			if (blnFlag== true)
			{
				blnFlag=Page("pgeSterilization").Element("//*[text()='"+strDonorKey+"']/../..//input").isSelected();
				if (!blnFlag)
					Err.Description = "Checkbox not checked successfully.";
			}
			else 
				Err.Description = "Unable to perform click action.";
			return blnFlag;
		}

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fetchDonorKey
        //''@Objective: This function fetches donor key for the specified recovery id from Recovered Tissue page. 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strRecoveryID - The Recovery ID for which the Donor Key needs to be fetched.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fetchDonorKey("WK14H025")      
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[08-11-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean fetchDonorKey(String strRecoveryId)
		{
			String strDonorKey="";
			boolean blnFlag = false;
			strDonorKey=Page("pgeRecovered_Tissue").Element("//*[text()='"+strRecoveryId+"']/../../td[2]").GetText();
			if(!(strDonorKey.equals("") || strDonorKey.equals(null)))
			{
				blnFlag=sendValuetoCommonSheet("strDonorKey",strDonorKey);
				if (!blnFlag) 
					Err.Description = Err.Description;
					blnFlag=true;
			}
			else
			{
				Err.Description = "Unable to fecth donor key.";
				blnFlag=false;
			}

			return blnFlag;	
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: scanTissue
        //''@Objective: This function enters all the available Tissue Names in the edit box on for Sterilization and Sterilization Returns functionality. 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	Page - Page name to mapping the object on the page.
        //''@	Element - object name on which action needs to perform.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= scanTissue("pgeSterilizationReturns","objQtyAvailable")       
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-13-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean scanTissue(String Page, String Element)
		{
			boolean blnFlag = false;
			String strQty="";
			int intTemp=1;
			int intQty=0;
			String strTissue;
			
			strQty = Page(Page).Element(Element).GetText();
			intQty = Integer.parseInt(strQty);
			try
			{
				while((intTemp<=intQty))  
				{
					strTissue=Page(Page).Element("(//span[contains(@id,'itemtissue')])["+intTemp+"]").GetText();
					// blnFlag=Page(Page).Element(scanElement).Type(strTissue);
					driver.findElement(By.xpath("//textarea[contains(@class,'wideText')]")).sendKeys(strTissue);
					driver.findElement(By.xpath("//textarea[contains(@class,'wideText')]")).sendKeys(Keys.RETURN);
					intTemp = intTemp + 1;					
				}
				if(intTemp==(intQty+1))
				{
					blnFlag=true;
				}
			}	
			catch(Exception e)
			{
				Err.Description = "All the tissues were not added for process scan.";
			}

		return blnFlag;
		}

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: scanTissueAddFillerPouch
        //''@Objective: This function first calls scanTissue function for entering the tissues in edit box, and then adds empty filler pouches. 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	Page - Page name to mapping the object on the page.
        //''@	Element - object name in which the quantity of tissues available needs to be retrieved.
		//''@	fillElement - object name in which the quantity of empty pouches needed needs to be retrieved.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= scanTissueAddFillerPouch("pgeSterilizationReturns","objQtyAvailable","objFillElement")    
		//''@---------------------------------------------------------------------------------------------------------------------------		
		//''@Created by[Date]: Ashish Khare[08-13-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean scanTissueAddFillerPouch(String Page, String Element, String fillElement)
		{
			boolean blnFlag = false;
			String strFillerQty="";
			int intTemp=1;
			int intFillerQty=0;
			String strFillerPouch;
			
			strFillerQty = Page(Page).Element(fillElement).GetText();
			intFillerQty = Integer.parseInt(strFillerQty);
			
			blnFlag=scanTissue(Page,Element);
			if (blnFlag==true)
			{
				try
				{
					while((intTemp<=intFillerQty))  
					{
						strFillerPouch="FILLER POUCH-"+String.format("%03d", intTemp);
						// blnFlag=Page(Page).Element(scanElement).Type(strFillerPouch);
						driver.findElement(By.xpath("//*[@class='wideTextInput']")).sendKeys(strFillerPouch);
						driver.findElement(By.xpath("//*[@class='wideTextInput']")).sendKeys(Keys.RETURN);
						intTemp = intTemp + 1;					
					}
					if(intTemp==(intFillerQty+1))
					{
						blnFlag=true;
					}
				}
				catch(Exception e)
				{
					Err.Description = "All the filler pouches were not added for process scan.";
				}
			}
			else
				Err.Description = "All the tissues were not added for process scan.";
			return blnFlag;
		}	
	
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyReturnedCheckboxes
        //''@Objective: This function verifies that all the 'Returned' checkboxes after certifying from sterilization is checked. 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	Page - Page name to mapping the object on the page.
        //''@	Element - object name in which the quantity of tissues available needs to be retrieved.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyReturnedCheckboxes("pgeSterilizationReturns","objQtyAvailable")     
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-13-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean verifyReturnedCheckboxes(String Page, String Element)
		{
			boolean blnFlag = true;
			String strqty;
			int intTemp = 0;
			int intqty = 0;

			strqty = Page(Page).Element(Element).GetText();
			intqty = Integer.parseInt(strqty);
		
			while((intTemp<intqty) && (blnFlag!=false))  
			{
				blnFlag =  Page("pgeSterilizationReturns").Element("//*[@id='tblsterilizationitems:"+intTemp+":itemreturned']").isSelected();
				intTemp = intTemp + 1;					
			}
			
			if(!blnFlag)	
			Err.Description = "All the tissues 'Return' checkboxes were not checked";
			
			return blnFlag;
		}
			
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyLocationGridUpdated
        //''@Objective: This function verifies that Location is updated under 'Validate Returned Items from Sterilization' grid under Location heading. 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strLocation - The value of the 'Location' that needs to be verified if it is updated in 'Validate Returned Items for Sterlization'.        
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyLocationGridUpdated("Sterlization")      
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-18-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean verifyLocationGridUpdated(String strLocation)
		{
			boolean blnFlag = true;
			String strLocationGrid;
			Page("pgeSterilizationReturns").Element("objLocationGrid").MouseHover();
			strLocationGrid = Page("pgeSterilizationReturns").Element("objLocationGrid").GetText();
			
			blnFlag = strLocationGrid.equalsIgnoreCase(strLocation);
									
				if (!blnFlag) 
				{
				Err.Description = "The Location under grid is not updated correctly. It is updated as '"+strLocationGrid+"' instead of '"+strLocation+"'.";
				}					
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fetchDonorKeyPackageAssembly
        //''@Objective: This function retrieves the first donor key which is eligible for Package Assembly and processing. 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fetchDonorKeyPackageAssembly()      
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-20-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean fetchDonorKeyPackageAssembly()
		{
			String strDonorKey="";
			boolean blnFlag = false;
			
			Page("pgeDonorstoPackage").Element("txtStatusSearch").Type("New");
			waitForPageRefresh();
			strDonorKey=Page("pgeDonorstoPackage").Element("lnkDonorID").GetText();
			
			if(!(strDonorKey.equals("") || strDonorKey.equals(null)))
			{
				Script.dicCommonValue.put("DonorKey", strDonorKey);
				blnFlag=true;
			}
			else
			{
				Err.Description = "Unable to fetch donor key.";
				blnFlag=false;
			}
			
			//Clearing the entry in the Status Search field
			Page("pgeDonorstoPackage").Element("txtStatusSearch").Type("");
			
			return blnFlag;	
		}
	
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillPackagingPage
        //''@Objective: This function fills the Packaging Collection Page with the required entries. 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strUserName - The user name that is used to sign at the bottom of the Packaging Collection page.        	
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fillPackagingPage("akhare")      
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-21-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean fillPackagingPage(String strUserName)
		{
			boolean blnFlag = false;
						
			blnFlag = Page("pgePackagingCollection").Element("chkProductCarton").Click(20);
			if (blnFlag)
			{
				blnFlag = Page("pgePackagingCollection").Element("chkPouch").Click(20);
				if (blnFlag)
				{
					blnFlag = Page("pgePackagingCollection").Element("chkInstructionsforUse").Click(20);
					if (blnFlag)
					{
						blnFlag = Page("pgePackagingCollection").Element("chkTURCard").Click(20);
						if(blnFlag)
						{
							blnFlag = Page("pgePackagingCollection").Element("chkStickers").Click(20);
							if(blnFlag)
							{
								blnFlag = Page("pgePackagingCollection").Element("chkCMVCard").Click(20);
								if(blnFlag)
								{
									blnFlag = Page("pgePackagingCollection").Element("chkBackLabel").Click(20);
									if(!blnFlag)
									{
										Err.Description = "Unable to click the 'Back Label' check box.";
									}
								}
								else
								Err.Description = "Unable to click the 'CMV Card' check box.";
							}
							else
							Err.Description = "Unable to click the 'Stickers (If Applicable)' check box.";
						}
						else
						Err.Description = "Unable to click the 'TUR Card' check box.";
					}
					else
					Err.Description = "Unable to click the 'Instructions for Use' check box.";
				}
				else
				Err.Description = "Unable to click the 'Pouch' check box.";
			}
			else
			Err.Description = "Unable to click the 'Product Carton' check box.";
			
			//Proceed only if the checkboxes are all clicked
			if(blnFlag)
			{	
				if(Page("pgePackagingCollection").Element("lstCMVCard").Exist())
					blnFlag = Page("pgePackagingCollection").Element("lstCMVCard").Select("SB171.004 (Negative)");
				if(blnFlag)
				{
					blnFlag = Page("pgePackagingCollection").Element("txtSignature").Type(strUserName);
					if(blnFlag)
					{
						blnFlag = canvasSignature(driver);
						if(!blnFlag)
						Err.Description = "Unable to enter the signature in the canvas pad.";
					}
					else
					Err.Description = "Unable to enter the User Name '"+strUserName+"' in the 'Collected By' Area.";
				}
				else
				Err.Description = "Unable to select the CMV Card value '"+strUserName+"' from the 'CMV Card' dropdown.";
			}	
						
			
			return blnFlag;	
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillPackagingPage
        //''@Objective: This function fills the Packaging Assembly Page with the required entries. 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strUserName - The user name that is used to sign at the bottom of the Packaging Assembly page.        
		//''@	strAssemblyLine - The Assembly Line value that needs to be selected from the Assembly Line dropdown.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fillPackagingPage("asmith","Line 1")       
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[09-02-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean fillPackagingPage(String strUserName, String strAssemblyLine)
		{
			boolean blnFlag = false;
						
			blnFlag = Page("pgePackagingCollection").Element("chkProductCarton").Click(20);
			if (blnFlag)
			{
				blnFlag = Page("pgePackagingCollection").Element("chkPouch").Click(20);
				if (blnFlag)
				{
					blnFlag = Page("pgePackagingCollection").Element("chkInstructionsforUse").Click(20);
					if (blnFlag)
					{
						blnFlag = Page("pgePackagingCollection").Element("chkTURCard").Click(20);
						if(blnFlag)
						{
							blnFlag = Page("pgePackagingCollection").Element("chkStickers").Click(20);
							if(blnFlag)
							{
								blnFlag = Page("pgePackagingCollection").Element("chkCMVCard").Click(20);
								if(blnFlag)
								{
									blnFlag = Page("pgePackagingCollection").Element("chkBackLabel").Click(20);
									if(!blnFlag)
									{
										Err.Description = "Unable to click the 'Back Label' check box.";
									}
								}
								else
								Err.Description = "Unable to click the 'CMV Card' check box.";
							}
							else
							Err.Description = "Unable to click the 'Stickers (If Applicable)' check box.";
						}
						else
						Err.Description = "Unable to click the 'TUR Card' check box.";
					}
					else
					Err.Description = "Unable to click the 'Instructions for Use' check box.";
				}
				else
				Err.Description = "Unable to click the 'Pouch' check box.";
			}
			else
			Err.Description = "Unable to click the 'Product Carton' check box.";
			
			//Proceed only if the checkboxes are all clicked
			blnFlag = Page("pgePackagingCollection").Element("lstAssemblyLine").Select(strAssemblyLine);
			if(blnFlag)
			{			
				if(blnFlag)
				{	
					if(Page("pgePackagingCollection").Element("lstCMVCard").Exist())
						blnFlag = Page("pgePackagingCollection").Element("lstCMVCard").Select("SB171.004 (Negative)");
					if(blnFlag)
					{
						blnFlag = Page("pgePackagingCollection").Element("txtSignature").Type(strUserName);
						if(blnFlag)
						{
							blnFlag = canvasSignature(driver);
							if(!blnFlag)
							Err.Description = "Unable to enter the signature in the canvas pad.";
						}
						else
						Err.Description = "Unable to enter the User Name '"+strUserName+"' in the 'Collected By' Area.";
					}
					else
					Err.Description = "Unable to select the CMV Card value '"+strUserName+"' from the 'CMV Card' dropdown.";
				}
			}
			else
			Err.Description = "Unable to select the Assembly Line value '"+strAssemblyLine+"' from the 'Assembly Line' dropdown.";
							
			return blnFlag;	
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyTextContains
        //''@Objective: This Function verifies that the expected text is contained in the object or not.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	PageName - The page mapping to the object which needs to be validated.
		//''@	Element - The object which needs to be validated if it contains the expected text.
		//''@	strExpText - The expected text to be validated , if it is contained in the Element.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyTextContains("pgeDonorstoPackage", "objStatusResult", "Needs Pre-packaging Inspection Signature")      
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-21-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean verifyTextContains(String PageName, String Element, String strExpText)
		{
			boolean blnFlag = false;
			String strActualText = Page(PageName).Element(Element).GetText();
			if ((strActualText.trim()).contains(strExpText.trim()))
			{
				blnFlag = true;
			}
			else
			{
				Err.Description = "The expected text, '"+strExpText+"' is not contained in the actual element's text '"+strActualText+"'.";
				blnFlag = false;				
			}
			return blnFlag;
		}	

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyExactText
        //''@Objective: This Function verifies that the expected text is present in the object or not.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	PageName - The page mapping to the object which needs to be validated.
		//''@	Element - The object which needs to be validated if it contains the expected text.
		//''@	strExpText - The expected text to be validated , if it is present in the Element.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyExactText("pgeDonorstoPackage", "objStatusResult", "Needs Pre-packaging Inspection Signature")       
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-21-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean verifyExactText(String PageName, String Element, String strExpText)
		{
			boolean blnFlag = false;
			String strActualText = Page(PageName).Element(Element).GetText();
			strActualText=strActualText.trim();
			strExpText=strExpText.trim();
			
			if (strActualText.equalsIgnoreCase(strExpText)) 
			{
				blnFlag = true;
			}
			else
			{
				Err.Description = "The expected text, '"+strExpText+"' does not match the actual element's text '"+strActualText+"'.";
				blnFlag = false;				
			}
			return blnFlag;
		}			
			
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickVerifyCheckboxesAddTissue
        //''@Objective: This function clicks or verifies all the non-archive check boxes depending on the action to be performed on all the tissues		
		//''@(i.e. @'Pass'/'Reject'/'Micronize'/'Outer Pouch Seal'/'Outer Label') in Tissue Inspection/Validate Outer Packaging page and then verifies the //''@same, leaving the archive samples as it is. 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	Page - Page name to mapping the object on the page.
        //''@	Element - object name from which the quantity of tissues can be retrieved.
		//''@	strAction - The action that is to be taken on all the tissues for Inspection- E.g. Pass/Reject/Micronize/etc.
		//''@	strActiontoPerform - The action that is to be performed on the checkboxes (Can be either 'Click' or to 'Verify' click).
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickVerifyCheckboxesAddTissue("pgeAddTissue","objQuantity","Pass","Click")        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-22-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean clickVerifyCheckboxesAddTissue(String Page, String Element, String strAction, String strActiontoPerform)
		{
			boolean blnFlag = false;
			String strqty;
			int intTemp = 0;
			int intqty = 0;
			String strObjectIdentifier = "";
			
			strqty = Page(Page).Element(Element).GetText();
			intqty = Integer.parseInt(strqty);	
			
			// Set the object according to the type of columns in Tissue Inspection page
			if (strAction.equalsIgnoreCase("Pass"))
			{
				strObjectIdentifier = "pass";
				// Start the loop leaving the first two archive sample as it is, so start loop from the third row
				intTemp = intTemp + 2;	
				blnFlag = true;				
			}
			else if (strAction.equalsIgnoreCase("Reject"))
			{
				strObjectIdentifier = "reject";
				blnFlag = true;
			}
			else if (strAction.equalsIgnoreCase("Micronize"))
			{
				strObjectIdentifier = "micro";
				blnFlag = true;
			}
			else if (strAction.equalsIgnoreCase("Ragged Edge"))
			{
				strObjectIdentifier = "raggededge";
				blnFlag = true;
			}
			else if (strAction.equalsIgnoreCase("Indistinct Logo"))
			{
				strObjectIdentifier = "indistinctlogo";
				blnFlag = true;
			}
			else if (strAction.equalsIgnoreCase("Size"))
			{
				strObjectIdentifier = "irregularshape";
				blnFlag = true;
			}
			else if (strAction.equalsIgnoreCase("Air Pocket"))
			{
				strObjectIdentifier = "notflat";
				blnFlag = true;
			}
			else if (strAction.equalsIgnoreCase("Debris"))
			{
				strObjectIdentifier = "debris";
				blnFlag = true;
			}
			else if (strAction.equalsIgnoreCase("Chorion Missing"))
			{
				strObjectIdentifier = "chorionmissing";
				blnFlag = true;
			}
			//Set the object according to the type of columns in Validate Outer Packaging page
			else if (strAction.equalsIgnoreCase("Outer Pouch Seal"))
			{
				strObjectIdentifier = "outseal";
				// Reduce the quantity to the actual number so that the loop can stop checking at the right time
				intqty = intqty - 2;
				blnFlag = true;
			}
			else if (strAction.equalsIgnoreCase("Outer Label"))
			{
				strObjectIdentifier = "outlbl";
				// Reduce the quantity to the actual number so that the loop can stop checking at the right time
				intqty = intqty - 2;
				blnFlag = true;
			}
			else 
			{
				blnFlag = false;
				Err.Description = "Pass valid paramaters to the function 'clickCheckboxesTissueInspection'.";
			}
			
			if(blnFlag)
			{				
				if (strActiontoPerform.equalsIgnoreCase("Click"))
				{					
					//Click on all the non-archive checkboxes under the column mentioned	
					while((intTemp<intqty) && (blnFlag!=false))  
					{
						blnFlag =  Page("pgeAddTissue").Element("//*[@id='rform:tbltissueitems:"+intTemp+":item"+strObjectIdentifier+"']").Click(20);
						intTemp = intTemp + 1;					
					}					
				}
				else
				{				
					//Verify if all the non-archive checkboxes under the column mentioned are clicked	
					while((intTemp<intqty) && (blnFlag!=false))  
					{
						blnFlag =  Page("pgeAddTissue").Element("//*[@id='rform:tbltissueitems:"+intTemp+":item"+strObjectIdentifier+"']").isSelected();
						intTemp = intTemp + 1;					
					}					
				}
				if(!blnFlag)	
						Err.Description = "All the non-archive tissues '"+strAction+"' checkboxes have not been clicked correctly.";	
			}
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: selectCertificateCheckboxes
        //''@Objective: This function checks the verification checkboxes on 'Edit Certificate of Conformance' page. 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= selectCertificateCheckboxes()       
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[08-21-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean selectCertificateCheckboxes()
		{
			boolean blnFlag = false;
			
			blnFlag=Page("pgeEditCertificateOfConformance").Element("chkDonorEligibility").Click(20);
			if (blnFlag==true)
			{
				blnFlag=Page("pgeEditCertificateOfConformance").Element("chkManufacturingProcess").Click(20);
				if (blnFlag==true)
				{
					blnFlag=Page("pgeEditCertificateOfConformance").Element("chkInspection").Click(20);
					if (blnFlag==true)
					{
						blnFlag=Page("pgeEditCertificateOfConformance").Element("chkInspectionProcess").Click(20);
						if (blnFlag==true)
						{
							blnFlag=Page("pgeEditCertificateOfConformance").Element("chkSterilizationProcess").Click(20);
							if (blnFlag==true)
							{
								blnFlag=Page("pgeEditCertificateOfConformance").Element("chkPackagingProcess").Click(20);
								if (blnFlag==true)
								{
									blnFlag=Page("pgeEditCertificateOfConformance").Element("chkProcessingRecords").Click(20);
									if (blnFlag==true)
									{
										blnFlag=Page("pgeEditCertificateOfConformance").Element("chkDissectionDehydration").Click(20);
										if (blnFlag==true)
										{
											blnFlag=Page("pgeEditCertificateOfConformance").Element("chkTraceability").Click(20);
											if (blnFlag==true)
											{
												blnFlag=Page("pgeEditCertificateOfConformance").Element("chkReleaseApproval").Click(20);
												if (blnFlag==true)
												{
													blnFlag=Page("pgeEditCertificateOfConformance").Element("btnCommit").Exist();
													if (!blnFlag) 
													Err.Description = "All check boxes are not selected.";
												}
												else 
													Err.Description = "Unable to check Release Approval checkbox.";
											}
											else 
												Err.Description = "Unable to check Traceability checkbox.";
										}
										else
											Err.Description = "Unable to check Dissection and Dehydration checkbox.";
									}
									else
										Err.Description = "Unable to check Processing Records checkbox.";
								}
								else 
									Err.Description = "Unable to check Packaging Process checkbox.";	
							}
							else 
								Err.Description = "Unable to check Sterilization Process checkbox.";
						}
						else 
							Err.Description = "Unable to check second Inspection Process checkbox.";
					}
					else 
						Err.Description = "Unable to check Inspection Process checkbox.";
				}
				else 
					Err.Description = "Unable to check Manufacturing Process checkbox.";
			}
			else
				Err.Description = "Unable to check Donor Eligibility checkbox.";		

			return blnFlag;	
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: commitCertificate
        //''@Objective: This function commits the certificate for donor.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strUser - The User Initials signature that needs to be entered.
		//''@	strPassword - The User password that needs to be entered.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= commitCertificate()       
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[08-21-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean commitCertificate() 
		{
			boolean blnFlag = false;
			
			waitForSync(10);
			blnFlag=canvasSignature(driver);
			if (blnFlag==true)
			{
				waitForSync(10);
				blnFlag=Page("pgeEditCertificateOfConformance").Element("btnCommit").Click(20);
				if (blnFlag==true)
				{
					blnFlag=!(Page("pgeEditCertificateOfConformance").Element("btnCommit").Exist());
					if (!blnFlag) 
						Err.Description = " 'Commit' button not disappeared.";
				}
				else
					Err.Description = "Unable to click 'Commit' button.";				
			}
			else
				Err.Description = "Unable to sign in canvas signature.";
				
			return blnFlag;
		}
	
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickCertificateofConformanceLink
        //''@Objective: This function clicks on Certificate of Conformance link on Certificate of Conformance page and internally call   
		//''@ the function verifyPDF to verify pdf content of the new tab.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strExpText - The expected text to validate in pdf.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickCertificateofConformanceLink("Certificate of ConformanceLink")       
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[08-25-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickCertificateofConformanceLink(String strDonorKey, String strExpText)
		{
			boolean blnFlag = false;
			strExpText=strExpText+","+strDonorKey;
			
			blnFlag = Page("pgeSterilization").Element("lnkCertificateofConformance").Click(20);
			waitForSync(10);
			
			if (blnFlag==true)
			{
				blnFlag=verifyPDF(strExpText);
				if(!blnFlag)
					Err.Description=Err.Description;
			}
			else
			{
				Err.Description="Unable to click on 'Certificate of Conformance' link";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyPDF
        //''@Objective: This function switches between the two tabs and internally call the function getPDFContent() to copy 
		//''@ the pdf content from new tab.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strExpText - The expected text to validate in pdf.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyPDF("Certificate of ConformanceLink")      
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[08-25-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################	
		public boolean verifyPDF(String strExpText) 
		{
			boolean blnFlag = false;
			try 
			{
				//Store the current window handle
				String winHandleBefore = driver.getWindowHandle();
				//get window handlers as list
				ArrayList<String> browserTabs = new ArrayList<String> (driver.getWindowHandles());
				//switch to new tab
				driver.switchTo().window(browserTabs.get(1));
				
				//copy pdf content from new tab
				blnFlag=getPDFContent(strExpText);
				if(!blnFlag)
					Err.Description=Err.Description;			
			}
			catch(Exception e)
			{
				blnFlag=false;
				Err.Description="Error occured while verifying the PDF.";
			}
									
			return blnFlag;
		}

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: closePDF
        //''@Objective: This function closes the newly opened PDF tab, and switches the driver to the page from where the PDF was navigated to.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strExpHeader - The expected header of the page from where the PDF was opened.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= closePDF("Donor Tissues")      
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[09-15-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################	
		public boolean closePDF(String strExpHeader)
		{
			boolean blnFlag = false;
			
			//get window handlers as list
			ArrayList<String> browserTabs = new ArrayList<String> (driver.getWindowHandles());
			//Close tab and get back
			driver.close();
			driver.switchTo().window(browserTabs.get(0));
			
			//validate the page title to verify if switched back to correct page
			blnFlag = getPageTitle(strExpHeader);
			if (!blnFlag)
				Err.Description="The PDF has not closed correctly. The Expected page title on the navigated page is '"+strExpHeader+"'.";
			return blnFlag;
			
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: getPDFContent
        //''@Objective: This function gets the pdf content and internally call the function verifyPDFContent() to validate the pdf 
		//''@ content from new tab.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strExpText - The expected text to validate in pdf.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= getPDFContent("Certificate of ConformanceLink")      
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[08-25-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################	
		public boolean getPDFContent(String strExpText) 
		{
			boolean blnFlag = false;
			String result = null;
			// In case of Chrome or IE browsers, use clipboard to validate data in PDF	
				if(!(Script.dicConfigValues.get("strBrowserType").equalsIgnoreCase("FIREFOX")))
				{
					try
					{
/*						driver.findElement(By.xpath("//embed[@type='application/pdf']")).click();
						driver.findElement(By.xpath("//embed[@type='application/x-google-chrome-pdf']")).click();
						
						  Actions builder=new Actions(driver);
						  builder.keyDown(Keys.CONTROL)
								 .sendKeys("a")
								 .keyUp(Keys.CONTROL)
								 .keyDown(Keys.CONTROL)
								 .sendKeys("c")
								 .keyUp(Keys.CONTROL)
								 .release()
								 .perform();				 
*/								 
						driver.findElement(By.tagName("embed")).click();
						
						Robot robot = new Robot();
						  
						robot.keyPress(KeyEvent.VK_CONTROL);
						robot.keyPress(KeyEvent.VK_A);
						// CTRL+A is now pressed (receiving application should see a "key down" event.)
						robot.keyRelease(KeyEvent.VK_A);
						robot.keyRelease(KeyEvent.VK_CONTROL);
						// CTRL+A is now released (receiving application should now see a "key up" event - as well as a "key pressed" event).
						  
						robot.keyPress(KeyEvent.VK_CONTROL);
						robot.keyPress(KeyEvent.VK_C);
						// CTRL+A is now pressed (receiving application should see a "key down" event.)
						robot.keyRelease(KeyEvent.VK_C);
						robot.keyRelease(KeyEvent.VK_CONTROL);
						// CTRL+A is now released (receiving application should now see a "key up" event - as well as a "key pressed" event).
						
						
						//sleep just a little to let the clipboard contents get updated
						waitForSync(10);

						Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
						
						if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor))
						{
							result = (String) t.getTransferData(DataFlavor.stringFlavor);
							blnFlag = true;
						}
						else
						{
							Err.Description = "Unable to copy into clipboard.";									
						}															
					} 
					catch (Exception e) 
					{
						if (e instanceof AWTException) 
						{
							e.printStackTrace();
							Err.Description = "Unable to copy into clipboard.";
						}
						else if (e instanceof NoSuchElementException)
						{
							e.printStackTrace();	
							Err.Description = "The PDF has not opened successfully.";							
						}
						else
						{
							Err.Description = e.getMessage();
						}							
					}			
				}
				// In case of Firefox, directly fetch the data in the PDF and validate , no clipboard required
				else
				{
					try
					{
						driver.findElement(By.xpath("//div[@id='mainContainer']")).click();
						result = driver.findElement(By.xpath("//div[@id='mainContainer']")).getText();
						blnFlag = true;
					}					
					catch(NoSuchElementException e)
					{
						Err.Description = "The PDF has not opened successfully.";		
					}					
				}
				// Validate only if the PDF is opened successfully
				if(blnFlag)
				{					
					if(!(result.isEmpty()))  
					{
						blnFlag=verifyPDFContent(result, strExpText);					
						if(!blnFlag)
						{						
							Err.Description = "The PDF content validation failed. Expected Text in the PDF is '"+strExpText+"'.";
						}					
					}
					else
					{
						Err.Description = "There is no data in the opened PDF.";
						blnFlag = false;
					}					
				}			
			return blnFlag;	
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyPDFContent
        //''@Objective: This function validated the clipboard text with the expected text.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strExpText - The expected text to validate in pdf.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyPDFContent("clipboard data", "Certificate of ConformanceLink")      
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[08-25-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################	
		public boolean verifyPDFContent(String clipboard, String strExpText) 
		{
			boolean blnFlag = false;
			String strclipboard;

			if(strExpText != null && strExpText.length() > 0)
			{
				String [] expTextArr = strExpText.split(",");
				strclipboard = clipboard.replace("\n", "");	

				for (int i = 0; i < expTextArr.length; i++) 
				{
					if (strclipboard.contains(expTextArr[i]))
					{
						blnFlag = true;		
						Script.dicTestData.put("ExpectedText", strExpText);			
					}
					else 
					{
						blnFlag = false;
						Err.Description = "The PDF content validation failed. Expected Text in the PDF is '"+strExpText+"'.";
						break;
					}
				}
			}
			return blnFlag;
		}	
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: sendValuetoCommonSheet
        //''@Objective: This function pushes the data which is necessary for further scripts in the Common Data Sheet. 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strColumnNameinExcel - The exact column value in Common Sheet against which the value needs to be updated for future use.				
		//''@	strValuetoUpdate - The Value which needs to be updated in Common Sheet for future use.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= sendValuetoCommonSheet("strRecoveryId_DonorProcessing","WA14G099") 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-26-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean sendValuetoCommonSheet(String strColumnNameinExcel, String strValuetoUpdate)
		{
			boolean blnFlag = false;
				try
				{					
					ExcelUtil.WriteToExcel(strColumnNameinExcel,strValuetoUpdate);	
					//Update the dictionary Common value for running in a batch execution.
					Script.dicCommonValue.put(strColumnNameinExcel, strValuetoUpdate);
					blnFlag=true;				
				}
				catch (Exception e)
				{
					Err.Description = "There was an error while writing data("+strValuetoUpdate+") for this script into the Common Sheet against ("+strColumnNameinExcel+").";				
				}
			return blnFlag;	
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickPrintNalgeneLabelsLink
        //''@Objective: This function clicks on 'Print Nalgene Labels' link and internally call   
		//''@ the function verifyPDF to verify pdf content of the new tab.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strDonorKey - The Donor Key which is to be validated in the pdf.
		//''@	strExpText - The expected text to validate in pdf.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickPrintNalgeneLabelsLink("20143035", "AMNION,CHORION")       
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-27-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickPrintNalgeneLabelsLink(String strDonorKey, String strExpText)
		{
			boolean blnFlag = false;
			//strExpText=strExpText+","+strDonorKey;
			strExpText=strDonorKey+","+strExpText;
			
			blnFlag = Page("pge_ProcessRecoveredTissues").Element("lnkPrintNalgeneLabels").Click(20);
			waitForSync(10);
			
			if (blnFlag==true)
			{
				blnFlag=verifyPDF(strExpText);
				if(!blnFlag)
					Err.Description=Err.Description;
			}
			else
			{
				Err.Description="Unable to click on 'Print Nalgene Labels' link";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickPrintLogRefrigerator
        //''@Objective: This function clicks on 'Print Log' link and internally calls   
		//''@ the function verifyPDF to verify pdf content of the new tab.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strRecoveryID - The Recovery ID which needs to be validated in the pdf.
		//''@	strExpText - The expected text to validate in pdf.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickPrintLogRefrigerator("NA14G154","Placenta,Quarantine,Released,RFI-015Q,RFI-015R,Ashish Khare")       
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-27-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickPrintLogRefrigerator(String strRecoveryID, String strExpText)
		{
			boolean blnFlag = false;
			//strExpText=strExpText+","+strRecoveryID;
			strExpText=strRecoveryID+","+strExpText;
			
			blnFlag = Page("pgeDonorProcessing").Element("btnPrintLog").Click(20);
			waitForSync(10);
			
			if (blnFlag==true)
			{
				blnFlag=verifyPDF(strExpText);
				if(!blnFlag)
					Err.Description=Err.Description;
			}
			else
			{
				Err.Description="Unable to click on 'Print Log' link";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickPrintAssessment
        //''@Objective: This function clicks on 'Print Assessment Form' link and internally calls   
		//''@ the function verifyPDF to verify pdf content of the new tab.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strDonorKey - The Donor Key which is to be validated in the pdf.
		//''@	strExpText - The expected text to validate in pdf.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickPrintAssessment("20143133","Ashish Khare, Jonathan Laura")      
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-27-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickPrintAssessment(String strDonorKey, String strExpText)
		{
			boolean blnFlag = false;
			//strExpText=strExpText+","+strDonorKey;
			strExpText=strDonorKey+","+strExpText;
			
			blnFlag = Page("pgeRawTissueAssessment").Element("lnkPrintAssessmentForm").Click(20);
			waitForSync(10);
			
			if (blnFlag==true)
			{
				blnFlag=verifyPDF(strExpText);
				if(!blnFlag)
					Err.Description=Err.Description;
			}
			else
			{
				Err.Description="Unable to click on 'Print Assessment Form' link";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickPrintForm
        //''@Objective: This function clicks on 'Print Form' link on 'Product Dehydration' page and internally calls   
		//''@ the function verifyPDF to verify pdf content of the new tab.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strDonorKey - The Donor Key which is to be validated in the pdf.
		//''@	strExpText - The expected text to validate in pdf.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickPrintForm("20143133","Product Dehydration Form,Tissue Dryer,TD-005,SNG-001,1234,HS-004,GS SHEET,Ashish Khare")       
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-27-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickPrintForm(String strDonorKey, String strExpText)
		{
			boolean blnFlag = false;
			strExpText=strExpText+","+strDonorKey;
			
			blnFlag = Page("pgeProductDehydration").Element("lnkPrintForm").Click(20);
			waitForSync(10);
			
			if (blnFlag==true)
			{
				blnFlag=verifyPDF(strExpText);
				if(!blnFlag)
					Err.Description=Err.Description;
			}
			else
			{
				Err.Description="Unable to click on 'Print Form' link on Product Dehydration page.";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickInnerBarcode
        //''@Objective: This function clicks on 'Inner Barcode' link on 'Donor Tissues' page and internally calls   
		//''@ the function verifyPDF to verify pdf content of the new tab.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strDonorKey - The Donor Key which is to be validated in the pdf.
		//''@	strExpText - The expected text to validate in pdf.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickInnerBarcode("20143133","GS44")
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-27-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickInnerBarcode(String strDonorKey, String strExpText)
		{
			boolean blnFlag = false;
			strExpText=strExpText+","+strDonorKey;
			
			blnFlag = Page("pgeDonorProcessing").Element("lnkInnerBarcode").Click(20);
			waitForSync(10);
			
			if (blnFlag==true)
			{
				blnFlag=verifyPDF(strExpText);
				if(!blnFlag)
					Err.Description=Err.Description;
			}
			else
			{
				Err.Description="Unable to click on 'Inner Code' link on 'Donor Tissues' page.";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickVIForm
        //''@Objective: This function clicks on 'VI Form' link on 'Donor Tissues' page and internally calls   
		//''@ the function verifyPDF to verify pdf content of the new tab.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strDonorKey - The Donor Key which is to be validated in the pdf.
		//''@	strExpText - The expected text to validate in pdf.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickVIForm("20143133","Visual Inspection Form,GS44")
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-27-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickVIForm(String strDonorKey, String strExpText)
		{
			boolean blnFlag = false;
			strExpText=strExpText+","+strDonorKey;
			
			blnFlag = Page("pgeDonorProcessing").Element("lnkVIForm").Click(20);
			waitForSync(10);
			
			if (blnFlag==true)
			{
				blnFlag=verifyPDF(strExpText);
				if(!blnFlag)
					Err.Description=Err.Description;
			}
			else
			{
				Err.Description="Unable to click on 'VI Form' link on 'Donor Tissues' page.";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: completeOuterPackaging_ArchiveSample
        //''@Objective: This function completes the 'Outer Packaging' functionality for the Archive Sample Tissue, if added in 'Donor Tissues' page.   		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= completeOuterPackaging_ArchiveSample("akhare","India@123")
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-27-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean completeOuterPackaging_ArchiveSample(String strUserName, String strPassword)
		{
			boolean blnFlag = false;
			
			blnFlag = Page("pgeDonorProcessing").Element("//a[@id='centerform:tbldonorproducts:1:lnkshowdonortissue']").Exist();
			if(blnFlag)
			{			
			blnFlag = Page("pgeDonorProcessing").Element("//*[@id='centerform:tbldonorproducts:1:lnkshowdonortissue']").Click(20);
				if (blnFlag)
				{				
					blnFlag = Page("pgeAddTissue").Element("btnOuterPackaging").Click(20);
					if(blnFlag)
					{
						blnFlag = Page("pgeAddTissue").Element("txtUserName").Type(strUserName);
						if(blnFlag)
						{
							blnFlag = Page("pgeAddTissue").Element("txtPassword").Type(strPassword);
							if(blnFlag)
							{
								blnFlag = Page("pgeCommon").Element("btnCommit").Click(20);
								if(blnFlag)
								{
									blnFlag = Page("pgeCommon").Element("btnReturn").Click(20);
									if(blnFlag)
									{
										blnFlag = Page("pgeDonorProcessing").Element("//a[@id='centerform:tbldonorproducts:1:lnkprntvif']").Exist();
										if(!blnFlag)
											Err.Description="The 'VI Form' link is not enabled for the Archive Sample";
									}
									else
									Err.Description="The 'Return' button could not be clicked for the Archive Sample";
								}
								else
								Err.Description="The 'Commit' button could not be clicked for the Archive Sample";
							}
							else
							Err.Description="The 'Password' could not be typed in the text box for archive sample.";
						}
						else
						Err.Description="The 'Username' could not be typed in the text box for archive sample.";
					}
					else
					Err.Description="The 'Outer Packaging' button could not be clicked for archive sample.";
				}
				else
				Err.Description="The 'Archive Sample' link could not be clicked.";
			}			
			// If the Archive Sample is not there, just return 'True' and quit function
			else
			blnFlag = true;		
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: enterRecoveryId
        //''@Objective: This function enters the recovery id in process scan text area on Read Scans page. 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strRecoveryId - Recovery Id to be entered in process scan area.				
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= enterRecoveryId("WA14G099") 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish khare[08-27-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean enterRecoveryId(String strRecoveryId)
		{
			boolean blnFlag = false;
			strRecoveryId = strRecoveryId.substring(0, 5)+String.format("%03d",(Integer.parseInt(strRecoveryId.substring(5,8)) + 1));
			blnFlag=Page("pgeRecovery").Element("txtScanList").Type(strRecoveryId);
			if(blnFlag==true)
			{
				blnFlag=sendValuetoCommonSheet("strRecoveryId",strRecoveryId);
				if (!blnFlag) 
					Err.Description = Err.Description;
			}
			else
				Err.Description="Unable to enter 'Recovery Id' in edit box.";
			return blnFlag;	
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: scanTissueandVerify
        //''@Objective: This function inputs a tissue into the Scan text box, hits 'Enter', and verifies if the object passed is 
		//''@ present on the page or not.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strDonorKey - The Donor Key which is to be validated in the pdf.
		//''@	strExpText - The expected text to validate in pdf.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= scanTissueandVerify("pgePackagingCollection","txtScanTissue_Assembly","objTissue_Assembly","objLabelPrinting")
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[09-02-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean scanTissueandVerify(String Page, String ElementScan,String ElementTissue, String ElementtoVerify)
		{
			boolean blnFlag = false;
			String strTissue;
			int intPosition;
					
			strTissue = Page(Page).Element(ElementTissue).GetText();
			if (strTissue.contains("Package Item:"))
			{
				intPosition = strTissue.indexOf(':');
				strTissue = strTissue.substring(intPosition + 2);
			}
			
			blnFlag = Page(Page).Element(ElementScan).Type(strTissue);
			if (blnFlag)
			{
				//Alternative to hitting 'Enter' key, CLick on the tissue in the grid.
				blnFlag = Page(Page).Element(ElementTissue).Click(20);
				waitForPageRefresh();
				if(blnFlag)
				{
					Page(Page).Element(ElementtoVerify).Exist();
					if(!blnFlag)
						Err.Description="Unable to verify presence of the element '"+ElementtoVerify+"'.";
				}
				else
					Err.Description="Unable to click on of the element '"+ElementTissue+"'. This click is to simulate same behavior of Enter key press.";
			}
			else			
				Err.Description="Unable to type in the value '"+strTissue+"' in the element '"+ElementScan+"'.";
						
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: electronicSignaturePackaging
        //''@Objective: This Function signs the signs the canvas on Packaging page and fill the canvas.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strUserName - Initials of the user to fill in the Signature field in Packaging page and fill the canvas.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= electronicSignaturePackaging("akhare")       
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[09-03-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean electronicSignaturePackaging(String strUserName)
		{
			boolean blnFlag = false;
			
			blnFlag = canvasSignature(driver);
			if(blnFlag)
			{			
				blnFlag = Page("pgePackagingCollection").Element("txtSignature").Type(strUserName);
				if(!blnFlag)
					Err.Description = "Unable to type user ID '"+strUserName+"'.";							
			}
			else
			{
				blnFlag = false;
				Err.Description = "Unable to enter signature in canvas.";	
			}		
				
			return blnFlag;
		}
		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: selectPackageInspectionCheckboxes
        //''@Objective: This function checks all the checkboxes on 'Packaging Inspection' page. 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= selectPackageInspectionCheckboxes()        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[09-03-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean selectPackageInspectionCheckboxes()
		{
			boolean blnFlag = false;
			
			blnFlag=Page("pgePackagingCollection").Element("chkProductCarton").Click(20);
			if (blnFlag)
			{
				blnFlag=Page("pgePackagingCollection").Element("chkTURCard").Click(20);
				if (blnFlag)
				{
					blnFlag=Page("pgePackagingCollection").Element("chkBackLabel").Click(20);
					if (blnFlag)
					{
						blnFlag=Page("pgePackagingCollection").Element("chkInstructionsforUse").Click(20);
						if (blnFlag)
						{
							blnFlag=Page("pgePackagingCollection").Element("chkStickers").Click(20);
							if (blnFlag)
							{
								blnFlag=Page("pgePackagingCollection").Element("chkCMVCard").Click(20);
								if (!blnFlag) 
									Err.Description = "Unable to check CMV Card checkbox.";								
							}
							else 
								Err.Description = "Unable to check Stickers checkbox.";
						}
						else 
							Err.Description = "Unable to check Instrcutions for Use checkbox.";
					}
					else 
						Err.Description = "Unable to check Back Label checkbox.";
				}
				else 
					Err.Description = "Unable to check TUR Card checkbox.";
			}
			else
				Err.Description = "Unable to check Product Carton checkbox.";		

		return blnFlag;	
		}

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickPrintPickListLink
        //''@Objective: This function clicks on Print Pick List link on Sterilization page and internally call the function verifyPDF    
		//''@ to verify pdf content of the new tab.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strSterilizationId - Sterilization Id to validate in pdf.
		//''@	strExpText - The expected text to validate in pdf.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickPrintPickListLink("410","Print Pick List")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[09-02-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickPrintPickListLink(String strSterilizationId, String strExpText)
		{
			boolean blnFlag = false;
			strExpText=strExpText+","+strSterilizationId;
			
			blnFlag = Page("pgeSterilization").Element("lnkPrintPickList").Click(20);
			waitForSync(10);
			
			if (blnFlag==true)
			{
				blnFlag=verifyPDF(strExpText);
				if(!blnFlag)
					Err.Description=Err.Description;
			}
			else
			{
				Err.Description="Unable to click on 'Print Pick List' link";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: saveAndAllocate
        //''@Objective: This function clicks on Save and Allocate button on View Open Order Line page and internally call   
		//''@ the function clickAndVerify to verify button clicked sucessfully or not.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strShipmentQty - Quantity to be shipped.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= saveAndAllocate("2")       
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[09-03-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean saveAndAllocate(String strShipmentQty)
		{
			boolean blnFlag = false;
			blnFlag=Page("pgeSalesShipOrders").Element("txtShipmentQty").Type(strShipmentQty);
			if (blnFlag==true)
			{
				// blnFlag=Page("pgeSalesShipOrders").Element("btnSaveAndAllocate").Click(20);
				blnFlag=clickAndVerify("pgeSalesShipOrders","btnSaveAndAllocate", "View Open Order Line");
				if (!blnFlag)
					Err.Description=Err.Description;
			}
			else
				Err.Description="Unable to fill quantity in 'Shipment Quantity' text box.";
			return blnFlag;
		}

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: processScanForShipping
        //''@Objective:  This function clicks on Process Scan and Ship button on Edit Shipment page and internally call   
		//''@ the function clickAndVerify to verify button clicked sucessfully or not.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= processScanForShipping()       
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[09-03-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean processScanForShipping()
		{
			boolean blnFlag = false;
			blnFlag=scanTissue("pgeSalesShipOrders","txtShipmentQty_EditShipment");
			if (blnFlag)
			{
				blnFlag=Page("pgeSalesShipOrders").Element("btnProcessScans").Click(20);
				if (blnFlag)
				{
					blnFlag=Page("pgeSalesShipOrders").Element("btnShip").Exist();
					if (blnFlag)
					{
						blnFlag=clickAndVerify("pgeSalesShipOrders","btnShip", "View Open Order");
						if (!blnFlag)
							Err.Description=Err.Description;
					}
					else
						Err.Description="'Ship' button not displayed on 'Edit Shipment' page.";
				}
				else
					Err.Description="Unable to click 'Process Scans' button on 'Edit Shipment' page.";
			}
			else
				Err.Description=Err.Description;
			return blnFlag;
		}

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickGenerateLabel
        //''@Objective: This Function validated the Generate Label button PDF
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strTissueCode - The Recovery ID of the tissue.
		//''@	strDonorKey - The expected title of the navigated page.
		//''@	strAvailableQuantity - The expected title of the navigated page.
		//''@	strDonorProductId - The expected title of the navigated page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickGenerateLabel("TN44", "20143145", "12", "33491")       
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[19-Sep-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickGenerateLabel(String strTissueCode, String strDonorKey, String strAvailableQuantity, String strDonorProductId) 
		{
			boolean blnFlag = false;
			waitForSync(2);
			String strExpText=strTissueCode+","+strDonorKey+","+strAvailableQuantity+","+strDonorProductId;
			blnFlag = Page("pgeSterilizationReturns").Element("btnGenerateLabel").Click(20);			
			waitForSync(10);
			if (blnFlag==true)
			{
				blnFlag=verifyPDF(strExpText);
				if(!blnFlag)
					Err.Description=Err.Description;
			}
			else
			{
				Err.Description="Unable to click on 'Generate Label' button.";
			}
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: enterCurrentDate
        //''@Objective: This Function sets a dictionary object with the current date returned in the format specified.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	PageName - The Page to which the Element is mapped.
		//''@	Element - The Element in which the formatted current date is to be entered.
		//''@	strFormat - The format in which the current date needs to be entered.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= enterCurrentDate("pgeReturnedGoods","txtDate","MM/DD/YYYY")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[09-24-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean enterCurrentDate(String PageName, String Element, String strFormat) 
		{
			boolean blnFlag = false;
			String strDate = null;
			
			try
			{					
				Date date = new Date();
				strDate= new SimpleDateFormat(strFormat).format(date);		
				
				blnFlag = Page(PageName).Element(Element).Type(strDate);
				if(!blnFlag)
					Err.Description = "Unable to enter the current date in the '"+Element+"'.";
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Unable to format the current date.";
			}	
			
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickTab
        //''@Objective: This function clicks on specified tab and validated tab is clicked succesffuly or not.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	pgeName - Page to which the Element is mapped..
		//''@	eleName - Element on which action to performed.
		//''@	strTabName - Tab name to be clicked.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickTab("pgeSterilization","txtAgentName", strAgentName)        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[09-24-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickTab(String pgeName, String eleName, String strTabName) 
		{
			boolean blnFlag = false;
			String tempXpath,finalXpath;
			blnFlag=Page(pgeName).Element(eleName).Click(20);
			waitForPageRefresh();
			if (blnFlag==true)
			{
				tempXpath=Page(pgeName).Element(eleName).GetXpath();
				finalXpath=tempXpath+"/..";
				String state=driver.findElement(By.xpath(finalXpath)).getAttribute("aria-expanded");
				if(state.equalsIgnoreCase("true")) 
				{
					blnFlag=true;
				}
				else
				{
					blnFlag=false;
					Err.Description="'"+strTabName+"' tab not clicked.";
				}
			}
			else 
				Err.Description="Unable to click '"+strTabName+"' tab.";
			
			return blnFlag;
		}	

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: enterAgentName
        //''@Objective: This function enteres agent name 'Agent' text box and validated page refresh.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strAgentName - Agent name to be provided in 'Agent' text box.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= enterAgentName("JOHN SCOTT")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[09-24-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean enterAgentName(String strAgentName) 
		{
			boolean blnFlag = false;
			String tempSource = driver.getPageSource();
			blnFlag=Page("pgeFieldTracking").Element("txtAgentName").Type(strAgentName);
			if (blnFlag==true)
			{
				waitForSync(3);
				// waitForPageRefresh();
				//Page("pgeFieldTracking").Element("lstAgentName").Click(20);
				blnFlag=Page("pgeFieldTracking").Element("//li[@data-item-value='" + strAgentName + "']").Click(20);
				if (blnFlag==true)
				{
					waitForPageRefresh();
					if (!tempSource.equals(driver.getPageSource())) 
						{
							blnFlag = true;
						}
						else 
						{
							blnFlag = false;	
							Err.Description="Page not refreshed.";		
						}
				}
				else
					Err.Description="Unable to select agent name from list.";	
			}
			else
				Err.Description="Unable to enter agent name in 'Agent' input area.";	

			return blnFlag;
		}		

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillTissueUtilizationGrid
        //''@Objective: This function fills Tissue Utilization grid entries on Field Tracking page..
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strPatientId - Patient Id to be provided in Tissue Utilization grid.
		//''@	strFirstName - Patient's first name to be provided in Tissue Utilization grid.
		//''@	strLastName - Patient's last name to be provided in Tissue Utilization grid.
		//''@	strBirthDate - Patient's birth date to be provided in Tissue Utilization grid.
		//''@	strGender - Patient's gender to be selected in Tissue Utilization grid.
		//''@	strSurgeonFirst - Surgeon's first name to be provided in Tissue Utilization grid.
		//''@	strSurgeonLast - Surgeon's last name to be provided in Tissue Utilization grid.
		//''@	strProcedureType - Procedure type to be selected in Tissue Utilization grid.
		//''@	strImplantDate - Implant date to be provided in Tissue Utilization grid.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fillTissueUtilizationGrid("12345","Jane","Doe","09/11/1984","Female","Simon","Tom","707.13 - Ulcer of 
		//''@Ankle","9/25/2014")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[09-25-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################				
		public boolean fillTissueUtilizationGrid(String strPatientId,String strFirstName, String strLastName, String strBirthDate, String strGender, String strSurgeonFirst, String strSurgeonLast, String strProcedureType, String strImplantDate)
		{
			boolean blnFlag = false;
			
			blnFlag=Page("pgeFieldTracking").Element("txtPatientId").Type(strPatientId);
			
			if (blnFlag == true) 
			{
				blnFlag=Page("pgeFieldTracking").Element("txtFirstName").Type(strFirstName);

				if (blnFlag == true) 
				{
					blnFlag = Page("pgeFieldTracking").Element("txtLastName").Type(strLastName);

					if (blnFlag == true) 
					{
						blnFlag = Page("pgeFieldTracking").Element("txtBirthDate").Type(strBirthDate);
						driver.findElement(By.xpath("//form[@id='centerform']")).click();
						waitForSync(2);

						if (blnFlag == true) 
						{
							if(strGender.equals("Male"))
							{							
								blnFlag = Page("pgeFieldTracking").Element("rdoGenderMale").Click(20);
								if(!blnFlag)
								Err.Description="Unable to click radio button.";
							}
							else if(strGender.equals("Female"))
							{							
								blnFlag = Page("pgeFieldTracking").Element("rdoGenderFemale").Click(20);
								if(!blnFlag)
								Err.Description="Unable to click radio button.";
							}
							else 
							{
								blnFlag=false;
								Err.Description ="Unable to click radio button.Gender not specified correctly in test data sheet.";
							}

							if (blnFlag == true) 
							{
								blnFlag = Page("pgeFieldTracking").Element("txtSurgeonFirst").Type(strSurgeonFirst);

								if (blnFlag == true) 
								{
									blnFlag = Page("pgeFieldTracking").Element("txtSurgeonLast").Type(strSurgeonLast);

									if (blnFlag == true) 
									{
										blnFlag = Page("pgeFieldTracking").Element("lstProcedureType").Select(strProcedureType);
										
										if (blnFlag == true)
										{
											blnFlag = enterCurrentDate("pgeFieldTracking","txtImplantDate",strImplantDate);	
											// blnFlag = Page("pgeFieldTracking").Element("txtImplantDate").Type(strImplantDate);	
											driver.findElement(By.xpath("//form[@id='centerform']")).click();
											waitForSync(2);
											
											if (blnFlag == true)
											{
												blnFlag=Page("pgeFieldTracking").Element("btnSave").Click(20);
												waitForSync(3);
												
												if (blnFlag == true)
												{
													if(!(Page("pgeFieldTracking").Element("objError").Exist()))
													{
														blnFlag=true;
													}
													else
													{
														blnFlag=false;
														Err.Description = "Actual error:"+Page("pgeNewRecoveredTissue").Element("objAddTissueError").GetText();
													}	
												}
												else
													Err.Description = "Unable to click 'Save' button.";
											}
											else
												Err.Description = "Unable to enter Implant Date.";
										}
										else					
											Err.Description = "Unable to select 'Procedure Type' drop down.";
									}
									else 
										Err.Description = "Unable to enter Surgeon last name.";
								} 
								else
									Err.Description = "Unable to enter Surgeon first name.";
							} 
							else
								Err.Description = Err.Description;
						} 
						else
							Err.Description = "Unable to enter Birth Date.";
					} 
					else
						Err.Description = "Unable to enter Patient last name.";				
				}
				else
					Err.Description = "Unable to enter Patient first name.";			
			}
			else
				Err.Description = "Unable to eneter Patient Id.";	
			
		return blnFlag;
	}	
	
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyRecordNotExist
        //''@Objective: This function verifies that record should not be displayed in grid.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - Page on which the Element whose text needs to be got exists.
		//''@	Element - The Element whose text needs to be retrieved.
		//''@	strTissueId - Tissue id to be validate in grid.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyRecordNotExist("pgeFieldTracking","objEmptyTable","GS23-20141613-008")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[09-26-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean verifyRecordNotExist(String Page, String Element, String strTissueId) 
		{
			boolean blnFlag = false;
			String strRecord = Page(Page).Element(Element).GetText();
			if(!(strRecord.trim().contains(strTissueId)))  
			{
				blnFlag = true;
			} 
			else 
			{
				blnFlag = false;
				Err.Description = "Tissue '"+strTissueId+"' still displayed in the grid.";
			}

			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyPrefilledValues
        //''@Objective: This function verifies pre-filled values on the form.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strPatientId - Patient id to be validate in form.
		//''@	strFirstName - Patient's first name to be validate in form.
		//''@	strLastName - Patient's last name to be validate in form.
		//''@	strBirthDate - Patient's DOB to be validate in form.
		//''@	txtSurgeonFirst - Surgeon's first name to be validate in form.
		//''@	txtSurgeonLast - Surgeon's last name to be validate in form.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyPrefilledValues("1234","Jane","Doe","10/04/1965","Hitendra","Hansalia")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[10-06-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean verifyPrefilledValues(String strPatientId, String strFirstName, String strLastName, String strBirthDate, String txtSurgeonFirst, String txtSurgeonLast) 
		{
			boolean blnFlag = false;
			String strInputValue="";
			strInputValue = Page("pgeFieldTracking").Element("//input[@id='centerform:patientid']").GetValue();
			blnFlag=verifyInputValue(strInputValue, strPatientId, "Patient Id");
			if (blnFlag == true)
			{
				strInputValue = Page("pgeFieldTracking").Element("//*[@id='centerform:patientfirst']").GetValue();
				blnFlag=verifyInputValue(strInputValue, strFirstName, "Patient's First Name");
				if (blnFlag == true)
				{
					strInputValue = Page("pgeFieldTracking").Element("//*[@id='centerform:patientlast']").GetValue();
					blnFlag=verifyInputValue(strInputValue, strLastName, "Patient's Last Name");
					if (blnFlag == true)
					{
						strInputValue = Page("pgeFieldTracking").Element("//*[@id='centerform:patientdob_input']").GetValue();
						blnFlag=verifyInputValue(strInputValue, strBirthDate, "Patient DOB");
						if (blnFlag == true)
						{
							strInputValue = Page("pgeFieldTracking").Element("//*[@id='centerform:surgeonfirst']").GetValue();
							blnFlag=verifyInputValue(strInputValue, txtSurgeonFirst, "Surgeon's First Name");
							if (blnFlag == true)
							{
								strInputValue = Page("pgeFieldTracking").Element("//*[@id='centerform:surgeonlast']").GetValue();
								blnFlag=verifyInputValue(strInputValue, txtSurgeonLast, "Surgeon's Last Name");
								if (!blnFlag)
									Err.Description=Err.Description;
							}
							else
								Err.Description=Err.Description;	
						}
						else
							Err.Description=Err.Description;
					}
					else
						Err.Description=Err.Description;
				}
				else
					Err.Description=Err.Description;				
			}
			else
				Err.Description=Err.Description;
				
			return blnFlag;
		}		

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyInputValue
        //''@Objective: This function verifies pre-filled input values on the form.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strInputValue - Form value to be validated.
		//''@	strValue - Input value to be validated.
		//''@	strField - Field value to be validated.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyInputValue("Jane","Jane","Patient's First Name")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[10-06-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################	
		public boolean verifyInputValue(String strInputValue, String strValue, String strField) 
		{
			boolean blnFlag = false;
			if(strInputValue.equalsIgnoreCase(strValue))
			{
				blnFlag = true;
			} 
			else 
			{
				blnFlag = false;
				Err.Description = "Value displayed in field '"+strField+"' is: '"+strInputValue+"' and expected value is: '"+strValue+"'.";
			}

			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillCustomerName
        //''@Objective: This function verifies pre-filled input values on the form.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strCustomerName - Customer name to be filled on form.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fillCustomerName("VAMC Ship To")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[10-28-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean fillCustomerName(String strCustomerName) 
		{
			boolean blnFlag = false;
			String tempSource = driver.getPageSource();
			blnFlag=Page("pgeConsignmentPurchaseOrder").Element("txtCustomer").Type(strCustomerName);
			if (blnFlag==true)
			{
				waitForSync(3);
				// waitForPageRefresh();
				// Page("pgeConsignmentPurchaseOrder").Element("lstCustomer").Click(20);
				blnFlag=Page("pgeConsignmentPurchaseOrder").Element("//li[contains(@data-item-value,'" + strCustomerName + "')]").Click(20);
				if (blnFlag==true)
				{
					waitForPageRefresh();
					if (!tempSource.equals(driver.getPageSource())) 
						{
							blnFlag = true;
						}
						else 
						{
							blnFlag = false;	
							Err.Description="Page not refreshed.";		
						}
				}
				else
					Err.Description="Unable to select customer name from list.";
			}
			else
				Err.Description="Unable to enter customer name in 'Customer' text box.";	
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyFieldnotEmpty
        //''@Objective: This Function verifies if the field is not empty in the page.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	PageName - The Page name which maps to the Element mentioned.
		//''@	Element - The Element which needs to be verified if it is non-empty.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyFieldnotEmpty("pgeTissueUtilization","objDistributor")         
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[10-14-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean verifyFieldnotEmpty(String PageName, String Element)  
		{
			boolean blnFlag = false;
			try
			{
				String strTexttoVerify = Page(PageName).Element(Element).GetText();
				blnFlag = strTexttoVerify.isEmpty();
				if (blnFlag)
				{
					strTexttoVerify = Page(PageName).Element(Element).GetValue();
					blnFlag = strTexttoVerify.isEmpty();
					if(blnFlag)
					{
						Err.Description = "The Element '"+Element+"' on page '"+PageName+"' is empty.";
						blnFlag = false;
					}
					else
						blnFlag = true;
				}
				else
					blnFlag = true;
			}
			catch (Exception e)
			{
				Err.Description = e.getMessage();
				blnFlag = false;
			}
						
			return blnFlag;
		}			
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: selectAndMoveTissue
        //''@Objective: This function selects a tissue from 'Available Tissues' and move to 'Selected Tissues' list box on 
		//''@'Consignment Purchase Order' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= selectAndMoveTissue()        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[10-28-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean selectAndMoveTissue() 
		{
			boolean blnFlag = false;
			String strSourceTissueId="";
			String strTargetTissueId="";
// 			String xSourceTissue="//ul[contains(@class,'ui-picklist-source')]/li[@data-item-label='"+strTissueId+"']";
//			String xTargetTissue="//ul[contains(@class,'ui-picklist-target')]/li[@data-item-label='"+strTissueId+"']";
			String xSourceTissue="//ul[contains(@class,'ui-picklist-source')]/li[last()]";
			String xTargetTissue="//ul[contains(@class,'ui-picklist-target')]/li";
			strSourceTissueId = Page("pgeConsignmentPurchaseOrder").Element(xSourceTissue).GetText();
		
			if (strSourceTissueId != null && !strSourceTissueId.isEmpty())
			{

				blnFlag = Page("pgeConsignmentPurchaseOrder").Element(xSourceTissue).Click(20);
								
				if (blnFlag == true)
				{
					blnFlag = Page("pgeConsignmentPurchaseOrder").Element("btnAdd").Click(20);
					
					if (blnFlag == true)
					{
						strTargetTissueId = Page("pgeConsignmentPurchaseOrder").Element(xTargetTissue).GetText();
						blnFlag=verifyInputValue(strTargetTissueId, strSourceTissueId, "'Selected Tissues' grid");
						
						if (blnFlag)
							Script.dicTestData.put("TargetTissue", strTargetTissueId);
						else
							Err.Description=Err.Description;			
					}
					else
						Err.Description="Unable to click on right arrow button under 'Outstanding Tissues' grid.";
				}
				else
					Err.Description="Unable to select the tissue under 'Available Tissues' grid.";				
			}
			else
				Err.Description="Tissues not displayed under 'Available Tissues' grid hence unable to move to 'Selected Tissues' grid.";
				
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickCommitAndVerifyConfirmationMessage
        //''@Objective: This Function verifies that the expected text is present in the object or not.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strExpText - The expected text to be validated , if it is present in the Element.
		//''@	strPurchaseOrder - Purchase Order to be validated , if it is present in the Element.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickCommitAndVerifyConfirmationMessage("Purchase Order 12345 was submitted for processing.\nE-Mail to Consignment Billing 
		//''@Successfully Sent")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[10-29-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
 		public boolean clickCommitAndVerifyConfirmationMessage(String strExpText, String strPurchaseOrder)
		{
			boolean blnFlag = true;
			String strActualFirstLine="";
			String strActualSecondLine="";
			String [] expTextArr = strExpText.split("\n");
			String strExpFirstLine=expTextArr[0];
			String strExpSecondLine=expTextArr[1];
			strExpFirstLine=strExpFirstLine.replace("'strPurchaseOrder'", strPurchaseOrder);
			
			blnFlag = Page("pgeConsignmentPurchaseOrder").Element("btnCommit").Click(20);
			waitForSync(2);
			
			if (blnFlag == true)
			{
				blnFlag = verifyTextContains("pgeConsignmentPurchaseOrder", "objConfMessage", strExpFirstLine);
				if (blnFlag == true)
				{
					blnFlag = verifyTextContains("pgeConsignmentPurchaseOrder", "objEmailMessage", strExpSecondLine);					
					if (!blnFlag)
						Err.Description=Err.Description;	
				}
				else
					Err.Description=Err.Description;
			}
			else
				Err.Description="Unable to click 'Commit' button.";
			return blnFlag;
		}	
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickPrintScheduleLink
        //''@Objective: This function clicks on Certificate of Conformance link on Certificate of Conformance page and internally call   
		//''@ the function verifyPDF to verify pdf content of the new tab.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - Action to be performed on page.
		//''@	Element - Action to be performed on element.
		//''@	strExpText - The expected text to validate in pdf.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickPrintScheduleLink("Recovery Schedule")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[11-05-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickPrintScheduleLink(String Page, String Element, String strExpText)
		{
			boolean blnFlag = false;
			
			blnFlag = Page(Page).Element(Element).Click(20);
			waitForSync(10);
			
			if (blnFlag==true)
			{
				blnFlag=verifyPDF(strExpText);
				if(!blnFlag)
					Err.Description=Err.Description;
			}
			else
			{
				Err.Description="Unable to click on 'Print Current Schedule' link";
			}
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: calculateNewItem
        //''@Objective: This Function checks if the name of the item which needs to be added, is already added. If added, it calculates a new 
		//''@ 			value for the item.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strItemNameBaseString - The Base string that is used for the Item value by Test team.	
		//''@	strDBItemValue - The latest item value from the Db
		//''@	strCommonSheetValue - The entry in the Common Sheet against which the value needs to be pushed
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= calculateNewItem("Test Line", "strLineName", "strNewAssemblyLine")    
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[11-19-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean calculateNewItem(String strItemNameBaseString, String strDBItemValue, String strCommonSheetValue)  
		{
			boolean blnFlag = false;			
			String strNewItemName = null; 
			// Verify if an item that has been retrieved from the DB is empty.				
			if (strDBItemValue != null)
			{
				try
				{					
									
					String [] strtemparray = strDBItemValue.split(" ");
					//If the last character is a numeral, then get the numeral and add 1 to it
					if((strtemparray[(strtemparray.length)-1]).matches("-?\\d+(\\.\\d+)?"))
				   {
					   // Increment the number by one 
					   strtemparray[(strtemparray.length)-1] = String.valueOf(Integer.parseInt((strtemparray[(strtemparray.length)-1])) + 1);
					   strNewItemName = StringUtils.join(strtemparray, ' ').trim();
				   }
				   else
				   {
					   strNewItemName = StringUtils.join(strtemparray, ' ').trim() + " 1";
				   }									
					blnFlag = true;	
				}
			
				catch(Exception e)
				{				
					blnFlag = false;					
				}
			}
			else
			{
			// If empty, use the same Base String as strNewItemName
				strNewItemName = strItemNameBaseString;
				blnFlag = true;	
			}
			boolean blnFlag1 = sendValuetoCommonSheet(strCommonSheetValue,strNewItemName);
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: calculateNewItem
        //''@Objective: This Function checks if the name of the item which needs to be added, is already added. If added, it calculates a new 
		//''@ 			value for the item.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strItemNameBaseString - The Base string that is used for the Item value by Test team.	
		//''@	strDBItemValue - The latest item value from the Db		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= calculateNewItem("Test Line", "strLineName")    
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[11-19-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean calculateNewItem(String strItemNameBaseString, String strDBItemValue)  
		{
			boolean blnFlag = false;                                               
			String strNewItemName = null;
			// Verify if an item that has been retrieved from the DB is empty.                                                           
			if (strDBItemValue != null)
			{
				try
				{    
					String [] strtemparray = strDBItemValue.split(" ");
					//If the last character is a numeral, then get the numeral and add 1 to it
					if((strtemparray[(strtemparray.length)-1]).matches("-?\\d+(\\.\\d+)?"))
					{
					   // Increment the number by one
					   strtemparray[(strtemparray.length)-1] = String.valueOf(Integer.parseInt((strtemparray[(strtemparray.length)-1])) + 1);
					   strNewItemName = StringUtils.join(strtemparray, ' ').trim();
					}
					else
					{
					   strNewItemName = StringUtils.join(strtemparray, ' ').trim() + " 1";
					}                                                                                                                                         
					blnFlag = true;  
				}
				catch(Exception e)
				{                                                             
					blnFlag = false;                                                                 
				}
			}
			else
			{
				// If empty, use the same Base String as strNewItemName
				strNewItemName = strItemNameBaseString;
				blnFlag = true;  
			}
			Script.dicCommonValue.put("strCalculatedItem",strNewItemName);                 
											   
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillNewAssemblyLineForm
        //''@Objective: This function fills the 'New Assembly Line' form with the required details.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strAssemblyLine - The Line Name which needs to be added.
		//''@	strDeviceName_SmallBarCode - The Small Bar Code Device Name which needs to be selected.
		//''@	strVerifyDeviceOnline_SmallBarCode - Yes/No - Depending on which of the values need to be selected.
		//''@	strTimeout_SmallBarCode - Small Bar Code Device Timeout that needs to be entered.
		//''@	strIPAddress_SmallBarCode - The IP Address of the Small Bar Code device which needs to be entered.
		//''@	strDeviceName_ProdLabel - The Small Bar Code Device Name which needs to be selected.		
		//''@	strVerifyDeviceOnline_ProdLabel - Yes/No - Depending on which of the values need to be selected.
		//''@	strIPAddress_ProdLabel - The IP Address of the Product Label  device which needs to be entered.
		//''@	strTimeout_ProdLabel - Product Label Device Timeout that needs to be entered.
		//''@	strDeviceName_Custom - The Custom Device Name which needs to be selected.		
		//''@	strVerifyDeviceOnline_Custom - Yes/No - Depending on which of the values need to be selected.
		//''@	strIPAddress_Custom - The IP Address of the Custom device which needs to be entered.
		//''@	strTimeout_Custom - Custom Device Timeout that needs to be entered.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fillNewAssemblyLineForm 
		//''@("TestLine","PL1-SmallBarcode","No","","","PL1-ProductLabels","Yes","10.0.112.71","30000","PL1-CustomLabels","No","","")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[11-06-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean fillNewAssemblyLineForm(String strAssemblyLine,String strDeviceName_SmallBarCode,String strVerifyDeviceOnline_SmallBarCode,String strIPAddress_SmallBarCode,String strTimeout_SmallBarCode,String strDeviceName_ProdLabel,String strVerifyDeviceOnline_ProdLabel,String strIPAddress_ProdLabel,String strTimeout_ProdLabel, String strDeviceName_Custom,String strVerifyDeviceOnline_Custom, String strIPAddress_Custom,String strTimeout_Custom) 
		{
			boolean blnFlag = false;
						
			blnFlag = Page("pgeAssemblyLines").Element("txtLineName").Type(strAssemblyLine);
			if(blnFlag)
			{
				//Small Bar Code Details
				blnFlag = Page("pgeAssemblyLines").Element("lstDeviceName_SmallBarCode").Select(strDeviceName_SmallBarCode);
				if(blnFlag)
				{
					if(strVerifyDeviceOnline_SmallBarCode.equalsIgnoreCase("Yes"))
					{
						blnFlag = Page("pgeAssemblyLines").Element("rdoVerifyDeviceOnlineSmallBarCode_Yes").Click(20);
						if (blnFlag)
						{
							blnFlag = Page("pgeAssemblyLines").Element("txtIPAddress_SmallBarCode").Type(strIPAddress_SmallBarCode);
							if(blnFlag)
							{
								blnFlag = Page("pgeAssemblyLines").Element("txtTimeout_SmallBarCode").Type(strTimeout_SmallBarCode);
								if(!blnFlag)
									Err.Description = "'"+strTimeout_SmallBarCode+"' was not entered in the Small Bar Code Timeout text box.";
							}
							else	
								Err.Description = "'"+strIPAddress_SmallBarCode+"' was not entered in the Small Bar Code IP Address text box.";
						}
						else						
							Err.Description = "Small Bar Code device Verify online radio button 'Yes' was not clicked.";						
					}
					else
					{
						blnFlag = Page("pgeAssemblyLines").Element("rdoVerifyDeviceOnlineSmallBarCode_No").Click(20);
						if(!blnFlag)
							Err.Description = "Small Bar Code device Verify online radio button 'No' was not clicked.";					
					}					
				}
				else				
					Err.Description = "'"+strDeviceName_SmallBarCode+"' was not selected from the Small Bar Code Device Name list box.";	
				
				if(blnFlag)
				{
					//Product Label Details - Execute only if Small Bar Code is executed fine
					blnFlag = Page("pgeAssemblyLines").Element("lstDeviceName_ProductLabel").Select(strDeviceName_ProdLabel);
					if(blnFlag)
					{
						if(strVerifyDeviceOnline_ProdLabel.equalsIgnoreCase("Yes"))
						{
							blnFlag = Page("pgeAssemblyLines").Element("rdoVerifyDeviceOnlineProductLabel_Yes").Click(20);
							if (blnFlag)
							{
								blnFlag = Page("pgeAssemblyLines").Element("txtIPAddress_ProductLabel").Type(strIPAddress_ProdLabel);
								if(blnFlag)
								{
									blnFlag = Page("pgeAssemblyLines").Element("txtTimeout_ProductLabel").Type(strTimeout_ProdLabel);
									if(!blnFlag)
										Err.Description = "'"+strTimeout_ProdLabel+"' was not entered in the Product Label Timeout text box.";
								}
								else	
									Err.Description = "'"+strIPAddress_ProdLabel+"' was not entered in the Product Label IP Address text box.";
							}
							else						
								Err.Description = "Product Label device Verify online radio button 'Yes' was not clicked.";						
						}
						else
						{
							blnFlag = Page("pgeAssemblyLines").Element("rdoVerifyDeviceOnlineSmallBarCode_No").Click(20);
							if(!blnFlag)
								Err.Description = "Product Label device Verify online radio button 'No' was not clicked.";					
						}					
					}
					else				
						Err.Description = "'"+strDeviceName_ProdLabel+"' was not selected from the Product Label Device Name list box.";	
				}					
				
				if(blnFlag)
				{
					//Custom Label Details - Execute only if previous loop has executed correctly
					blnFlag = Page("pgeAssemblyLines").Element("lstDeviceName_CustomLabel").Select(strDeviceName_Custom);
					if(blnFlag)
					{
						if(strVerifyDeviceOnline_Custom.equalsIgnoreCase("Yes"))
						{
							blnFlag = Page("pgeAssemblyLines").Element("rdoVerifyDeviceOnlineCustom_Yes").Click(20);
							if (blnFlag)
							{
								blnFlag = Page("pgeAssemblyLines").Element("txtIPAddress_Custom").Type(strIPAddress_Custom);
								if(blnFlag)
								{
									blnFlag = Page("pgeAssemblyLines").Element("txtTimeout_Custom").Type(strTimeout_Custom);
									if(!blnFlag)
										Err.Description = "'"+strTimeout_Custom+"' was not entered in the Custom Label Timeout text box.";
								}
								else	
									Err.Description = "'"+strIPAddress_Custom+"' was not entered in the Custom Label IP Address text box.";
							}
							else						
								Err.Description = "Custom Label device Verify online radio button 'Yes' was not clicked.";						
						}
						else
						{
							blnFlag = Page("pgeAssemblyLines").Element("rdoVerifyDeviceOnlineCustom_No").Click(20);
							if(!blnFlag)
								Err.Description = "Custom Label device Verify online radio button 'No' was not clicked.";					
						}					
					}
					else				
						Err.Description = "'"+strDeviceName_Custom+"' was not selected from the Custom Label Device Name list box.";	
				}	
			}
			else
				Err.Description = "'"+strAssemblyLine+"' is not typed in the Line Name text box successfully.";
						
			
			return blnFlag;
		}	
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: pushFullNametoCommonSheet
        //''@Objective: This Function gets the full name of the logged in user and pushes it to the Common Sheet under 'strFullName'.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strUserName - User Name which is used to login to the application.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= pushFullNametoCommonSheet()        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[10-Nov-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean pushFullNametoCommonSheet() 
		{
			boolean blnFlag = false;
			String strFullName = Page("pgeAssembly_Lines").Element("lnkUserName").GetText();
			blnFlag = !strFullName.isEmpty();
			
			if(blnFlag)				
			{
				blnFlag = sendValuetoCommonSheet("strFullName",strFullName);						
				if(!blnFlag)
					Err.Description = "The value '"+strFullName+"' is not pushed successfully to the field 'strFullName' in the Common Sheet.";				
			}
			else 			
				Err.Description = "The Full Name field is not displayed on the UI. The field is empty.";				
			
			return blnFlag;
		}

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillCSectionScheduleEntries
        //''@Objective: This Function fills C-Section Schedule entries.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strHospital - Hospital Name which is used to fill on the form.
		//''@	strPhysician - Physician Name which is used to fill on the form.
		//''@	strNote - Note which is used to fill on the form.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fillCSectionScheduleEntries("Wellstar Cobb", "Hitendra Hansalia, MD", "Test Schedule")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[11-Nov-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean fillCSectionScheduleEntries(String strHospital, String strPhysician, String strNote) 
		{
			boolean blnFlag = false;
			
			blnFlag = Page("pgeCSectionSchedule").Element("txtHospital").Type(strHospital);
			waitForPageRefresh();
			Page("pgeCSectionSchedule").Element("lstAutoComplete").Click(20);
			if(blnFlag)				
			{
				blnFlag = Page("pgeCSectionSchedule").Element("txtPhysician").Type(strPhysician);	
				waitForPageRefresh();
				Page("pgeCSectionSchedule").Element("lstAutoComplete").Click(20);						
				if(blnFlag)
				{
					waitForSync(2);
					blnFlag = Page("pgeCSectionSchedule").Element("txtNote").Type(strNote);
					if(blnFlag)
					{
						String tempXpath=Page("pgeCSectionSchedule").Element("btnAdd").GetXpath();
						tempXpath=tempXpath+"/..";
						String state=driver.findElement(By.xpath(tempXpath)).getAttribute("aria-disabled");
						if(state.equalsIgnoreCase("false")) 
						{
							blnFlag=true;
						}
						else
						{
							blnFlag=false;
							Err.Description="'Add' button not enabled on the form.";
						}
					}
					else
						Err.Description = "Unable to enter 'Note' on the form.";			
				}
				else
					Err.Description = "Unable to enter 'Physician' on the form.";				
			}
			else 			
				Err.Description = "Unable to enter 'Hospital' on the form.";				
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: validateEntryInGrid
        //''@Objective: This Function validates added entry in schedule grid.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strScheduleId - Latest Schedule id in DB.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= validateEntryInGrid("16598")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[11-Nov-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean validateEntryInGrid(String strScheduleId)
		{
			boolean blnFlag = false;
			String tempId=String.valueOf(Integer.parseInt(strScheduleId) + 1);
			String strAddedId="";
			
			strAddedId = Page("pgeCSectionSchedule").Element("objScheduleId").GetText();
			waitForSync(2);
			if(strAddedId.equalsIgnoreCase(tempId)) 
			{
				blnFlag=true;
				Script.dicTestData.put("strAddedId", strAddedId);
			}
			else
			{
				blnFlag=false;
				Err.Description="Added entry not validated with the database value.";
			}		
		return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickPrintPackagingForm
        //''@Objective: This Function clicks on Print Packaging Form link on Packaged Donors page and verfies the tilte of the navigated page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strExpectedText - The Expected Text in the PDF.
		//''@	strDonorKey - The DonorKey of the tissue.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickPrintPackagingForm("20131869", "Product Packaging Assembly Form")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[11-Nov-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickPrintPackagingForm(String strExpectedText, String strDonorKey) throws Exception
		{
			boolean blnFlag = false;			
			String strExpText=strExpectedText+","+strDonorKey;
			blnFlag = Page("pgePackagedDonors").Element("lnkPrintPackagingForms").Click(20);						
			if (blnFlag==true)
			{
				blnFlag=verifyPDF(strExpText);
				if(!blnFlag)
					Err.Description=Err.Description;
			}
			else
			{
				Err.Description="Unable to click on 'Print Packaging Form' Link";
			}
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickPackagingAssemblyForm
        //''@Objective: This Function clicks on Print Packaging Assembly Form link on Packaging History page and verfies the title of the navigated page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 	
        //''@	strExpectedText - The Expected Text in the PDF.
		//''@	strDonorKey - The DonorKey of the tissue.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickPackagingAssemblyForm("20131869", "Product Packaging Assembly Form") 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[11-Nov-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickPackagingAssemblyForm(String strExpectedText, String strDonorKey) throws Exception
		{
			boolean blnFlag = false;			
			String strExpText=strExpectedText+","+strDonorKey;
			blnFlag = Page("pgePackagingHistory").Element("lnkPackagingAssemblyForm").Click(20);						
			if (blnFlag==true)
			{
				blnFlag=verifyPDF(strExpText);
				if(!blnFlag)
					Err.Description=Err.Description;
			}
			else
			{
				Err.Description="Unable to click on 'Packaging Assembly Form' Link";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickHCTPPackagingForm
        //''@Objective: This Function clicks on HCT/P Packaging Form link on Packaging History page and verfies the title of the navigated page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 	
        //''@	strExpectedText - The Expected Text in the PDF.
		//''@	strDonorKey - The DonorKey of the tissue.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickHCTPPackagingForm("20131869", "Product Packaging Assembly Form") 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[11-Nov-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickHCTPPackagingForm(String strExpectedText, String strDonorKey) 
		{
			boolean blnFlag = false;			
			String strExpText=strExpectedText+","+strDonorKey;
			blnFlag = Page("pgePackagingHistory").Element("lnkHCTPPackagingForm").Click(20);	
			waitForSync(15);			
			if (blnFlag==true)
			{
				blnFlag=verifyPDF(strExpText);
				if(!blnFlag)
					Err.Description=Err.Description;
			}
			else
			{
				Err.Description="Unable to click on 'HCT/P Packaging Form' Link";
			}
			return blnFlag;
		}			
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifySearchRecordNotFound
        //''@Objective: This function verifies that searched record is not be displayed in grid.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - Page on which the Element whose text needs to be retrieved and the element in which search text needs to be entered exists.
		//''@	Element - The Element whose in which the search text is entered.
		//''@	Element1 - The Element whose text needs to be retrieved.
		//''@	strSearchText - The search text which should not be available in Element1.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifySearchRecordNotFound("pgePackagedDonors","txtDonor","objPackagedDonorsData","213456")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[11-11-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean verifySearchRecordNotFound(String Page, String Element, String Element1, String strSearchText) 
		{
			boolean blnFlag = false;
			strSearchText=strSearchText.trim();
			
			blnFlag = Page(Page).Element(Element).Type(strSearchText);
			if (blnFlag)
			{
				waitForPageRefresh();
				blnFlag=verifyRecordNotExist(Page,Element1,strSearchText);
				if(!blnFlag)
					Err.Description = "'"+strSearchText+"' is still displayed in the grid.";
			}
			else
				Err.Description = "Unable to enter '"+strSearchText+"' in the grid.";

			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: provideAddAssemblySignature
        //''@Objective: This function provides the Signature for Add Assembly page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strUserName - The User name which needs to be entered.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= provideAddAssemblySignature("smoketest")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[11-12-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean provideAddAssemblySignature(String strUserName) 
		{
			boolean blnFlag = false;
			
			blnFlag = Page("pgeProductAssembly").Element("txtUsername").Type(strUserName);
			if (blnFlag)
			{
				blnFlag= canvasSignature(driver);   				
				if(!blnFlag)
					Err.Description = "The canvas pad could not be signed.";
			}
			else
				Err.Description = "Unable to enter '"+strUserName+"' in the Username field.";

			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyEntriesAfterUpdate
        //''@Objective: This Function verifies the entries after adding or editing in a grid.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	PageName - The Page Name where the search box is present.
		//''@	ElementName - The Object name where the search text needs to be entered.
		//''@	text - The search text which needs to be validated.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyEntriesAfterUpdate(PageName, ElementName, loadedElementName, "WC14F036")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[14-Nov-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean verifyEntriesAfterUpdate(String Page, String Element, String text) 
		{
			boolean blnFlag = false;
			
			String tmpText = Page(Page).Element(Element).GetText();
			if (tmpText.contains(text)) 
			{
				blnFlag = true;
			} 
			else 
			{
				blnFlag = false;
				Err.Description = "Expected text '"+text+"' and Actual text '"+tmpText+"' not matched.";
			}

		return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: setCurrentDate
        //''@Objective: This Function sets a dictionary object with the current date in the format specified.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 	
		//''@	strFormat - The format in which the current date needs to be entered.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= setCurrentDate("MM/DD/YYYY")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[11-17-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean setCurrentDate(String strFormat) 
		{
			boolean blnFlag = false;
			String strDate = null;
			
			try
			{					
				Date date = new Date();
				strDate= new SimpleDateFormat(strFormat).format(date);	
				Script.dicCommonValue.put("strCurrentDate", strDate);
				blnFlag = true;
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Unable to format the current date.";
			}	
			
			return blnFlag;
		}	
		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickPrintReport
        //''@Objective: This function clicks on 'Print Form' link on 'Product Dehydration' page and internally calls   
		//''@ the function verifyPDF to verify pdf content of the new tab.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strCurrentDate - The Current Date is to be validated in the pdf.
		//''@	strExpectedText - The expected text to validate in pdf.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickPrintReport("November 17 2014","Product Inventory")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[11-17-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickPrintReport(String strCurrentDate, String strExpectedText)
		{
			boolean blnFlag = false;
			String strExpText=strExpectedText+","+strCurrentDate;
			
			blnFlag = Page("pgeProductInventory").Element("lnkPrintReport").Click(20);
			waitForSync(15);
			
			if (blnFlag==true)
			{
				blnFlag=verifyPDF(strExpText);
				if(!blnFlag)
					Err.Description=Err.Description;
			}
			else
			{
				Err.Description="Unable to click on 'Print Report' link on Product Inventory page.";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillNewGroupForm
        //''@Objective: This Function fills New Group entries.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strNewGroup - Group Name which is used to fill on the form.
		//''@	strEmail - Email which is used to fill on the form.
		//''@	strGroupRate - Group Rate which is used to fill on the form.
		//''@	strRejectRule - Reject Rule to be selected on the form.
		//''@	strAutoUpdate - Auto Update to be checked on the form.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fillNewGroupForm("Test Group", "akhare@mimedx.com", "200.00", "Grp", "Yes")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[11-18-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean fillNewGroupForm(String strNewGroup, String strEmail, String strGroupRate, String strRejectRule, String strAutoUpdate) 
		{
			boolean blnFlag = false;
			
			if (strAutoUpdate.equalsIgnoreCase("Yes"))
			{
				Page("pgeRecoveryGroups").Element("chkAutoUpdate").Click(20);
			}
			else
			{
				// Do nothing leave Auto Update checkbox unchecked
			}
			
			blnFlag = Page("pgeRecoveryGroups").Element("txtGroupName").Type(strNewGroup);

			if(blnFlag)				
			{
				blnFlag = Page("pgeRecoveryGroups").Element("txtEmail").Type(strEmail);	
						
				if(blnFlag)
				{
					blnFlag = Page("pgeRecoveryGroups").Element("txtGroupRate").Type(strGroupRate);
					
					if(blnFlag)
					{
						
						blnFlag=Page("pgeRecoveryGroups").Element("lstRejectRule").Select(strRejectRule);
						waitForSync(2);
						if(!blnFlag)
							Err.Description = "Unable to seletc 'Reject Rule' on the form.";	

					}
					else
						Err.Description = "Unable to enter 'Group Rate' on the form.";			
				}
				else
					Err.Description = "Unable to enter 'E-mail' on the form.";				
			}
			else 			
				Err.Description = "Unable to enter 'Group Name' on the form.";				
			
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillNewPhysicianForm
        //''@Objective: This Function fills New Physician entries.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False fillNewPhysicianForm(strFirstName, strLastName, strTitle, strNewQualifier, strManager);
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strFirstName - First name which is used to fill on the form.
		//''@	strLastName - Last name which is used to fill on the form.
		//''@	strTitle - Title which is used to fill on the form.
		//''@	strNewQualifier - Qualifier which is used to fill on the form.
		//''@	strManager - Manager name which is used to fill on the form.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fillNewPhysicianForm("Simmon", "Thomas", "MD", "Test")       
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[11-21-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean fillNewPhysicianForm(String strFirstName, String strLastName, String strTitle, String strNewQualifier) 
		{
			boolean blnFlag = false;
			
			blnFlag = Page("pgePhysicians").Element("txtFirstName").Type(strFirstName);

			if(blnFlag)				
			{
				blnFlag = Page("pgePhysicians").Element("txtLastName").Type(strLastName);	
						
				if(blnFlag)
				{
					blnFlag = Page("pgePhysicians").Element("txtTitle").Type(strTitle);
					
					if(blnFlag)
					{
						blnFlag=Page("pgePhysicians").Element("txtQualifier").Type(strNewQualifier);
						if(!blnFlag)
							Err.Description = "Unable to enter 'Qualifier' on the form.";	
					}
					else
						Err.Description = "Unable to enter 'Title' on the form.";			
				}
				else
					Err.Description = "Unable to enter 'Last Name' on the form.";				
			}
			else 			
				Err.Description = "Unable to enter 'First Name' on the form.";				
			
			return blnFlag;
		}				
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: validateGreenTickMark
        //''@Objective: This function clicks on specified tab and validated tab is clicked succesffuly or not.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strImage - Image name which is used to validate on the form.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= validateGreenTickMark("accept.png")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[11-25-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean validateGreenTickMark(String strImage) 
		{
			boolean blnFlag = false;
			String tempXpath,source;	
			
			
			tempXpath=Page("pgeProbeConfiguration").Element("imgAccept").GetXpath();
			
			source=driver.findElement(By.xpath(tempXpath)).getAttribute("src");
			
			if(source.contains(strImage))
			{
				blnFlag=true;
			}
			else
			{
				blnFlag=false;
				Err.Description="Green tick mark not displayed on the page.";
			}
		
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: calculateHospitalNameAndPrefix
        //''@Objective: This function clicks on specified tab and validated tab is clicked succesffuly or not.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strHospitalName - The Base string that is used for the hospital name by Test team.	
		//''@	strDBHospitalName - The latest hospital name from the Db	
		 //''@	strRecoveryPrefix - The Base string that is used for the prefix by Test team.	
		//''@	strDBRecoveryPrefix - The latest prefix from the Db	
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= calculateNewItem("Test Hospital", "Test Hospital", "T0", "T0")       
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[11-26-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean calculateHospitalNameAndPrefix(String strHospitalName, String strDBHospitalName, String strRecoveryPrefix, String strDBRecoveryPrefix) 
		{
			boolean blnFlag = false;
			String strtempHospital,strtempPrefix;
			
			blnFlag=calculateNewItem(strHospitalName, strDBHospitalName); 
			
			if(blnFlag)				
			{
				strtempHospital = Script.dicCommonValue.get("strCalculatedItem");
				
				blnFlag=calculatePrefix(strRecoveryPrefix, strDBRecoveryPrefix); 
				
				if(blnFlag)
				{
					strtempPrefix = Script.dicCommonValue.get("strPrefixValue");
										
					Script.dicCommonValue.put("strHospitalName",strtempHospital);    
					
					Script.dicCommonValue.put("strRecoveryPrefix",strtempPrefix);     
				}
				else
					Err.Description = "Unable to claculate Hospital Prefix that does not exist in the system.";	
			}
			else
				Err.Description="Unable to claculate Hospital Name that does not exist in the system.";
		
			return blnFlag;
		}	
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: calculatePrefix
        //''@Objective: This Function checks if the name of the item which needs to be added, is already added. If added, it calculates a new 
		//''@ 			value for the item.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strItemNameBaseString - The Base string that is used for the Item value by Test team.	
		//''@	strDBItemValue - The latest item value from the Db		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= calculatePrefix("Test Line", "strLineName")    
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[11-25-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean calculatePrefix(String strItemNameBaseString, String strDBItemValue)  
		{
			boolean blnFlag = false;                                               
			String strNewItemName = null;
			// Verify if an item that has been retrieved from the DB is empty.                                                           
			if (strDBItemValue != null)
			{
				// Increment the number by one - Old code has been commented out to handle data exceeding 10 hospitals within same code				
				//strNewItemName=strDBItemValue.substring(0,1)+String.valueOf(Integer.parseInt(strDBItemValue.substring(1,2))+1);                      
				strNewItemName = String.valueOf(Integer.parseInt(strDBItemValue)+1);                                                             
				blnFlag = true;  	
			}
			else
			{
				// If empty, use the same Base String as strNewItemName
				strNewItemName = strItemNameBaseString;
				blnFlag = true;  
			}
			Script.dicCommonValue.put("strPrefixValue",strNewItemName);                 
											   
			return blnFlag;
		}
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillNewHospitalForm
        //''@Objective: This Function fills New hospital entries.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strHospitalName - Hospital Name which is used to fill on the form.
        //''@	strRecoveryPrefix - Recovery Prefix which is used to fill on the form.
		//''@	strEmail - Email which is used to fill on the form.
		//''@	strFacilityRate - Facility Rate which is used to fill on the form.
		//''@	strGroupRate - Group Rate which is used to fill on the form.
		//''@	strRejectRule - Reject Rule to be selected on the form.
		//''@	strAutoUpdate - Auto Update to be checked on the form.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fillNewHospitalForm("Test Hospital", "TH", "akhare@mimedx.com", "200.00", "200.00", "All", "Yes")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[11-26-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean fillNewHospitalForm(String strHospitalName, String strRecoveryPrefix, String strEmail, String strFacilityRate, String strGroupRate, String strRejectRule, String strAutoUpdate) 
		{
			boolean blnFlag = false;
			
			if (strAutoUpdate.equalsIgnoreCase("Yes"))
			{
				Page("pgeHospitals").Element("chkAutoUpdate").Click(20);
			}
			else
			{
				// Do nothing leave Auto Update checkbox unchecked
			}
			
			blnFlag = Page("pgeHospitals").Element("txtHospitalName").Type(strHospitalName);
			
			if(blnFlag)				
			{
				blnFlag = Page("pgeHospitals").Element("txtPrefix").Type(strRecoveryPrefix);	

				if(blnFlag)				
				{
					blnFlag = Page("pgeHospitals").Element("txtEmail").Type(strEmail);	
					
					if(blnFlag)
					{
						blnFlag = Page("pgeHospitals").Element("txtFacilityRate").Type(strFacilityRate);
							
						if(blnFlag)
						{
							blnFlag = Page("pgeHospitals").Element("txtGroupRate").Type(strGroupRate);
							
							if(blnFlag)
							{
								blnFlag=Page("pgeHospitals").Element("lstRejectRule").Select(strRejectRule);
								waitForSync(2);
								if(!blnFlag)
									Err.Description = "Unable to seletc 'Reject Rule' on the form.";	
							}
							else
								Err.Description = "Unable to enter 'Group Rate' on the form.";	
						}
						else
							Err.Description = "Unable to enter 'Facility Rate' on the form.";								
					}
					else
						Err.Description = "Unable to enter 'E-mail' on the form.";				
				}
				else 			
					Err.Description = "Unable to enter 'Recovery Prefix' on the form.";		
			}
			else 			
				Err.Description = "Unable to enter 'Hospital Name' on the form.";						
			
			return blnFlag;
		}			
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickPrintReportLink
        //''@Objective: This function clicks on Print Report link on Consignment Billing page and internally call   
		//''@ the function verifyPDF to verify pdf content of the new tab.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - Page on which action to be performed.
		//''@	Element - Element on which action to be performed.
		//''@	strPurchaseOrder - variable used for appending the expected text.
		//''@	strExpText - The expected text to validate in pdf.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickPrintReportLink("pgeConsignmentBilling","lnkPurchaseOrder_Processed","12345","Consignment Billing")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[08-25-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickPrintReportLink(String Page, String Element, String strPurchaseOrder, String strExpText)
		{
			boolean blnFlag = false;
			strExpText=strExpText+","+strPurchaseOrder;
			
			blnFlag = Page(Page).Element(Element).Click(20);
			waitForSync(10);
			
			if (blnFlag==true)
			{
				blnFlag=verifyPDF(strExpText);
				if(!blnFlag)
					Err.Description=Err.Description;
			}
			else
			{
				Err.Description="Unable to click on 'Print Report' link";
			}
			return blnFlag;
		}	

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickTransferLink
        //''@Objective: This function clicks on specified 'Transfer' and calls the function getPageTitle() to verify user is on correct page or not.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strSalesOrderLine - 'Transfer' link to be clicked on specified order line.
		//''@	strExpText - The page title which needs to be validated after click of the 'Transfer' link.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickTransferLink("2","Transfer")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[12-03-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean clickTransferLink(String strSalesOrderLine, String strExpText) 
		{
			boolean blnFlag = false;
			blnFlag=Page("pgeSalesOrder").Element("//tbody[@id='rform:tblorderlines_data']/tr["+strSalesOrderLine+"]/td[last()]/a").Click(20);
			if (blnFlag)
			{
				blnFlag = getPageTitle(strExpText);
				if(!blnFlag)
					Err.Description =Err.Description;
			}
			else
				Err.Description="Unbale to click on specified 'Transfer' link.";
				
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillTransferOrderForm
        //''@Objective: This Function fills New Physician entries.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False 
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strShipTo - Ship To name which is used to fill on the form.
		//''@	strShipToAddress - Last name which is used to fill on the form.
		//''@	strBillTo - Title which is used to fill on the form.
		//''@	strBillToAddress - Qualifier which is used to fill on the form.
		//''@	strManager - Manager name which is used to fill on the form.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fillTransferOrderForm("Simmon", "Thomas", "MD", "Test")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[12-03-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean fillTransferOrderForm(String strShipTo, String strShipToAddress, String strBillTo, String strBillToAddress) 
		{
			boolean blnFlag = false;			
			String strInputValue="";
					
			blnFlag = Page("pgeSalesOrder").Element("txtShipTo").Type(strShipTo);
			waitForPageRefresh();
			if(blnFlag)				
			{
				blnFlag=Page("pgeSalesOrder").Element("//li[contains(@data-item-value,'"+ strShipTo +"')]").Click(20);

				if(blnFlag)				
				{
					waitForPageRefresh();
					strInputValue = Page("pgeSalesOrder").Element("drpShipToAddress").GetText();
					blnFlag=verifyInputValue(strInputValue, strShipToAddress, "Ship To Address");
					
					if(blnFlag)
					{
						strInputValue = Page("pgeSalesOrder").Element("txtBillTo").GetValue();
						blnFlag=verifyInputValue(strInputValue, strBillTo, "Bill To");
						
						if(blnFlag)
						{
							strInputValue = Page("pgeSalesOrder").Element("drpBillToAddress").GetText();
							blnFlag=verifyInputValue(strInputValue, strBillToAddress, "Bill To Address");
							if(!blnFlag)
								Err.Description = Err.Description;	
						}
						else
							Err.Description = Err.Description;			
					}
					else
						Err.Description = Err.Description;				
				}
				else 			
					Err.Description = "Unable to select list value of 'Ship To' field on the form.";		
			}		
			else 			
				Err.Description = "Unable to enter value in 'Ship To' field on the form.";				
			
			return blnFlag;
		}
		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyTransferLinkNotExist
        //''@Objective: This function verifies that 'Transfer' link should not be displayed in grid for the specified order line.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strSalesOrderLine - Presence of 'Transfer' link to be verified on specified order line.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyTransferLinkNotExist("2")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[12-05-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean verifyTransferLinkNotExist(String strSalesOrderLine) 
		{
			boolean blnFlag = false;
			String strNewSalesOrder=Page("pgeSalesOrder").Element("//tbody[@id='rform:tblorderlines_data']/tr["+strSalesOrderLine+"]/td[last()]").GetText();
			blnFlag=verifyRecordNotExist("pgeSalesOrder", "//tbody[@id='rform:tblorderlines_data']/tr["+strSalesOrderLine+"]/td[last()]", "Transfer"); 
			if(!blnFlag)
				Err.Description = "'Transfer' link still displayed instead of new Sales Order for 'Order Line: "+strSalesOrderLine+"'.";	
				
			Script.dicCommonValue.put("strNewSalesOrder",strNewSalesOrder);    
			
			return blnFlag;
		}		
        //''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillAddNewProductForm
        //''@Objective: This Function fills the form to add a new Product.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strProductID - a Product Code that does not exists in the application, fetched from 'calculateNewItem' function
		//''@   strProductName - a Product Name to be filled in the 'Name' field in the form
		//''@   strTissuePrefix - a value to be selected from 'Tissue Prefix' list in the form
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fillAddNewProductForm("Test Prod 1","TestProduct","AC24 -- 2.0 x 4.0 cm");       
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Kamlesh Yadav[12-08-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean fillAddNewProductForm(String strProductID,String strProductName,String strTissuePrefix)
		{
			boolean blnFlag = false;
			waitForSync(2);
			
			blnFlag = Page("pgeProducts").Element("txtProductCode").Type(strProductID);
			waitForSync(1);
			
			if (blnFlag == true) 
			{
				blnFlag = Page("pgeProducts").Element("txtProductName").Type(strProductName);
				waitForSync(2);
				
				blnFlag = Page("pgeProducts").Element("txtProductName").Type(strProductName);
				waitForSync(2);
				
				if (blnFlag == true) 
				{
					blnFlag = Page("pgeProducts").Element("lstTissuePrefix").Select(strTissuePrefix);
					waitForSync(4);
					
					blnFlag = Page("pgeProducts").Element("lstTissuePrefix").Select(strTissuePrefix);
					waitForSync(4);
					
					if (blnFlag == true) 
					{
                        blnFlag = verifyFieldnotEmpty("pgeProducts","txtDimensions") ;
						
						if (blnFlag == true)
						{
						    blnFlag = verifyFieldnotEmpty("pgeProducts","txtArchiveSamples") ; 
							
							if (blnFlag == true)
							{
							    blnFlag = verifyFieldnotEmpty("pgeProducts","txtResidualMoistureSamples") ; 
					            
								if (!blnFlag)
						         Err.Description="Data under field 'ResidualMoistureSamples' failed to auto-populate";
						    }
						    else
						        Err.Description="Data under field 'ArchiveSamples' failed to auto-populate";
					    }
					    else
					        Err.Description="Data under field 'Dimensions' failed to auto-populate";
				    }
				    else
				        Err.Description="'"+strTissuePrefix+"' was not selected from the TissuePrefix list.";
			    }
			     else
			         Err.Description="'"+strProductName+"' was not entered in the Name Field.";
			}
			else
			    Err.Description="'"+strProductID+"' was not entered in the Product Code Field.";
					 
		return blnFlag;
		}


		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: generatePurchaseOrder
        //''@Objective: This function generates a purchase order to enter on 'Consignment Purchase Order' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strPurchaseOrder - Base purchase order on the basis new purchase order be calculated.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= generatePurchaseOrder("TestPO")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[12-08-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean generatePurchaseOrder(String strPurchaseOrder) 
		{
			boolean blnFlag = false;
			String strPO="";
			int min=1;
			int max=50;
			Random rand = new Random();
			int randomNum = rand.nextInt((max - min) + 1) + min; 
			strPO = strPurchaseOrder+"-"+String.format("%03d",randomNum);
			if(!(strPO.isEmpty() || strPO.equals("") || strPO.equals(null)))
			{
				Script.dicCommonValue.put("strPO",strPO); 
				blnFlag=true;
			}
			else
			{
				blnFlag = false;
				Err.Description ="Unable to generate random purchase order for testing.";
			}
			return blnFlag;
		}	
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: enterPurchaseOrder
        //''@Objective: This function enters the purchase order on 'Consignment Purchase Order' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strPurchaseOrder - Base purchase order on the basis new purchase order be calculated.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= enterPurchaseOrder("TestPO")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[12-08-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean enterPurchaseOrder(String strPurchaseOrder) 
		{
			boolean blnFlag = false;
			
			blnFlag=generatePurchaseOrder(strPurchaseOrder) ;
			if (blnFlag)
			{
				strPurchaseOrder=Script.dicCommonValue.get("strPO");
				blnFlag=Page("pgeConsignmentPurchaseOrder").Element("txtPurchaseOrder").Type(strPurchaseOrder);
				if(blnFlag)
				{
					Script.dicCommonValue.put("strPurchaseOrder",strPurchaseOrder);
				}
				else
					Err.Description = "Unable to enter purchase order in 'Purchase Order' field.";	
					
			}
			else
				Err.Description=Err.Description;
			
			return blnFlag;
		}	
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickCommitAndVerifyConfirmationMessage
        //''@Objective: This Function verifies that the expected text is present in the object or not.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strExpText - The expected text to be validated , if it is present in the Element.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickCommitAndVerifyConfirmationMessage("E-Mail to Consignment Billing Successfully Sent")        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[12-11-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
 		public boolean clickCommitAndVerifyConfirmationMessage(String strExpText)
		{
			boolean blnFlag = true;
			
			blnFlag = Page("pgeConsignmentPurchaseOrder").Element("btnCommit").Click(20);
			waitForSync(2);

			if (blnFlag == true)
			{
				blnFlag = verifyTextContains("pgeConsignmentPurchaseOrder", "objConfMessage", strExpText);					
				if (!blnFlag)
					Err.Description=Err.Description;	
			}
			else
				Err.Description="Unable to click 'Commit' button.";
			return blnFlag;
		}	
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillRejectionDetailsAndCommit
        //''@Objective: This Function fills Rejection Details and Commit the form.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strRejectionReason - Rejection reason is used to fill on the form.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fillRejectionDetailsAndCommit("Rejected for testing")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[12-11-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean fillRejectionDetailsAndCommit(String strRejectionReason) 
		{
			boolean blnFlag = false;
			
			blnFlag = Page("pgeConsignmentBilling").Element("txtRejectReason").Type(strRejectionReason);
			
			if(blnFlag)				
			{
				blnFlag = Page("pgeConsignmentBilling").Element("btnCommit").Click(20);	

				if(blnFlag)				
				{
					blnFlag = Page("pgeConsignmentBilling").Element("objReasonForRejection").Exist();	
					
					if(!blnFlag)
						Err.Description = "Reason for Rejection not displayed on 'Consignment Billing' page.";	
				}
				else
					Err.Description = "Unable to click 'Commit' button in 'Reject Purchase Order' window.";	
			}
			else
				Err.Description = "Unable to enter rejection reason in 'Reject Purchase Order' window.";								
									
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fetchQuantity
        //''@Objective: This function fetch initial quantity as pre-requisite for 'MRB Disposal' functionality
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - Page name to mapping the object on the page.
        //''@	Element - Object name to mapping the object on the page.
		//''@	String pageTitle - Page name from where quantity needs to be fetched.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fetchQuantity("pgeInventoryLocation","objQuantity","Inventory")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[12-16-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean fetchQuantity(String Page, String Element, String pageTitle) 
		{
			boolean blnFlag = false;
			String strQuantity="";
			
			strQuantity = Page(Page).Element(Element).GetText();
			blnFlag = !strQuantity.isEmpty();
			
			if(blnFlag)
			{
				Script.dicCommonValue.put("strQuantity",strQuantity);						
			}
			else
				Err.Description = "Quantity not fetched on '"+pageTitle+"' page.";
												
			return blnFlag;
		}	
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: getTissueCode
        //''@Objective: This function gets tissue code as pre-requisite for 'MRB Disposal' functionality
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strLocation - Location name to be used in script.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= getTissueCode("MRB Quarantine")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[12-16-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean getTissueCode(String strLocation) 
		{
			boolean blnFlag = false;
			String strTissueCode="";
			
				blnFlag = Page("pgeInventoryLocation").Element("objTissueCode").Click(20);	

				if(blnFlag)				
				{
					strTissueCode = Page("pgeInventoryLocation").Element("objTissue").GetText();
					blnFlag = !strTissueCode.isEmpty();
					
					if(blnFlag)
					{
						Script.dicCommonValue.put("strTissueCode",strTissueCode);						
					}
					else
						Err.Description = "Unable to fetch tissue code on '"+strLocation+" - Tissue' page.";
							
				}
				else
					Err.Description = "Unable to click 'Tissue Code' link on '"+strLocation+" - Inventory' page.";									
									
			return blnFlag;
		}	
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: validateQuantity
        //''@Objective: This function validates initial and final quantity after 'MRB Disposal' process.        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - Page name to mapping the object on the page.
        //''@	Element - Object name to mapping the object on the page.
		//''@	String pageTitle - Page name from where quantity needs to be fetched.
		//''@	String strQuantity - Initial quantity from Inventory page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= validateQuantity("pgeInventoryLocation","objQuantity","Inventory","958")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[12-18-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean validateQuantity(String Page, String Element, String pageTitle, String strQuantity) 
		{
			boolean blnFlag = false;
			String strInitialQuantity=strQuantity;
			String strFinalQuantity="";
			int intInitialQty=0;
			int intFinalQty=0;
			
			blnFlag = fetchQuantity(Page, Element, pageTitle);

			if(blnFlag)				
			{
				strFinalQuantity=Script.dicCommonValue.get("strQuantity");				
				blnFlag = !strFinalQuantity.isEmpty();
				
				if(blnFlag)
				{
					intInitialQty=Integer.parseInt(strInitialQuantity);
					intFinalQty=Integer.parseInt(strFinalQuantity);
					if(intFinalQty < intInitialQty)
					{
						Script.dicCommonValue.put("strFinalQuantity",strFinalQuantity);	
					}
					else
					{
						blnFlag=false;
						Err.Description = "Unable to validate 'Initial' and 'Final' quantity.";
					}					
				}
				else
					Err.Description = "Quantity not fetched on '"+pageTitle+"' page.";
						
			}
			else
				Err.Description = Err.Description;									
									
			return blnFlag;
		}	
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillAddFacilityForm
        //''@Objective: This Function fills the form to add a new Facility.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strFacilityName - Facility Name to be entered in the Facility Form
		//''@   strTelephone - Telephone to be entered in the Facility Form
		//''@   strCity - City to be entered in the Facility Form 
		//''@   strState - State to be entered in the Facility Form 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus=fillAddFacilityForm("A Step Up Podiatry","661-832-1667""Manalapan","NJ","7726");      
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Kamlesh Yadav[12-15-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean fillAddFacilityForm(String strFacilityName,String strTelephone,String strCity,String strState,String strZipCode)
		{
			
			boolean blnFlag = false;
 
            blnFlag=Page("pgeFacility").Element("txtFacilityName").Type(strFacilityName);
		   
		   
		    if (blnFlag == true)
    	    {
		    
   			    blnFlag=Page("pgeFacility").Element("txtTelephone").Type(strTelephone);
				
				
				if (blnFlag == true )
			    {
			   
			        blnFlag=Page("pgeFacility").Element("txtCity").Type(strCity);
					
			     
   				    if (blnFlag == true)
			        {
			    
				        blnFlag=Page("pgeFacility").Element("txtState").Type(strState);
						
				    
       					if (blnFlag == true)
				        {
			                
							blnFlag=Page("pgeFacility").Element("txtZipCode").Type(strZipCode);
							
		
                      		if (!blnFlag)
                                Err.Description="'"+strZipCode+"' was not entered in the ZipCode field.";	
				        }
				        else
				            Err.Description="'"+strState+"' was not entered in the State field.";
			        }
			        else
				        Err.Description="'"+strCity+"' was not entered in the City field.";
			    }
			    else
			        Err.Description="'"+strTelephone+"' was not entered in the Telephone field.";
	        }
		    else 
		        Err.Description="'"+strFacilityName+"' was not entered in the Facility Name field.";
			
		return blnFlag;
	   }
	   
	   //''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: goToPreviousMonthTab
        //''@Objective: This Function goes to the tab mentioned for any past month.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strTabtobeClicked - The tab to be clicked on the Reimbursement Rates page (Can take values - Reimbursement/Certification/Changes).
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= goToPreviousMonthTab("Reimbursement")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[12-15-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean goToPreviousMonthTab(String strTabtobeClicked) 
		{
			boolean blnFlag = false;
			
			// Calculate the previous month / Year
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -2);
			Date result = cal.getTime();
			String strDate= new SimpleDateFormat("MMM,yyyy").format(result);
			String [] strCalcMonYear = strDate.split(",");
			
			// Select previous month from dropdown
			blnFlag = Page("pgeReimbursementRates").Element("drpMonth").Select(strCalcMonYear[0]);
			if(blnFlag)
			{
				// Enter previous year 
				waitForSync(10);
				blnFlag = Page("pgeReimbursementRates").Element("txtYear").Type(strCalcMonYear[1]);
				// Select the 'Rates' Tab so that focus moves from the Year text box
				String xpathTemp = "//a[text()='Rates']";
				Page("pgeReimbursementRates").Element(xpathTemp).Click(20);
				if(blnFlag)
				{
					// Click on Tab specified
					try
					{
						try{
							waitForPageRefresh();
							waitForSync(10);
							}
							catch(Exception e)
							{						
							}	
							
						if(strTabtobeClicked.equalsIgnoreCase("Reimbursement"))
						{
							String xpath= "//a[text()='Reimbursement "+strCalcMonYear[0]+"-"+strCalcMonYear[1]+"']";							
							blnFlag = Page("pgeReimbursementRates").Element(xpath).Click(20);								
							waitForSync(20);							
						}
						else if (strTabtobeClicked.equalsIgnoreCase("Certification"))
						{
							String xpath= "//a[text()='Certification "+strCalcMonYear[0]+"-"+strCalcMonYear[1]+"']";							
							blnFlag = Page("pgeReimbursementRates").Element(xpath).Click(20);							
							waitForSync(20);
						}
						else if (strTabtobeClicked.equalsIgnoreCase("Changes"))
						{
							try{
							String xpath= "//a[text()='Changes "+strCalcMonYear[0]+"-"+strCalcMonYear[1]+"']";	
							blnFlag = Page("pgeReimbursementRates").Element(xpath).Click(20);							
							waitForSync(20);
							}
							catch(Exception e)
							{						
							}					
						}
						else
						{
							blnFlag = false;
							Err.Description="Provide correct Tab name to be clicked.";		
						}
						blnFlag = true;
					}
					catch(Exception e)
					{
						blnFlag = false;						
					}
										
					if(blnFlag)
					{
						try
						{
							waitForPageRefresh();
							blnFlag = true;
						}
						catch(Exception e)
						{							
						}
					}
					else
						Err.Description="The tab mentioned could not be clicked.";	
				}
				else
					Err.Description="Unable to type previous year.";	
			}
			else
				Err.Description="Unable to select previous month from dropdown.";	
									
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: sortWaitforPageRefresh
        //''@Objective: This Function clicks on the sort icon and waits for the page to refresh.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - The Page on which the sorting object exists.
		//''@	Element - The sorting element which needs to be clicked.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= sortWaitforPageRefresh("pgeReimbursementRates", "objRecoveredDateSort")
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[12-16-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean sortWaitforPageRefresh(String Page, String Element) 
		{
			boolean blnFlag = false;
			
			// Click on the sort element
			blnFlag = Page(Page).Element(Element).Click(20);
			if(blnFlag)
			{
				try
				{
					waitForPageRefresh();
				}
				catch(Exception e)
				{
					blnFlag = false;
					Err.Description="Page refresh after clicking on sort icon was not successful.";	
				}			
			}			
			else
				Err.Description="Unable to click on Sort icon.";	
				
			Page(Page).Element(Element).MouseHover();
			
			return blnFlag;
		}	
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyMonYear
        //''@Objective: This Function verifies if the entry specified falls under the past month and year.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - The Page on which the object exists, the object which contains the date to be validated.
		//''@	Element - The object which has the date to be validated.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyMonYear("pgeReimbursementRates", "objRecoveredDate")
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[12-16-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean verifyMonYear(String Page, String Element) 
		{
			boolean blnFlag = false;
			
			// Calculate the previous month / Year
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -2);
			Date result = cal.getTime();
			String strDate= new SimpleDateFormat("MM,yyyy").format(result);
			String [] strCalcMonYear = strDate.split(",");
			
			// Verify if there is data in the table
			blnFlag  = Page(Page).Element("objNoRecords").Exist();			
			if (blnFlag) 
			{
				Script.dicCommonValue.put("strActual", "There is no data for the Month and Year selected.");				
			}
			else
			{		
				Script.dicCommonValue.put("strActual", "The first entry under 'Recovered Date' column falls under the month and year selected.");
				Page(Page).Element(Element).MouseHover();
				String strUIDate = Page(Page).Element(Element).GetText();
				String [] strUIDateArr = strUIDate.split("-");
				
				blnFlag = (strCalcMonYear[1].trim()).equalsIgnoreCase(strUIDateArr[0].trim());
							
				if(blnFlag)
				{
					blnFlag = (strCalcMonYear[0].trim()).equalsIgnoreCase(strUIDateArr[1].trim());
					if(!blnFlag)
						Err.Description="The Month entry from the application ("+strUIDateArr[1]+") does not match the month entry expected ("+strCalcMonYear[0]+").";	
				}			
				else
					Err.Description="The Year entry from the application ("+strUIDateArr[0]+") does not match the year entry expected ("+strCalcMonYear[1]+").";	
			}	
									
			return blnFlag;
		}			
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickPrintReportReimbursement
        //''@Objective: This function clicks on 'Print Report' link on Reimbursements page and internally calls   
		//''@ the function verifyPDF to verify pdf content of the new tab.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 		
		//''@	strExpText - The expected text to validate in pdf.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickPrintReportReimbursement("Donor Reimbursement - Draft,Hospitals,Groups")        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[12-18-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickPrintReportReimbursement(String strExpText)
		{
			boolean blnFlag = false;
			
			blnFlag = Page("pgeReimbursementRates").Element("btnPrintReport").Click(20);
			waitForSync(20);
			
			if (blnFlag==true)
			{
				blnFlag=verifyPDF(strExpText);
				if(!blnFlag)
					Err.Description=Err.Description;
			}
			else
			{
				Err.Description="Unable to click on 'Print Report' link";
			}
			return blnFlag;
		}
		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: generateFacilityRate
        //''@Objective: This function generates a facility rate to enter on 'Edit Hospital' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= generateFacilityRate()        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[12-18-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean generateFacilityRate() 
		{
			boolean blnFlag = false;
			
			int min=100;
			int max=250;
			String strExistingFacilityRate;
			String strCalcFacilityRate;			
			
			try
			{				
				strExistingFacilityRate = Page("pgeReimbursementRates").Element("objFirstFacilityRate").GetText().trim();
				
				// Calculate a random Facility rate which is not equal to the one displayed on the Reimbursements UI
				do
				{
					Random rand = new Random();
					int randomNum = rand.nextInt((max - min) + 1) + min; 
					strCalcFacilityRate = String.valueOf(randomNum);
					strCalcFacilityRate = strCalcFacilityRate + ".00";
				}
				while(strExistingFacilityRate.equalsIgnoreCase(strCalcFacilityRate));
				
				if(!(strCalcFacilityRate.isEmpty() || strCalcFacilityRate.equals("") || strCalcFacilityRate.equals(null)))
				{
					Script.dicCommonValue.put("strCalcFacilityRate",strCalcFacilityRate); 
					blnFlag=true;
				}
				else
				{
					blnFlag = false;
					Err.Description ="Unable to generate random Facility Rate.";
				}
			
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description ="Unable to generate random Facility Rate.";
			}
			
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: retrieveFacilityName
        //''@Objective: This function retrieves the first Facility name on Reimbursements page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= retrieveFacilityName()        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[12-18-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean retrieveFacilityName() 
		{
			boolean blnFlag = false;			
			String strFirstFacility = "" ; 		
			
			try
			{				
				strFirstFacility = Page("pgeReimbursementRates").Element("lnkFirstFacility").GetText().trim();				
				
				if(!(strFirstFacility.isEmpty() || strFirstFacility.equals("") || strFirstFacility.equals(null)))
				{
					Script.dicCommonValue.put("strFirstFacility",strFirstFacility); 
					blnFlag=true;
				}
				else
				{
					blnFlag = false;
					Err.Description ="Unable to retrieve the first Facility name on Reimbursements page.";
				}
			
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description ="Unable to retrieve the first Facility name on Reimbursements page.";
			}
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickSubTab
        //''@Objective: This Function goes to the sub-tab mentioned.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strSubTabtobeClicked - The tab to be clicked on the Reimbursement Rates page (Can take values - Hospital Payments/Group Payments).
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickSubTab("Hospital")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[12-19-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean clickSubTab(String strSubTabtobeClicked) 
		{
			boolean blnFlag = false;

			// Calculate the previous month / Year
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -2);
			Date result = cal.getTime();
			String strDate= new SimpleDateFormat("MMM,yyyy").format(result);
			String [] strCalcMonYear = strDate.split(",");			
				
			// Click on Tab specified
			try
			{					
				if(strSubTabtobeClicked.equalsIgnoreCase("Hospital Payments"))
				{
					String xpath= "//a[text()='Hospital Payments "+strCalcMonYear[0]+"-"+strCalcMonYear[1]+"']";							
					blnFlag = Page("pgeReimbursementRates").Element(xpath).Click(20);								
					waitForSync(20);							
				}
				else if (strSubTabtobeClicked.equalsIgnoreCase("Group Payments"))
				{
					String xpath= "//a[text()='Group Payments "+strCalcMonYear[0]+"-"+strCalcMonYear[1]+"']";							
					blnFlag = Page("pgeReimbursementRates").Element(xpath).Click(20);							
					waitForSync(20);
				}			
				else
				{
					blnFlag = false;
					Err.Description="Provide correct Sub-Tab name to be clicked.";		
				}
				blnFlag = true;
			}
			catch(Exception e)
			{									
			}
								
			if(blnFlag)
			{
				try
				{
					waitForPageRefresh();
					blnFlag = true;
				}
				catch(Exception e)
				{							
				}
			}
			else
				Err.Description="The tab mentioned could not be clicked.";			
									
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fetchFacilityID
        //''@Objective: This Function fetches the newly added Facility ID from the UI.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fetchFacilityID();    
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Kamlesh Yadav[12-16-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################  
	    public boolean fetchFacilityID()
		{
			
		    boolean blnFlag = false;
			
			try
			{
			    String strNewFacilityID = Page("pgeFacility").Element("ObjFacilityID").GetText();
			    if (!(strNewFacilityID.equals("") || strNewFacilityID.equals(null)))
			    {
			        Script.dicCommonValue.put("FacilityID",strNewFacilityID);
				    blnFlag = true;
			    }
			    else
				{
			        Err.Description="Unable to fetch Facility ID";
			     	blnFlag = false;
				}
			}
			catch (Exception e)
			{
				Err.Description="Unable to fetch Facility ID";
			    blnFlag = false;
			}
			
		return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID:
        //''@Function Name: handleAlertsLocationInventory
        //''@Objective: This function clicks on the 'Reset' or 'reconcile' button,handles the re-scan dialog-box and verifies the navigated
		//''@           page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc:
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Input Parameters:
        //''@      strPlaceofFuncCall - String which refers to the Element on which action to be performed.
        //''@      strExpText - Location name to be used in script.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= handleAlertsLocationInventory("Reconcile","Visual Inspection");     
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Kamlesh Yadav[12-18-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]:
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes:
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################	
        public boolean handleAlertsLocationInventory(String strPlaceofFuncCall,String strExpText)
		{
		    boolean blnFlag=false;
		    String strElement = "";
		
		    if (strPlaceofFuncCall.equalsIgnoreCase("Reconcile"))
		    {
		        strElement = "btnReconcile";
		        blnFlag = true;
		    }
		    else if (strPlaceofFuncCall.equalsIgnoreCase("Reset"))
		    {
		        strElement = "btnReset";
		        blnFlag = true;
		    }
		    else
		    {   
                Err.Description="Pass valid parameters to the function";		
			    blnFlag = false;
		    }
		
		    if (blnFlag == true)
		    {  
			    
				String xpath=Page("pgeInventoryLocation").Element(strElement).GetXpath();
            
     			if (!(xpath.equals("") || xpath.equals(null)))
			    { 
				    //Used this line of code to handle re-scan alert dialog-box
					driver.findElement(By.xpath(xpath)).click(); 
                    waitForSync(4);			
			        driver.switchTo().alert().accept();
					waitForSync(4);	
			
 			        blnFlag=getPageTitle(strExpText);
			        
					 if(!blnFlag)
		        	{
				    	Err.Description = "Unable to navigate to '"+strExpText+"' page.";	
				    }
			    }
				else
				{
			        Err.Description="Unable to fetch the xpath for the object '"+strElement+"'";
					 blnFlag = false;
				}
		    }
		return blnFlag;	
	    }
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fetchTissueForReconcilation
        //''@Objective: This function fetches the missing tissue to be moved to the area after 'Reconciliation.'
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@      Page - Page from which the tissue is to be fetched
        //''@      Element - Object from which the Tissue value is to be fetched
        //''@      pageTitle - Verify the Page title from which tissue needs to be fetched		
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus=fetchTissueForReconcilation("pgeInventoryLocation","objTissueGrid","Validate Inventory at Visual Inspection") ;     
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Kamlesh Yadav[12-16-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################  
		public boolean fetchTissueForReconcilation(String Page, String Element, String pageTitle) 
		{
			boolean blnFlag = false;
			String strTissue ="";
			
			strTissue = Page(Page).Element(Element).GetText();
			blnFlag = !strTissue.isEmpty();
			
			if(blnFlag)
			{
				Script.dicCommonValue.put("strTissue",strTissue);						
			}
			else
				Err.Description = "Tissue not fetched from '"+pageTitle+"' page.";
												
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID:
        //''@Function Name: clickOnInventoryRecReport
        //''@Objective: This function clicks on Inventory's Rec. Report and verifies the PDF and its content.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc:
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Input Parameters:
        //''@      strExpText - text to be verified on the PDF
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickOnInventoryRecReport("Visual Inspection");       
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Kamlesh Yadav[12-18-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]:
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes:
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean clickOnInventoryRecReport(String strExpText)
		{
		    boolean blnFlag=false;
			
			blnFlag = Page("pgeInventoryLocation").Element("lnkRecReport").Click(20);
            waitForSync(10);			
			
			    if (blnFlag)
				{ 
				     blnFlag = verifyPDF(strExpText);
					
					if (!blnFlag)
                    {
                        Err.Description="Unable to verify PDF's Content";
                    }
                else
                    Err.Description="Unable to perform click operation.";	
				}
		 
		return blnFlag;
        } 
		
		//''@###########################################################################################################################
        //''@Function ID:
        //''@Function Name: clickOnInventoryFixReport
        //''@Objective: This function clicks on Inventory's Fix Report and verifies the PDF and its content.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc:
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Input Parameters:
        //''@      strTissueCode - Tissue code to be verified on the report
        //''@      strExpText - text to be verified on the PDF
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickOnInventoryRecReport("AD012-20144939-003","Visual Inspection");       
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Kamlesh Yadav[12-18-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]:
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes:
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
			public boolean clickOnInventoryFixReport(String strTissueCode, String strExpText)
		{
		    boolean blnFlag=false;
			strExpText=strExpText+","+strTissueCode;
			
			blnFlag = Page("pgeInventoryLocation").Element("lnkFixReport").Click(20);	
			waitForSync(10);
			
			    if (blnFlag)
				{ 
				    blnFlag = verifyPDF(strExpText);
					
					if (!blnFlag)
                    {
                        Err.Description="Unable to verify PDF's Content";
                    }
                else
                    Err.Description="Unable to perform click operation.";	
				}
		 
		return blnFlag;
        } 
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: backMonthSelection
        //''@Objective: This Function selects no of specified prvious months for search results in grid on Release Processing page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strMonth - nth number of previous month to be selected.        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= backMonthSelection("http://mdxinfas01:8080/SBWEB/")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[01-09-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean backMonthSelection(String strMonth) 
		{
			boolean blnFlag = false;
			blnFlag=Page("pgeRelease_Processing").Element("//label[@id='centerform:filter_label']").Click(20);
			if (blnFlag) 
			{
				blnFlag = Page("pgeRelease_Processing").Element("//li[@data-label='"+strMonth+"']").Click(20);
				waitForPageRefresh();
				if (!blnFlag) 
				{					
					Err.Description = "Unable to select month from drop down list.";
				}					
			} 
			else
				Err.Description = "Unable to click on drop down button.";
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: genderSelection
        //''@Objective: This Function selects the corresponding radio button based on the Gender passed.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strGender - The Gender that needs to be selected.        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= genderSelection("Male")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[02-03-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean genderSelection(String strGender) 
		{
			boolean blnFlag = false;
			
			if (strGender.equalsIgnoreCase("male"))
			{
				blnFlag = Page("pgeTissueUtilization").Element("rdoGenderMale").Click(20);
				if (!blnFlag) 							
					Err.Description = "Unable to click on the 'Male' radio button.";				
			}
			else
			{
				blnFlag = Page("pgeTissueUtilization").Element("rdoGenderFemale").Click(20);
				if (!blnFlag) 							
					Err.Description = "Unable to click on the 'Female' radio button.";
			}
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyExpirationDate
        //''@Objective: This Function verifies if the Expiration Date is five years in future to the current date and the 1st of that Month.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	Page - The Page that has the Expiration Element.        
		//''@	Element - The Expiration Date that needs to be verified.     
		//''@	strProcessingDate - The Process Start Date.   
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyExpirationDate("pgeAddTissue", "objExpirationDate","2015-01-28 08:42")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[02-05-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean verifyExpirationDate(String Page, String Element, String strProcessingDate) 
		{
			boolean blnFlag = false;
						
			String strExpirationDate = Page(Page).Element(Element).GetText();
			// Processing Date + 5 years ; 1st of that month
			try
			{
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date date = format.parse(strProcessingDate);
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.add(Calendar.YEAR, 5);		
				cal.set(Calendar.DATE, 1);
				Date result = cal.getTime();
				String strExpDate= new SimpleDateFormat("EEE. MMM d, yyyy").format(result);			
				
				// Compare the calculated date and date on the application
				blnFlag = strExpDate.equalsIgnoreCase(strExpirationDate);
				if(!blnFlag)
					Err.Description="The Expiration Date on the application does not match the calulated date. Expected Date is '"+strExpDate+"' ; Acyual Date is '"+strExpirationDate+".";	
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description= "Error occured while calculating the Expiration Date.";
			}
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: getProcessStartDate
        //''@Objective: This Function retrieves the Process Start Date and saves it in a Common dictionary object.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	Page - The Page that has the Process Start Date Element.        
		//''@	Element - The Process Start Date.     		
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= getProcessStartDate("pgeDonorProcessing", "txtProcessingStartedDate")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[02-06-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean getProcessStartDate(String Page, String Element) 
		{
			boolean blnFlag = false;
				
			try
			{
				String strProcessStartDate = Page(Page).Element(Element).GetValue().trim();
				blnFlag = !strProcessStartDate.isEmpty();				
				if(blnFlag)
					Script.dicCommonValue.put("ProcessStartDate", strProcessStartDate);
				else
					Err.Description="The Process Start Date field is empty.";	
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description= "Unable to retrieve the Process Started Date.";
			}
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickReviewFormDonorReject
        //''@Objective: This function clicks on Review form link on Recovered Tissue page and internally call   
		//''@ the function getPDFContent to verify pdf content that opens in the navigated page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strRecoveryKey - The Recovery Key to validate in the pdf.
		//''@	strExpText - The expected text to validate in the pdf.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickReviewFormDonorReject("WC15B004","Donor Release for Processing Checklist")       
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[02-10-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickReviewFormDonorReject(String strRecoveryKey, String strExpText)
		{
			boolean blnFlag = false;
			strExpText=strRecoveryKey+","+strExpText;
			try
			{
				blnFlag = Page("pgeRecovered_Tissue").Element("lnkReviewForm_Reject").Click(20);
				waitForPageRefresh();
				
				//Validate the PDF content on this window	
				driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@id='pdfcontents']")));
				
				if (blnFlag)
				{
					blnFlag=getPDFContent(strExpText);
					if(!blnFlag)
						Err.Description=Err.Description;
				}
				else
				{
					Err.Description="Unable to click on 'Review Form' link";
				}
				
				driver.switchTo().defaultContent();
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description="Error in switching the driver to the iframe.";
			}
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: selectDataItemValueTextBox
        //''@Objective: This Function selects the specified value from the specified auto-complete text box.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	Page - The Page on which the auto-complete text box resides.
		//''@	Element - The auto-complete text box from where the value needs to be selected.
		//''@	strSelectionValue - The value of the that needs to be selected from the specified auto-complete text box.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= selectDataItemValueTextBox("pgeAddAutoclaveLog","txtLoadTechnician","Joanna Reynolds")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[02-12-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean selectDataItemValueTextBox(String Page, String Element, String strSelectionValue)
		{
			boolean blnFlag = false;
			try
			{
				String xpath=Page(Page).Element(Element).GetXpath();
				String strTextbox = driver.findElement(By.xpath(xpath)).getAttribute("role");
				if(strTextbox.equalsIgnoreCase("textbox"))
				{
					driver.findElement(By.xpath(xpath)).sendKeys(strSelectionValue);
					//Wait for three second to wait for selection value to show up in the list
					waitForSync(3);
					Page(Page).Element("//li[@data-item-value='" + strSelectionValue + "']").Click(20);								
				}
				else
				// If it is not a text box, then do not send keys and use 'data value'  for the item selection
				{
					//Wait for three second to wait for selection value to show up in the list
					waitForSync(3);
					Page(Page).Element("//li[@data-label='" + strSelectionValue + "']").Click(20);							
				}
			blnFlag = true;	
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Unable to select the option '" +strSelectionValue+ "' from the auto-complete text box.";					
			}
			
			return blnFlag;
		}			
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: pushLoadNumbertoCommonSheet
        //''@Objective: This Function validates if the load number is not empty and pushes it to the Common Sheet under 'strLoadNumber'.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strUserName - User Name which is used to login to the application.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= pushLoadNumbertoCommonSheet()        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[13-Feb-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean pushLoadNumbertoCommonSheet(String strLoadNumber) 
		{
			boolean blnFlag = false;
			blnFlag=verifyFieldnotEmpty("pgeAddAutoclaveLog", "objLoadNumber");
						
			if(blnFlag)				
			{	
				String strLoadNumberValue = Page("pgeAddAutoclaveLog").Element("objLoadNumber").GetText().trim();;
				blnFlag = sendValuetoCommonSheet(strLoadNumber,strLoadNumberValue);						
				if(!blnFlag)
					Err.Description = "The value '"+strLoadNumberValue+"' is not pushed successfully to the field '"+strLoadNumber+"' in the Common Sheet.";	
			}
			else 			
				Err.Description = "The Load Number field is either empty or not displayed on the UI.";				
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: getDateInterval
        //''@Objective: This Function gets the date as specified for the interval in the format specified.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strFormat - The Format in which the date needs to be provided.
		//''@	strInterval - The interval in form of days (+,- included for future or past).
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= getDateInterval("MM-yyyy","120")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[02-13-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean getDateInterval(String strFormat, String strInterval) 
		{
			boolean blnFlag = false;
			
			try
			{
				// Calculate the date after/before the interval
				Calendar cal = Calendar.getInstance();
				// Change string to int
				int intInterval = Integer.parseInt(strInterval);
				cal.add(Calendar.DATE, intInterval);
				Date result = cal.getTime();
				String strDate= new SimpleDateFormat(strFormat).format(result);
				blnFlag = !strDate.isEmpty();								
				if(blnFlag)
					Script.dicTestData.put("strCalculatedDate", strDate);
				else
					Err.Description="The Calculated date is empty.";	
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description="Error while calculating the required date.";
			}			
									
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: enterDateinField
        //''@Objective: This Function enters a date calculated on the interval and format specified, in the field specified.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	PageName - The Page to which the Element is mapped.
		//''@	Element - The Element in which the formatted current date is to be entered.
		//''@	strFormat - The format in which the current date needs to be entered.
		//''@	strInterval - The number of days that need to be added/ subtracted from the current date.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= enterDateinField("pgeAddAutoclaveLog","txt3MExpirationDate","MM-YYYY","120")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[13-02-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean enterDateinField(String PageName, String Element, String strFormat, String strInterval) 
		{
			boolean blnFlag = false;
			String strDate = null;
			
			try
			{					
				blnFlag= getDateInterval(strFormat,strInterval);  
				
				if(blnFlag)
				{
					strDate = Script.dicTestData.get("strCalculatedDate");
					// Enter the calculated date in the Page -> Element
					waitForSync(5);	
					blnFlag = Page(PageName).Element(Element).Type(strDate);
					waitForSync(5);		
					if(!blnFlag)
						Err.Description = "Unable to enter the calculated date("+strDate+") in the Page ("+PageName+"),Element ("+Element+").";				
				}			
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Unable to enter the calculated date.";
			}	
			
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: validateInstrumentsGrid
        //''@Objective: This Function validates if the Instruments, Part number and quantity are added in the grid.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strInstruments - The Instrument+Part number which needs to be validated in the grid.
		//''@	strQuantity - The quantity which needs to be validated in the grid.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= validateInstrumentsGrid("Injectable Rack - (INJ-001)","1")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[13-02-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean validateInstrumentsGrid(String strInstruments, String strQuantity) 
		{
			boolean blnFlag = false;

			try
			{	
				//	Validate if Total Quantity is updated
				blnFlag= verifyTextContains("pgeAddAutoclaveLog", "objTotalQuantity", strQuantity);  
				if(blnFlag)
				{	
					String [] tempArr = strInstruments.split("- \\(");
					String strIntrument = tempArr[0].trim();
					String strPartNumber = (tempArr[1].replace(")", "")).trim();	
					// Verify if the details are updated in the grid
					blnFlag = verifyExactText("pgeAddAutoclaveLog","objInstruments",strIntrument);
					if(blnFlag)
					{
						blnFlag = verifyExactText("pgeAddAutoclaveLog","objPartNumber",strPartNumber);
						if(blnFlag)
						{
							String strtempValue = Page("pgeAddAutoclaveLog").Element("objQuantity").GetValue();
							blnFlag = strtempValue.equalsIgnoreCase(strQuantity);
							if(!blnFlag)	
								Err.Description = "Quantity ("+strQuantity+") is not updated in the grid successfully.";
						}
						else
							Err.Description = "Part number ("+strPartNumber+") is not updated in the grid successfully.";
					}
					else
						Err.Description = "Instrument ("+strIntrument+") is not updated in the grid successfully.";						
				}
				else
					Err.Description = "There is no entry in the Instruments grid.";
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occurred while validating the Instruments grid.";
			}	

			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: uploadScannedReport
        //''@Objective: This Function clicks on Upload Button on 'Edit Autoclave Log' page and upload a sample 'pdf' file.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strFilePath - The file path of the sample PDF file that needs to be loaded.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= uploadScannedReport("secondsdoc.pdf")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[02-17-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean uploadScannedReport(String strFilePath) 
		{
			boolean blnFlag=false;

			try
			{
				blnFlag=Page("pgeEditAutoclaveLog").Element("btnUpload").Click(20);
				if (blnFlag)
				{
					waitForPageRefresh(15);
					blnFlag=uploadFile(strFilePath);
					if (blnFlag)
					{
						if(Page("pgeEditAutoclaveLog").Element("objLoadFormError").Exist())
						{
							blnFlag=false;
							Err.Description = "Unable to upload 'pdf' file for 'Scanned Report'.";
						}
					}
					else		
						Err.Description = Err.Description;					
				}
				else
					Err.Description = "Unable to click on 'Upload' button.";		
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occurred while uploading PDF file for 'Scanned Report'.";	
			}

			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: electronicSignature
        //''@Objective: This Function signs the canvas on any page and fill the canvas.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	pgeName - The Page on which the element exists.
		//''@	eleName - The element on which action is performed.
        //''@	strUserName - Initials of the user to fill in the Signature field on page and fill the canvas.
		 //''@	eleCanvas - Signature canvas element.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= electronicSignature("pgeEditAutoclaveLog", "txtInitials", "akhare", "//canvas")       
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[02-19-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean electronicSignature(String pgeName, String eleName, String strUserName, String eleCanvas)
		{
			boolean blnFlag = false;
			
			blnFlag = canvasSignature(pgeName, eleCanvas);
			if(blnFlag)
			{			
				blnFlag = Page(pgeName).Element(eleName).Type(strUserName);
				if(!blnFlag)
					Err.Description = "Unable to type user ID '"+strUserName+"'.";							
			}
			else
			{
				blnFlag = false;
				Err.Description = "Unable to enter signature in canvas.";	
			}		
				
			return blnFlag;
		}		
		
			//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillIndicatorResultInitials
        //''@Objective: This Function selects the required value from the SIS Indicator Type, signs the canvas pad, and enter Initials.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strSISIndicatorResult - The SIS Indicator PASS/FAIL result.
		//''@	strUserName - The Username who signs the Edit page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fillIndicatorResultInitials("PASS","smoketest")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[02-20-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean fillIndicatorResultInitials(String strSISIndicatorResult, String strUserName) 
		{
			waitForSync(5);				
			
			boolean blnFlag=false;
			
			try
			{
				blnFlag=Page("pgeEditAutoclaveLog").Element("drpSISIndicatorResults").Select(strSISIndicatorResult);	
				waitForSync(5);				
				if (blnFlag)
				{
					blnFlag= electronicSignaturePackaging(strUserName);
					waitForSync(5);				
					if (!blnFlag)
						Err.Description = "Unable to select 'UserName'("+strUserName+") and sign the canvas pad.";									
				}
				else
					Err.Description = "Unable to select 'SIS Indicator Result'("+strSISIndicatorResult+").";		
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occurred while selecting the SIS Indicator Result and Initials and signature.";	
			}
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: sendAssemblyLinetoCommonSheet
        //''@Objective: This function validates if the Assembly Line that is fetched from the DB is not null and sends it to the Common Sheet. 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strAssemblyLine - The Assembly Line which needs to be sent to the Common Sheet.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= sendAssemblyLinetoCommonSheet("Line 1")      
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[02-24-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean sendAssemblyLinetoCommonSheet(String strAssemblyLine)
		{
			boolean blnFlag = false;
			
			try
			{	
				strAssemblyLine = strAssemblyLine.trim();
				blnFlag = !strAssemblyLine.equals("");
				// If there is a valid value , send it to Common Sheet	
				if (blnFlag)
				{
					blnFlag=sendValuetoCommonSheet("strAssemblyLine",strAssemblyLine);			
					if(!blnFlag)
						Err.Description = "There was some error while updating the Common Sheet.";
				}
				// If the value is blank also send it to Common Sheet
				else
				{
					blnFlag=sendValuetoCommonSheet("strAssemblyLine",strAssemblyLine);			
					if(blnFlag)
					{
						Err.Description = "The Assembly Line retrieved from DB is blank.";				
						blnFlag = false;
					}
					else
						Err.Description = "There was some error while updating the Common Sheet.The Assembly Line retrieved from DB is blank.";				
				}
					
			}
			catch(Exception NullPointerException)
			{	
				// If the value is null, also send blank to the Common Sheet column
				blnFlag = false;
				blnFlag=sendValuetoCommonSheet("strAssemblyLine","");
				if(!blnFlag)
				{
					Err.Description = "There was some error while updating the Common Sheet.There is no Assembly Line which is free for Packaging.";
				}
				else
				{
					Err.Description = "There is no Assembly Line which is free for Packaging.The Assembly Line retrieved from the DB is null.";
					blnFlag = false;
				}
			}		
			
			return blnFlag;	
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: isValueSelected
        //''@Objective: This Function verifies if the value that is selected matches with the value indicated.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - The Page on which the dropdown exists.
		//''@	Element - The dropdown on which the selected value needs to be verified.
        //''@	strValueSelectedExpected - The expected value that should be selected.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= isValueSelected("pgeEditAutoclaveLog","drpBIColorChange","PASS")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[02-25-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean isValueSelected(String Page, String Element, String strValueSelectedExpected) 
		{
			waitForSync(3);				
			
			boolean blnFlag=false;
			
			blnFlag=Page(Page).Element(Element).IsValueSelected(strValueSelectedExpected);	
			if (!blnFlag)
				Err.Description = "The Expected selected value("+strValueSelectedExpected+") does not match the actual selected value.";				
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickPrintLabels
        //''@Objective: This Function clicks on Print Labels button on 'Edit Autoclave Log' page and verfies some details of the navigated page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strLoadNumber - The Load Number of the log.
		//''@	strEquipmentNumber - The Equipment Number for which the log has been added.
		//''@	strInstrument - The Instrument added in the Log.
		//''@	strPartNumber - The Part Number added in the Log.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickPrintLabels("0035C1", "AUTO-002", "Injectable Rack", "INJ-001")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[26-Feb-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickPrintLabels(String strLoadNumber, String strEquipmentNumber, String strInstrument, String strPartNumber) 
		{
			boolean blnFlag = false;
			waitForSync(2);
			String strExpText=strLoadNumber+","+strEquipmentNumber+","+strInstrument+","+strPartNumber;
			blnFlag = Page("pgeEditAutoclaveLog").Element("btnPrintLabels").Click(20);			
			waitForSync(10);
			if (blnFlag==true)
			{
				blnFlag=verifyPDF(strExpText);
				if(!blnFlag)
					Err.Description=Err.Description;
			}
			else
			{
				Err.Description="Unable to click on 'Print Labels' button.";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickPrintReportAutoclave
        //''@Objective: This Function clicks on Print Report button on 'Edit Autoclave Log' page and verfies some details of the navigated page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 	
		//''@	strExpectedText - The PDF Heading.
        //''@	strLoadNumber - The Load Number of the log.
		//''@	strEquipmentNumber - The Equipment Number for which the log has been added.
		//''@	strInstrument - The Instrument added in the Log.
		//''@	strPartNumber - The Part Number added in the Log.		
		//''@	strLoadTech - The Load Technician added in the Log.		
		//''@	strUnloadTech - The Unload Technician added in the Log.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickPrintReportAutoclave("Autoclave Log","0035C1", "AUTO-002", "Injectable Rack", "INJ-001","Joanna Reynolds","Carol Tutherow")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[26-Feb-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickPrintReportAutoclave(String strExpText,String strLoadNumber, String strEquipmentNumber, String strInstrument, String strPartNumber, String strLoadTech, String strUnloadTech) 
		{
			boolean blnFlag = false;
			waitForSync(2);
			strExpText=strExpText+","+strLoadNumber+","+strEquipmentNumber+","+strInstrument+","+strPartNumber+","+strLoadTech+","+strUnloadTech;
			blnFlag = Page("pgeEditAutoclaveLog").Element("btnPrintReport").Click(20);			
			waitForSync(10);
			if (blnFlag==true)
			{
				blnFlag=verifyPDF(strExpText);
				if(!blnFlag)
					Err.Description=Err.Description;
			}
			else
			{
				Err.Description="Unable to click on 'Print Report' button.";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickPrintReportAutoclave
        //''@Objective: This Function clicks on Print Report button on 'Edit Autoclave Log' page and verfies some details of the navigated page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 	
        //''@	strLoadNumber - The Load Number of the log.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickPrintReportAutoclave("0035C1")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[27-Feb-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickPrintReportAutoclave(String strLoadNumber) 
		{
			boolean blnFlag = false;
			waitForSync(2);
			String strExpText=strLoadNumber;
			blnFlag = Page("pgeEditAutoclaveLog").Element("btnPrintReport").Click(20);			
			waitForSync(10);
			if (blnFlag==true)
			{
				blnFlag=verifyPDF(strExpText);
				if(!blnFlag)
					Err.Description=Err.Description;
			}
			else
			{
				Err.Description="Unable to click on 'Print Report' button.";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickPrint
        //''@Objective: This Function clicks on Print button on 'Autoclave Log' page and verfies some details of the navigated page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 	
		//''@	Page - The Page on which the Element exists.
		//''@	Element - The Element which needs to be clicked.
		//''@	strExpectedText - The PDF Heading.
        //''@	strLoadNumber - The Load Number of the log.
		//''@	strEquipmentNumber - The Equipment Number for which the log has been added.		
		//''@	strLoadTech - The Load Technician added in the Log.				
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickPrint("pgeAutoclaveLog","lnkPrintReport_FirstRow","Autoclave Log","0035C1", "AUTO-002","Joanna Reynolds")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[26-Feb-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickPrint(String Page, String Element, String strExpText,String strLoadNumber, String strEquipmentNumber, String strLoadTech) 
		{
			boolean blnFlag = false;
			waitForSync(2);
			strExpText=strExpText+","+strLoadNumber+","+strEquipmentNumber+","+strLoadTech;
			blnFlag = Page(Page).Element(Element).Click(20);			
			waitForSync(10);
			if (blnFlag==true)
			{
				blnFlag=verifyPDF(strExpText);
				if(!blnFlag)
					Err.Description=Err.Description;
			}
			else
			{
				Err.Description="Unable to click on 'Print' button.";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: retrieveValueFromField
        //''@Objective: This Function stores the text of the Page->Element in a dictionary  for use in the script.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	PageName - The Page name which maps to the Element mentioned.
		//''@	Element - The Element which whose text needs to be retrieved.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= retrieveValueFromField("pgeTissueUtilization","objDistributor")         
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[02-26-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean retrieveValueFromField(String PageName, String Element)  
		{
			boolean blnFlag = false;
			try
			{
				String strTexttoVerify = Page(PageName).Element(Element).GetText();
				blnFlag = !strTexttoVerify.isEmpty();
				if (blnFlag)
				{
					Script.dicTestData.put("strValueRetrievedfromField", strTexttoVerify);
				}
				else
					Err.Description = "The text value of the field is empty.";
			}
			catch (Exception e)
			{
				Err.Description = e.getMessage();
				blnFlag = false;
			}
						
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clearFieldandWait
        //''@Objective: This Function clears the field mentioned and waits for Page Refresh.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	PageName - The Page Name where the search box is present.
		//''@	ElementName - The Object name where the search text needs to be entered.	
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clearFieldandWait(PageName, ElementName)        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[27-Feb-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clearFieldandWait(String Page, String Element) 
		{
			boolean blnFlag = false;			
			try
			{
				//Page(Page).Element(Element).Type("");
				String tmpXpath= Page(Page).Element(Element).GetXpath();
				driver.findElement(By.xpath(tmpXpath)).sendKeys(Keys.BACK_SPACE);
				driver.findElement(By.xpath(tmpXpath)).sendKeys(Keys.chord(Keys.CONTROL, "a"));
				driver.findElement(By.xpath(tmpXpath)).sendKeys(Keys.DELETE);
				//driver.findElement(By.xpath(tmpXpath)).sendKeys(Keys.TAB);
				final String tempSource = driver.getPageSource();
				(new WebDriverWait(driver, 120)).until(new ExpectedCondition<Boolean>() 
				{
					public Boolean apply(WebDriver driver) 
					{
						boolean flag = false;
						if (!tempSource.equals(driver.getPageSource())) 
						{
							flag = true;
						}
						return flag;
					}
				});
					blnFlag=true;
			}
			catch (Exception e)
			{
				if (e instanceof TimeoutException) 
				{
					e.printStackTrace();
					blnFlag = true;
					//Err.Description = "Unable to copy into clipboard.";
				}
				else
					Err.Description="There was an error while executing the script.";
			}
			
		return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickWaitforPageRefresh
        //''@Objective: This Function clicks on Object passed and waits for the page to refresh.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - The Page on which the sorting object exists.
		//''@	Element - The sorting element which needs to be clicked.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickWaitforPageRefresh("pgeReimbursementRates", "objRecoveredDateSort")
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[2-27-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean clickWaitforPageRefresh(String Page, String Element) 
		{
			boolean blnFlag = false;
			
			// Click on the sort element
			blnFlag = Page(Page).Element(Element).Click(20);
			if(blnFlag)
			{
				try
				{
					waitForPageRefresh();
				}
				catch(Exception e)
				{
					blnFlag = false;
					Err.Description="Page refresh after clicking the Page->'"+Page+"' Element->'"+Element+"' was not successful.";	
				}			
			}			
			else
				Err.Description="Unable to click on the object Page->'"+Page+"' Element->'"+Element+"'.";	
				
			Page(Page).Element(Element).MouseHover();
			
			return blnFlag;
		}	
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: selectDataItemValueTextBox
        //''@Objective: This Function selects the specified value from the specified hidden auto-complete text box, by first clicking on the object.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	Page - The Page on which the auto-complete text box resides.
		//''@	ElementtoClick - The auto-complete object from where the value needs to be selected. This needs to be clicked before the textt box shows up
		//''@	Element - The auto-complete text box from where the value needs to be selected.
		//''@	strSelectionValue - The value of the that needs to be selected from the specified hidden auto-complete text box.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= selectDataItemValueTextBox("pgeAddAutoclaveLog","objLoadTechnician","txtLoadTechnician","Joanna Reynolds")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[03-03-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean selectDataItemValueTextBox(String Page, String ElementtoClick, String Element, String strSelectionValue)
		{
			boolean blnFlag = false;
			try
			{
				blnFlag = Page(Page).Element(ElementtoClick).Click(20);
				if(blnFlag)
				{
					blnFlag = selectDataItemValueTextBox(Page,Element,strSelectionValue);
					if(!blnFlag)
						Err.Description = "Unable to select the option '" +strSelectionValue+ "' from the auto-complete text box.";	
				}
				else
					Err.Description = "Unable to click on the Element mentioned Page->'" +Page+ "'; Element->  '" +ElementtoClick+ "'.";	
							
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Unable to select the option '" +strSelectionValue+ "' from the auto-complete text box.";					
			}
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: enterValueandPressEnter
        //''@Objective: This Function selects the specified value from the specified hidden auto-complete text box, by first clicking on the object.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	Page - The Page on which the element resides.		
		//''@	Element - The text box into which the value needs to be entered.
		//''@	strValuetoEnter - The value of that needs to be enterred in the Page->Element.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= enterValueandPressEnter("pgeAmnionChorionScan","txtRefrigerator","RFI-015R")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[03-11-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean enterValueandPressEnter(String Page, String Element, String strValuetoEnter)
		{
			boolean blnFlag = false;
			try
			{
				blnFlag = Page(Page).Element(Element).Type(strValuetoEnter);
				if(blnFlag)
				{
					String tmpXpath= Page(Page).Element(Element).GetXpath();
					//Simulate Enter key press
					driver.findElement(By.xpath(tmpXpath)).sendKeys(Keys.RETURN);
					waitForPageRefresh();
					blnFlag = true;		
				}
				else
					Err.Description = "Unable to enter value  '" +strValuetoEnter+ "' in Page->'" +Page+ "'; Element->  '" +Element+ "'.";	
							
			}
			catch(Exception e)
			{
				Err.Description = "Unable to press enter value '" +strValuetoEnter+ "' in the element '"+Element+"' and press Enter.";					
			}
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: enterDonorDetailAmnionChorion
        //''@Objective: This Function enters the amnion and chorion detail based on the Donor ID on the Amnion/Chorion Scan page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strDonorID - The Donor ID which needs to be entered.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= enterDonorDetailAmnionChorion("20133185")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[03-11-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean enterDonorDetailAmnionChorion(String strDonorID)
		{
			boolean blnFlag = false;
			try
			{
				String tmpXpath = Page("pgeAmnionChorionScan").Element("txtScanArea").GetXpath();
				//Enter Amnion detail			
				driver.findElement(By.xpath(tmpXpath)).sendKeys("a-"+strDonorID);
				// Press Enter
				driver.findElement(By.xpath(tmpXpath)).sendKeys(Keys.RETURN);
				//Enter Chorion detail		
				driver.findElement(By.xpath(tmpXpath)).sendKeys("c-"+strDonorID);
				
				blnFlag = true;									
			}
			catch(Exception e)
			{
				Err.Description = "Error occurred while entering Amnion and Chorion detail in the scan area.";					
			}
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: prerequisiteReviewDashboard
        //''@Objective: This Function deletes all the existing entries in all the four tabs of the Review Dashboard as a pre-requisite for further testing.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 			
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= prerequisiteReviewDashboard()        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[03-12-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean prerequisiteReviewDashboard()
		{
			boolean blnFlag = false;
			
			try
			{
				// Delete all entries in Injectable Spec tab
				blnFlag = deleteAllEntriesReviewDashboard("InjectableSpecifications");
				if(blnFlag)
				{
					blnFlag = Page("pgeReviewDashboard").Element("lnkProductSpecifications").Click(20);					
					if(blnFlag)
					{
						blnFlag = deleteAllEntriesReviewDashboard("ProductSpecifications");
						if(blnFlag)
						{
							blnFlag = Page("pgeReviewDashboard").Element("lnkPackagePrefixes").Click(20);	
							if(blnFlag)
							{
								blnFlag = deleteAllEntriesReviewDashboard("PackagePrefixes");
								if(blnFlag)
								{
									blnFlag = Page("pgeReviewDashboard").Element("lnkSterilizationVendorPrefixes").Click(20);	
									if(blnFlag)
									{
										blnFlag = deleteAllEntriesReviewDashboard("SterilizationVendorPrefixes");
										if(!blnFlag)
											Err.Description = Err.Description;	
									}
									else
										Err.Description = "The Sterilization Vendor Prefixes Tab is not clicked successfully.";
								}
								else
									Err.Description = Err.Description;	
							}
							else
								Err.Description = "The Package Prefixes Tab is not clicked successfully.";
						}
						else
							Err.Description = Err.Description;	
					}
					else
						Err.Description = "The Product Specification Tab is not clicked successfully.";
				}
				else
					Err.Description = Err.Description;		
				
				blnFlag = true;									
			}
			catch(Exception e)
			{
				Err.Description = "Error occurred while deleting all the existing entries in the Review Dashboard.";					
			}
			
			return blnFlag;
			
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: deleteAllEntriesReviewDashboard
        //''@Objective: This Function deletes all the existing entries in all the four tabs of the Review Dashboard as a pre-requisite for further testing.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 			
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= deleteAllEntriesReviewDashboard("InjectableSpecifications")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[03-12-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean deleteAllEntriesReviewDashboard(String strTabName)
		{
			boolean blnFlag = false;
			String strTabKeyword= "";
			String strXpath = "";
			try
			{
				
				// Based on the Tab name, create the XPath
				if (strTabName.equalsIgnoreCase("InjectableSpecifications"))
				{
					strTabKeyword = "tblinjectablelist";
					blnFlag = true;
				}
				else if(strTabName.equalsIgnoreCase("ProductSpecifications"))
				{
					strTabKeyword = "tblproductlist";
					blnFlag = true;
				}
				else if(strTabName.equalsIgnoreCase("PackagePrefixes"))
				{
					strTabKeyword = "tblpackagelist";
					blnFlag = true;
				}
				else if(strTabName.equalsIgnoreCase("SterilizationVendorPrefixes"))
				{
					strTabKeyword = "tblvendorlist";
					blnFlag = true;
				}
				else
				{
					blnFlag = false;
					Err.Description = "Pass valid paramaters to the function 'deleteAllEntriesReviewDashboard'.";	
				}
					
				if (blnFlag)
				{
				
				// Get the count of the number of Delete links on that page
				 java.util.List<WebElement> elements = driver.findElements(By.xpath("//a[text()='Delete']"));    
					int intNumberofElements = elements.size();
					if (intNumberofElements>0)
					{
						for (int i = 0;i<intNumberofElements; i++)
						{
							strXpath = "//a[@id='centerform:tabview:"+strTabKeyword+":0:lnkdelete']";
							driver.findElement(By.xpath(strXpath)).click();
							waitForPageRefresh();
						}
					}
				}						
													
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occurred while clicking on the Delete links.";					
			}
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyColor
        //''@Objective: This Function verifies the background color of the Element passed.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 	
		//''@	Page - The Page on which the Element resides.				
		//''@	Element - The Element whose style color needs to be validated.				
		//''@	strColor - The color expected.				
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyColor("pgeReviewDashboard","objInjectableSpecLastUpdated","yellow")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[03-12-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean verifyColor(String Page,String Element, String strColor)
		{
			boolean blnFlag = false;
			String strHexColor = "";
			try
			{
				
				// Based on the Tab name, create the XPath
				if (strColor.equalsIgnoreCase("yellow"))
				{
					strHexColor = "#FFFFD0";					
					blnFlag = true;
				}
				else if(strColor.equalsIgnoreCase("red"))
				{
					strHexColor = "#FC1501";
					blnFlag = true;
				}
				else if(strColor.equalsIgnoreCase("green"))
				{
					strHexColor = "#d0ffd0";
					blnFlag = true;
				}				
				else
				{
					blnFlag = false;
					Err.Description = "Pass valid paramaters to the function 'verifyColor'.";	
				}
					
				if (blnFlag)
				{
					// Get the color of the element
					String tmpXpath= Page(Page).Element(Element).GetXpath();
					String style=driver.findElement(By.xpath(tmpXpath)).getAttribute("style");
					style = style.substring((style.indexOf("(")+1),style.indexOf(")"));
					String[] strArr = style.split(", ");
					int r = Integer.parseInt(strArr[0]);		
					int g = Integer.parseInt(strArr[1]);		
					int b = Integer.parseInt(strArr[2]);	
					style = String.format("#%02x%02x%02x", r, g, b);
										
					if(!style.contains(strHexColor))
						Err.Description = "The color expected '"+strColor+"' does not match the actual color of the Object.";							
				}						
													
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occurred while verifying color of the element.";					
			}
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyTableReviewDashboard
        //''@Objective: This Function verifies the table has been updated with the correct values, and the background color expected.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 	
		//''@	Page - The Page on which the Element resides.						
		//''@	Element1 - The Last updated Date Element which needs to be verified.						
		//''@	Element2 - The 'Last updated By' Element which needs to be verified.						
		//''@	strColor - The expected style color of the object.						
        //''@---------------------------------------------------------------------------------------------------------------------------        //''@Example:blnStatus=verifyTableReviewDashboard("pgeReviewDashboard","objInjectableSpecLastUpdated","objInjectableSpecLastUpdatedBy","yellow")      
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[03-12-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean verifyTableReviewDashboard(String Page,String Element1, String Element2, String strColor)
		{
			boolean blnFlag = false;
			
			try
			{
				//Verify if current date is updated in the first element
				blnFlag = getDateInterval("MMM dd, yyyy","0");
				if(blnFlag)
				{
					String strCalculatedDate = Script.dicTestData.get("strCalculatedDate");	
					String stractualtext1 = Page(Page).Element(Element1).GetText();
					blnFlag = stractualtext1.contains(strCalculatedDate);
					if(blnFlag)
					{
						// Verify if the background color of this field is the color mentioned
						blnFlag = verifyColor(Page,Element1,strColor);
						if(blnFlag)
						{
							// Verify if Updated By value is the Logged in User
							String stractualtext2 = Page(Page).Element(Element2).GetText();
							String strUpdatedBy = Page("pgeReviewDashboard").Element("lnkLoggedInUser").GetText();
							blnFlag = stractualtext2.contains(strUpdatedBy);
							if(blnFlag)
							{
								// Verify if the background color of this field is the color mentioned
								blnFlag = verifyColor(Page,Element2,strColor); 
								if(!blnFlag)
									Err.Description = "The Object "+Element1+" color is not "+strColor+".";	
							}
							else								
								Err.Description = "The Logged in User ("+strUpdatedBy+") is not updated on the table in '"+stractualtext2+"'.";	
						}
						else
							Err.Description = "The Object "+Element1+" color is not "+strColor+".";	
					}
					else
						Err.Description = "The Current date ("+strCalculatedDate+") is not updated on the table in '"+stractualtext1+"'.";	
					
				}
				else
					Err.Description = "Error occurred while retrieving current date.";	
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occurred while verifying the table.";					
			}
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifySelectedTab
        //''@Objective: This Function verifies that the given tab is selected or not on the given page/landing-page
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	Page - The Page on which the tab is present
		//''@	tabElement - Tab that needs to be verified whether it is selected or not. The hyperlink inside the tab (li element)
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = verifySelectedTab("pgeReviewDashboard", "lnkInjectableSpecifications")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[11-03-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean verifySelectedTab(String Page, String tabElement)
		{
			boolean blnFlag = false;
			try
			{
				String expandedStatus = driver.findElement(By.xpath(Page(Page).Element(tabElement).GetXpath() + "/..")).getAttribute("aria-expanded");
				
				if(expandedStatus.equalsIgnoreCase("true"))
				{
					blnFlag = true;	
				}
				else
					blnFlag = false;							
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Unable to find the tab '" + Page + "' on the page '" + tabElement + "'.";					
			}
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillReleaseScanEntries
        //''@Objective: This Function fills entries on 'Release Scan' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - Page for release scan
		//''@	eleRefrigerator - Element for refrigerator input field
		//''@	eleScanList - Element for Recovery Key input text area
		//''@	btnProcessScan - Element for Process Scan button
		//''@	strRefrigerator - Refrigerator name to be filled.
		//''@	strRecoveryId - Recovery Id to be filled.
		//''@	strScannedMessage - Expected message to be verified on screen.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = fillReleaseScanEntries("pgeReleaseScan", "txtRefrigerator", "txtScanList", "btnProcessScans", "RFI-015R", "WK15B011", 
		//''@                                            strScannedMessage);       
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[03-10-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
  		public boolean fillReleaseScanEntries(String Page, String eleRefrigerator, String eleScanList, String btnProcessScan,String strRefrigerator, String strRecoveryId, String strScannedMessage)
		{
			boolean blnFlag = false;
			try
			{
				blnFlag = Page(Page).Element(eleRefrigerator).Type(strRefrigerator);
				waitForSync(2);
				driver.switchTo().activeElement().sendKeys(Keys.TAB);
				waitForPageRefresh();

				if (blnFlag)
				{
					blnFlag=Page(Page).Element(eleScanList).Type(strRecoveryId);
					if (blnFlag)
					{								
						blnFlag=clickAndVerifyConfirmationMessage(Page, btnProcessScan,"Process Scans",strScannedMessage,"strRecoveryId",strRecoveryId);
						if (!blnFlag)
							Err.Description=Err.Description;
					}
					else
						Err.Description="Unable to enter recovery id in scan area.";
				}
				else
						Err.Description="Unable to enter refrigerator name.";
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description="Error while filling the release scan entries.";
			}			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickAndVerifyConfirmationMessage
        //''@Objective: This Function verifies that the expected text is present in the object or not.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - Page name on which element to be clicked.
		//''@	Element - Page Element to be clicked.
		//''@	btnAction - Button name on which click action to be performed.
		//''@	strExpText - The expected text to be validated , if it is present in the Element.
		//''@	strReplaceString - Replacebale string.
		//''@	strRecoveryId - Recovery Id to be validated , if it is present in the Element.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickAndVerifyConfirmationMessage("pgeReleaseScan","btnProcessScans","Process Scans","Recovery Key CL15B023 processed //''@successfully.","'strRecoveryId'",PF15B011")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[03-10-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
 		public boolean clickAndVerifyConfirmationMessage(String Page, String Element, String btnAction, String strExpText, String strReplaceString, String strRecoveryId)
		{
			boolean blnFlag = false;
			try 
			{
				strExpText=strExpText.replace("'"+strReplaceString+"'", strRecoveryId);
				
				blnFlag = Page(Page).Element(Element).Click(20);
				waitForSync(2);

				if (blnFlag == true)
				{
					blnFlag = verifyTextContains(Page, "objConfMessage", strExpText);					
					if (!blnFlag)
						Err.Description=Err.Description;	
				}
				else
					Err.Description="Unable to click '"+btnAction+"' button.";
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description="Error while verifying the confirmation message.";
			}	

			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyRefrigeratorDate
        //''@Objective: This Function verifies the RefrigeratorDate.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - Page name on which element to be clicked.
		//''@	Element - Page Element to be clicked.
		//''@	strDateFormat - The expected text to be validated , if it is present in the Element.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyRefrigeratorDate("pgeRecovered_Tissue", "objRefrigerationDate", "MMM DD,YYYY")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[03-13-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
 		public boolean verifyRefrigeratorDate(String Page, String Element, String strDateFormat)
		{
			boolean blnFlag = false;
			String strExpText="";
			try
			{
				blnFlag = getDateInterval(strDateFormat,"0");
				waitForSync(2);
				strExpText=Script.dicTestData.get("strCalculatedDate");
				
				if (blnFlag == true)
				{
					blnFlag = verifyTextContains(Page, Element, strExpText);					
					if (!blnFlag)
						Err.Description=Err.Description;	
				}
				else
					Err.Description=Err.Description;
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description="Error while validating the refrigerator date.";
			}	

			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickVerifyCheckboxVendor
        //''@Objective: This function clicks or verifies the vendor checkbox that needs to be clicked or verified on 'Edit Tissue Code' page. 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters:                 		
		//''@	strVendor - The Vendor whose checkbox needs to be clicked or verified.
		//''@	strActiontoPerform - The action that is to be performed on the checkboxes (Can be either 'Click' or to 'Verify' click).
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickVerifyCheckboxVendor("Sterigenics (Main)","Click")        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[03-19-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean clickVerifyCheckboxVendor(String strVendor, String strActiontoPerform)
		{
			boolean blnFlag = false;
			String ElementName = "";
			
			if (strVendor.contains("Sterigenics (Main)"))
			{
				ElementName = "chkSterigenicsMain";		
				blnFlag = true;				
			}
			else if (strVendor.contains("Synergy Health (Main)"))
			{
				ElementName = "chkSynergyHealthMain";		
				blnFlag = true;
			}
			else if (strVendor.contains("Sterigenics Charlotte"))
			{
				ElementName = "chkSterigenicsCharlotte";		
				blnFlag = true;
			}
			else if (strVendor.contains("Synergy Health Lima"))
			{
				ElementName = "chkSynergyHealthLima";		
				blnFlag = true;
			}			
			else 
			{
				blnFlag = false;
				Err.Description = "Pass valid paramaters to the function 'clickVerifyCheckboxVendor'.";
			}
			
			if(blnFlag)
			{					
				if (strActiontoPerform.equalsIgnoreCase("Click"))
				{				
					//Click on the corresponding checkbox	
					blnFlag =  Page("pgeAddEditTissueCode").Element(ElementName).Click(20);		
					if(!blnFlag)	
						Err.Description = "The checkbox for '"+strVendor+"' is not clicked.";
				}
				else
				{				
					//Verify if the checkbox is clicked						
					blnFlag =  Page("pgeAddEditTissueCode").Element(ElementName).isSelected();									
				}
				if(!blnFlag)	
						Err.Description = "The checkbox for '"+strVendor+"' is not selected.";;	
			}
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillAddTissueCode
        //''@Objective: This Function fills the 'Add Tissue Code' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strNewTissueCode - The New calculated Tissue Code to be entered in 'Add Tissue' page.
		//''@	strActive - The Active dropdown value to be selected in 'Add Tissue' page.
		//''@	strForDevelopment - The For Development dropdown value to be selected in 'Add Tissue' page.
		//''@	strUOM - The UOM dropdown value to be selected in 'Add Tissue' page.
        //''@	strX - The X value to be entered in 'Add Tissue' page.
		//''@	strY - The Y value to be entered in 'Add Tissue' page.
		//''@	strZ - The Z value to be entered in 'Add Tissue' page.
		//''@	strSpecification - The Specification value to be entered in 'Add Tissue' page.
		//''@	strArchiveSamples - The Archive Sample value to be entered in 'Add Tissue' page.
		//''@	strResidualMoistureSamples - The Residual Moisture Sample value to be entered in 'Add Tissue' page.
		//''@	strCustomLabel - The Custom Label value to be entered in 'Add Tissue' page.
		//''@	strMaxValue - The Max value to be entered in 'Add Tissue' page
		//''@	strMinValue - The Min value to be entered in 'Add Tissue' page
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fillAddTissueCode("Test Tissue Code", "YES","NO", "cm", "1.5", "2","1","Specification for QA Testing Purposes","1","1","SB116-001","0.022","0.018")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[19-Mar-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean fillAddTissueCode(String strNewTissueCode,String strActive, String strForDevelopment, String strUOM, String strX, String strY, String strZ, String strSpecification, String strArchiveSamples, String strResidualMoistureSamples, String strCustomLabel, String strMaxValue, String strMinValue)
		{
			boolean blnFlag = false;			
			
			blnFlag=Page("pgeAddEditTissueCode").Element("txtTissueCode").Type(strNewTissueCode);			
			if (blnFlag) 
			{
				blnFlag=Page("pgeAddEditTissueCode").Element("lstActive").Select(strActive);				
				waitForSync(2);					
				if (blnFlag) 
				{
					blnFlag=Page("pgeAddEditTissueCode").Element("lstForDev").Select(strForDevelopment);	
					waitForSync(2);					
					if (blnFlag) 
					{
						blnFlag = Page("pgeAddEditTissueCode").Element("lstUOM").Select(strUOM);						
						waitForSync(2);					
						if (blnFlag) 
						{
							blnFlag = Page("pgeAddEditTissueCode").Element("txtX").Type(strX);						
							if (blnFlag) 
							{
								blnFlag = Page("pgeAddEditTissueCode").Element("txtY").Type(strY);															
								if (blnFlag) 
								{
									blnFlag = Page("pgeAddEditTissueCode").Element("txtZ").Type(strZ);									
									if (blnFlag) 
									{
										blnFlag = Page("pgeAddEditTissueCode").Element("txtSpecification").Type(strSpecification);	
										if (blnFlag) 
										{
											blnFlag = Page("pgeAddEditTissueCode").Element("txtArchiveSamples").Type(strArchiveSamples);
											if (blnFlag)
											{
												blnFlag = Page("pgeAddEditTissueCode").Element("txtResidualMoistureSamples").Type(strResidualMoistureSamples);
												if (blnFlag)
												{
													blnFlag = Page("pgeAddEditTissueCode").Element("txtCustomLabel").Type(strCustomLabel);
													if (blnFlag)
													{
														blnFlag=Page("pgeAddEditTissueCode").Element("txtMaxValue").Type(strMaxValue);					
														if (blnFlag)
														{
															blnFlag=Page("pgeAddEditTissueCode").Element("txtMinValue").Type(strMinValue);
															if(!blnFlag)
																Err.Description = "Unable to enter Min Value.";							
														}
														else 
															Err.Description = "Unable to enter Max Value.";
													}
													else
														Err.Description = "Unable to enter Custom Label.";
												}
												else
													Err.Description = "Unable to enter Residual Moisture sample.";
											}
											else					
												Err.Description = "Unable to enter Archive sample.";
										}
										else 
											Err.Description = "Unable to enter Specification.";
									} 
									else
										Err.Description = "Unable to enter Z value.";
								} 
								else
									Err.Description = "Unable to enter Y value.";
							} 
							else
								Err.Description = "Unable to enter X value.";
						} 
						else
							Err.Description = "Unable to select 'UOM' drop down.";				
					}
					else
						Err.Description = "Unable to select 'For Development' drop down.";			
				}
				else
					Err.Description = "Unable to select 'Active' dropdown.";	
			}
			else
				Err.Description = "Unable to enter Tissue Code.";
				
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyElementExist
        //''@Objective: This Function verifies element exist on the page or not.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - Page name on which element to be clicked.
		//''@	Element - Page Element to be clicked.    
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyElementExist("pgeRefrigerators","lnkShowContents")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[03-17-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean verifyElementExist(String Page, String Element) 
		{
			boolean blnFlag = false;
			String tempXpath=Page(Page).Element(Element).GetXpath();
			try
			{
				if (!(tempXpath.startsWith("/")|| tempXpath.startsWith("//"))) 
				{
					Element="//*[text()='"+Element+"']";
				}
				
				blnFlag = Page(Page).Element(Element).Exist();
				if (!blnFlag) 
					Err.Description = "Object not found in the page.";
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description="Error while validating the existance of the element.";
			}	
	
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyElementExist
        //''@Objective: This Function verifies non existance of the element on the page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - Page name on which element to be clicked.
		//''@	Element - Page Element to be clicked.    
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyElementExist("pgeRefrigerators","lnkShowContents")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[03-17-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean verifyElementNotExist(String Page, String Element) 
		{
			boolean blnFlag = false;
			String tempXpath=Page(Page).Element(Element).GetXpath();
			try
			{
				if (!(tempXpath.startsWith("/")|| tempXpath.startsWith("//"))) 
				{
					Element="//*[text()='"+Element+"']";
				}
				
				blnFlag = !(Page(Page).Element(Element).Exist());
				if (!blnFlag) 
					Err.Description = "Object still present on the page.";
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description="Error while validating the non existance of the element.";
			}	

			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickStartTransitLink
        //''@Objective: This function clicks on Start Transit link and verify navigated page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - Page name on which element to be clicked.
		//''@	strRecoveryId - Recovery Id.
		//''@	strPageTitle - Page title for navigated page. 		
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickStartTransitLink("pgeRefrigerators","AL14K003","RFI-015R")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[03-17-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean clickStartTransitLink(String Page, String strRecoveryId, String strPageTitle) 
		{
			boolean blnFlag = false;
			String tempXpath="//*[text()='"+strRecoveryId+"']/../../td[last()]/a";
			try
			{
				blnFlag=clickAndVerify("pgeRefrigerators",tempXpath, strPageTitle);
				if (!blnFlag) 
					Err.Description = Err.Description;
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description="Error while clicking on 'Start Transit' link.";
			}	

			return blnFlag;
		}			
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: validateLineEntryAndStatus
        //''@Objective: This function validates entries on Refrigerators page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - Page name on which element to be clicked.
		//''@	strRecoveryId - Recovery Id.
		//''@	strPageTitle - Page title for navigated page. 		
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= validateLineEntryAndStatus("pgeRefrigerators","AL14K003","RFI-015R")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[03-17-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean validateLineEntryAndStatus(String Page, String strRefrigerator, String strStatus) 
		{
			boolean blnFlag = false;
			
			try
			{
				blnFlag=verifyElementExist(Page,strRefrigerator);
				if (blnFlag)
				{
					blnFlag=verifyElementExist(Page,strStatus);
					if (!blnFlag) 
						Err.Description = Err.Description;
				}				
				else
					Err.Description = Err.Description;
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description="Error while validating the refrigerator line entry or status.";
			}	

			return blnFlag;
		}	
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickPDFReportLink
        //''@Objective: A generic function to click on PDF link and validate pdf content of the new tab.   
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - Page on which action to be performed.
		//''@	Element - Element on which action to be performed.
		//''@	strLink - Link name on which click action to be performed.
		//''@	strExpText - The expected text to validate in pdf.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickPDFReportLink("Recovered Tissue","lnkPrintRecovery","Print Recovery Form","Incoming Tissue Inspection Form")       
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[03-18-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickPDFReportLink(String Page, String Element, String strLink, String strExpText)
		{
			boolean blnFlag = false;
			try
			{
				blnFlag = Page(Page).Element(Element).Click(20);
				waitForSync(10);
				
				if (blnFlag==true)
				{
					blnFlag=verifyPDF(strExpText);
					if(!blnFlag)
						Err.Description=Err.Description;
				}
				else
					Err.Description="Unable to click on '"+strLink+"' link";
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description="Error while validating the pdf contents.";
			}

			return blnFlag;
		}

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: validateDonorHistoryEntries
        //''@Objective: This function validates entries on 'Donor History' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - Page name on which element to be clicked.
		//''@	eleEvent - Object name for getting text runtime to validate with expected text.
		//''@	eleDetails - Object name for getting text runtime to validate with expected text.
		//''@	strExpEvent - Expected text to validate.
		//''@	strExpDetails - Expected text to validate. 	
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= validateLineEntryAndStatus("pgeNewRecoveredTissue","objEvent","objDetails-015R",
		//''@"Recovered Tissue In Transit Check Out","Recovery Key: WP15B028 in transit check out from refrigerator RFI-015R")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[03-18-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean validateDonorHistoryEntries(String Page, String eleEvent, String eleDetails, String strExpEvent, String strRecoveryId, String strRefrigerator) 
		{
			boolean blnFlag = false;
			String strExpDetails="Recovery Key: "+strRecoveryId+" in transit check out from refrigerator "+strRefrigerator;
			
			try
			{
				blnFlag=verifyTextContains(Page,eleEvent, strExpEvent);
				if (blnFlag)
				{
					blnFlag=verifyTextContains(Page,eleDetails,strExpDetails);
					if (blnFlag) 
						Script.dicTestData.put("ExpectedText",strExpEvent+","+strExpDetails);
					else	
						Err.Description = Err.Description;
				}				
				else
					Err.Description = Err.Description;
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description="Error while validating entries on 'Donor History' page.";
			}	
			
			return blnFlag;
		}	

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: processScans
        //''@Objective: This Function enters the given Recovey Key, clicks 'Process Scans' button on the 'Start Transit' page of the
        //''@           'Refrigertaor' section and returns scan success status
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	recoveryKey - Recovery ID to be scanned
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = processScans("WK15C012")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[03-16-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean processScans(String recoveryKey)
		{
			boolean blnFlag = false;
			String strExpText="Recovery Key: " + recoveryKey + " scanned successfully.";
			try
			{
				blnFlag = Page("pgeRefrigerators").Element("txtscanList").Type(recoveryKey);
				
				if(blnFlag)
				{
					blnFlag = clickAndVerify("pgeRefrigerators","btnProcessScans", "Recovered Tissue");
					
					if(blnFlag)
					{
						blnFlag = verifyTextContains("pgeStartTransitRecoveredTissue","objScanSuccessfulMessage", strExpText);						
						if(!blnFlag)
							Err.Description = "Unable to find the 'Recovery Key: " + recoveryKey + " scanned successfully.' message.";	
					}
					else
						Err.Description = "Unable to find the 'Process Scans' button or not able to click it.";

				}
				else
					Err.Description = "Unable to find the textarea on the page or not able to type Recovery Key in the textarea.";
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description="Error while processing scans.";
			}	

			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: saveEndTransitRecoveredTissue
        //''@Objective: This Function fills the form and clicks Save button for End transit of recovered tissue.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= saveEndTransitRecoveredTissue("smoketest", "India@123")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[19-Mar-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean saveEndTransitRecoveredTissue()
		{
			boolean blnFlag = false;
			waitForSync(2);
			try
			{
				blnFlag = Page("pgeNewRecoveredTissue").Element("drpShippingContainer").Select("Yes");
				waitForSync(4);
			
				if (blnFlag == true) 
				{
					blnFlag = Page("pgeNewRecoveredTissue").Element("drpCoolent").Select("Yes");
					waitForSync(4);
					
					if (blnFlag == true) 
					{
						blnFlag = Page("pgeNewRecoveredTissue").Element("drpPaperwork").Select("Yes");
						waitForSync(4);
						
						if (blnFlag == true) 
						{
							blnFlag = Page("pgeNewRecoveredTissue").Element("drpTissueContainer").Select("Yes");
							waitForSync(4);
							
							if (blnFlag == true) 
							{
								blnFlag = Page("pgeNewRecoveredTissue").Element("drpTissueAccepted").Select("Yes");
								waitForSync(4);
								if (blnFlag == true)
								{
									blnFlag = canvasSignature(driver);
									if (blnFlag == true)
									{
										blnFlag=Page("pgeNewRecoveredTissue").Element("btnSave").Click(20);
										waitForSync(1);
												
										if (blnFlag == true)
										{
											blnFlag=!(Page("pgeNewRecoveredTissue").Element("objAddTissueError").Exist());
											if(!blnFlag)
												Err.Description = "Error exists while filling the details on 'Add Recovered Tissue' page.";					
										}
										else 
											Err.Description = "Unable to click 'Save' button.";
									}
									else
										Err.Description = "Unable to sign in canvas signature.";
								}
								else 
									Err.Description = "Unable to select 'Tissue Accepted?' drop down.";
							} 
							else
								Err.Description = "Unable to select 'tissue container?' drop down.";
						} 
						else
							Err.Description = "Unable to select 'paperwork present?' drop down.";
					} 
					else
						Err.Description = "Unable to select 'contents cool?' drop down.";
				} 
				else
					Err.Description = "Unable to select 'sealed and intact?' drop down.";				
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description="Error while save and end transit recovered tissue.";
			}	
			return blnFlag;
		}					
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifySearchResults
        //''@Objective: This function enters the search string in the Field Tracking page and verifies search results    
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - Page which contains the search box
		//''@	strSearchBox - Name of the column to search value 
		//''@   strSearchText - Text to be typed in the search input field
		//''@	eleExpectedObject - Object name expected after search to get the runtime text to validate with expected text
		//''@	strExpectedText - Expected text of the expected object after search
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = verifySearchNoResults("pgeFieldTracking", "Display Tab", "Search String", "objNoRecordsFound", "No records found");     
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[03-26-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean verifySearchResults(String Page, String strSearchBox, String strSearchText, String eleExpectedObject, String strExpectedText)
		{
			boolean blnFlag = false;
			String strTempXpath="";
			strSearchText = strSearchText.trim();
			String strXpath = Page(Page).Element(strSearchBox).GetXpath();
			if(strXpath.isEmpty())
			{
				strTempXpath = "//th[@role='columnheader']/span[text()='" + strSearchBox + "']/following::input";
			}
			else 
				strTempXpath=strXpath;
				
			try
			{
				blnFlag = Page(Page).Element(strTempXpath).Type(strSearchText); 
				// driver.findElement(By.xpath(strTempXpath)).sendKeys(Keys.TAB);
				waitForPageRefresh();
				if(blnFlag)
				{
					waitForSync(2);	
					blnFlag = verifyTextContains("pgeCommon", eleExpectedObject, strExpectedText);
					if(!blnFlag)
						Err.Description = "Expected text not displayed in the page";
				}
				else
					Err.Description = Err.Description; // "Failed to type text in the search box.";				
			}
			catch (Exception e)
			{
				blnFlag = false;	
				Err.Description = "Error encountered while entering text in search box.";				
			}
			return blnFlag;
		}			
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clearSearchField
        //''@Objective: This Function clears the mentioned search field in the Field Tracking page and waits for Page Refresh.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	PageName - The Page Name where the search box is present.
		//''@	strSearchBox - Name of the Search Box to be cleared		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = clearSearchField(Page, "Display Tab");        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[03-26-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clearSearchField(String Page, String strSearchBox) 
		{
			boolean blnFlag = false;	
			String strTempXpath = "//th[@role='columnheader']/span[text()='" + strSearchBox + "']/following::input";
			
			try
			{
				blnFlag = clearFieldandWait(Page, strTempXpath);
				if(!blnFlag)
					Err.Description = "Failed to clear the input field";
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Failed to clear the input field";
			}
			return blnFlag;
		}	
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyErrorMessage
        //''@Objective: This function enter text in input box click on button and validate the error message on the page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	pgeName - The Page Name where the element is present.
		//''@	eleButton - The button on which click action to be performed.
		//''@	pgeCommon - The Page Name where the element is present.
		//''@	eleError - The error message element.
		//''@	strExpText - Expected text to be validated.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = verifyErrorMessage("pgeHospitals", "btnSave", "pgeCommon", "eleErrorMessage", strExpText); 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[04-01-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean verifyErrorMessage(String pgeName, String eleButton, String pgeCommon, String eleError, String strExpText) 
		{
			boolean blnFlag = false;	
			try
			{
				blnFlag = Page(pgeName).Element(eleButton).Click(20);
				if(blnFlag)
				{
					blnFlag = verifyTextContains(pgeCommon, eleError, strExpText);
					if (!blnFlag)
						Err.Description = Err.Description;
				}
				else
					Err.Description = "Unable to click on button.";		
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occured while validating the error message.";
			}
			return blnFlag;
		}	
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: addProduct
        //''@Objective: This function enters details in 'Manage Customer Products' pop-up displayed on clicking 'Add Product' in 
		//''@'Donor History' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strProduct - Name of the product to be selected.
		//''@	strMinLevel - Minimum Level value to be entered.
		//''@	strMaxLevel - Maximum Level value to be entered.
		//''@	Page - Page on which the button object is present.
		//''@	eleButton - Object of the button to be clicked (Save or Cancel). 	
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = addProduct("GS-5024", "2", "5", "pgeCustomers", "btnSaveOnAddProduct");    
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: [03-31-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean addProduct(String strProduct, String strMinLevel, String strMaxLevel, String Page, String eleButton) 
		{
			boolean blnFlag = false;
			
			try
			{
				if(!(strProduct == null) && !(strProduct == ""))
				{
					blnFlag = Page("pgeCustomers").Element("lstProduct").Select(strProduct);		
				}
				else
				{
					blnFlag = true;
				}				
				if (blnFlag)
				{
					if(strMinLevel != null && strMinLevel != "")
					{
						blnFlag = Page("pgeCustomers").Element("txtMinimumLevel").Type(strMinLevel);
					}
					else
					{
						blnFlag = true;
					}
					if (blnFlag) 
					{
						if(strMaxLevel != null && strMaxLevel != "")
						{
							blnFlag = Page("pgeCustomers").Element("txtMaximumLevel").Type(strMaxLevel);
						}
						else
						{
							blnFlag = true;
						}							
						
						if(blnFlag)
						{
							blnFlag = Page(Page).Element(eleButton).Click(10);
							if(!blnFlag)
								Err.Description = "Unable to click the Save or Cancel button.";
						}
						else
						{
							Err.Description = "Unable to enter maximum level.";
						}
					}
					else
					{
						Err.Description = "Unable to enter minimum level.";
					}	
				}				
				else
				{
					Err.Description = "Unable to select the product.";
				}	
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error while entering product details.";
			}	
			
			return blnFlag;
		}

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifySearchResults
        //''@Objective: This function enters the search string in the Field Tracking page and verifies search results    
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - Page which contains the search box
		//''@	strSearchBox - Name of the column to search value 
		//''@   strSearchText - Text to be typed in the search input field
		//''@	PageExpected - Page which contains the search box
		//''@	eleExpectedObject - Object name expected after search to get the runtime text to validate with expected text
		//''@	strExpectedText - Expected text of the expected object after search
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = verifySearchResults("pgeFieldTracking", "Display Tab", "Search String", "pgeCommon","objNoRecordsFound", "No records found");     
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[04-07-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean verifySearchResults(String Page, String strSearchBox, String strSearchText, String PageExpected, String eleExpectedObject, String strExpectedText)
		{
			boolean blnFlag = false;
			strSearchText = strSearchText.trim();
			String strTempXpath = "//th[@role='columnheader']/span[text()='" + strSearchBox + "']/following::input";
			
			try
			{
				blnFlag = Page(Page).Element(strTempXpath).Type(strSearchText); 
				// driver.findElement(By.xpath(strTempXpath)).sendKeys(Keys.TAB);
				waitForPageRefresh();
				if(blnFlag)
				{
					waitForSync(2);	
					blnFlag = verifyTextContains(PageExpected, eleExpectedObject, strExpectedText);
					if(!blnFlag)
						Err.Description = "Expected text not displayed in the page";
				}
				else
					Err.Description = Err.Description; // "Failed to type text in the search box.";				
			}
			catch (Exception e)
			{
				blnFlag = false;	
				Err.Description = "Error encountered while entering text in search box.";				
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyErrorMessage
        //''@Objective: This function enter text in input box click on button and validate the error message on the page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	pgeName - The Page Name where the element is present.
		//''@	eleInput - The element in which input text to be entered.
		//''@	strInputText - Input text to be validated.		
		//''@	eleButton - The button on which click action to be performed.		
		//''@	pgeError - The Page Name where the error element is present.
		//''@	eleError - The error message element.
		//''@	strExpText - Expected text to be validated.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = verifyErrorMessage("pgeHospitals", "txtPrefix", "", "btnSave", "pgeCommon", "objErrorMessage", strExpText); 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[04-07-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean verifyErrorMessage(String pgeName, String eleInput, String strInputText, String eleButton, String pgeError, String eleError, String strExpText) 
		{
			boolean blnFlag = false;	
			try
			{
				blnFlag = Page(pgeName).Element(eleInput).Type(strInputText);
				if (blnFlag) 
				{
					blnFlag = Page(pgeName).Element(eleButton).Click(20);
					if(blnFlag)
					{
						blnFlag = verifyTextContains(pgeError, eleError, strExpText);
						if (!blnFlag)
							Err.Description = Err.Description;
					}
					else
						Err.Description = "Unable to click on button.";
				} 
				else
					Err.Description = "Unable to type in input field.";
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occured while validating the error message.";
			}
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: waitForPageRefresh
        //''@Objective: This Function wait for grid to refresh.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= waitForPageRefresh(5)        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[9-Apr-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public void waitForPageRefresh(int intSeconds) 
		{
			try 
			{
				final String tempSource = driver.getPageSource();
				(new WebDriverWait(driver, intSeconds)).until(new ExpectedCondition<Boolean>() 
				{
					public Boolean apply(WebDriver driver) 
					{
						boolean flag = false;
						if (!tempSource.equals(driver.getPageSource())) 
						{
							flag = true;
						}
						return flag;		
					}
				});
			}
			catch (TimeoutException e)
			{
				Err.Description="Timed out after "+intSeconds+" seconds wait.";
			}
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyErrorMessage
        //''@Objective: This function enter text in input box click on button and validate the error message on the page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	pgeName - The Page Name where the element is present.
		//''@	eleInput - The element in which input text to be entered.
		//''@	strInputText - Input text to be validated.		
		//''@	pgeButton - The Page Name on which the Button to be clicked is present.		
		//''@	eleButton - The button on which click action to be performed.		
		//''@	pgeError - The Page Name where the error element is present.
		//''@	eleError - The error message element.
		//''@	strExpText - Expected text to be validated.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = verifyErrorMessage("pgeHospitals", "txtPrefix", "", "pgeCommon", "btnSave", "pgeCommon", "objErrorMessage", strExpText); 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[04-07-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean verifyErrorMessage(String pgeName, String eleInput, String strInputText, String pgeButton, String eleButton, String pgeError, String eleError, String strExpText) 
		{
			boolean blnFlag = false;	
			try
			{
				blnFlag = Page(pgeName).Element(eleInput).Type(strInputText);
				if (blnFlag) 
				{
					blnFlag = Page(pgeButton).Element(eleButton).Click(20);
					if(blnFlag)
					{
						blnFlag = verifyTextContains(pgeError, eleError, strExpText);
						if (!blnFlag)
							Err.Description = Err.Description;
					}
					else
						Err.Description = "Unable to click on button.";
				} 
				else
					Err.Description = "Unable to type in input field.";
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occured while validating the error message.";
			}
			return blnFlag;
		}

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyMultipleErrorMessages
        //''@Objective: This functions clicks on a button without entering any data and verify multiple error messages
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	pgeName - The Page Name where the button is present.
		//''@	eleButton - The button on which click action to be performed.
		//''@	eleErrMessage - The element where the error element is present.
		//''@	pgeError - The Page Name where the error element is present.
		//''@	strErrMessages - Expected text for each of the error message to be validated.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus=verifyMultipleErrorMessages("pgeProductAssembly","btnSave","pgeCommon","objUIMessageErrorSummary",strErrMessages);  
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]:Kamlesh Yadav[04-13-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean verifyMultipleErrorMessages(String pgeName,String eleButton,String pgeError,String eleErrMessage,String strErrMessages) 
		{
			boolean blnFlag = false;
			String strNotFound = "";
			try
			{
				//click on the button passed from test-data
				blnFlag = Page(pgeName).Element(eleButton).Click(10);
				
				if (blnFlag)
				{
					//Fetching the xpath for the object containing all the error messages
					String strXpath = Page(pgeError).Element(eleErrMessage).GetXpath();
					
					//creating a list pf web-elements for each error message
					java.util.List<WebElement> lstErrMessages = driver.findElements(By.xpath(strXpath));
					//fetching the size of the list
					int intSize = lstErrMessages.size();
						
					// splitting the list of errors passed from testdata
					String[] options = strErrMessages.trim().split(";");
					
					int counter = 0;
					
					//verifying if count of error messages passed from test-data matches the count on the UI
					if (intSize==options.length)
					{

						//iterates through each option name
						for (int i=0; i < options.length; i++)
						{
							
							for (int j=0; j < intSize; j++)
							{
								//fetches text for each web-element for each error message
								String strValue = lstErrMessages.get(j).getText();
								if (strValue.trim().equalsIgnoreCase(options[i]))
								{
									counter++;
									break;
								}
								else
								{
									if  (j == intSize-1)
	                                  {
										strNotFound= strNotFound +"'"+ options[i] +"'";
	                                  } 
								}
							}
						}
						if (counter==intSize)
						{
							blnFlag = true;
						}
						else
						{
							Err.Description = strNotFound + " Error messages not found.";
							blnFlag = false;
						}	
					}
					else
					{
						blnFlag = false;
						Err.Description = "List of error messages passed as test data: ("+strErrMessages+") is not matching with the number of error messages appearing on the page.";
					}
				 }
				else
					Err.Description = "Unable to click on button.";
			}
				
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occured while validating the error message.";
			}
		return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clearFields
        //''@Objective: This functions fetches the size of the fields on the basis of tag names and clear out data from each
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 	
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus=clearFields() 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]:Kamlesh Yadav[04-14-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clearFields() 
		{
			boolean blnFlag = false;
			
			try
			{
				String[] lstTagNames = {"input","textarea"};
				java.util.List<WebElement> lstErrMessages;
			
					for (int i=0; i < lstTagNames.length; i++)
					{
						lstErrMessages = driver.findElements(By.tagName(lstTagNames[i]));
						
						if(lstErrMessages.size()>0)
						{
							for (int j = 0 ; j < lstErrMessages.size(); j++)
							{
								String strTagValue = lstErrMessages.get(j).getTagName();
								if (strTagValue.equals("input"))
								{
									String Value = lstErrMessages.get(j).getAttribute("type");
									if (Value.equals("text"))
									{
										lstErrMessages.get(j).clear();		
									}
									
								}
								String strValue = lstErrMessages.get(j).getText();
								if(!(strValue.equals("") || strValue.equals(null)))
								{
									lstErrMessages.get(j).clear();								
								}								
								else
									blnFlag = true;
									
							}
							
						}						
						
					}			
			}
				
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occurred while validating the error message.";
			}
			
		return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickUnblockButton
        //''@Objective: This functions clicks on unblock button on 'Product Assembly' page
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus=clickUnblockButton();  
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]:Kamlesh Yadav[04-15-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickUnblockButton() 
		{
			boolean blnFlag = false;
		
			try
			{				
					blnFlag = Page("pgeProductAssembly").Element("btnUnlock").Exist();
					if (blnFlag)
					{
						blnFlag = Page("pgeProductAssembly").Element("btnUnlock").Click(10);
						if (!blnFlag)
						{
							Err.Description = "Unable to click on Unblock button on 'Product Assembly' page.";
						}
					}
					else
						blnFlag = true;
			}
				
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occurred while validating the error message.";
			}
		return blnFlag;
		}		
		

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: generateRandomChars
        //''@Objective: This function generates the required number of characters/special characters/numbers.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strMaxLength - The maximum length of the field to be generated.
		//''@	strTemplate - The template of alphabets/numerals/special characters to be generated.			
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = generateRandomChars("10", "1@abc"); 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[04-14-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean generateRandomChars(String strMaxLength, String strTemplate) 
		{
			boolean blnFlag = false;	
			try
			{
				int intMaxLength = Integer.parseInt(strMaxLength);				
				char[] tempArray = strTemplate.toCharArray();
				StringBuilder sb = new StringBuilder();
				Random random = new Random();
				for (int i = 0; i < intMaxLength; i++) 
				{
				  char tempChar = tempArray[random.nextInt(tempArray.length)];
				  sb.append(tempChar);
				}
				String strGeneratedInput = sb.toString();
			
				blnFlag = !(strGeneratedInput.equals("") || strGeneratedInput.equals(null));
				if(blnFlag)
				{
					Script.dicCommonValue.put("strGeneratedInput",strGeneratedInput);
				}
				else
				{
					Err.Description = "Unable to generate the random maximum character input.";
					blnFlag=false;
				}				
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occured while generating the maximum character random input.";
			}
			return blnFlag;
		}

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyMaxCharacErrorMessage
        //''@Objective: This function enter the generated max character text in input box click on button and validate the error message on the page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	pgeName - The Page Name where the element is present.
		//''@	eleInput - The element in which input text to be entered.
		//''@	strInputText - Input text to be validated.
		//''@	strMaxLength - The maximum length of the field to be generated.
		//''@	pgeButton - The Page Name on which the Button to be clicked is present.		
		//''@	eleButton - The button on which click action to be performed.		
		//''@	pgeError - The Page Name where the error element is present.
		//''@	eleError - The error message element.
		//''@	strExpText - Expected text to be validated.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = verifyMaxCharacErrorMessage("pgeHospitals", "txtPrefix", "1#wer", "25", "pgeCommon", "btnSave", "pgeCommon", //''@"objErrorMessage", strExpText); 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[04-14-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean verifyMaxCharacErrorMessage(String pgeName, String eleInput, String strInputText, String strMaxLength, String pgeButton, String eleButton, String pgeError, String eleError, String strExpText) 
		{
			boolean blnFlag = false;	
			try
			{				
				blnFlag = generateRandomChars(strMaxLength, strInputText);
				if(blnFlag)
				{
					strInputText = Script.dicCommonValue.get("strGeneratedInput"); 
					blnFlag = Page(pgeName).Element(eleInput).Type(strInputText);
					if (blnFlag) 
					{
						blnFlag = Page(pgeButton).Element(eleButton).Click(20);
						if(blnFlag)
						{
							blnFlag = verifyTextContains(pgeError, eleError, strExpText);
							if (!blnFlag)
								Err.Description = Err.Description;
						}
						else
							Err.Description = "Unable to click on button.";
					} 
					else
						Err.Description = "Unable to type in input field.";
				}
				else
					Err.Description = Err.Description;
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occured while validating the error message.";
			}
			return blnFlag;
		}
		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyMaxCharacErrorMessage
        //''@Objective: This function enter the generated max character text in input box click on button and validate the error message on the page
		//''@ after clearing/entering text on some other field on the same page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	pgeName - The Page Name where the element is present.
		//''@	elePrevInput - The Previous element in which input text to be entered/cleared.
		//''@	strPrevValuetoEnter - Input text to be entered for the previous field/to clear the previous field.
		//''@	eleInput - The element in which input text to be entered.
		//''@	strInputText - Input text to be validated.
		//''@	strMaxLength - The maximum length of the field to be generated.
		//''@	pgeButton - The Page Name on which the Button to be clicked is present.		
		//''@	eleButton - The button on which click action to be performed.		
		//''@	pgeError - The Page Name where the error element is present.
		//''@	eleError - The error message element.
		//''@	strExpText - Expected text to be validated.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = verifyMaxCharacErrorMessage("pgeHospitals", "txtPrefix", "", "txtFacilityRate", "1#wer", "25", "pgeCommon", "btnSave", "pgeCommon", "objErrorMessage", strExpText); 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[04-14-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean verifyMaxCharacErrorMessage(String pgeName, String elePrevInput, String strPrevValuetoEnter, String eleInput, String strInputText, String strMaxLength, String pgeButton, String eleButton, String pgeError, String eleError, String strExpText) 
		{
			boolean blnFlag = false;	
			try
			{
				blnFlag = Page(pgeName).Element(elePrevInput).Type(strPrevValuetoEnter);
				if(blnFlag)
				{				
					blnFlag = generateRandomChars(strMaxLength, strInputText);
					if(blnFlag)
					{
						strInputText = Script.dicCommonValue.get("strGeneratedInput"); 
						blnFlag = Page(pgeName).Element(eleInput).Type(strInputText);
						if (blnFlag) 
						{
							blnFlag = Page(pgeButton).Element(eleButton).Click(20);
							if(blnFlag)
							{
								blnFlag = verifyTextContains(pgeError, eleError, strExpText);
								if (!blnFlag)
									Err.Description = Err.Description;
							}
							else
								Err.Description = "Unable to click on button.";
						} 
						else
							Err.Description = "Unable to type in input field.";
					}
					else
						Err.Description = Err.Description;
				}
				else
					Err.Description = "Unable to type '"+strPrevValuetoEnter+"' in the previous field '"+elePrevInput+"'.";
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occured while validating the error message.";
			}
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clearAllField
        //''@Objective: This Function clears the mentioned  field on the page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	PageName - The Page Name where the search box is present.
		//''@	eleInputField - Name of the input field to be cleared.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = clearAllField(Page, "txtDisplayTab","txtPhysician");        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[04-16-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clearAllField(String Page, String... eleInputField) 
		{
			boolean blnFlag = false;	
					
			try
			{
				for(String element:eleInputField)
				{
					blnFlag = Page(Page).Element(element).Type("");
					if (!blnFlag)
					{
						Err.Description = "Unable to clear input field.";
						break;
					}
				}
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Failed to clear the input field";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clearAllFieldAndVerifyErrorMessage
        //''@Objective: This function clear text in input box click on button and validate the error message on the page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	pgeButton - The Page Name where the element is present.
		//''@	eleButton - The button on which click action to be performed.
		//''@	pgeCommon - The Page Name where the error element is present.
		//''@	eleError - The error message element.
		//''@	strExpText - Expected text to be validated.		
		//''@	pgeName - The Page Name where the element is present.		
		//''@	eleInput - Name of the input field to be cleared.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = clearAllFieldAndVerifyErrorMessage("pgeHospitals", "txtPrefix", "", "btnSave", "pgeCommon", "objErrorMessage", //''@strExpText); 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[04-16-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clearAllFieldAndVerifyErrorMessage(String pgeButton, String eleButton, String pgeCommon, String eleError, String strExpText, String pgeName, String... eleInput) 
		{
			boolean blnFlag = false;	
			try
			{
				blnFlag = clearAllField(pgeName, eleInput);
				if (blnFlag) 
				{
					blnFlag = verifyMultipleErrorMessages(pgeButton, eleButton, pgeCommon, eleError, strExpText);
					if (!blnFlag)
						Err.Description = Err.Description;					
				} 
				else
					Err.Description = "Unable to clear input field.";
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occured while validating the error message.";
			}
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: searchAndRename()
        //''@Objective: This function searches the given item (Name or ID) and renames it to the given new name
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	pgeSearchPage - Page to search the given ID or Name
		//''@	eleSearchBox - Search box to type the search string
		//''@	strSearchText - Text to be searched in the search box
		//''@	lnkSearchResult - Search result element displayed in the result grid
		//''@	pgeNewPage - New page displayed on clicking lnkSearchResult
		//''@	eleRenameField - Input field for the item to be renamed
		//''@	pgeButtonPage - Page for the button (Save) to be clicked after renaming
		//''@	eleButton - Button(Save) to be clicked after renaming
		//''@	strNewName - New name that is added/appended to the renamed field
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = searchAndRename("pgePhysicians", "txtPhysicianId", strDBRecoveryPhysicianId, "lnkPhysicianId", "pgePhysicians", "txtFirstName", "pgePhysicians", "btnSave", "Renamed Value"); 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[04-15-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean searchAndRename(String pgeSearchPage, String eleSearchBox, String strSearchText, String eleSearchResultLink, String pgeNewPage, String eleRenameField, String pgeButtonPage, String eleButton, String strNewName) 
		{
			boolean blnFlag = false;	
			
			try
			{
				if(!(strSearchText == null || strSearchText == ""))
				{
					blnFlag = calculateNewItem(strNewName, strSearchText); 
					
					if (blnFlag)
					{
						strNewName = strNewName + Script.dicCommonValue.get("strCalculatedItem");
					}
					else
					{
						Err.Description = "Unable to generate random name.";
					}
					
					blnFlag = verifySearch(pgeSearchPage, eleSearchBox, eleSearchResultLink, strSearchText);
					
					if(blnFlag)
					{
						blnFlag = Page(pgeSearchPage).Element(eleSearchResultLink).Click(10);
						
						if(blnFlag)
						{
							blnFlag = Page(pgeNewPage).Element(eleRenameField).Type(strNewName);
							
							if(blnFlag)
							{
								blnFlag = Page(pgeButtonPage).Element(eleButton).Click(10);

								if(!blnFlag)
									Err.Description = "Unable to click the button.";
							}
							else
								Err.Description = "Unable to type in the input field to be renamed.";
						}
						else
							Err.Description = "Unable to click the searched element.";
					}
					else
						Err.Description = "Searched item was not found.";
				}
				else
				{
					blnFlag = true;
					//Err.Description = "Group Id provided is either null or blank.";
				}
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occured while renaming the item.";
			}
			return blnFlag;
		}	

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: addFillerPouchAndVerifyErrorMessage
        //''@Objective: This function adds more empty filler pouches then required, click on "Process Scans' button and verify error message. 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	Page - Page name to mapping the object on the page.
        //''@	Element - object name in which the quantity of tissues available needs to be retrieved.
		//''@	fillElement - object name in which the quantity of empty pouches needed needs to be retrieved.
		//''@	eleButton - The button on which click action to be performed.		
		//''@	pgeError - The Page Name where the error element is present.
		//''@	eleError - The error message element.
		//''@	strExpText - Expected text to be validated.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= addFillerPouchAndVerifyErrorMessage("pgeSterilizationReturns","objQtyAvailable" //''@"btnProcess","objFillElement","pgeCommon", "objUIMessageErrorSummary","Error to be verified")    
		//''@---------------------------------------------------------------------------------------------------------------------------		
		//''@Created by[Date]: Ashish Khare[04-22-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean addFillerPouchAndVerifyErrorMessage(String Page, String fillElement, String eleButton, String pgeCommon, String eleError, String strExpText)
		{
			boolean blnFlag = false;
			String strFillerQty="";
			int intTemp=1;
			int intFillerQty=0;
			String strFillerPouch;
			
			strFillerQty = Page(Page).Element(fillElement).GetText();
			intFillerQty = Integer.parseInt(strFillerQty);
			intFillerQty = intFillerQty+1;
			strExpText=strExpText.replace("###", String.format("%03d", intFillerQty));
					
			try
			{
				while((intTemp<=intFillerQty))  
				{
					strFillerPouch="FILLER POUCH-"+String.format("%03d", intTemp);
					driver.findElement(By.xpath("//*[@class='wideTextInput']")).sendKeys(strFillerPouch);
					driver.findElement(By.xpath("//*[@class='wideTextInput']")).sendKeys(Keys.RETURN);
					intTemp = intTemp + 1;					
				}
				if(intTemp==(intFillerQty+1))
				{
					blnFlag=true;
				}
				if (blnFlag)
				{
					blnFlag = verifyMultipleErrorMessages(Page, eleButton, pgeCommon, eleError, strExpText);
					if (blnFlag)
					{
						Script.dicTestData.put("ExpectedText", strExpText);
					}
					else
						Err.Description = Err.Description;	
					
				}
				else
					Err.Description = "All the tissues were not added for process scan.";
				
			}
			catch(Exception e)
			{
				Err.Description = "Error while adding all the filler pouches into scan area.";
				blnFlag=false;
			}		
			
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyErrorMessageOnSelectWithDBValue
        //''@Objective: This function selects a value from the drop-down, clicks on button and validates the error message on the page by
		//''@			replacing place-holder value in the expected text with the DB value.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	pgeName - The Page Name where the element is present.
		//''@	eleSelect - The element in which the value has to be selected.
		//''@	strInputText - Input text to be validated.		
		//''@	pgeButton - The Page Name on which the Button to be clicked is present.		
		//''@	eleButton - The button on which click action has to be performed.		
		//''@	pgeError - The Page Name where the error element is present.
		//''@	eleError - The error message element.
		//''@	strExpText - Expected text to be validated.
		//''@	strReplaceString - Text to be replaced from the strExpText.	
		//''@	strDBValue - Value from the DB which will replace the strReplaceString. 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = verifyErrorMessageOnSelectWithDBValue("pgeFacility", "lstFacilityCustomer", strCustomer, "pgeFacility", "btnSave", "pgeCommon", "objUIMessageErrorSummary", strAlreadyReferenceCustomerError, "CustomerName", strCustomerDb); 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[04-23-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean verifyErrorMessageOnSelectWithDBValue(String pgeName, String eleSelect, String strInputText, String pgeButton, String eleButton, String pgeError, String eleError, String strExpText, String strReplaceString, String strDBValue) 
		{
			if (!strDBValue.isEmpty())
			{
				strExpText = strExpText.replace("'" + strReplaceString + "'", strDBValue);
			}
			else 
				strExpText=strExpText;
						
			boolean blnFlag = false;	
			try
			{
				blnFlag = Page(pgeName).Element(eleSelect).Select(strInputText);
				waitForSync(4);
				if (blnFlag) 
				{
					blnFlag = Page(pgeButton).Element(eleButton).Click(20);
					if(blnFlag)
					{
						blnFlag = verifyTextContains(pgeError, eleError, strExpText);
						if (!blnFlag)
							Err.Description = Err.Description;
					}
					else
						Err.Description = "Unable to click on button.";
				} 
				else
					Err.Description = "Unable to type in input field.";
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occured while validating the error message.";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyAddDisabledOnCSectionScheduleEntry
        //''@Objective: This Function enters given value in the input field and verifies that the 'Add' button is disabled.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	pgeInputPage - Page for the input element.
		//''@	eleInput - Input element where text needs to be input.
		//''@	strInputText - Input text value.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = verifyAddDisabledOnCSectionScheduleEntry("pgeCSectionSchedule", "txtNote", strInvalidText);        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[23-Apr-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean verifyAddDisabledOnCSectionScheduleEntry(String pgeInputPage, String eleInput, String strInputText) 
		{
			boolean blnFlag = false;
			
			blnFlag = Page(pgeInputPage).Element(eleInput).Type(strInputText);
			waitForPageRefresh();

			if(blnFlag)				
			{
				String tempXpath = Page("pgeCSectionSchedule").Element("btnAdd").GetXpath();
				tempXpath = tempXpath + "/..";
				String state = driver.findElement(By.xpath(tempXpath)).getAttribute("aria-disabled");
				
				if(state.equalsIgnoreCase("true")) 
				{
					blnFlag=true;
				}
				else
				{
					blnFlag=false;
					Err.Description = "'Add' button is enabled on the form.";
				}
			}
			else 			
				Err.Description = "Unable to enter text in the input field on the form.";				
			
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifySearchResults
        //''@Objective: This function enters the search string in the Field Tracking page and verifies search results    
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - Page which contains the search box
		//''@	strSearchBox - Name of the column to search value 
		//''@   strSearchText - Text to be typed in the search input field
		//''@	eleExpectedObject - Object name expected after search to get the runtime text to validate with expected text
		//''@	strExpectedText - Expected text of the expected object after search
		//''@   eleParentElement - a parent element other than a page element having search grid 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = verifySearchResults("pgeFieldTracking", "Tissue", strSearchText, "pgeCommon", "objNoRecordsFound", //''@strExpectedText,"objTissueLocatorGrid");
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[04-21-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean verifySearchResults(String Page, String strSearchBox, String strSearchText,String PageExpected, String eleExpectedObject, String strExpectedText, String eleParentElement)
		{
			boolean blnFlag = false;
			strSearchText = strSearchText.trim();
			String strXpath = Page(Page).Element(eleParentElement).GetXpath();
			String strTempXpath = strXpath + "//th[@role='columnheader']/span[text()='" + strSearchBox + "']/following::input";
			
			try
			{
				blnFlag = Page(Page).Element(strTempXpath).Type(strSearchText); 
				// driver.findElement(By.xpath(strTempXpath)).sendKeys(Keys.TAB);
				waitForPageRefresh();
				if(blnFlag)
				{
					waitForSync(2);	
					blnFlag = verifyTextContains("pgeCommon", eleExpectedObject, strExpectedText);
					if(!blnFlag)
						Err.Description = "Expected text not displayed in the page";
				}
				else
					Err.Description = Err.Description; // "Failed to type text in the search box.";				
			}
			catch (Exception e)
			{
				blnFlag = false;	
				Err.Description = "Error encountered while entering text in search box.";				
			}
			return blnFlag;
		}		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clearSearchField
        //''@Objective: This Function clears the mentioned search field in the Field Tracking page and waits for Page Refresh.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	PageName - The Page Name where the search box is present.
		//''@	strSearchBox - Name of the Search Box to be cleared		
		//''@   eleParentEelement - a Parent element other than the generic page having search grid
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = clearSearchField(Page, "Display Tab");        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[04-21-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clearSearchField(String Page, String strSearchBox,String eleParentEelement) 
		{
			boolean blnFlag = false;	
			String strXpath = Page(Page).Element(eleParentEelement).GetXpath();
			String strTempXpath = strXpath + "//th[@role='columnheader']/span[text()='" + strSearchBox + "']/following::input";
			
			try
			{
				blnFlag = clearFieldandWait(Page, strTempXpath);
				if(!blnFlag)
					Err.Description = "Failed to clear the input field";
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Failed to clear the input field";
			}
			return blnFlag;
		}	
			
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: FieldTracking_FindTissueOnTissueLocator
        //''@Objective: This Function enters tissue ID,Clicks on 'Find' button, waits for page refresh and verify search results on Tissue Locator //''@           Pop-up
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strTissueId - Tissue ID to be entered on Tissue Locator pop-up
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = FieldTracking_FindTissueOnTissueLocator(strTissueId);       
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Kamlesh Kumar[04-22-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean FieldTracking_FindTissueOnTissueLocator(String strTissueId) 
		{
			boolean blnFlag = false;	
			
			try
			{
				blnFlag=Page("pgeFieldTracking").Element("txtTissueID").Type(strTissueId);
				
				if (blnFlag)
				{
					blnFlag = Page("pgeFieldTracking").Element("btnFindTissueLocator").Click(10);
					if (blnFlag)
					{
						waitForPageRefresh(20);
						blnFlag = verifyExactText("pgeFieldTracking","objTissueIDTissueLocator",strTissueId);
						if (!blnFlag)
						{
							Err.Description="Failed to click on 'Find' button.";	
						}
					}
					else
						Err.Description="Failed to verify tissue details for searched tissue ID: "+strTissueId+".";		
				}
				else
					Err.Description="Unable to enter Tissue ID: "+strTissueId+" on Tissue Locator Pop-up.";
			}
		
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Failed to enter Tissue ID on 'Tissue Locator' pop-up.";
			}
			return blnFlag;
		}	
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: VerifyOutstandingTissuesErrors
        //''@Objective: This function enters invalid/junk values in 'Avaiable/Selected Tissues' text-fields and verifies tissues in the grid disappears
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strSearchText - Invalid/Junk value to be entered
		//''@	eleInput - input field where data is entered
		//''@	eleTissueGrid - grid object from where tissues disappears (Available or Selected)
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = VerifyOutstandingTissuesErrors(strSearchText,"txtAvailableTissues","ObjSourceTissues"); 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]:Kamlesh Kumar[04-30-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean VerifyOutstandingTissuesErrors(String strSearchText , String eleInput,String eleTissueGrid) 
		{
			boolean blnFlag = false;	
			try
			{
				blnFlag = Page("pgeConsignmentPurchaseOrder").Element(eleInput).Type(strSearchText);
				if (blnFlag) 
				{
					String strXpath = Page("pgeConsignmentPurchaseOrder").Element(eleTissueGrid).GetXpath();
					
					//creating a list pf web-elements for each tissue present in the grid
					java.util.List<WebElement> lstOustandingTissues = driver.findElements(By.xpath(strXpath + "/li"));
					
					//fetching the size of the list
					int intSize = lstOustandingTissues.size();
					
					
					for (int j=0; j < intSize; j++)
					{
						String strAttribute = lstOustandingTissues.get(index).getAttribute("style");
						if (strAttribute.trim().contains("none"))
						{
							blnFlag = true;
						}
						else
						{
							blnFlag = false;
							Err.Description = "Unable to fetch 'style' attribute of the specified object.";
							break;
                        } 
					}
					blnFlag = clearFieldandWait("pgeConsignmentPurchaseOrder",eleInput);
					if (!blnFlag)
					{
						Err.Description = "Unable to clear out invalid/junk data from the input field.";
					}
				}
				else
					Err.Description = "Unable to enter invalid/junk values in the specified field.";
				
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occurred while validating the error message.";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: EnterAgentNameAndClickConsignmentBillingButton
        //''@Objective: This function enters Agent Name and clicks on 'Consignment Billing' button.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strAgentName - Agent Name to be filled
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = EnterAgentNameAndClickConsignmentBillingButton(strAgentName) ; 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Kamlesh Kumar[04-30-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean EnterAgentNameAndClickConsignmentBillingButton(String strAgentName) 
		{
			boolean blnFlag = false;	
			try
			{
				blnFlag = enterAgentName(strAgentName);
				if (blnFlag) 
				{
					blnFlag = clickAndVerify("pgeFieldTracking","btnConsignmentBilling", "Consignment Purchase Order");
					if (!blnFlag)
					{
						Err.Description = "Unable to navigate to 'Consignment Purchase Order' page.";
					}
				}
				else
					Err.Description = "Unable to enter agent name '"+strAgentName+"'.";
				
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occurred while validating the navigation.";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: EnterDataOnConsignmentPurchaseOrderAndClickNext
        //''@Objective: This function enters purchase order,enters a customer,click on 'Next' button on 'Consignment Purchase Order' page 
		//''@           and verify expected page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strPurchaseOrder - Purchase Order to be filled
		//''@	strCustomerName - Customer Name to be filler
		//''@	newElement - The Page Name where the error element is present.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = EnterDataOnConsignmentPurchaseOrderAndClickNext(strPurchaseOrder,strCustomerName,"ObjAvailableTissues"); 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Kamlesh Kumar[04-30-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean EnterDataOnConsignmentPurchaseOrderAndClickNext(String strPurchaseOrder,String strCustomerName,String newElement) 
		{
			boolean blnFlag = false;	
			try
			{
				blnFlag=enterPurchaseOrder(strPurchaseOrder);
				if (blnFlag) 
				{
					blnFlag=fillCustomerName(strCustomerName);
					if (blnFlag)
					{
						blnFlag=clickAndVerify("pgeConsignmentPurchaseOrder", "btnNext", "pgeConsignmentPurchaseOrder",newElement);
						if (!blnFlag)
						{
							Err.Description = "Unable to navigate to next step to 'Outstanding Tissues' section.";
						}
					}
					else
						Err.Description = "Unable to enter customer '"+strCustomerName+"'.";
				}
				else
					Err.Description = "Unable to enter Purchase Order '"+strPurchaseOrder+"'.";
				
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occurred while validating the navigation.";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: removeSelectedTissueAndMoveToAvailableTissuesGrid
        //''@Objective: This function removes a selected tissue from 'Selected Tissues' and move to 'Available Tissues' list box on 
		//''@'Consignment Purchase Order' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= removeSelectedTissueAndMoveToAvailableTissuesGrid();        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Kamlesh Kumar[04-30-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean removeSelectedTissueAndMoveToAvailableTissuesGrid() 
		{
			boolean blnFlag = false;
			String strSourceTissueId="";
			String strTargetTissueId="";
// 			String xSourceTissue="//ul[contains(@class,'ui-picklist-source')]/li[@data-item-label='"+strTissueId+"']";
//			String xTargetTissue="//ul[contains(@class,'ui-picklist-target')]/li[@data-item-label='"+strTissueId+"']";
			String xSourceTissue="//ul[contains(@class,'ui-picklist-source')]/li[last()]";
			String xTargetTissue="//ul[contains(@class,'ui-picklist-target')]/li";
			strTargetTissueId = Page("pgeConsignmentPurchaseOrder").Element(xTargetTissue).GetText();
		
			if (strTargetTissueId != null && !strTargetTissueId.isEmpty())
			{

				blnFlag = Page("pgeConsignmentPurchaseOrder").Element(xTargetTissue).Click(20);
								
				if (blnFlag == true)
				{
					blnFlag = Page("pgeConsignmentPurchaseOrder").Element("btnRemoveSelectedTissue").Click(20);
					
					if (blnFlag == true)
					{
						strSourceTissueId = Page("pgeConsignmentPurchaseOrder").Element(xSourceTissue).GetText();
						blnFlag=verifyInputValue(strSourceTissueId, strTargetTissueId, "'Available Tissues' grid");
						
						if (blnFlag)
							Script.dicTestData.put("SourceTissue", strSourceTissueId);
						else
							Err.Description=Err.Description;			
					}
					else
						Err.Description="Unable to click on left arrow button under 'Outstanding Tissues' grid.";
				}
				else
					Err.Description="Unable to select the tissue under 'Selected Tissues' grid.";				
			}
			else
				Err.Description="Tissues not displayed under 'Selected Tissues' grid hence unable to move to 'Available Tissues' grid.";
				
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyLoginError
        //''@Objective: This function verifies invalid user/password error message in Login Page.    
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strUserName - User Name that is used to login to MiMedx application.
        //''@	strPassword - Password that is used to login to MiMedx application.
		//''@	strExpectedText - Expected text of the expected object after search
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = verifyLoginError("strUserName", "strPassword", "strExpectedText");
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[05-01-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean verifyLoginError(String strUserName, String strPassword, String strExpectedText)
		{
			boolean blnFlag = false;
		
			try
			{
				blnFlag = Page("pgeLogin").Element("txtUser").Type(strUserName); 
				if(blnFlag)
				{
					blnFlag = Page("pgeLogin").Element("txtPassword").Type(strPassword); 
					if(blnFlag)
					{
						blnFlag = Page("pgeLogin").Element("btnLogin").Click(20);
						if(blnFlag)
						{
							blnFlag = verifyExactText("pgeLogin", "objLoginError", strExpectedText);
							if(!blnFlag)
								Err.Description = Err.Description;
						}
						else
							Err.Description = "Unable to click login button.";
					} 
					else
						Err.Description = "Unable to type in Password.";
				} 
				else
					Err.Description = "Unable to type in UserId.";	
			}
			catch (Exception e)
			{
				blnFlag = false;	
				Err.Description = "Error encountered while validating login error.";				
			}
			return blnFlag;
		}		

		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: enterCSectionProcedureDate
        //''@Objective: This function enters procedure date in the C-Section Schedule page
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strDate - Date to be enetered for the procedure date.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = enterCSectionProcedureDate(String strDate);
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Pankaj Kumar[04-24-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean enterCSectionProcedureDate(String strDate)
		{
			boolean blnFlag = false;
			JavascriptExecutor js = (JavascriptExecutor) driver;
			
			try
			{
				js.executeScript("document.getElementById('rform:cSectionDateTime_input').setAttribute('value', '" + strDate + "')");
				blnFlag = Page("pgeCSectionSchedule").Element("txtHospital").Click(5);
			}
			catch (Exception e)
			{
				blnFlag = false;	
				Err.Description = "Error encountered while entering procedure date.";				
			}
			return blnFlag;
		}

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifySearchedListItemNotExists()
        //''@Objective: This function searches a list and verifies that the searched item in not displayed in the list.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	pageInputPage - Page on which the searchbox and list item are present
		//''@	eleInputBox - Search input box
		//''@	eleList - List to be searched
		//''@	strSearchText - Text of the list item to be searched in the given list
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = verifySearchedListItemNotExists("pgePhysicians", "txtHospitalsSearchBox", "lstHospitals", strSearchText);
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Pankaj Kumar[04-27-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean verifySearchedListItemNotExists(String pageInputPage, String eleInputBox, String eleList, String strSearchText)
		{
			boolean blnFlag = false;
			
			try
			{
				blnFlag = Page(pageInputPage).Element(eleInputBox).Type(strSearchText);
				waitForPageRefresh();
				waitForSync(5);// Required for the list to update
				
				if(blnFlag)
				{
					String strTempXpath = Page(pageInputPage).Element(eleList).GetXpath();
					strTempXpath += "/li";
					java.util.List<WebElement> listItems = driver.findElements(By.xpath(strTempXpath)); 
					
					// Iterate through all the list items to ensure none is visivle
					for(WebElement ele : listItems)
					{						
						if(ele.isDisplayed())
						{
							blnFlag = false;
							Err.Description = "Search returned results which are displayed in the list box.";
							
							// break out of the loop since no further iterations are required for the test
							break; 
						}
					}
				
				}
				else
					Err.Description = "Error encountered while entering search text.";
			}
			catch (Exception e)
			{
				blnFlag = false;	
				Err.Description = "Error encountered while searching hospital.";				
			}
			return blnFlag;
		}		

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: addOrRemoveListItem
        //''@Objective: This function moves a list item from one list to another. Eg. Adds hospital on the Edit Physicians page and 
		//''@			verifies selected hospital appears in the selected hospitals list.   
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - Page which contains the search box
		//''@	strSourceList - Source list item from where list item needs to be selected 
		//''@   strTargetList - Target list to which the list item needs to be moved
		//''@	eleAddRemoveButton - Button to move the list item (Add/Remove)
		//''@	strListItemText - List item to be moved
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = addOrRemoveListItem("pgePhysicians", "lstHospitals", "lstSelectedHospitals", "btnRightArrow", strHospital);     
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Pankaj Kumar[04-28-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean addOrRemoveListItem(String Page, String strSourceList, String strTargetList, String eleAddRemoveButton, String strListItemText)
		{
			boolean blnFlag = false;
			String strTempXpathSourceItem = Page(Page).Element(strSourceList).GetXpath() + "/li[text()='" + strListItemText + "']";
			String strTempXpathTargetItem = Page(Page).Element(strTargetList).GetXpath() + "/li[text()='" + strListItemText + "']";
			
			try
			{
				blnFlag = Page(Page).Element(strTempXpathSourceItem).Click(10); 

				if(blnFlag)
				{
					blnFlag = Page(Page).Element(eleAddRemoveButton).Click(10); 
					
					if(blnFlag)
					{
						// Verify that the list item exists in the target list
						blnFlag = Page(Page).Element(strTempXpathTargetItem).Exist();
						
						if(blnFlag)
						{
							// Verify that the list item is visible as well
							blnFlag = Page(Page).Element(strTempXpathTargetItem).isVisible();
							if(!blnFlag)
							{
								Err.Description = "The added list item is not visible is the target list.";
							}
						}
						else
							Err.Description = "The selected list item was not added/moved to the target list.";
					}
					else
						Err.Description = "Failed to click the button to move item to the target list.";
				}
				else
					Err.Description = "Failed to select or find the given list item from the source list.";				
			}
			catch (Exception e)
			{
				blnFlag = false;	
				Err.Description = "Error encountered while selecting list item from the source list.";				
			}
			return blnFlag;
		}				
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clearAllFieldAndVerifyErrorMessage
        //''@Objective: This function clear text in input box click on button and validate the error message on the page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	pgeButton - The Page Name where the element is present.
		//''@	eleButton - The button on which click action to be performed.
		//''@	pgeCommon - The Page Name where the error element is present.
		//''@	eleError - The error message element.
		//''@	strExpText - Expected text to be validated.		
		//''@	pgeName - The Page Name where the element is present.		
		//''@	eleInput - Name of the input field to be cleared.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = clearAllFieldAndVerifyErrorMessage("pgeHospitals", "txtPrefix", "", "btnSave", "pgeCommon", "objErrorMessage", //''@strExpText); 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[04-16-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clearAllFieldAndVerifyErrorMessage(String pgeButton, String eleButton, String pgeCommon, String eleError, String strExpText, String pgeName,String eleEnterInput,String strInputText, String eleInput) 
		{
			boolean blnFlag = false;	
			try
			{
				blnFlag = clearAllField(pgeName, eleInput);
				if (blnFlag) 
				{
					blnFlag = Page(pgeName).Element(eleEnterInput).Type(strInputText);
					if (blnFlag)
					{
						blnFlag = verifyMultipleErrorMessages(pgeButton, eleButton, pgeCommon, eleError, strExpText);
						if (!blnFlag)
							Err.Description = Err.Description;		
					}
					else
						Err.Description = "Unable to enter value '"+strInputText+"' in the input field.";
				} 
				else
					Err.Description = "Unable to clear input field.";
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occurred while validating the error message.";
			}
			return blnFlag;
		}	
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: enterDataEditCertificateOfConformance
        //''@Objective: This function enters data in 'Moisture Testing %' field ,selects a sheet type on the basis of tissue code being
		//''@			being searched on 'Edit Certificate Of Conformance' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//'@	strTissueCode - input Tissue code to be fetched from DB
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = enterDataEditCertificateOfConformance(strTissueCode);
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Kamlesh Kumar[05-07-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean enterDataEditCertificateOfConformance(String strTissueCode)
		{
			boolean blnFlag = false;
			
			try		
			{
				String strSheetPrefix = strTissueCode.substring(0, 2);
				String strSheetType = strSheetPrefix +" SHEET";
				
				blnFlag = Page("pgeEditCertificateOfConformance").Element("txtMoistureTesting").Type("0.000");
				
				if (blnFlag)
				{
						blnFlag = Page("pgeEditCertificateOfConformance").Element("lstSheetType").Select(strSheetType);
						if (!blnFlag)
						{
							Err.Description = "Unable to select 'Sheet Type' from Sheet Type dropdown.";
						}
				}
				else
					Err.Description = "Unable to enter value in 'Moisture Testing' text-field.";		 
			}			
			catch (Exception e)
			{
				blnFlag = false;	
				Err.Description = "Error encountered while entering data.";				
			}
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: SearchPackagingCollectionDonorKeyAndClick
        //''@Objective: This Function searches for the entered donor Key,selects it on 'Packaging Collection' page and verifies the page navigated.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strDonorKey - Donor Key to be entered in the 'Donor' search-box
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = SearchPackagingCollectionDonorKeyAndClick(strDonorKey);
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Kamlesh Kumar[05-04-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean SearchPackagingCollectionDonorKeyAndClick(String strDonorKey)
		{
			boolean blnFlag = false;	
			
			try
			{
				blnFlag=verifySearch("pgeDonorstoPackage","txtDonorSearch","lnkDonorID", strDonorKey);
				
				if (blnFlag)
				{
						blnFlag=clickAndVerify("pgeDonorstoPackage", "lnkDonorID", "Packaging Collection");		
						if (!blnFlag)
						{
							Err.Description="Failed to navigate to 'Packaging Collection' page.";	
						}
				}
				else
					Err.Description="Failed to enter donor ID '"+strDonorKey+"' in the 'Donor' search-box.";		
			}
		
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occurred while selecting the Donor Key.";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: VerifyPackagingCollectionErrorMessages
        //''@Objective: This function verifies error messages for all the fields on 'Packaging Collection' page for a selected donor Key   
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - Page which contains all the fields (Packaging Collection)
		//''@	eleButton - button to be clicked on the same page
		//''@   strExpectedText - expected text of the error message
		//''@	eleCheckBox - list of the check-boxes to be selected passed as multiple objects
        //''@-------------------------------------------s--------------------------------------------------------------------------------
        //''@Example: blnStatus = VerifyPackagingCollectionErrorMessages("pgePackagingCollection","btnCommit",strExpectedText1,"chkPouch","chkInstructionsforUse","chkTURCard","chkStickers","chkCMVCard","chkBackLabel");	
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Kamlesh Kumar[04-21-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean VerifyPackagingCollectionErrorMessages(String Page,String eleButton,String strExpectedText,String... eleCheckBox)
		{
			boolean blnFlag = false;
			
			try
			{
				for(String element:eleCheckBox)
				{
					blnFlag = Page(Page).Element(element).Click(2);
					if (!blnFlag)
					{
						Err.Description = "Unable to select each check-box.";
						break;
					}
				}
				
				if(blnFlag)
				{
					if (Page(Page).Element("lstCMVCard").Exist())
					{
						blnFlag = Page(Page).Element("lstCMVCard").Select("SB171.004 (Negative)");
						if (!blnFlag)
						{
							Err.Description = "Unable to select a value from 'CMV Card' list.";
						}
					}

					blnFlag = verifyErrorMessage(Page,eleButton,"pgeCommon","objUIMessageErrorSummary",strExpectedText);
					if (!blnFlag)
					{
						Err.Description = "Failed to display error message '"+strExpectedText+"'.";
					}
				}
			}
			catch (Exception e)
			{
				blnFlag = false;	
				Err.Description = "Error encountered while selecting check-boxes.";				
			}
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fetchSelectedValueFromDropdown
        //''@Objective: This function fetches the currently selected value from a drop-down/list on a page and saves it in dicCommonValue  
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - Page which contains the list/drop-down
		//''@	Element - list/drop-down from the current selection is to be fetched
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = fetchSelectedValueFromDropdown("pgeConsignmentBilling","lstAgentName");
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Kamlesh Kumar[05-06-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean fetchSelectedValueFromDropdown(String Page,String Element)
		{
			boolean blnFlag = false;
			
			try
			{	
				String strXpath = Page(Page).Element(Element).GetXpath();
				
				if(!(strXpath.equals("") || strXpath.equals(null)))
				{
				
					 Select sel = new Select(driver.findElement(By.xpath(strXpath)));
					 
					 String strCurrentValue = sel.getFirstSelectedOption().getText();
					 
					 if(!(strCurrentValue.equals("") || strCurrentValue.equals(null)))
					 {	
						blnFlag = true;
						Script.dicCommonValue.put("strCurrentValue", strCurrentValue);
					 }
					 else
					 {
						 blnFlag = false;
						 Err.Description = "Unable to fetch the selected value from the dropdown.";
					 }
				}
				else
				{
					blnFlag = false;
					 Err.Description = "Unable to fetch xpath for the dropdown object.";
				}
					 
				 
			}
			catch (Exception e)
			{
				blnFlag = false;	
				Err.Description = "Error encountered while fetching the currently selected value.";				
			}
			return blnFlag;
		}	
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: VerifyDropDownNewSelectionErrorMessages
        //''@Objective: This function selects a value(other than the default one) from a drop-down/list,clicks on a button on the same 
		//''@           page and verifies error messages.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - Page which contains the drop-down/list
		//''@	Element - list/drop-down from which value is to be selected
		//''@   eleButton - button to be clicked on the same page
		//''@	eleError - element where the error message appears
		//''@	strExpectedText - Expected text of the error message
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = VerifyDropDownNewSelectionErrorMessages("pgeConsignmentBilling","lstAgentName","btnSave","objUIMessageWarnSummary",strExpectedText2);
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Kamlesh Kumar[05-06-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean VerifyDropDownNewSelectionErrorMessages(String Page,String Element,String eleButton,String eleError,String strExpectedText)
		{
			boolean blnFlag = false;
			
			try
			{	
					String strXpath = Page(Page).Element(Element).GetXpath();
					 
					if(!(strXpath.equals("") || strXpath.equals(null)))
					{
						blnFlag = Page(Page).Element(Element).Click(2);
						if (blnFlag)
						{
							//driver.findElement(By.xpath(strXpath)).getAttribute("");
							
							driver.findElement(By.xpath(strXpath)).sendKeys(Keys.ARROW_DOWN);
							
							driver.findElement(By.xpath(strXpath)).sendKeys(Keys.ENTER);				
							waitForSync(5);
							blnFlag = verifyErrorMessage(Page,eleButton,"pgeCommon",eleError,strExpectedText);
							if (!blnFlag)
							{
								Err.Description = "Error Message '"+strExpectedText+"' failed to display.";
							}
						}	
						else
							Err.Description = "Unable to click on dropdown to expand.";	
					}
					else
						Err.Description = "Unable to fetch xpath of the specified object.";
			}
			catch (Exception e)
			{
				blnFlag = false;	
				Err.Description = "Error encountered while selecting a value from the dropdown.";				
			}
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: selectValueClickButtonAndNavigate
        //''@Objective: This function selects a value from a list,clicks on the specified button on the same page and verify navigated page.   
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - Page which contains the list and the button to be clicked
		//''@	Element - list from which the value is to be selected
		//''@   strListValue - value to be selected from the list
		//''@	eleButton - button to be clicked on the same page
		//''@	strExpText - Expected page to be navigated
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = selectValueClickButtonAndNavigate("pgeConsignmentBilling","lstAgentName",strCurrentAgent,"btnSave","Consignment //''@                     Billing");
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Kamlesh Kumar[05-06-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean selectValueClickButtonAndNavigate(String Page,String Element,String strListValue,String eleButton,String strExpText)
		{
			boolean blnFlag = false;
			
			try
			{	
				blnFlag = Page(Page).Element(Element).Select(strListValue);
				
				if(blnFlag)
				{
					blnFlag = Page(Page).Element(eleButton).Click(5);
					if (blnFlag)
					{
						blnFlag = getPageTitle(strExpText);
						if (!blnFlag)
						{
							Err.Description = "Unable to navigate to the expected page.";
						}
					}
					else
						Err.Description = "Unable to click on the button.";
				}
				else
					Err.Description = "Unable to select specified value from the list.";
				 
			}
			catch (Exception e)
			{
				blnFlag = false;	
				Err.Description = "Error encountered while selecting a value and navigating to next page.";				
			}
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillEntriesForAllFieldAddNewUser
        //''@Objective: This function fill entries in all field of 'Add New User' page and click on save button and verify it.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------       
        //''@Input Parameters: 
		//''@	strLoginName - Login Name to be provided in text-box.
		//''@	strFullName - Full Name to be provided in text-box.
	    //''@	strEmail - Email to be provided in text-box.
		//''@	strConfMessage - Confirmation Message to be entered.
		//''@Example: blnStatus= fillEntriesForAllFieldAddNewUser("comment for testing","akhare","India@123")         
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[05-06-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean fillEntriesForAllFieldAddNewUser(String strLoginName, String strFullName,String  strEmail) 
		{
			boolean blnFlag = false;
			try
			{
				waitForSync(2);
				blnFlag= Page("pgeUsers").Element("txtNewUserName").Type(strLoginName);
				if (blnFlag)
				    {
				     blnFlag= Page("pgeUsers").Element("txtFullName").Type(strFullName); 
					if (blnFlag)
					{
						blnFlag= Page("pgeUsers").Element("txtEmail").Type(strEmail); 
						if (blnFlag)
						{
							blnFlag=clickAndVerify("pgeCommon", "btnSave", "Users");
							if (!blnFlag)
							{
							   /* blnFlag=verifyExactText("pgeUsers", "objInformationMessage", strConfMessage);
								if (!blnFlag)  
									Err.Description= Err.Description; //"Failed to click on 'Save' button.";	 */
									Err.Description= Err.Description; //"Failed to click on 'Save' button.";   	
							}		
						}	
						else
							Err.Description = "Unable to type in Email text area.";		
					}
					else
						Err.Description = "Unable to type in Full Name text area.";				
				}
				else
					Err.Description = "Unable to type in Login Name text area.";	
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Failed validate error message.";
			}		
	   return blnFlag;
	  }		
	  
	  //''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickChangePasswordAndSaveButton
        //''@Objective: This Function click on Change Password and Save button and verify Expected text.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strExpectedText - Expected Text to be entered.
        //''@---------------------------------------------------------------------------------------------------------------------------		  
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[06-May-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean clickChangePasswordAndSaveButton(String strExpectedText) 
		{
			boolean blnFlag = false;
			try
			{
				waitForSync(2);
				blnFlag= Page("pgeEditUser").Element("btnChangePassword").Click(20);
				if (blnFlag)
				{
				   blnFlag= Page("pgeEditUser").Element("btnChangeSave").Click(20);
				   if(blnFlag)
				   {
						blnFlag=verifyExactText("pgeCommon", "objUIMessageErrorSummary", strExpectedText);
						if (!blnFlag)
							Err.Description= Err.Description; //"Unable to click on save button."; 
				   } 
				   else	
					Err.Description= "Unable to click on 'Save' button in 'Change Password' pop up."; 				
				}	
				else
					Err.Description= "Unable to click on 'Change Password' button."; 
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Failed validate error message.";
			}
				
			return blnFlag;
		}	  
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickMissingYellowButtonInventoryReconciliation
        //''@Objective: This function clicks on 'Missing' button if it exists on 'Inventory Reconciliation' page otherwise clicks on 'Resolve' //''@			link and navigate to 'Resolve' page.  
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = clickMissingYellowButtonInventoryReconciliation();
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Kamlesh Kumar[05-06-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean clickMissingYellowButtonInventoryReconciliation()
		{
			boolean blnFlag = false;
			
			try
			{	
				blnFlag = Page("pgeFieldTracking").Element("btnMissing").Exist();
				if (blnFlag)
				{
					blnFlag = Page("pgeFieldTracking").Element("btnMissing").Click(20);
					if (blnFlag)
					{
						waitForSync(5);
						blnFlag = Page("pgeFieldTracking").Element("lnkResolve").Click(20);
					}
				}
				else
				{
					blnFlag = Page("pgeFieldTracking").Element("lnkResolve").Click(20);
				}
				 
			}
			catch (Exception e)
			{
				blnFlag = false;	
				Err.Description = "Error encountered while selecting a value and navigating to next page.";				
			}
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: deselectResolutionActionTakenListsOnFieldTracking
        //''@Objective: This function de-selects 'Resolution' and 'Action Taken' drop-downs on 'Field Reconciliation Inventory-Resolve Anomaly' 
		//''@           page if there is already a default selection.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = deselectResolutionActionTakenListsOnFieldTracking();
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Neha Chauhan[05-13-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean deselectResolutionActionTakenListsOnFieldTracking()
		{
			boolean blnFlag = false;
			
			try
			{	
				blnFlag = Page("pgeFieldTracking").Element("lstResolution").IsValueSelected("---");
				if (!blnFlag)
				{
					blnFlag = Page("pgeFieldTracking").Element("lstResolution").Select("---");
					if (!blnFlag)
					{
						Err.Description = "Unable to de-select 'Resolution' drop-down on 'Reconcile Field Inventory' page.";		
					}
				}
				else
					Err.Description = "Unable to verify current selection for the 'Resolution' drop-down.";	
				
				blnFlag = Page("pgeFieldTracking").Element("lstActionTaken").IsValueSelected("---");
				if (!blnFlag)
				{
					blnFlag = Page("pgeFieldTracking").Element("lstResolution").Select("---");
					if (blnFlag)
					{
						Err.Description = "Unable to de-select 'Action Taken' drop-down on 'Reconcile Field Inventory' page.";	
					}
				}
				else
					Err.Description = "Unable to verify current selection for the 'Action Taken' drop-down.";	
			}
			catch (Exception e)
			{
				blnFlag = false;	
				Err.Description = "Error encountered while selecting a value and navigating to next page.";				
			}
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
		//''@Function ID: 
		//''@Function Name: searchDonorClickandVerifyDonorIDLink
		//''@Objective: This Function searches for a 'Recovery ID' passed as test-data on 'Donor Processing' page, clicks on the associated   
		//''@           donor's link under 'Donor' column and verifies 'Donor Tissues' page is displayed.
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Return Desc: 
		//''@     Success - True
		//''@     Failure - False //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	eleInput - Input element where test-data to be searched is entered
		//''@	objSearchGrid - search grid where searched data populates
		//''@	strRecoveryId - Recovery ID to be passed as test data
		//''@---------------------------------------------------------------------------------------------------------------------------		  
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Created by[Date]: Neha Chauhan[12-May-2015]
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Reviewed by[Date]: 
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@History Notes: 
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@###########################################################################################################################		
		public boolean searchDonorClickandVerifyDonorIDLink(String eleInput,String objSearchGrid,String strRecoveryId)
		{
			boolean blnFlag = false;
			try
			{
				waitForSync(2);
				blnFlag=verifySearch("pgeDonor_List",eleInput,objSearchGrid,strRecoveryId);
				if (blnFlag)
				{
				   blnFlag = clickandVerifyDonorIDLink(strRecoveryId, "Donor Tissues");
				   if(!blnFlag)
					{
						Err.Description= "Unable to select Recovery ID '"+strRecoveryId+"' link.";
					} 
				}
				else	
					Err.Description= "Unable to search with Recovery ID '"+strRecoveryId+"' on 'Donor Tissues' page."; 				
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Failed to verify expected page.";
			}
				
			return blnFlag;
		}	  
		
		//''@###########################################################################################################################
		//''@Function ID: 
		//''@Function Name: VerifyHoldReleaseAmnionErrorMessages
		//''@Objective: This function clicks on specified 'Hold Amnion/Chorion' or 'Release Amnion/Chorion' button,selects a refrigerator,enters 
		//''@ 			blank/invalid 'Refrigerator Date' and verifies error messages displayed on 'Donor Tissues' page.
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Return Desc: 
		//''@     Success - True
		//''@     Failure - False //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	eleExpectedButton - button to be clicked on 'Donor Tissues' page.
		//''@	strRefrigerator - Refrigerator to be selected
		//''@	strInputText - Invalid/Blank value to be entered in 'Refrigerator Date' field.
		//''@	strExpectedText - expected text of the error message
		//''@---------------------------------------------------------------------------------------------------------------------------		  
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Created by[Date]: Neha Chauhan[12-May-2015]
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Reviewed by[Date]: 
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@History Notes: 
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@###########################################################################################################################		
		public boolean VerifyHoldReleaseAmnionErrorMessages(String eleExpectedButton,String strRefrigerator,String strInputText,String strExpectedText)
		{
			boolean blnFlag = false;
			try
			{
				waitForSync(2);
				blnFlag= Page("pgeDonorProcessing").Element(eleExpectedButton).Click(20);
				if (blnFlag)
				{
				   blnFlag = Page("pgeDonorProcessing").Element("lstRefrigerator").Select(strRefrigerator);
				   if(blnFlag)
					{
						blnFlag = Page("pgeDonorProcessing").Element("txtRefrigeratorDate").Type(strInputText);
						if (blnFlag)
						{
							blnFlag = verifyErrorMessage("pgeDonorProcessing","btnCommit","pgeCommon","objUIMessageErrorSummary",strExpectedText);
							if (!blnFlag)
							{
								Err.Description= "Unable to verify error message '"+strExpectedText+"' on entering blank/invalid 'Refrigerator Date'."; 	
							}
						}
						else
							Err.Description= "Unable to enter 'Refrigerator Date' on the pop-up."; 	
					}
					else
						Err.Description= "Unable to select Refrigerator '"+strRefrigerator+"' from 'Refrigerator' drop-down on the pop-up."; 
				}
				else	
					Err.Description= "Unable to click on specified button on 'Donor Tissues' page."; 				
			}
			
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Failed to verify expected page.";
			}
				
			return blnFlag;
		}	  
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: strikeAndInitialExists
        //''@Objective: This Function verifies strike & Initial button exists or not and if exists then click on it.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= strikeAndInitialExists()   
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[05-13-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean strikeAndInitialExists()
		{
			boolean blnFlag = false;
			blnFlag=Page("pgeReimbursementRates").Element("btnStrikeInitial").Exist();
			if(blnFlag)
			{
			    blnFlag = Page("pgeReimbursementRates").Element("btnStrikeInitial").Click(20);
				if (blnFlag)
				{
					blnFlag = Page("pgeReimbursementRates").Element("btnCertify").Exist();
					if (!blnFlag)
						Err.Description="Unable to existence of 'Certify' button.";  
				}
			    else
					Err.Description="Unable to click on Strike & Initial button.";
			}		  
			else
			{
				blnFlag = Page("pgeReimbursementRates").Element("btnCertify").Exist();
				if (!blnFlag)
					Err.Description="Unable to existence of 'Certify' button.";  
			}
	
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clearDateRecoveryPage
        //''@Objective: This Function clears the date mentioned on recovery page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	PageName - The Page Name where the search box is present.
		//''@	ElementName - The Object name where the search text needs to be entered.	
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clearDateRecoveryPage(PageName, ElementName)        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[14-May-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################	
		public boolean clearDateRecoveryPage(String Page, String Element) 
		{
			boolean blnFlag = false;			
			try
			{
				//Page(Page).Element(Element).Type("");
				String tmpXpath= Page(Page).Element(Element).GetXpath();
				driver.findElement(By.xpath(tmpXpath)).sendKeys(Keys.BACK_SPACE);
				driver.findElement(By.xpath(tmpXpath)).sendKeys(Keys.chord(Keys.CONTROL, "a"));
				driver.findElement(By.xpath(tmpXpath)).sendKeys(Keys.DELETE);
				waitForSync(3);
				blnFlag = true;
			}
				
				catch (Exception e)
			{
				e.printStackTrace();
				Err.Description = "Unable to clear the contents.";
			}
				return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: validateProccessScan
        //''@Objective: This function validates the negative functionality on proccess scan page
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strRecoveryId - Recovery Id to be entered in process scan area.		
       	//''@	strRefrigirator - Refrigirator to be selected from list on proccess scan page.	
		//''@	strErrorText - ErrorText to be verified on page.	
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= validateProccessScan("WA14G099","RFI-015Q","You must choose a refrigerator") 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish khare[05-12-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean validateProccessScan(String strNewTissueID,String strExistingRecoveryId,String strRefrigerator,String strErrorText,String eleMessageArea)
		{
			String strRecoveryId;
			boolean blnFlag = false;
			if(strExistingRecoveryId.isEmpty())
			   {
				strRecoveryId = strNewTissueID.substring(0, 5)+String.format("%03d",(Integer.parseInt(strNewTissueID.substring(5,8)) + 1));
				boolean blnFlag1=sendValuetoCommonSheet("strRecoveryId",strRecoveryId);
				if (!blnFlag1) 
					Err.Description = "The value '"+strRecoveryId+"' is not pushed successfully to the field 'strFullName' in the Common Sheet.";;
			   }
			   else 
			strRecoveryId=strExistingRecoveryId;
			blnFlag=Page("pgeRecovery").Element("txtScanList").Type(strRecoveryId);
			if(blnFlag==true)
			{
				blnFlag=Page("pgeRecovery").Element("lstRefrigerator").Type(strRefrigerator);
				 if(blnFlag==true)
				 {
				 blnFlag=verifyErrorMessage("pgeRecovery","btnProcessScans", "pgeCommon" ,eleMessageArea,strErrorText);
				 if (!blnFlag) 
					Err.Description = Err.Description;
					}
				 else
				  Err.Description="Unable to select 'Refrigirator' from dropdown.";				
			}
			else
				Err.Description="Unable to enter 'Recovery Id' in edit box.";			
			
			return blnFlag;	
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: enterSurgeon
        //''@Objective: This Function fills the surgeon name text box on Add Recovered tissue page
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strSurgeon - Surgeon name associated with the hospital.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= enterSurgeon("Barbara Simmons, MD")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[14-05-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean enterSurgeon(String strSurgeon)
		{
			boolean blnFlag = false;
			
			blnFlag=Page("pgeNewRecoveredTissue").Element("txtSurgeon").Type(strSurgeon);
			waitForSync(4);
			
			if (blnFlag == true) 
			{
				String xpath=Page("pgeNewRecoveredTissue").Element("txtSurgeon").GetXpath();
				driver.findElement(By.xpath(xpath)).sendKeys(Keys.RETURN);
				waitForPageRefresh(200);
				waitForSync(3);
				blnFlag=Page("pgeNewRecoveredTissue").Element("btnRecruited").Exist();
				if (!blnFlag)
						Err.Description = "Recruited button not displayed on 'Add Recovered Tissue' page.";				
			} 
			else
				Err.Description = "Unable to type surgeon name.";	

			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: calcTissueCode
        //''@Objective: This function calculates an invalid tissue code and enters it in Scan area. 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strRecoveryId - Tissue code to be entered in scan area.				
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= calcTissueCode("GS77") 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish khare[05-15-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean calcTissueCode(String strTissueCode)
		{
			boolean blnFlag = false;
			strTissueCode = strTissueCode.substring(0, 2)+String.format("%03d",(Integer.parseInt(strTissueCode.substring(2,4)) + 1));
			blnFlag=Page("pgeSterilizationReturns").Element("txtscanListSter").Type(strTissueCode);
	
		    if (blnFlag)
			{
				Script.dicTestData.put("InvalidTissue", strTissueCode);
			}
			else			
				Err.Description = Err.Description;
			return blnFlag;	
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyPartialText
        //''@Objective: This Function verifies that the expected text is contained in the object or not.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	PageName - The page mapping to the object which needs to be validated.
		//''@	Element - The object which needs to be validated if it contains the expected text.
		//''@	strExpText - The expected text to be validated , if it is present in the Element.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyPartialText("pgeDonorstoPackage", "objStatusResult", "Needs Pre-packaging Inspection Signature")       
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Neha Chauhan[08-22-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean verifyPartialText(String PageName, String Element, String strExpText)
		{
			boolean blnFlag = false;
			String strActualText = Page(PageName).Element(Element).GetText();
			strActualText=strActualText.trim();
			strExpText=strExpText.trim();
			
			if (strActualText.contains(strExpText))
			{
				blnFlag = true;
			}
			else
			{
				Err.Description = "The expected text, '"+strExpText+"' does not contain the actual element's text '"+strActualText+"'.";
				blnFlag = false;				
			}
			return blnFlag;
		}			
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fillPackagingPageErrorCondition
        //''@Objective: This function fills the Packaging Assembly Page with the required entries. 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@strAssemblyLine - The Assembly Line value that needs to be selected from the Assembly Line dropdown.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fillPackagingPageErrorCondition("Line 1")       
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[05-19-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean fillPackagingPageErrorCondition(String strAssemblyLine)
		{
			boolean blnFlag = false;
						
			blnFlag = Page("pgePackagingCollection").Element("chkProductCarton").Click(20);
			if (blnFlag)
			{
				blnFlag = Page("pgePackagingCollection").Element("chkPouch").Click(20);
				if (blnFlag)
				{
					blnFlag = Page("pgePackagingCollection").Element("chkInstructionsforUse").Click(20);
					if (blnFlag)
					{
						blnFlag = Page("pgePackagingCollection").Element("chkTURCard").Click(20);
						if(blnFlag)
						{
							blnFlag = Page("pgePackagingCollection").Element("chkStickers").Click(20);
							if(blnFlag)
							{
								blnFlag = Page("pgePackagingCollection").Element("chkCMVCard").Click(20);
								if(blnFlag)
								{
									blnFlag = Page("pgePackagingCollection").Element("chkBackLabel").Click(20);
									if(!blnFlag)
									{
										Err.Description = "Unable to click the 'Back Label' check box.";
									}
								}
								else
								Err.Description = "Unable to click the 'CMV Card' check box.";
							}
							else
							Err.Description = "Unable to click the 'Stickers (If Applicable)' check box.";
						}
						else
						Err.Description = "Unable to click the 'TUR Card' check box.";
					}
					else
					Err.Description = "Unable to click the 'Instructions for Use' check box.";
				}
				else
				Err.Description = "Unable to click the 'Pouch' check box.";
			}
			else
			Err.Description = "Unable to click the 'Product Carton' check box.";
			
			//Proceed only if the checkboxes are all clicked
			blnFlag = Page("pgePackagingCollection").Element("lstAssemblyLine").Select(strAssemblyLine);
			if(blnFlag)
			{			
				if(Page("pgePackagingCollection").Element("lstCMVCard").Exist())
				{
						blnFlag = Page("pgePackagingCollection").Element("lstCMVCard").Select("SB170.004 (Positive)");
						waitForSync(10);
						if(!blnFlag)
							Err.Description = Err.Description;
					}
					else
					Err.Description = "Unable to select the CMV Card value from the 'CMV Card' dropdown.";
				}
			else
			Err.Description = "Unable to select the Assembly Line value '"+strAssemblyLine+"' from the 'Assembly Line' dropdown.";
							
			return blnFlag;	
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: scanTissueandVerifyErrorMsg
        //''@Objective: This function inputs a tissue into the Scan text box, hits 'Enter', and verifies if the object passed is 
		//''@ present on the page or not.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	strDonorKey - The Donor Key which is to be validated in the pdf.
		//''@	strExpText - The expected text to validate in pdf.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= scanTissueandVerifyErrorMsg("pgePackagingCollection","ABcd",strErrorText,"txtScanTissue_Assembly","objTissue_Assembly","objLabelPrinting")
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[05-20-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean scanTissueandVerifyErrorMsg(String Page,String strTissue,String strErrorText,String ElementScan,String ElementTissue,String eleMessageArea)
		{
			boolean blnFlag = false;
			String xpath=Page(Page).Element(ElementScan).GetXpath();
			if((strTissue.equals("")))
			{
			driver.findElement(By.xpath(xpath)).sendKeys(Keys.ENTER);
			blnFlag = true;
			//blnFlag = Page(Page).Element(ElementScan).sendKeys(Keys.ENTER);
			}
			else
			blnFlag = Page(Page).Element(ElementScan).Type(strTissue);
					
			if (blnFlag)
			{
				//Alternative to hitting 'Enter' key, CLick on the tissue in the grid.
				blnFlag = Page(Page).Element(ElementTissue).Click(20);
				waitForPageRefresh();
				if(blnFlag)
				{
					blnFlag=verifyTextContains("pgeCommon", eleMessageArea, strErrorText);
					if(!blnFlag)
						Err.Description=Err.Description;
				}
				else
					Err.Description="Unable to click on of the element '"+ElementTissue+"'. This click is to simulate same behavior of Enter key press.";
			}
			else			
				Err.Description="Unable to type in the value '"+strTissue+"' in the element '"+ElementScan+"'.";
						
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: completeTissueInspection
        //''@Objective: This function takes a donor product through to Inspection which has only  1 tissue..   		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= completeTissueInspection("smoketest","smoketest2","India@123","Line 2")
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[05-21-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean completeTissueInspection(String strAssemblyUserName,String strUserNameReviewer,String strPassword,String strLineName) throws Exception
		{
			boolean blnFlag = false;
			String strDonorKey;
			blnFlag=verifySearch("pgeDonorstoPackage","txtStatusSearch","objStatusResult", "New");
			waitForSync(10);
			if(blnFlag)
			{			
			blnFlag=clickAndVerify("pgeDonorstoPackage", "lnkQuantity", "pgeDonorstoPackage", "objQuantity");		
				if (blnFlag)
				{	
					strDonorKey = Page("pgePackagingCollection").Element("eleDonorID").GetText();
					blnFlag=sendValuetoCommonSheet("StrDonorKey_Negative",strDonorKey);	
					if (!(strDonorKey.equals("") || strDonorKey.equals(null)))
					{
						blnFlag=clickAndVerify("pgeDonorstoPackage", "lnkDonorID", "Packaging Collection");	
						  if(blnFlag)
						  {
							blnFlag = fillPackagingPage(strAssemblyUserName);
							  if(blnFlag)
								{
							     blnFlag = clickAndVerifyNotExist("pgeCommon", "btnCommit", "pgeCommon", "btnCommit");		
									if(blnFlag)
										{
											blnFlag = logoutMiMedx(strAssemblyUserName);	
										if(blnFlag)
											{											
											 blnFlag = loginMiMedx(strUserNameReviewer, strPassword);
											if(blnFlag)
											{	
								              blnFlag=clickAndVerify("pgeAssembly_Lines", "lnkDonorstoPackage", "Donors to Package");	
											waitForSync(10);											  
											 if(blnFlag)
												{	
												blnFlag=verifySearch("pgeDonorstoPackage","txtDonorSearch","lnkDonorID", strDonorKey);
												if(blnFlag)
												{	
												blnFlag=verifySearch("pgeDonorstoPackage","txtStatusSearch","objStatusResult", "Needs Pre-packaging Inspection Signature");
												if(blnFlag)
												{	
													blnFlag=clickAndVerify("pgeDonorstoPackage", "lnkQuantity", "pgeDonorstoPackage", "objQuantity");		
													waitForSync(10);
												 if(blnFlag)
												 {
												 blnFlag=clickAndVerify("pgeDonorstoPackage", "lnkDonorID", "Packaging Assembly");		
												  if(blnFlag)
													{
													blnFlag= fillPackagingPage(strUserNameReviewer,strLineName);	
													if(blnFlag)
													{													
						                             blnFlag=clickAndVerify("pgePackagingCollection", "btnNext", "pgePackagingCollection", "objScanTissuestobePackage");	
													 if(blnFlag)
													{													
													 blnFlag=scanTissueandVerify("pgePackagingCollection","txtScanTissue_Assembly","objTissue_Assembly","objLabelPrinting");
													 waitForSync(10);
													if(blnFlag)
													{	
													 blnFlag=clickAndVerify("pgePackagingCollection", "btnNext", "pgePackagingCollection", "objPackagedBy");
													 if(blnFlag)
													{
													 blnFlag = canvasSignature(driver);	
													 waitForSync(10);
													  if(blnFlag)
													{
														blnFlag=clickAndVerifyNotExist("pgeCommon", "btnCommit", "pgeCommon", "btnCommit");
														waitForSync(10);
														 if(blnFlag)
													{
														blnFlag = logoutMiMedx(strUserNameReviewer);		
                                                        if (!blnFlag) 
														Err.Description = Err.Description;														
													}				
						
									else
									Err.Description="Not able to click on 'commit' button";						
				  }
				else
				Err.Description="Not able to enter signature";
			}	
				else
			Err.Description="Not able to click on 'Next' button";
			}				
			// If the Archive Sample is not there, just return 'True' and quit function
			else
			Err.Description="Not able to scan tissue";	
			}
			else
			Err.Description="Not able to click on 'Next' button";
			}
			else
			Err.Description="Not able to fill the packaging information";
			}
			else
			Err.Description="Not able to click on link for Donor Key";
			}
			else
			Err.Description="Not able to sort ascendingly with Quantity";
			}
			else
			Err.Description="Not able to search with status 'Needs Pre-packaging Inspection Signature'";
			}
			else
			Err.Description="Not able to search with the given donor key'"+strDonorKey+"'";
			}
			else
			Err.Description="Not able to login with user name'"+strUserNameReviewer+"'";
			}
			else
			Err.Description="Not able to logout with username'"+strAssemblyUserName+"'";
			}
			else
			Err.Description="Not able to click on button 'Commit'";
			}
			else
			Err.Description="Not able to fill the 'Packaging' information";
			}
			else
			Err.Description="Not able to enter donor key'"+strDonorKey+"'in common sheet";
			}
			else
			Err.Description="Not able to click the link for Donor ID";
			}
			else
			Err.Description="Not able to sort ascendingly with Quantity";
			}
			else
			Err.Description="Not able to search with status 'New'";
			}
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: chkAndVerifyMultipleErrorMessages
        //''@Objective: This function validates the negative functionality on Packaging Inspection page
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strRecoveryId - Recovery Id to be entered in process scan area.		
       	//''@	strRefrigirator - Refrigirator to be selected from list on proccess scan page.	
		//''@	strErrorText - ErrorText to be verified on page.	
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= chkAndVerifyMultipleErrorMessages("pgeRecovery","chkTURCard","btnNext",strErrorMessage) 
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish khare[05-21-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean chkAndVerifyMultipleErrorMessages(String Page,String eleCheckbox,String strPage,String eleButton,String strErrMessages)
		{
			boolean blnFlag = false;
			blnFlag=Page(Page).Element(eleCheckbox).Click(20);
			if(blnFlag==true)
			{
				
				 blnFlag=verifyMultipleErrorMessages(strPage,eleButton,"pgeCommon","objUIMessageErrorSummary",strErrMessages);  
				 if (!blnFlag) 
					Err.Description = Err.Description;
			}				
			else
				Err.Description="Unable to select the checkbox";			
			
			return blnFlag;	
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyErrorMessage
        //''@Objective: This function enter/clear data from any previous field,enters data in the new field and verify error message 
		//''@			on clicking a button on the page (overloaded existing function to clear/enter in previous field)
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	pgeName - The Page Name where the element is present.
		//''@	elePrevInput - input element to enter/clear data from
		//''@	strPrevValuetoEnter - value to enter in the previous field or clear it out
		//''@	eleInput - The element in which input text to be entered.
		//''@	strInputText - Input text to be validated.		
		//''@	pgeButton - The Page Name on which the Button to be clicked is present.		
		//''@	eleButton - The button on which click action to be performed.		
		//''@	pgeError - The Page Name where the error element is present.
		//''@	eleError - The error message element.
		//''@	strExpText - Expected text to be validated.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = verifyErrorMessage("pgeHospitals", "txtPrefix", "", "txtDate",strDate,"pgeCommon", "btnSave", "pgeCommon", "objErrorMessage", strExpText); 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Neha Chauhan[05-26-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean verifyErrorMessage(String pgeName, String elePrevInput, String strPrevValuetoEnter,String eleInput, String strInputText, String pgeButton, String eleButton, String pgeError, String eleError, String strExpText)
		{
			boolean blnFlag = false;	
			try
			{
				blnFlag = Page(pgeName).Element(elePrevInput).Type(strPrevValuetoEnter);
				if (blnFlag)
				{
					waitForSync(10);
					blnFlag = Page(pgeName).Element(eleInput).Type(strInputText);
					if (blnFlag) 
					{	
						waitForSync(5);
						blnFlag = Page(pgeButton).Element(eleButton).Click(20);
						if(blnFlag)
						{
							blnFlag = verifyTextContains(pgeError, eleError, strExpText);
							if (!blnFlag)
								Err.Description = Err.Description;
						}
						else
							Err.Description = "Unable to click on button.";
					} 
					else
						Err.Description = "Unable to type in input field.";
				}
				else
					Err.Description = "Unable to type '"+strPrevValuetoEnter+"' in the previous field '"+elePrevInput+"'.";
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occurred while validating the error message.";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: addAmnionChorionAndSaveTissueDetails
        //''@Objective: This function add amnion/chorion tissues as passed from outside and verify if 'commit' populates on the same page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strTissueType - Type of issue to be added passed from outside(Amnion/Chorion)
		//''@	strHeight - Height of amnion/chorion
		//''@	strWidth - Width of amnion/chorion	
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = addAmnionChorionAndSaveTissueDetails(strTissueType,strHeight,strWidth);
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Neha Chauhan[05-28-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean addAmnionChorionAndSaveTissueDetails(String strTissueType,String strHeight, String strWidth)
		{
			boolean blnFlag = false;	
			try
			{
				blnFlag=addAmnionorChorion(strTissueType,strHeight,strWidth);
				if (blnFlag)
				{
					waitForSync(10);
					blnFlag= Page("pgeCommon").Element("btnCommit").Exist();
					if (!blnFlag)
						Err.Description = "Failed to display 'Commit' button on 'Raw Tissue Assessment' page.";
				}
				else
					Err.Description = Err.Description;
			
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occurred while adding 'Tissue Assessment' details.";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: donorProcessingAddProductDehydrationData
        //''@Objective: This function enters all the required data on 'Product Dehydration - Donor Processing' click on 'Commit' button.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strFixture - Fixture to be added under 'Fixtures'
		//''@	strLoadNumber - Load Number to be added under 'Fixtures'
		//''@	strEquipmentType - Equipment Type to be added under 'Equipments'		
		//''@	strEquipment - Equipment to be added under 'Equipments'		
		//''@	strSheet - Name of the sheet to be added		
		//''@	strEquipmentPouch - Equipment Pouch to be added under 'Pouches'
		//''@	strSealedAtTemp - Value to be added under 'Pouches'
		//''@	strFullName - Full name of the logged-in user to be verified(Smoke Tester)		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = donorProcessingAddProductDehydrationData(strFixture,strLoadNumber,strEquipmentType,strEquipment,strSheet
		//''@					,strEquipmentPouch,strSealedAtTemp,strFullName);
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Neha Chauhan[05-27-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean donorProcessingAddProductDehydrationData(String strFixture,String strLoadNumber, String strEquipmentType,String strEquipment,String strSheet,String strEquipmentPouch,String strSealedAtTemp,String strFullName)
		{
			boolean blnFlag = false;	
			try
			{
				blnFlag=fillFixtureFields(strFixture, strLoadNumber);
				if (blnFlag)
				{
					blnFlag=clickAndVerify("pgeProductDehydration", "btnAddFixture", "pgeProductDehydration", "lnkDelete");
					if (blnFlag)
					{
						blnFlag=verifyFixtureGridProductDehydration(strFixture, strLoadNumber);
						if (blnFlag)
						{
							blnFlag = clickAndVerifyVisible("pgeProductDehydration", "lnkNoEquipmentSpecified", "pgeProductDehydration", "btnAddEquipment");
							if (blnFlag)
							{
								blnFlag = selectEquipmentTypeListBox(strEquipmentType);
								if (blnFlag)
								{
									blnFlag = selectEquipmentListBox(strEquipment,"Equipment");
									if (blnFlag)
									{
										blnFlag = selectConfirmTempCheckBox();
										if (blnFlag)
										{
											blnFlag=enterCurrentDate("pgeProductDehydration","txtDate_Equipment","MM-dd-YYYY");  
											if (blnFlag)
											{
												blnFlag = Page("pgeProductDehydration").Element("btnAddEquipment").Click(20);
													if (blnFlag)
													{
														blnFlag = verifyEquipmentGridProductDehydration(strEquipmentType,  strEquipment, strFullName);
														if (blnFlag)
														{
															blnFlag = clickAndVerifyVisible("pgeProductDehydration", "lnkNoConfigurationsSpecified", "pgeProductDehydration", "btnStartSheet");
															if (blnFlag)
															{
																blnFlag = Page("pgeProductDehydration").Element("txtSheet").Type(strSheet);
																if (blnFlag)
																{
																	blnFlag = selectEquipmentListBox(strEquipment,"Sheet");
																	if (blnFlag)
																	{
																	
																			blnFlag = clickAndVerify("pgeProductDehydration", "btnStartSheet", "pgeProductDehydration", "lnkEquipment_Sheet");		
																			if (blnFlag)
																			{
																				blnFlag = verifyStartSheetTable(strEquipment,strFullName);
																				if (blnFlag)
																				{
																					blnFlag = clickAndVerifyVisible("pgeProductDehydration", "lnkNoPouchesSpecified", "pgeProductDehydration", "btnAddPouch");
																					if (blnFlag)
																					{
																						blnFlag = Page("pgeProductDehydration").Element("txtLotNumber").Type(strLoadNumber);
																						if (blnFlag)
																						{
																							blnFlag = Page("pgeProductDehydration").Element("lstEquipment_Pouches").Select(strEquipmentPouch);
																							if (blnFlag)
																							{
																								blnFlag = Page("pgeProductDehydration").Element("txtSealedAt").Type(strSealedAtTemp);
																								if (blnFlag)
																								{
																									blnFlag = Page("pgeProductDehydration").Element("btnAddPouch").Click(20);
																									if (blnFlag)
																									{
																										blnFlag = verifyPouchesTable(strLoadNumber , strEquipmentPouch , strSealedAtTemp);   
																										if (blnFlag)
																										{
																											blnFlag=clickAndVerify("pgeProductDehydration", "btnCommit", "pgeProductDehydration", "lnkPrintForm");
																											if (!blnFlag)
																											{
																												Err.Description="Failed to click on 'Commit' button.";
																											}
																										}
																										else
																											Err.Description="Failed to verify data added under 'Pouches' table.";
																									}
																									else
																										Err.Description="Failed to click on 'Add Pouch' button.";
																								}
																								else
																									Err.Description="Failed to enter 'Sealed At' temperature:'"+strSealedAtTemp+"'.";
																							}
																							else
																								Err.Description="Failed to select Equipment Pouch:'"+strEquipmentPouch+"'.";
																						}
																						else
																							Err.Description="Failed to enter Lot #:'"+strLoadNumber+"'.";
																					}
																					else
																						Err.Description="Failed to expand 'No Pouches Specified' section.";
																				}
																				else
																					Err.Description="Failed to verify data added under Start Sheet table.";
																			}
																			else
																				Err.Description="Failed to click on 'Start Sheet' button.";
																		}
																		else
																			Err.Description="Failed to select 'Equipment' from 'Equipment' list.";
																	}
																	else
																		Err.Description="Failed to enter sheet name:'"+strSheet+"' in 'Sheet' field.";
																}
																else	
																	Err.Description="Failed to expand 'No Configurations Specified' section.";
															}
															else
																Err.Description="Failed to verify data added under 'Equipment' section.";
														}
														else
															Err.Description="Failed to click on 'Add Equipment' button.";
													}
													else
														Err.Description="Failed to enter current date in 'Equipment' Section.";
												}
												else
													Err.Description="Failed to select 'Dryer' temperature.";
											}
											else
												Err.Description="Failed to select Equipment:'"+strEquipment+"' under 'Equipment' section.";
										}
										else
											Err.Description="Failed to select Equipment Type:'"+strEquipmentType+"' under 'Equipment' section.";
									}
									else
										Err.Description="Failed to expand 'No Equipment Specified' section.";
								}
								else
									Err.Description="Failed to verify data under 'Fixtures' section.";
							}
							else
								Err.Description="Failed to click on 'Add Fixture' button.";
						}
						else
							Err.Description="Failed to add Fixture:'"+strFixture+"' and Load Number:'"+strLoadNumber+"' under 'Fixture' section.";
					}

			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occurred while adding 'Product Dehydration' details.";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyStartSheetTable
        //''@Objective: This Function verifies the 'Start' log with Start details of the Sheet.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 			
		//''@	strEquipment - The value of the Equipment that needs to be verified from the Start grid on Product Dehydration page.		
		//''@	strFullName - The user's full name that needs to be verified from the Start grid on Product Dehydration page.					
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyStartSheetTable("TD-005","akhare");   
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Neha Chauhan[05-27-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean verifyStartSheetTable(String strEquipment, String strFullName)
		{
			boolean blnFlag = false;
			String tempVar;
								
			Page("pgeProductDehydration").Element("//*[contains(@id,'tblsheetlist')]/td").MouseHover();					
			tempVar = Page("pgeProductDehydration").Element("//*[contains(@id,'tblsheetlist')]/td/a").GetText();
			blnFlag = tempVar.equalsIgnoreCase(strEquipment);
							
			if (blnFlag == true) 
			{
				tempVar= Page("pgeProductDehydration").Element("//*[contains(@id,'tblsheetlist')]/td[3]").GetText();
				blnFlag = tempVar.equalsIgnoreCase(strFullName);		
				if (!blnFlag)
					Err.Description = strFullName+" entry is not correct under 'Name' in the Starts and Stops table in the Stops section.";	
			}										
			else
				Err.Description = strEquipment+" entry is not correct in the under 'Sheet' in the Starts and Stops table.";							
		
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: reimbursementRatesSelectFutureMonthYear
        //''@Objective: This Function selects a future month, a future year from respective drop-downs on 'Reimbursement Rates' page
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= reimbursementRatesSelectFutureMonthYear();       
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Neha Chauhan[06-05-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean reimbursementRatesSelectFutureMonthYear() 
		{
			boolean blnFlag = false;
			try
			{
			
				// Calculate future month / Year
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MONTH, +2);
				cal.add(Calendar.YEAR, +2);
				Date result = cal.getTime();
				String strDate= new SimpleDateFormat("MMM,yyyy").format(result);
				String [] strCalcMonYear = strDate.split(",");
				
				// Select future month from dropdown
				blnFlag = Page("pgeReimbursementRates").Element("drpMonth").Select(strCalcMonYear[0]);
				if(blnFlag)
				{
					// Enter future year 
					waitForSync(10);
					blnFlag = Page("pgeReimbursementRates").Element("txtYear").Type(strCalcMonYear[1]);	
					waitForSync(10);
					if(!blnFlag)
					{
						Err.Description="Unable to select future year:'"+strCalcMonYear[1]+"' from 'Year' drop-down.";		
					}
				}
				else	
					Err.Description="Unable to select future month:'"+strCalcMonYear[0]+"' from 'Month' drop-down.";		
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occurred while selecting future month and year.";
			}
		return blnFlag;
		
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: newDonorProduct_AddTissue
        //''@Objective: This Function adds a new new tissue code to a selected recovery ID with a valid quantity on 'Add Tissue' page within
		//''@			'Donor Processing' flow.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= newDonorProduct_AddTissue("GS44","2");    
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Neha Chauhan[06-05-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean newDonorProduct_AddTissue(String strTissueCode,String strQuantity) 
		{
			boolean blnFlag = false;
			try
			{
				blnFlag=Page("pgeAddTissue").Element("txtTissueCode").Type(strTissueCode);
				if (blnFlag)
				{
					blnFlag=Page("pgeAddTissue").Element("txtQuantity").Type(strQuantity);
					if (blnFlag)
					{
						blnFlag=clickAndVerify("pgeAddTissue", "btnSave", "pgeDonorProcessing", "lnkAddedTissue");
						if (!blnFlag)
						{	
							Err.Description = "Failed to click on the added tissue:'"+strTissueCode+".";
						}
					}
					else
					Err.Description = "Failed to enter Quantity:'"+strQuantity+".";
				}
				else
					Err.Description = "Failed to enter Tissue Code:'"+strTissueCode+".";
			}
			
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occurred while adding a tissue on 'New Donor-Product' page.";
			}
		return blnFlag;
		
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: processScanForPackaging
        //''@Objective:  This function enters 'Archive Samples' and 'Moisture Testing' tissue codes in the scan area,clicks on 'Commit' button
		//''@			 on 'Validate Outer Packaging' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= processScanForPackaging(strArchiveTissue,strMoistureTissue);      
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Neha Chauhan[09-19-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean processScanForPackaging(String strArchiveTissue,String strMoistureTissue)
		{
			boolean blnFlag = false;
			try
			{
					String strXpath =Page("pgeAddTissue").Element("txtScanListPackaging").GetXpath();			
					if (!(strXpath.equals("") || strXpath.equals(null)))
					{
						driver.findElement(By.xpath("//*[@id='rform:scanList']")).sendKeys(strArchiveTissue);
						driver.findElement(By.xpath("//*[@id='rform:scanList']")).sendKeys(Keys.RETURN);
						driver.findElement(By.xpath("//*[@id='rform:scanList']")).sendKeys(strMoistureTissue);
						driver.findElement(By.xpath("//*[@id='rform:scanList']")).sendKeys(Keys.RETURN);
						waitForSync(5);
						blnFlag=Page("pgeCommon").Element("btnCommit").Click(20);
						if (blnFlag)
						{
							blnFlag=Page("pgeAddTissue").Element("btnGenerateLabel").Exist();
							if (!blnFlag)
								Err.Description="Failed to populate 'Generate Label' button.";
						}
						else
							Err.Description="Failed to click on 'Commit' button on 'Validate Outer Packaging' page.";
					}
					else
						Err.Description="Failed fetch the xpath for the scan-list on 'Validate Outer Packaging' page.";	
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occurred during Process Scan of tissues for packaging.";
			}
		return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: fetchTissueForPackaging
        //''@Objective: This function fetches the 'Archive Samples' and 'Moisture Testing' tissue codes from the UI and saves them as 
		//''@			dicCommonValues for further use.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@      Page - Page from which the tissue is to be fetched
        //''@      Element1 - 'Archive Samples' Object from which the Tissue value is to be fetched
		//''@	   Element2 - 'Moisture Testing' Object from which the Tissue value is to be fetched
        //''@      pageTitle - Verify the Page title from which tissue needs to be fetched		
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus=fetchTissuesForPackaging("pgeInventoryLocation","objArchiveSampleTissue","objMoistureTestingTissue","Inventory Location"); 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Neha Chauhan[09-19-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################  
		public boolean fetchTissuesForPackaging(String Page, String Element1, String Element2,String pageTitle) 
		{
			boolean blnFlag = false;
			
			String strArchiveTissue = Page(Page).Element(Element1).GetText();
			blnFlag = !strArchiveTissue.isEmpty();
			
			if(blnFlag)
			{
				Script.dicCommonValue.put("strArchiveTissue",strArchiveTissue);						
			}
			else
				Err.Description = "Tissue not fetched from '"+pageTitle+"' page.";
			
			String strMoistureTissue = Page(Page).Element(Element2).GetText();
			blnFlag = !strMoistureTissue.isEmpty();
			
			if(blnFlag)
			{
				Script.dicCommonValue.put("strMoistureTissue",strMoistureTissue);						
			}
			else
				Err.Description = "Tissue not fetched from '"+pageTitle+"' page.";
												
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: calcTissueCodeAndScan
        //''@Objective: This function calculates a valid but non-inspected tissue code,saves it as common value and enters it in Scan area on 'Add Tissue' screen.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strTissueCode - Tissue code to be entered in scan area.				
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= calcTissueCodeAndScan(strTissueCode);
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Neha Chauhan[05-19-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean calcTissueCodeAndScan(String strTissueCode)
		{
			boolean blnFlag = false;
			String strNewItemName = null;
			String strNonInspectedTissue = null;
			try
			{    
				if (strTissueCode != null)
				{
					String [] strtemparray = strTissueCode.split("-");
					//If the last character is a numeral, then get the numeral and add 1 to it
					if((strtemparray[(strtemparray.length)-1]).matches("-?\\d+(\\.\\d+)?"))
					{
					   // Increment the number by one
					   strtemparray[(strtemparray.length)-1] = String.valueOf(Integer.parseInt((strtemparray[(strtemparray.length)-1])) + 1);
					   waitForSync(5);
					   strNewItemName = StringUtils.join(strtemparray, "00").trim();
					   String strValue = strNewItemName.substring(strNewItemName.length() - 3);
					   strNonInspectedTissue = strtemparray[0] +"-"+ strtemparray[1] +"-"+ strValue;
					   waitForSync(5);
					   blnFlag=Page("pgeAddTissue").Element("txtScanListPackaging").Type(strNonInspectedTissue);
					   if (blnFlag)
					   {	
							waitForSync(5);
							Script.dicCommonValue.put("strNonInspectedTissue",strNonInspectedTissue);      
					   }
					   else
						   Err.Description = "Unable to enter Non-Inspected Tissue code: '"+strNonInspectedTissue+"' in scan-area.";
					}
					else
						Err.Description = "Invalid tissue code specified.";
				}
				else
					Err.Description = "No Tissue code specified.";
			}
			
			catch (Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occurred while scanning the non-inspected tissue code.";
			}
		return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickRefrigeratorShowLogLink
        //''@Objective: This function clicks on specified recoveryid and calls the function getPageTitle() to verify user is on correct page or not.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strRecoveryId - The Recovery ID of the tissue which needs to be clicked.
		//''@	strExpText - The page title which needs to be validated after click of the Recovery ID.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickRefrigeratorShowLogLink(strRefrigerator,strExpText);      
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Neha Chauhan[05-15-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean clickRefrigeratorShowLogLink(String strRefrigerator, String strExpText) 
		{
			boolean blnFlag = false;
			
			Page("pgeRefrigerators").Element("//*[text()='" +strRefrigerator+ "']//following::a[2]").Click(20);
			waitForSync(5);
			blnFlag = getPageTitle(strExpText);
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyMultipleErrorMessages
        //''@Objective: This functions over-rides the existing function to just verify the multiple error messages on a page 
		//''@			without performing any action.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	eleErrMessage - The element where the error element is present.
		//''@	pgeError - The Page Name where the error element is present.
		//''@	strErrMessages - Expected text for each of the error message to be validated.		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus=blnFlag = verifyMultipleErrorMessages(PageName,Element,strErrMessages) ;
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]:Neha Chauhan[06-18-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean verifyMultipleErrorMessages(String pgeError,String eleErrMessage,String strErrMessages) 
		{
			boolean blnFlag = false;
			String strNotFound = "";
			try
			{
					//Fetching the xpath for the object containing all the error messages
					String strXpath = Page(pgeError).Element(eleErrMessage).GetXpath();
					
					//creating a list pf web-elements for each error message
					java.util.List<WebElement> lstErrMessages = driver.findElements(By.xpath(strXpath));
					//fetching the size of the list
					int intSize = lstErrMessages.size();
						
					// splitting the list of errors passed from testdata
					String[] options = strErrMessages.trim().split(";");
					
					int counter = 0;
					
					//verifying if count of error messages passed from test-data matches the count on the UI
					if (intSize==options.length)
					{

						//iterates through each option name
						for (int i=0; i < options.length; i++)
						{
							
							for (int j=0; j < intSize; j++)
							{
								//fetches text for each web-element for each error message
								String strValue = lstErrMessages.get(j).getText();
								if (strValue.trim().equalsIgnoreCase(options[i]))
								{
									counter++;
									break;
								}
								else
								{
									if  (j == intSize-1)
	                                  {
										strNotFound= strNotFound +"'"+ options[i] +"'";
	                                  } 
								}
							}
						}
						if (counter==intSize)
						{
							blnFlag = true;
						}
						else
						{
							Err.Description = strNotFound + " Error messages not found.";
							blnFlag = false;
						}	
					}
					else
					{
						blnFlag = false;
						Err.Description = "List of error messages passed as test data: ("+strErrMessages+") is not matching with the number of error messages appearing on the page.";
					}
			}
				
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occured while validating the error message.";
			}
		return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: locationInventoryVerifyErrorMessages
        //''@Objective: This Function handles 'Reconcile'/'Reset' scanning pop-ups and verifies error messages on 'Location Inventory' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: strPlaceofFuncCall - String which refers to the Element on which action to be performed.
        //''@      strExpText - Location name to be used in script.
		//''@		PageName - Page on which error messages appearing
		//''@		Element - The element where the error element is present.
		//''@		strErrMessages - Expected text for each of the error message to be validated.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= locationInventoryVerifyErrorMessages("Reconcile","Validate Inventory at Archive Samples","pgeCommon","objUIMessageErrorSummary",strExpectedText1);        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Neha Chauhan[06-19-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean locationInventoryVerifyErrorMessages(String strPlaceofFuncCall,String strExpText,String PageName, String Element, String strErrMessages) 
		{
			boolean blnFlag = false;
			try
			{
				blnFlag=handleAlertsLocationInventory(strPlaceofFuncCall,strExpText);
				if (blnFlag)
				{
					blnFlag = verifyMultipleErrorMessages(PageName,Element,strErrMessages) ;
					if (!blnFlag)
					{
						Err.Description = "Failed to display error messages '"+strErrMessages+"'.";
					}
				}
				else
					Err.Description =  Err.Description;
			}
			
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occurred while verifying error messages on 'Location Inventory' page.";
			}
		return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyGridDataDisappearsOnAddRemoveItems
        //''@Objective: This function verifies that the list of available users/selected users in a grid disappears once user enters
		//''@			invalid data in respective text-fields in the grid.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	PageName - Page on which the grid exists
        //''@	strSearchText - Invalid/Junk value to be entered
		//''@	eleInput - input field where data is entered
		//''@	eleTissueGrid - grid object from where users disappears (Available or Selected)
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = verifyGridDataDisappearsOnAddRemoveItems("pgeEmailDistribution",strSearchText,"txtSelectedUsers","lstSelectedUsers"); 
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]:Neha Chauhan[06-25-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean verifyGridDataDisappearsOnAddRemoveItems(String PageName,String strSearchText , String eleInput,String eleTissueGrid) 
		{
			boolean blnFlag = false;	
			try
			{
				blnFlag = Page(PageName).Element(eleInput).Type(strSearchText);
				if (blnFlag) 
				{
					String strXpath = Page(PageName).Element(eleTissueGrid).GetXpath();
					
					//creating a list pf web-elements for each user present in the grid
					java.util.List<WebElement> lstOustandingTissues = driver.findElements(By.xpath(strXpath + "/li"));
					
					//fetching the size of the list
					int intSize = lstOustandingTissues.size();
					
					
					for (int j=0; j < intSize; j++)
					{
						String strAttribute = lstOustandingTissues.get(index).getAttribute("style");
						if (strAttribute.trim().contains("none"))
						{
							blnFlag = true;
						}
						else
						{
							blnFlag = false;
							Err.Description = "Unable to fetch 'style' attribute of the specified object.";
							break;
                        } 
					}
					blnFlag = clearFieldandWait(PageName,eleInput);
					if (!blnFlag)
					{
						Err.Description = "Unable to clear out invalid/junk data from the input field.";
					}
				}
				else
					Err.Description = "Unable to enter invalid/junk values in the specified field.";
				
			}
			catch(Exception e)
			{
				blnFlag = false;
				Err.Description = "Error occurred while validating the error message.";
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: verifyAssemblyLineErrorMessage
        //''@Objective: This function verify 'Add Assembly Line' page Error Message.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	eleCheckbox1 - First checkbox to be checked.
		//''@	strDropdown - Dropdown object from which value is to be selected..
		//''@	strSelectListValue - Select list value to be selected from the dropdown.
		//''@	eleCheckbox2 - Second checkbox to be checked.
		//''@	strErrMessage-Error message to be verified on page.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: //blnStatus=verifyAssemblyLineErrorMessage("pgeAssemblyLines","rdoVerifyDeviceOnlineSmallBarCode_No","lstDeviceName_ProductLabel",
		//"rdoVerifyDeviceOnlineProductLabel_Yes","pgeCommon","btnSave","")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Kamlesh Kumar[06-30-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean verifyAssemblyLineErrorMessage(String pgeName,String eleCheckbox1,String strDropdown,String eleCheckbox2,String page, String eleButton,String strErrMessage )
		{
			boolean blnFlag = false;
						
			blnFlag=Page(pgeName).Element(eleCheckbox1).Click(20);
			if(blnFlag)
			{
			    //blnFlag = Page(pgeName).Element(strDropdown).Select(strSelectListValue);
				blnFlag=selectFirstOptionFromDropdown(pgeName,strDropdown);
				if(blnFlag)
				{
				    blnFlag=Page(pgeName).Element(eleCheckbox2).Click(20);
					if(blnFlag)
					{
					    blnFlag=verifyErrorMessage(page,eleButton,"pgeCommon","objUIMessageErrorSummary",strErrMessage);
						 if (!blnFlag)
						     Err.Description = Err.Description;
					}
					else
					  Err.Description="Unable to select the second checkbox.";
				}
				else
				   Err.Description = "was not selected from the list box.";
			
			}
			else
			   Err.Description="Unable to select the first checkbox.";
			
			return blnFlag;	
		}	
		
		//''@##################################################################################################################
        //''@Function ID: 
        //''@Function Name: VerifyCheckboxesSelection
        //''@Objective: This function verifies all the non-archive check boxes depending on the action to be performed on all the tissues		
		//''@(i.e. @'Pass'/'Reject'/'Micronize'/'Outer Pouch Seal'/'Outer Label') in Tissue Inspection/Validate Outer Packaging page and then verifies the //''@same, leaving the archive samples as it is. 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	Page - Page name to mapping the object on the page.
        //''@	Element - object name from which the quantity of tissues can be retrieved.
		//''@	strAction - The action that is to be taken on all the tissues for Inspection- E.g. Pass/Reject/Micronize.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= VerifyCheckboxesSelection("pgeAddTissue","objQuantity","Pass")        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Kamlesh Kumar[07-14-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@----------------------------------------------------------------------------------------------------------------
		
		public boolean VerifyCheckboxesSelection(String Page, String Element, String strAction)
		{
		
		    boolean blnFlag = false;
			String strqty;
			int intTemp = 0;
			int intqty = 0;
			String strObjectIdentifier = "";
		
		    strqty = Page(Page).Element(Element).GetText();
			intqty = Integer.parseInt(strqty);
			
			if (strAction.equalsIgnoreCase("Pass"))
			{
				strObjectIdentifier = "pass";
				// Start the loop leaving the first two archive sample as it is, so start loop from the third row
				intTemp = intTemp + 2;	
				blnFlag = true;				
			}
			else if (strAction.equalsIgnoreCase("Reject"))
			{
				strObjectIdentifier = "reject";
				blnFlag = true;
			}
			else if (strAction.equalsIgnoreCase("Micronize"))
			{
				strObjectIdentifier = "micro";
				blnFlag = true;
			}
			else if (strAction.equalsIgnoreCase("Ragged Edge"))
			{
				strObjectIdentifier = "raggededge";
				blnFlag = true;
			}
			else if (strAction.equalsIgnoreCase("Indistinct Logo"))
			{
				strObjectIdentifier = "indistinctlogo";
				blnFlag = true;
			}
			else if (strAction.equalsIgnoreCase("Size"))
			{
				strObjectIdentifier = "irregularshape";
				blnFlag = true;
			}
			else if (strAction.equalsIgnoreCase("Air Pocket"))
			{
				strObjectIdentifier = "notflat";
				blnFlag = true;
			}
			else if (strAction.equalsIgnoreCase("Debris"))
			{
				strObjectIdentifier = "debris";
				blnFlag = true;
			}
			else if (strAction.equalsIgnoreCase("Chorion Missing"))
			{
				strObjectIdentifier = "chorionmissing";
				blnFlag = true;
			}
			//Set the object according to the type of columns in Validate Outer Packaging page
			else if (strAction.equalsIgnoreCase("Outer Pouch Seal"))
			{
				strObjectIdentifier = "outseal";
				// Reduce the quantity to the actual number so that the loop can stop checking at the right time
				intqty = intqty - 2;
				blnFlag = true;
			}
			else if (strAction.equalsIgnoreCase("Outer Label"))
			{
				strObjectIdentifier = "outlbl";
				// Reduce the quantity to the actual number so that the loop can stop checking at the right time
				intqty = intqty - 2;
				blnFlag = true;
			}
			else 
			{
				blnFlag = false;
				Err.Description = "Pass valid paramaters to the function 'clickCheckboxesTissueInspection'.";
			}
			
			if(blnFlag)
			{
			   while((intTemp<intqty) && (blnFlag!=false)) 
			   {
			   String strValue =  driver.findElement(By.xpath("//*[@id='rform:tbltissueitems:"+intTemp+":item"+strObjectIdentifier+"']//following::span")).getAttribute("class");
			   blnFlag = strValue.contains("check");
			   intTemp = intTemp + 1;
			   }
			   
			if(!blnFlag)	
						Err.Description = "All the non-archive tissues '"+strAction+"' checkboxes have not been clicked correctly.";
			}
			return blnFlag;
			
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: VerifyDropDownNewSelectionErrorMessages
        //''@Objective: This function selects a value(other than the default one) from a drop-down/list,clicks on a button on the same 
		//''@           page and verifies error messages.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - Page which contains the drop-down/list
		//''@	Element - list/drop-down from which value is to be selected
		//''@   pgeError - Page on which error message appears
		//''@   eleButton - button to be clicked on the same page
		//''@	eleError - element where the error message appears
		//''@	strExpectedText - Expected text of the error message
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = //''@VerifyDropDownNewSelectionErrorMessages("pgeConsignmentBilling","lstAgentName","pgeCommon","btnSave","objUIMessageWarnSummary",strExpectedText2);
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Neha Chauhan[07-24-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean VerifyDropDownNewSelectionErrorMessages(String Page,String Element,String pgeError,String eleButton,String eleError,String strExpectedText)
		{
			boolean blnFlag = false;
			
			try
			{	
					String strXpath = Page(Page).Element(Element).GetXpath();
					 
					if(!(strXpath.equals("") || strXpath.equals(null)))
					{
						blnFlag = Page(Page).Element(Element).Click(2);
						if (blnFlag)
						{
							//driver.findElement(By.xpath(strXpath)).getAttribute("");
							
							driver.findElement(By.xpath(strXpath)).sendKeys(Keys.ARROW_DOWN);
							
							driver.findElement(By.xpath(strXpath)).sendKeys(Keys.ENTER);				
							waitForSync(5);
							blnFlag = verifyErrorMessage(pgeError,eleButton,"pgeCommon",eleError,strExpectedText);
							if (!blnFlag)
							{
								Err.Description = "Error Message '"+strExpectedText+"' failed to display.";
							}
						}	
						else
							Err.Description = "Unable to click on dropdown to expand.";	
					}
					else
						Err.Description = "Unable to fetch xpath of the specified object.";
			}
			catch (Exception e)
			{
				blnFlag = false;	
				Err.Description = "Error encountered while selecting a value from the dropdown.";				
			}
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: selectFirstOptionFromDropdown
        //''@Objective: This function selects the first option available in a drop-down be fetching the xpath and performs actions using send keys
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@	Page - Page which contains the drop-down/list
		//''@	Element - list/drop-down from which value is to be selected
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus = selectFirstOptionFromDropdown("pgeConsignmentBilling","lstAgentName");
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Neha Chauhan[07-24-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean selectFirstOptionFromDropdown(String Page,String Element)
		{
			boolean blnFlag = false;
			
			try
			{	
					String strXpath = Page(Page).Element(Element).GetXpath();
					 
					if(!(strXpath.equals("") || strXpath.equals(null)))
					{
						blnFlag = Page(Page).Element(Element).Click(2);
						if (blnFlag)
						{
							//driver.findElement(By.xpath(strXpath)).getAttribute("");
							
							driver.findElement(By.xpath(strXpath)).sendKeys(Keys.ARROW_DOWN);
							
							driver.findElement(By.xpath(strXpath)).sendKeys(Keys.ENTER);				
						}	
						else
							Err.Description = "Unable to click on dropdown to expand.";	
					}
					else
						Err.Description = "Unable to fetch xpath of the specified object.";
			}
			catch (Exception e)
			{
				blnFlag = false;	
				Err.Description = "Error encountered while selecting a value from the dropdown.";				
			}
			return blnFlag;
		}		
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: completePackagingInspectionFlow
        //''@Objective: This function takes a donor product?(that is through to Inspection having 1 tissue) and completes the entire
		//''@			'Packaging Inspection' flow so as to make it available for 'Packaging History' flow execution.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
		//''@		
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= completePackagingInspectionFlow("Line 2","smoketest2");
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Neha Chauhan[07-24-2015]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean completePackagingInspectionFlow(String strAssemblyLine,String strInspectionUserName) throws Exception
		{
			boolean blnFlag = false;
			
			try
			{
				blnFlag=clickAndVerify("pgeAssembly_Lines", "lnkPackagingInspection", "Packaging Inspection");
				if (blnFlag)
				{
					blnFlag=Page("pgePackagingCollection").Element("lstAssemblyLine_PackagingInspection").Select(strAssemblyLine);	
					if (blnFlag)
					{
						blnFlag=Page("pgePackagingCollection").Element("btnNext").Click(20);
						if (blnFlag)
						{
							blnFlag= scanTissueandVerify("pgePackagingCollection","txtScanTissue_SmallBarCode","objTissue_Inspection","objScanPouchBarCode");
							if (blnFlag)
							{
								blnFlag= scanTissueandVerify("pgePackagingCollection","txtScanTissue_Inspection","objPackageItemNumber","objScanProductLabel");
								if (blnFlag)
								{
									blnFlag= scanTissueandVerify("pgePackagingCollection","txtScanTissue_Inspection","objPackageItemNumber","objPleaseVerifyRemainingItems");
									if (blnFlag)
									{
										blnFlag= selectPackageInspectionCheckboxes();
										if (blnFlag)
										{
											blnFlag=clickAndVerify("pgeCommon", "btnSave", "pgePackagingCollection", "objInspectedBy");		
											if (blnFlag)
											{
												blnFlag=electronicSignaturePackaging(strInspectionUserName);	
												if (blnFlag)
												{
													blnFlag=clickAndVerifyNotExist("pgeCommon", "btnCommit", "pgeCommon", "btnCommit");		
													if (blnFlag)
													{
														blnFlag= verifyTextContains("pgePackagingCollection", "objMessage", "was moved successfully to Packaged Goods.");
														if (!blnFlag)
														{
															Err.Description = Err.Description;
														}
													}
													else
														Err.Description = Err.Description;
												}
												else
													Err.Description = Err.Description;
											}
											else
												Err.Description = Err.Description;
										}
										else
											Err.Description = Err.Description;
									}
									else
										Err.Description = Err.Description;
								}
								else
									Err.Description = Err.Description;
							}
							else
								Err.Description = Err.Description;
						}
						else
							Err.Description = "Failed to click on 'Next' button";
					}
					else
						Err.Description = "Failed to select Assembly Line '"+strAssemblyLine+"'.";
				}
				else
					Err.Description = Err.Description;
			}
			catch (Exception e)
			{
				blnFlag = false;	
					Err.Description = "Error encountered while completing 'Packaging Inspection' flow.";				
			}
			
			return blnFlag;
		}		

}
