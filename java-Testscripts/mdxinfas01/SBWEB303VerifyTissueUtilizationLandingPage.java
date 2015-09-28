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

import java.text.SimpleDateFormat;
import java.util.Date;


import mdxinfas01.*;

public class SBWEB303VerifyTissueUtilizationLandingPage extends Script {
	private static final Logger logger = Logger.getLogger(SBWEB303VerifyTissueUtilizationLandingPage.class);
    	
    @Override
    public void run() throws Exception {
    
    	boolean blnFlag = false;
    	String strStepDesc="";
		String strExpResult="";		
		java.util.Date date= new java.util.Date();
   	    String timeStamp =new Timestamp(date.getTime()).toString().replace(" ","").replace(":","").replace("-", "").replace(".","");
	
		// ########################################################################################################
		// # Test Case ID: SBWEB-303
		// # Test Case Name: VerifyTissueUtilizationLandingPage
		// #-------------------------------------------------------------------------------------------------------
		// # Description: This scripts verifies that the user can sign up and reach to the Tissue Utilization
		// # landing page and then successfully log out.
		// #-------------------------------------------------------------------------------------------------------
		// # Pre-conditions: User credentials are communicated to the user.
		// # 				 MiMedx SBWeb URL is accessible to the user.
		// # Post-conditions: NA
		// # Limitations: NA
		// #-------------------------------------------------------------------------------------------------------
		// # Owner: Aarti Suresh
		// # Created on: 09-24-2014
		// #-------------------------------------------------------------------------------------------------------
		// # Reviewer: 
		// # Review Date:
		// #-------------------------------------------------------------------------------------------------------
		// # History Notes:
		// ########################################################################################################

		//Getting data from config file  
		String strURL=Script.dicConfigValues.get("strApplicationURL");
		
		//Getting data from master test data file
		String strUserName = Script.dicTestData.get("strUserName");
		String strPassword = Script.dicTestData.get("StrPassword");

		// ########################################################################################################
		// Step 1 - Launch browser.
		// Open application URL.
		// ########################################################################################################
		strStepDesc = "Launch browser and open SBWEB application URL.";
		strExpResult = "Browser and SBWEB application URL should be opened.";
		blnFlag = LaunchApplicationUrl(strURL);
		if (blnFlag) 
		{
			reporter.ReportStep(strStepDesc, strExpResult, "SBWEB application url: '"+strURL+"' launched successfully.","Pass");
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
		// Step 3 - Click on Quality Assurance link.
		// ########################################################################################################
		strStepDesc = "Click on 'Quality Assurance' link given on left navigation area.";
		strExpResult = "'Orders' link should be clicked.";
		blnFlag=clickAndVerifyVisible("pgeAssembly_Lines", "lnkQuality_Assurance", "pgeAssembly_Lines", "lnkTissueUtilization");
		if (blnFlag == true) 
		{
			reporter.ReportStep(strStepDesc, strExpResult, "'Quality Assurance' link clicked successfully.", "Pass");
		} 
		else 
		{
			reporter.ReportStep(strStepDesc, strExpResult, Err.Description, "Fail");
		}

		// ########################################################################################################
		// Step 4 - Click on 'Tissue Utilization' link and verify landing page.
		// ########################################################################################################
		strStepDesc = "Click on 'Tissue Utilization' link and verify 'Tissue Utilization' landing page is displayed.";
		strExpResult = "'Tissue Utilization' landing page should be verified successfully.";
		blnFlag=clickAndVerify("pgeAssembly_Lines","lnkTissueUtilization", "Tissue Utilization");
		if (blnFlag == true) 
		{
			reporter.ReportStep(strStepDesc, strExpResult, "'Tissue Utilization' link is clicked and 'Tissue Utilization' landing page is verified successfully.", "Pass");
		}
		else 
		{
			reporter.ReportStep(strStepDesc, strExpResult, Err.Description, "Fail");
		}

		// ########################################################################################################
		// Step 5 - Logout from SBWEB test application.
		// ########################################################################################################
		strStepDesc = "Logout from SBWEB test application";
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
        //''@Example: blnStatus= LaunchApplicationUrl("http://mdxinfas01:8080/SBWEB/")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= loginMiMedx("akhare","India@123")        //''@---------------------------------------------------------------------------------------------------------------------------
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
					if (!blnFlag)
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
        //''@Example: blnStatus= logoutMiMedx("akhare")        //''@---------------------------------------------------------------------------------------------------------------------------
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
			Page("pgeAssembly_Lines").Element("btnLogout").Click(20);
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
        //''@Example: blnStatus= clickAndVerify(Page, Element, NewPage, NewElement)        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= getScreenTitle()        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= getPendingStatus()        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= clickLink("WC14F036", "Recovered Tissue")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= getPageTitle("Recovered Tissue")        //''@---------------------------------------------------------------------------------------------------------------------------
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
			String PageTitle = Page("pgeRecovered_Tissue").Element("objRecoveredTissuePage").GetText();
			if (PageTitle.contains(strExpText)) 
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
        //''@Example: blnStatus= canvasSignature(driver)        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@	strUser - The Signature initials of the user which need to be added in the 'Add Recovered Tissue' page.
		//''@	strPassword - The Signature password of the user which need to be added in the 'Add Recovered Tissue' page.
		//''@	strCollectionAgent - The Collection Technician's name which needs to be selected in the 'Add Recovered Tissue' page.
		//''@	dteDate - The date which needs to be added in the 'Add Recovered Tissue' page.
        //''@	strSurgeon - Surgeon name associated with the hospital.
		//''@	strFirstName - First name of the patient.
		//''@	strLastName - Last name of the patient.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= addTissue("akhare", "India@123","Hanif Anum", "Jun 29, 2014 00:00", "Barbara Simmons, MD", "Jane","Doe")        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[11-Jul-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean addTissue(String strUser,String strPassword, String strCollectionAgent, String dteDate, String strSurgeonName, String strtFirstName, String strLastName)
		{
			boolean blnFlag = false;
			waitForSync(2);
			
			blnFlag=enterSchedule(strSurgeonName,strtFirstName,strLastName);
			waitForSync(1);
			
			if (blnFlag == true) 
			{
				blnFlag=Page("pgeNewRecoveredTissue").Element("dteSelectDate").Type(dteDate);
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
											blnFlag = Page("pgeNewRecoveredTissue").Element("lstCollectionTech").Select(strCollectionAgent);//canvasSignature(driver);
												if (blnFlag == true)
												{
													blnFlag = Page("pgeNewRecoveredTissue").Element("txtUserName").Type(strUser);
													if (blnFlag == true)
													{
														blnFlag = Page("pgeNewRecoveredTissue").Element("txtPassword").Type(strPassword);
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
															Err.Description = "Unable to enter password.";
													}
													else
														Err.Description = "Unable to enter User Name.";
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
        //''@Example: blnStatus= enterSchedule("Barbara Simmons, MD", "Jane","Doe")        //''@---------------------------------------------------------------------------------------------------------------------------
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
				waitForPageRefresh();
				blnFlag=Page("pgeNewRecoveredTissue").Element("btnRecruited").Exist();
				
				if (blnFlag == true) 
				{
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
        //''@Example: blnStatus= verifySearch(PageName, ElementName, loadedElementName, "WC14F036")        //''@---------------------------------------------------------------------------------------------------------------------------
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
			try
			{
				Page(Page).Element(Element).Type(text);
				final String tempSource = driver.getPageSource();
				(new WebDriverWait(driver, 7)).until(new ExpectedCondition<Boolean>() 
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
				Err.Description="Timed out after 7 seconds.Searched element not found.";
			}
			if( blnFlag==true)
			{			
				String eleLink = Page(Page).Element(newElement).GetText();
				if (eleLink.contains(text)) 
				{
					blnFlag = true;
				} 
				else 
				{
					blnFlag = false;
					Err.Description = "Searched text not displayed in grid.";
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
        //''@Example: blnStatus= clickHistoryLink("WC14F036", "Donor Tissue")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= clickRecoveryFormLink("WC14F036", "Incoming Tissue Form")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= clickRefrigeratorLogLink("WC14F036", "Incoming Tissue Form")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= searchSerologyResult("Yes")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= waitForPageRefresh()        //''@---------------------------------------------------------------------------------------------------------------------------
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
				(new WebDriverWait(driver, 7)).until(new ExpectedCondition<Boolean>() 
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
				Err.Description="Timed out after 7 seconds wait.";
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
        //''@Example: blnStatus= clickLoadReviewForm(Status)        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= uploadFile("E:\\Workspace\\PDF_Files\\secondsdoc.pdf")        //''@---------------------------------------------------------------------------------------------------------------------------
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
			
			waitForSync(1);
			try {
				
				String[] dialog =  new String[]{"./AutoIt/Upload.exe",strFilePath, strDialog};
				Runtime.getRuntime().exec(dialog);
//				Process process = new ProcessBuilder("./AutoIt/Upload.exe",strFilePath, strDialog).start();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driver.findElement(By.xpath("//input[@id='rform:file']")).click();
			waitForSync(3);
			boolean flag=Page("pgeRecovered_Tissue").Element("btnLoad_File").Click(20);
			if (flag==true)
			{
				blnFlag=true;
			}
			else
			{
				Err.Description = "Unable to click on 'Load File' button.";
				blnFlag=false;
			}
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
        //''@Example: blnStatus= verifyScan("Ok")        //''@---------------------------------------------------------------------------------------------------------------------------
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
		//''@	strUser - The User Initials signature that needs to be entered on 'Pass Release Form' page.
		//''@	strPassword - The User password that needs to be entered on 'Pass Release Form' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= releasePassForm("Testing", "akhare", "India@123")        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[08-07-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean releasePassForm(String strPassComment, String strUser, String strPassword) 
		{
			boolean blnFlag = false;
			waitForSync(1);
			blnFlag=Page("pgeForms_Package").Element("txtUserComments").Type(strPassComment);
			if (blnFlag==true)
			{
				blnFlag=Page("pgeForms_Package").Element("txtSignUser").Type(strUser);
				if (blnFlag==true)
				{
					blnFlag=Page("pgeForms_Package").Element("txtSignPassword").Type(strPassword);
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
        //''@Example: blnStatus= markForReview()        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= verifyTissueStatus("pgeRecovered_Tissue", "objTissueStatus", "Eligibility Review")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= verifyRejectedStatus("Testing", "akhare", "India@123")        //''@---------------------------------------------------------------------------------------------------------------------------
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
				blnFlag=Page("pgeRecovered_Tissue").Element("txtUserIdRejectWindow").Type(strUserId);
				if (blnFlag==true)
				{
					blnFlag=Page("pgeRecovered_Tissue").Element("txtPasswordRejectWindow").Type(strPassword);
					if (blnFlag==true)
					{
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
					}
					else
						Err.Description = "Unable to type in Password.";
				}
				else
					Err.Description = "Unable to type in UserId.";
			}
			else
				Err.Description = "Unable to type in reason in text area.";
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
        //''@Example: blnStatus= verifySaveFunctionality("QA comment for testing")         //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= fillEntriesForNotEligible("comment for testing","akhare","India@123")         //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= fillEntriesForIncomplete("comment for testing","akhare","India@123")         //''@---------------------------------------------------------------------------------------------------------------------------
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
		//''@	strUser - User ID initials that needs to be entered in 'Tissue is Eligible for Transplant' window.
		//''@	strPassword - Password that needs to be entered in 'Tissue is Eligible for Transplant' window.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fillEntriesForEligible("comment for testing","akhare","India@123")         //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[08-05-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean fillEntriesForEligible(String strEligibleComment, String strUser, String strPassword) 
		{
			boolean blnFlag = false;
			waitForSync(2);
			blnFlag=Page("pgeForms_Package").Element("txtUserComments").Type(strEligibleComment);
			if (blnFlag==true)
			{
				blnFlag=Page("pgeForms_Package").Element("txtSignUser").Type(strUser);
				if (blnFlag==true)
				{
					blnFlag=Page("pgeForms_Package").Element("txtSignPassword").Type(strPassword);
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
        //''@Example: blnStatus= validateOpenedWindow(Page,Element,Title)         //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= fillEntriesAcceptAndRelease("comment for testing","akhare","India@123")         //''@---------------------------------------------------------------------------------------------------------------------------
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
				blnFlag=Page("pgeForms_Package").Element("txtSignUser").Type(strUser);
				if (blnFlag==true)
				{
					blnFlag=Page("pgeForms_Package").Element("txtSignPassword").Type(strPassword);
					if (blnFlag==true)
					{
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
        //''@Example: blnStatus= fillEntriesTransferTissue("RFI-15R")         //''@---------------------------------------------------------------------------------------------------------------------------
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
			driver.findElement(By.xpath("//a[contains(text(),'"+strRecoveryId+"')]/parent::*/preceding-sibling::td//input[@type='checkbox']")).sendKeys(Keys.SPACE);
			//Place the Donor Key in a variable for furture use
			strDonorKey = Page("pgeDonor_List").Element("objDonorKey").GetText();
			Script.dicTestData.put("strDonorKey", strDonorKey);
			if (( driver.findElement(By.xpath("//a[contains(text(),'"+strRecoveryId+"')]/parent::*/preceding-sibling::td//input[@type='checkbox']")).isSelected()) == true)
			{				
				blnFlag = true;
			}
			else
			{
				blnFlag = false;
				Err.Description = "Recovery Checkbox was not checked successfully.";
			}
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
        //''@Example: blnStatus= verifyPrepopulatedData("PA14G020")         //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@	
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyManufacturingLineTechnicianObjects()         //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-05-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean verifyManufacturingLineTechnicianObjects() throws Exception 
		{
			boolean blnFlag = false;
			blnFlag=Page("pge_ProcessRecoveredTissues").Element("lstManufacturingLine").Exist();
					
			if (blnFlag == true)
			{	
				blnFlag = Page("pge_ProcessRecoveredTissues").Element("txtTechnicianUserName").Exist();
				if(blnFlag == true)
				{
					blnFlag = Page("pge_ProcessRecoveredTissues").Element("txtTechnicianPassword").Exist();
					if(blnFlag == false)
					{
						Err.Description = "The Technician password text box does not display on 'Processed Recovered Tissues' page.";
					}
				}
				else
				{
					Err.Description = "The Technician username text box does not display on 'Processed Recovered Tissues' page.";
				}
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
        //''@Example: blnStatus= provideTechnicianSignature("akhare","India@123")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= clickandVerifyDonorIDLink("WC14F036", "Recovered Tissue")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= addAmnionorChorion("Amnion","10","15")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= deleteAmnionorChorion("Amnion")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= fillRawTissueAssmt("10","12","10","15")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= electronicSignatureTissueAssessment("akhare","India@123")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Objective: This Function fills the Hold or Release Amnion or Chorion popup form.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
		//''@---------------------------------------------------------------------------------------------------------------------------
		//''@Input Parameters: 
        //''@	strRefrigerator - The refrigerator name that is entered in the Hold/Release Amnion/Chorion popup.
		//''@	strUserName - The user name that is entered in the Hold/Release Amnion/Chorion popup.
		//''@	strPassword - The password that is entered in the Hold/Release Amnion/Chorion popup.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= fillAmnionChorionPopup("RFI-015R","akhare","India@123")       //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Aarti Suresh[08-07-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean fillAmnionChorionPopup(String strRefrigerator, String strUserName, String strPassword)
		{
			boolean blnFlag = false;			
					
			waitForSync(2);
			blnFlag = Page("pgeDonorProcessing").Element("lstRefrigerator").Select(strRefrigerator);
							
			if (blnFlag == true) 
			{
				blnFlag = Page("pgeDonorProcessing").Element("txtSignatureUserName").Type(strUserName);
									
				if (blnFlag == true) 
				{
					blnFlag = Page("pgeDonorProcessing").Element("txtSignaturePassword").Type(strPassword);
											
					if (!blnFlag)
					Err.Description = "Unable to enter Signature Password.";
				}
				else
				Err.Description = "Unable to enter Signature Username.";
			}
			else
			Err.Description = "Unable to select Refrigerator " +strRefrigerator+".";							
			
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
        //''@Example: blnStatus= verifyHoldReleaseRefrigeratorLog("Amnion","Hold","WC14F036","RFI-015R","Ashish Khare")       //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= fillFixtureFields("SNG-001","1234")       //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= verifyFixtureGridProductDehydration("SNG-001","1234")       //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= clickAndVerifyVisible("pgeProductDehydration", "lnkNoEquipmentSpecified", "pgeProductDehydration","btnAddEquipment")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= provideSignatureDehydration("akhare","Equipment")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= selectEquipmentTypeListBox("Tissue Dryer")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= selectEquipmentListBox("TD-005","Equipment")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= selectConfirmTempCheckBox()        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= verifyEquipmentGridProductDehydration("Tissue Dryer","TD-0005","akhare")       //''@---------------------------------------------------------------------------------------------------------------------------
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
					tempVar = Page("pgeProductDehydration").Element("//*[contains(@id,'tbleqplist_data')]//td[7]").GetText();
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
        //''@Example: blnStatus= verifyStartsStopsTable("Start","TD-005","Ashish Khare","akhare")       //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= verifyPouchesTable("1234","HS-004","250")       //''@---------------------------------------------------------------------------------------------------------------------------
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
		//''@	newPageName - The new Page Name which maps to the newElement which needs to be verified for non-existence after Page->Element has been //''@   clicked.
		//''@	newElement - The newElement which needs to be verified for non-existence after Page->Element has been clicked.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickAndVerifyNotExist("pgeDonorProcessing", "lnkDelete", "pgeDonorProcessing", "lnkAddCut_SecondRow")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= clickAndVerify(Page, Element, "Recovered Tissue")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= clickVendorName(Page, Element, "Recovered Tissue")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= selectTissueForSterilization("20131944")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= fetchDonorKey("WK14H025")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= scanTissue("pgeSterilizationReturns","objQtyAvailable")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= verifyReturnedCheckboxes("pgeSterilizationReturns","objQtyAvailable")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= verifyLocationGridUpdated("Sterlization")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= fetchDonorKeyPackageAssembly()        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= fillPackagingPage("akhare")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= fillPackagingPage("asmith","Line 1")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= verifyTextContains("pgeDonorstoPackage", "objStatusResult", "Needs Pre-packaging Inspection Signature")        //''@---------------------------------------------------------------------------------------------------------------------------
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
			if (strActualText.contains(strExpText)) 
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
        //''@Example: blnStatus= verifyExactText("pgeDonorstoPackage", "objStatusResult", "Needs Pre-packaging Inspection Signature")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Objective: This function clicks or verifies all the non-archive check boxes depending on the action to be performed on all the tissues		//''@(i.e. @'Pass'/'Reject'/'Micronize'/'Outer Pouch Seal'/'Outer Label') in Tissue Inspection/Validate Outer Packaging page and then verifies the //''@same, leaving the archive samples as it is. 
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
        //''@Example: blnStatus= selectCertificateCheckboxes()        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= commitCertificate("akhare", "India@123")        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[08-21-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean commitCertificate(String strUser, String strPassword) 
		{
			boolean blnFlag = false;
			blnFlag=Page("pgeEditCertificateOfConformance").Element("txtUser").Type(strUser);
			if (blnFlag==true)
			{
				blnFlag=Page("pgeEditCertificateOfConformance").Element("txtPassword").Type(strPassword);
				if (blnFlag==true)
				{
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
					Err.Description = "Unable to type in password field.";
			}
			else
				Err.Description = "Unable to type in username field.";
				
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
        //''@Example: blnStatus= clickCertificateofConformanceLink("Certificate of ConformanceLink")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= verifyPDF("Certificate of ConformanceLink")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= closePDF("Donor Tissues")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= getPDFContent("Certificate of ConformanceLink")        //''@---------------------------------------------------------------------------------------------------------------------------
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
						driver.findElement(By.xpath("//embed[@type='application/pdf']")).click();
						
						  Actions builder=new Actions(driver);
						  builder.keyDown(Keys.CONTROL)
								 .sendKeys("a")
								 .keyUp(Keys.CONTROL)
								 .keyDown(Keys.CONTROL)
								 .sendKeys("c")
								 .keyUp(Keys.CONTROL)
								 .release()
								 .perform();
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
        //''@Example: blnStatus= verifyPDFContent("clipboard data", "Certificate of ConformanceLink")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= sendValuetoCommonSheet("strRecoveryId_DonorProcessing","WA14G099") //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= clickPrintNalgeneLabelsLink("20143035", "AMNION,CHORION")        //''@---------------------------------------------------------------------------------------------------------------------------
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
			strExpText=strExpText+","+strDonorKey;
			
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
        //''@Example: blnStatus= clickPrintLogRefrigerator("NA14G154","Placenta,Quarantine,Released,RFI-015Q,RFI-015R,Ashish Khare")        //''@---------------------------------------------------------------------------------------------------------------------------
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
			strExpText=strExpText+","+strRecoveryID;
			
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
        //''@Example: blnStatus= clickPrintAssessment("20143133","Ashish Khare, Jonathan Laura")        //''@---------------------------------------------------------------------------------------------------------------------------
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
			strExpText=strExpText+","+strDonorKey;
			
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
        //''@Example: blnStatus= clickPrintForm("20143133","Product Dehydration Form,Tissue Dryer,TD-005,SNG-001,1234,HS-004,GS SHEET,Ashish Khare")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= enterRecoveryId("WA14G099") //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= electronicSignaturePackaging("akhare")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= selectPackageInspectionCheckboxes()        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= clickPrintPickListLink("410","Print Pick List")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= saveAndAllocate("2")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= processScanForShipping()        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= clickGenerateLabel("TN44", "20143145", "12", "33491")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= enterCurrentDate("pgeReturnedGoods","txtDate","MM/DD/YYYY")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@Example: blnStatus= clickTab("pgeSterilization","txtAgentName", strAgentName)        //''@---------------------------------------------------------------------------------------------------------------------------
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
			// waitForSync(2);
			waitForPageRefresh();
			Page("pgeFieldTracking").Element("lstAgentName").Click(20);
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
		//''@	strTissueId - Tissue id to be validate in grid.
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= verifyRecordNotExist("GS23-20141613-008")        
		//''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[09-26-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean verifyRecordNotExist(String strTissueId) 
		{
			boolean blnFlag = false;
			String strRecord = Page("pgeFieldTracking").Element("objEmptyTable").GetText();
			if(!(strRecord.contains(strTissueId))) 
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

}
