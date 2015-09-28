package mdxinfas01;


import java.sql.Timestamp;
import org.apache.log4j.Logger;
import com.pyramidconsulting.runner.Script;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import mdxinfas01.*;

public class SBWEB233VerifySearchFunctionality extends Script {
	private static final Logger logger = Logger.getLogger(SBWEB233VerifySearchFunctionality.class);
    	
    @Override
    public void run() throws Exception {
    
    	boolean blnFlag = false;
    	String strStepDesc="";
		java.util.Date date= new java.util.Date();
   	    String timeStamp =new Timestamp(date.getTime()).toString().replace(" ","").replace(":","").replace("-", "").replace(".","");
	
		// ########################################################################################################
		// # Test Case ID: SBWEB-233
		// # Test Case Name: VerifySearchFunctionality
		// #-------------------------------------------------------------------------------------------------------
		// # Description: This scripts verifies that the user can sign up and reach to the Recovered Tissue landing
		// # page and search some recovery id on the page then successfully log out.
		// #-------------------------------------------------------------------------------------------------------
		// # Pre-conditions: User is logged in into the MiMedx SBWeb application.
		// # 		         User is on "Recovered Tissue" page.
		// # Post-conditions: NA
		// # Limitations: NA
		// #-------------------------------------------------------------------------------------------------------
		// # Owner: Ashish Khare
		// # Created on: 07-08-2014
		// #-------------------------------------------------------------------------------------------------------
		// # Reviewer: Himanshu Gosain
		// # Review Date:
		// #-------------------------------------------------------------------------------------------------------
		// # History Notes:
		// ########################################################################################################

		//Getting data from config file  
		String strURL=Script.dicConfigValues.get("strApplicationURL");
		
		//Getting data from master test data file
		String strUserName = Script.dicTestData.get("strUserName");
		String strPassword = Script.dicTestData.get("StrPassword");
		String strRecoveryId = Script.dicTestData.get("strRecoveryId");

		// ########################################################################################################
		// Step 1 - Launch browser.
		// Open application URL.
		// ########################################################################################################
		strStepDesc = "Launch browser and open SBWEB application URL.";
		blnFlag = LaunchApplicationUrl();
		if (blnFlag == true) 
		{
			reporter.ReportStep(strStepDesc,"SBWEB application url: '"+strURL+"' launched successfully.","Pass");
		}
		else 
		{
			reporter.ReportStep(strStepDesc, "Launch application failed.", "Fail");
		}

		// ########################################################################################################
		// Step 2 - Enter all the mandatory fields with
		// valid data and click on login button.
		// ########################################################################################################
		strStepDesc = "Enter all the mandatory fields with valid data and click on login button.";
		blnFlag = loginMiMedx(strUserName, strPassword);
		if (blnFlag == true) 
		{
			reporter.ReportStep(strStepDesc, "User: '" + strUserName+ "' signed in successfully.", "Pass");
		} 
		else 
		{
			reporter.ReportStep(strStepDesc, Err.Description, "Fail");
		}

		// ########################################################################################################
		// Step 3 - Click on Recovery link.
		// ########################################################################################################
		strStepDesc = "Click on 'Recovery' link given on left navigation area";
		blnFlag=clickAndVerify("pgeAssembly_Lines", "lnkRecovery", "pgeAssembly_Lines", "lnkRecovered_Tissue");
		if (blnFlag == true) 
		{
			reporter.ReportStep(strStepDesc,"'Recovery' link clicked successfully.", "Pass");
		} 
		else 
		{
			reporter.ReportStep(strStepDesc, "'Recovery' link not clicked.", "Fail");
		}

		// ########################################################################################################
		// Step 4 - Click on Recovered Tissue link.
		// ########################################################################################################
		strStepDesc = "Click on 'Recovered Tissue' link.";
		blnFlag=clickAndVerify("pgeAssembly_Lines", "lnkRecovered_Tissue", "pgeRecovered_Tissue", "btnReadScan");
		if (blnFlag == true) 
		{
			reporter.ReportStep(strStepDesc,"'Recovered Tissue' link clicked successfully.", "Pass");
		}
		else 
		{
			reporter.ReportStep(strStepDesc, "'Recovered Tissue' link not clicked.", "Fail");
		}
				
		//########################################################################################################
		//Step 5 - 	Verify that if search functionality working properly in Recovered Tissue entry table.
		//########################################################################################################
		strStepDesc = "Verify search functionality working properly in Recovered Tissue entry table.";
		blnFlag=verifySearch("pgeRecovered_Tissue","txtRecoveryId_RecoveredTissue","lnkRecoveryId", strRecoveryId);
		if (blnFlag == true)
		{
			reporter.ReportStep(strStepDesc, "Search functionality working properly in Recovered Tissue grid.", "Pass");
		}
		else
		{
			reporter.ReportStep(strStepDesc, Err.Description, "Fail");
		}
		
		// ########################################################################################################
		// Step 6 - Logout from SBWEB test application.
		// ########################################################################################################
		strStepDesc = "Logout from SBWEB test application";
		blnFlag = logoutMiMedx(strUserName);
		if (blnFlag == true)
		{
			reporter.ReportStep(strStepDesc, "User: '" + strUserName+ "' signed out successfully.", "Pass");
		} 
		else 
		{
			reporter.ReportStep(strStepDesc, Err.Description, "Fail");
		}
    }
    
    // Library methods
    
//Methods from :func_application.txt

		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: loginMiMedx
        //''@Objective: This Function login into the application.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
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
			boolean blnFlag = false, bln=false;
			Page("pgeAssembly_Lines").Element("btnLogout").Click(20);
			bln=Page("pgeLogin").Element("objPleaseLogin").Exist();
			if(bln==true)
			{
				blnFlag=true;
			}
			else
			{
				Err.Description="User: '" + strUserName+ "' not sigend out from MiMedx application.";
				blnFlag=false;
			}
			
			return blnFlag;
		}
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickAndVerify
        //''@Objective: This Function logout from the application.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickAndVerify("akhare")        //''@---------------------------------------------------------------------------------------------------------------------------
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
        //''@     Failure - False
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
			String status = Page("pgeRecovered_Tissue").Element("objPending").GetText();
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
        //''@Objective: This Function clicks on specified recoveryid and calls the function getPageTitle() to verify user is on correct page or not.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= clickLink("WC14F036", "Recovered Tissue")        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[09-Jul-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean clickLink(String strRecoveryId, String strExpText)throws Exception 
		{
			boolean blnFlag = false;
			//driver.findElement(By.xpath("//a[contains(text(),'" + strRecoveryId + "')]")).click();
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
        //''@Example: blnStatus= getPageTitle("WC14F036", "Recovered Tissue")        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[09-Jul-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean getPageTitle(String strExpText)throws Exception 
		{
			boolean blnFlag = false;
			String PageTitle = Page("pgeRecovered_Tissue").Element("objRecoveredTissuePage").GetText();
			if (PageTitle.contains(strExpText)) 
			{
				blnFlag = true;
			}
			else
			{
				Err.Description = "User is not on the '" + strExpText+ "' page and is on the '" + PageTitle + "' page.";
				blnFlag = false;				
			}
			return blnFlag;
		}


/*
		public boolean eligiblityStatus() throws InterruptedException 
		{
			boolean blnFlag = false;
			driver.findElement(By.xpath("//button[@id='centerform:cmdeligible']")).click();
			waitForElementToRefresh(1);
			String winTitle = driver.findElement(By.xpath("//span[@id='utilwindow_title']")).getText();
			if (winTitle.equalsIgnoreCase("Tissue is Eligible for Transplant")) 
			{
				blnFlag = true;
			}
			else 
			{
			blnFlag = false;
			}
			return blnFlag;
		}

		public boolean elegibleTransplant(String strUserName, String strPassword) 
		{
			boolean blnFlag = false;
			driver.findElement(By.xpath("//textarea[@id='dialogform:pgEligibleWindowComments']")).sendKeys("Ok");
			driver.findElement(By.xpath("//input[@name='dialogform:j_idt53']")).sendKeys(strUserName);
			driver.findElement(By.xpath("//input[@name='dialogform:j_idt54']")).sendKeys(strPassword);
			driver.findElement(By.xpath("//button[@id='dialogform:cmdCommitEligibleWindow']")).click();
			String eleStatus = driver.findElement(By.xpath("//span[@id='centerform:status']")).getText();
			if (eleStatus.equalsIgnoreCase("Eligible for Transplant")) 
			{
				blnFlag = true;
			}
			else 
			{
				blnFlag = false;
			}
			return blnFlag;
		}

		public boolean verifyAcceptStatus() throws InterruptedException 
		{
			boolean blnFlag = false;
			driver.findElement(By.xpath("//button[@id='centerform:cmdaccept']")).click();
			waitForElementToRefresh(1);
			String winTitle = driver.findElement(By.xpath("//span[@id='utilwindow_title']")).getText();
			if (winTitle.equalsIgnoreCase("Accept and Release the Tissue for Processing")) 
			{
				blnFlag = true;
			}
			else 
			{
				blnFlag = false;
			}
			return blnFlag;
		}

		public boolean acceptAndRelease(String strUserName, String strPassword) 
		{
			boolean blnFlag = false;
			driver.findElement(By.xpath("//textarea[@id='dialogform:pgAcceptWindowComments']")).sendKeys("Ok");
			driver.findElement(By.xpath("//input[@name='dialogform:j_idt37']")).sendKeys(strUserName);
			driver.findElement(By.xpath("//input[@name='dialogform:j_idt38']")).sendKeys(strPassword);
			driver.findElement(By.xpath("//button[@id='dialogform:cmdCommitAcceptWindow']")).click();
			String eleStatus = driver.findElement(By.xpath("//span[@id='centerform:status']")).getText();
			if (eleStatus.equalsIgnoreCase("Release OK")) 
			{
				blnFlag = true;
			}
			else 
			{
				blnFlag = false;
			}
			return blnFlag;
		}

		public boolean releasedStatus() 
		{
			boolean blnFlag = false;
			String eleStatus = driver.findElement(By.xpath("//span[@id='centerform:status']")).getText();
			if (eleStatus.equalsIgnoreCase("Released")) 
			{
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
        //''@Function Name: canvasSignature
        //''@Objective: This Function sign the digital signature area.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - NA
        //''@     Failure - NA
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= canvasSignature(driver)        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[09-Jul-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public void canvasSignature(WebDriver driver) 
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
        //''@Example: blnStatus= addTissue("WC14F036", "akhare", "Hanif Anum", "Jun 29, 2014 00:00", "WC")        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[11-Jul-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################		
		public boolean addTissue(String RecoveryId, String txtUser,String strCollectionAgent, String dteDate, String strHospitalCode)
		{
			boolean blnFlag = false;
			waitForElementToRefresh(2);
			
			Page("pgeNewRecoveredTissue").Element("lstCollectionTech").Select(strCollectionAgent);
			Page("pgeNewRecoveredTissue").Element("dteSelectDate").Type(dteDate);
			Page("pgeNewRecoveredTissue").Element("txtHospital").Type(strHospitalCode);
			waitForElementToRefresh(1);
			
			Page("pgeNewRecoveredTissue").Element("lnkSurgeon").Click(20);
			waitForElementToRefresh(1);
			
			blnFlag = Page("pgeNewRecoveredTissue").Element("drpShippingContainer").Select("Yes");
			waitForElementToRefresh(2);
			
			if (blnFlag == true) 
			{
				blnFlag = Page("pgeNewRecoveredTissue").Element("drpCoolent").Select("Yes");
				waitForElementToRefresh(2);
				
				if (blnFlag == true) 
				{
					blnFlag = Page("pgeNewRecoveredTissue").Element("drpPaperwork").Select("Yes");
					waitForElementToRefresh(2);
					
					if (blnFlag == true) 
					{
						blnFlag = Page("pgeNewRecoveredTissue").Element("drpTissueContainer").Select("Yes");
						waitForElementToRefresh(2);
						
						if (blnFlag == true) 
						{
							blnFlag = Page("pgeNewRecoveredTissue").Element("drpTissueAccepted").Select("Yes");
							waitForElementToRefresh(2);
							
							if (!blnFlag)
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

			if (blnFlag == true) 
			{
				canvasSignature(driver);
				blnFlag = Page("pgeNewRecoveredTissue").Element("txtUserName").Type(txtUser);
			}

			/*
			 * driver.findElement(By.xpath("//button[@id='rform:cmdsave']")).click();
			 * waitForElementToRefresh(1);
			 * driver.findElement(By.xpath("//button[@id='rform:cmdreturn']"
			 * )).click();
			 */
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
        //''@Example: blnStatus= verifySearch(PageName, ElementName, loadedElementName, "WC14F036")        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[11-Jul-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean verifySearch(String Page, String Element, String newElement, String text) throws InterruptedException 
		{
			boolean blnFlag = false;
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
			blnFlag = Page("pgeRecovered_Tissue").Element("//*[contains(text(),'" + strRecoveryId+ "')]/following::a[contains(text(),'Recovery Form')]").Click(20);
			//driver.findElement(By.xpath("//*[contains(text(),'" + strRecoveryId+ "')]/following::a[contains(text(),'Recovery Form')]")).click();
			if (blnFlag==true)
			{
				blnFlag = getPageTitle(strExpText);
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
			blnFlag = Page("pgeRecovered_Tissue").Element("//*[contains(text(),'" + strRecoveryId+ "')]/following::a[contains(text(),'Refrigerator Log')]").Click(20);
			//driver.findElement(By.xpath("//*[contains(text(),'"+ strRecoveryId+ "')]/following::a[contains(text(),'Refrigerator Log')]")).click();
			if (blnFlag==true)
			{
				blnFlag = getPageTitle(strExpText);
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
			Page("pgeRelease_Processing").Element("txtSerologyResult_ReleaseProcessing").Type(strSerologyResult);
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
		
		//''@###########################################################################################################################
        //''@Function ID: 
        //''@Function Name: clickLoadReviewForm
        //''@Objective: This Function clicks on Load/Review link on 'Recovered Tissue [Recovery Id]' page and load 'pdf' file.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
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
						Status="'Load Form' link clicked and file uploaded successfully.";
						Script.dicCommonValue.put("Success", Status);
						Page("pgeRecovered_Tissue").Element("lnkLoad_Form").Click(20);
					}
					else		
						Err.Description = "Unable to upload 'pdf' file.";					
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
        //''@Example: blnStatus= uploadFile("E:\\Workspace\\PDF_Files\\secondsdoc.pdf")        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[07-22-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################
		public boolean uploadFile(String strFilePath) 
		{
			boolean blnFlag=false;
			waitForElementToRefresh(1);
//			driver.findElement(By.xpath("//input[@id='rform:file']")).click();
			driver.findElement(By.xpath("//input[@id='rform:file']")).sendKeys(strFilePath);
//			Page("pgeRecovered_Tissue").Element("txtUploadFile").Click(5);
//			blnFlag=Page("pgeRecovered_Tissue").Element("txtUploadFile").Type(strFilePath);
//			boolean bool=Page("pgeRecovered_Tissue").Element("btnLoad_File").Click(20);
			if (blnFlag==true)
			{
				blnFlag=true;
			}
			else
			{
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
        //''@Example: blnStatus= verifyScan("Ok")        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[07-23-2014]
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
								Page("pgeForms_Package").Element("btnPass").Click(20);
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
        //''@Objective: This Function releases the passed scan 'pdf' on 'Recovered Tissue Form Files' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Example: blnStatus= releasePassForm()        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Created by[Date]: Ashish Khare[07-29-2014]
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Reviewed by[Date]: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@History Notes: 
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@###########################################################################################################################			
		public boolean releasePassForm(String strUserName, String strPassword) 
		{
			boolean blnFlag = false;
			driver.findElement(By.xpath("//textarea[@id='dialogform:pgPassWindowComments']")).sendKeys("Ok");
			driver.findElement(By.xpath("//input[@name='dialogform:j_idt45']")).sendKeys(strUserName);
			driver.findElement(By.xpath("//input[@name='dialogform:j_idt46']")).sendKeys(strPassword);
			driver.findElement(By.xpath("//button[@id='dialogform:cmdCommitPassWindow']")).click();
			String button = driver.findElement(By.xpath("//button[@id='centerform:cmdmark']")).getText();
			if (button.equalsIgnoreCase("Mark for Review")) 
			{
				blnFlag = true;
			} 
			else
			{
				blnFlag = false;
			}
			return blnFlag;
		}
/*
		public boolean markForReview() 
		{
			boolean blnFlag = false;
			driver.findElement(By.xpath("//button[@id='centerform:cmdmark']")).click();
			String eleStatus = driver.findElement(By.xpath("//span[@id='centerform:status']")).getText();
			if (eleStatus.equalsIgnoreCase("Eligibility Review")) 
			{
				blnFlag = true;
			}
			else 
			{
				blnFlag = false;
			}
			return blnFlag;
		}

		public boolean verifyEligibilityLink() 
		{
			boolean blnFlag = false;
			String eleLink = driver.findElement(By.xpath("//span[@id='centerform:tblformlist:0:itemaction']")).getText();
			if (eleLink.equalsIgnoreCase("Verify Eligibility")) 
			{
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
        //''@Function Name: verifyRejectedStatus
        //''@Objective: This Function verifies tissue rejected status on 'Recovered Tissue' page.
        //''@---------------------------------------------------------------------------------------------------------------------------
        //''@Return Desc: 
        //''@     Success - True
        //''@     Failure - False
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


}
