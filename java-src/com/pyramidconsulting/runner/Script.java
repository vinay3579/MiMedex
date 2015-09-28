package com.pyramidconsulting.runner;

import java.awt.Robot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.WrapsDriver;

import com.pyramidconsulting.Action;
import com.pyramidconsulting.Constants;
import com.pyramidconsulting.Launcher;
import com.pyramidconsulting.reader.ExcelReader;
import com.pyramidconsulting.report.Reporter;
import com.pyramidconsulting.util.PropertyUtil;
import com.thoughtworks.selenium.Selenium;

public  class Script extends Action {

	private static Logger logger = Logger.getLogger(Script.class);

	private static String DEFAULT_WAIT_TIME = "10"; // 10 seconds

	protected String testCaseName = null;

	protected Selenium selenium;
	protected WebDriver driver;
	protected ExcelReader or;
	protected ExcelReader td;
	protected Reporter reporter;
	protected Map<String, Object> output = new HashMap<String, Object>();
	protected Map<String, String> iDict = new HashMap<String, String>();
	public static int index;
    public static int index1;
    public static String strWindowText;
    public static Map<String, String> dicCommonValue = new HashMap<String, String>(); // ?? chk
    public static Map<String, String> dicTestData = new HashMap<String, String>(); // ?? chk
    public static Map<String, String> dicConfigValues = new HashMap<String, String>(); //{{
    public static Boolean isNotePadFileError;
	public  void run()  throws Exception {};

	public void setSelenium(Selenium selenium) {
		this.selenium = selenium;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public void setOr(ExcelReader or) {
		this.or = or;
	}

	protected void init(Selenium selenium, WebDriver driver, ExcelReader or,
			ExcelReader td, Reporter reporter, String testCaseName) {
		this.selenium = selenium;
		this.driver = driver;
		this.or = or;
		this.td = td;
		this.reporter = reporter;
		this.testCaseName = testCaseName;
		/*if (this.selenium == null && driver != null) {
			this.selenium = new WebDriverBackedSelenium(driver, "");
		}
		if (this.selenium != null && driver == null) {
			this.driver = ((WrapsDriver) selenium).getWrappedDriver();
		}*/

	}

	public void destroy() {
		try {
			if (selenium != null) {
				selenium.close();
			}
			if (reporter != null) {
				try {
					reporter.saveReport();
				} catch (Throwable t) {
					logger.error("Could not save the report.", t);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public boolean LaunchApplicationUrl()
	{
		boolean blnFlag=false;
		if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
		{
		  driver.get(Script.dicConfigValues.get("strApplicationURL"));
		  blnFlag=true;
		  
		}
	  	else
	  	{
		  selenium.open(Script.dicConfigValues.get("strApplicationURL"));
		  blnFlag=true;
		}
		windowMaximize();
		return blnFlag;
	}
	
	protected boolean isElementPresent(String selector) {
		boolean returnValue = false;
		try {
			returnValue = selenium.isElementPresent(selector);
		} catch (Throwable t) {
			logger.error("could not check the presence of element:" + selector,
					t);
		}
		return returnValue;
	}

	protected void waitForPageToLoad() {
		try {
			selenium.waitForPageToLoad(DEFAULT_WAIT_TIME);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	

	protected void waitForPageToLoad(int waitingTime) {
		try {
			selenium.waitForPageToLoad(new Integer(waitingTime).toString());
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	protected String getText(String elementLocator) {
		return selenium.getText(elementLocator);
	}

	protected void waitForObject(Integer timeOut, String object) {
		try {
			String strTimeOut = timeOut.toString();
			selenium.waitForCondition(object, strTimeOut);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	protected boolean isVisible(String locator) {
		return selenium.isVisible(locator);
	}

	protected void click(final String selector) throws Exception {
		try {
			selenium.click(selector);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	protected boolean deleteCookies() throws Exception {
		try {
			selenium.deleteAllVisibleCookies();
		} catch (final Exception e) {
			logger.error("Error while deleting cookies", e);
			throw e;
		}
		return true;
	}

	public String getLocatorXPathProperty(final String strObjectDesc) {
		final String strAttr = strObjectDesc.substring(0, 4);
		final String strAttrValue = strObjectDesc.substring(4);
		String strXPathProperty = "";

		if ("idef".equals(strAttr)) {
			strXPathProperty = "@id='" + strAttrValue + "'";
		} else if ("txtf".equals(strAttr)) {
			strXPathProperty = "//*[contains(text(),'" + strAttrValue + "')"
					+ "]";
		} else if ("imgs".equals(strAttr)) {
			strXPathProperty = "contains(@src,'" + strAttrValue + "')";
		} else if ("idep".equals(strAttr)) {
			strXPathProperty = "contains(@id,'" + strAttrValue + "')";
		} else if ("clsf".equals(strAttr)) {
			strXPathProperty = "@class='" + strAttrValue + "'";
		} else if ("clsp".equals(strAttr)) {
			strXPathProperty = "contains(@class,'" + strAttrValue + "')";
		} else if ("altf".equals(strAttr)) {
			strXPathProperty = "@alt='" + strAttrValue + "'";
		} else if ("altp".equals(strAttr)) {
			strXPathProperty = "contains(@alt,'" + strAttrValue + "')";
		} else if ("href".equals(strAttr)) {
			strXPathProperty = "@href='" + strAttrValue + "'";
		} else if ("hrep".equals(strAttr)) {
			strXPathProperty = "contains(@href,'" + strAttrValue + "')";
		} else if ("valf".equals(strAttr)) {
			strXPathProperty = "@value='" + strAttrValue + "'";
		} else if ("valp".equals(strAttr)) {
			strXPathProperty = "contains(@value,'" + strAttrValue + "')";
		} else if ("namf".equals(strAttr)) {
			strXPathProperty = "@name='" + strAttrValue + "'";
		} else if ("namp".equals(strAttr)) {
			strXPathProperty = "contains(@name,'" + strAttrValue + "')";
		} else if ("srcf".equals(strAttr)) {
			strXPathProperty = "@src='" + strAttrValue + "'";
		} else if ("srcp".equals(strAttr)) {
			strXPathProperty = "contains(@src,'" + strAttrValue + "')";
		} else if ("typf".equals(strAttr)) {
			strXPathProperty = "@type='" + strAttrValue + "'";
		} else if ("typp".equals(strAttr)) {
			strXPathProperty = "contains(@type,'" + strAttrValue + "')";
		} else if ("oncf".equals(strAttr)) {
			strXPathProperty = "@onclick='" + strAttrValue + "'";
		} else if ("oncp".equals(strAttr)) {
			strXPathProperty = "contains(@onclick,'" + strAttrValue + "')";
		} else if ("lnkf".equals(strAttr)) {
			strXPathProperty = "link=" + strAttrValue;
		} else if ("lftf".equals(strAttr)) {
			strXPathProperty = "//div[contains(@class,'left')]//*[contains(text(),'"
					+ strAttrValue + "')]";
		} else if ("lftd".equals(strAttr)) {
			strXPathProperty = "//div[@id='id_dept_list']//*[contains(text(),'"
					+ strAttrValue + "')]";
		} else if ("cssf".equals(strAttr)) {
			strXPathProperty = "css=" + strAttrValue;
		} else {
			return strObjectDesc;
		}
		// if (!dicConfig.ContainsKey("strAttrValue"))
		// dicConfig.Add("strAttrValue", strAttrValue);
		// else
		// dicConfig["strAttrValue"] = strAttrValue;
		if (strAttr.equals("cssf") || strAttr.equals("lnkf")
				|| strAttr.equals("txtf") || strAttr.equals("lftf")
				|| strAttr.equals("lftd")) {
			return strXPathProperty;
		} else {
			return "//*[" + strXPathProperty + "]";
		}
	}

	protected void browserCertificateError() {
		try {
			// this section Handle certificate error on Browser IE
			if (td.getValue("Browser").toLowerCase().contains("ie")) {
				if (isElementPresent("NAME=overridelink")) {
					try {
						selenium.click("NAME=overridelink");
					} catch (final Exception e) {
					}
					waitForPageToLoad(240);
				}
			}
			// this section handle certificate error on browser chrome
			if (td.getValue("Browser").toLowerCase().contains("chrome")) {
				final String title = selenium.getTitle();
				if (title.contains("SSL")) {
					selenium.keyPressNative("9");
					selenium.keyPressNative("10");
					waitForPageToLoad(340);
				}
			}
			// this section handle Certificate Error in Safari
			if (td.getValue("Browser").toLowerCase().contains("safari")) {
				selenium.keyPressNative("9");
				selenium.keyPressNative("10");
				waitForPageToLoad(340);
			}

			// this section handle certificate error in firefox
			if (td.getValue("Browser").toLowerCase().contains("firefox")) {
				final String title = selenium.getTitle();
				if (title.toLowerCase().contains("untrusted")) {
					try {
						selenium.click("onclick=toggle('expertContent')");
					} catch (final Exception e) {
					}
					waitForPageToLoad(40);
					try {
						selenium.click("id=exceptionDialogButton");
					} catch (final Exception e) {
					}
					waitForPageToLoad(40);
					selenium.keyPressNative("9");
					selenium.keyPressNative("9");
					selenium.keyPressNative("9");
					selenium.keyPressNative("9");
					selenium.keyPressNative("10");
					waitForPageToLoad(40);
				}
			}

		} catch (final Exception e) {
			//
		}

	}

	

	protected void focus(final String selector) {
		try {
			selenium.focus(selector);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/////////////////// ExecuteActionOnPage starts/////////////////
	
		public boolean ExecuteActionOnPage(String parentObject,String objectName, String actionToPerform, String strDataorProperty) {
		
		WebElement weElement=null;
		List<WebElement> weEleList = null;
		String strTag = null;
		String strObjProp="";
		boolean blnObjectFound = false, blnActionPerformed = false;
		
		if(parentObject.isEmpty())
			parentObject = "//*";
		
		//@ Create the Reference of the WebElement		
		WebElement parentElement = driver.findElement(By.xpath(parentObject));
		
		strObjProp = objectName.substring(3);
		
		//@ Create Element by Text through xpath
		if (objectName.substring(0, 3).contains("txt"))
		{
			weElement = parentElement.findElement(By.xpath("//*[text()='"+ strObjProp +"']"));
			if (weElement != null) blnObjectFound =true;
				
		}

		else if (objectName.substring(0, 3).contains("pxt"))
		{
			weElement = parentElement.findElement(By.xpath("//*[contains(text(),'"+ strObjProp +"')]"));
			if (weElement != null) blnObjectFound =true;
				
		}
		
		else if (objectName.substring(0, 3).contains("hrf"))
		{
			weElement = parentElement.findElement(By.xpath("//*[contains(@href,'"+ strObjProp +"')]"));
			if (weElement != null) blnObjectFound =true;
				
		}		
		//@ Create element from xpath
		else if(objectName.contains("=") || objectName.contains("//")|| objectName.contains("]"))
		{
			
			weElement = parentElement.findElement(By.xpath(objectName));
			if (weElement != null) blnObjectFound =true;			
		}
		
		//@ Logic to  work on Object collection
		else
		{
			ArrayList<String> objectProperties = getAssistiveProperty(objectName);
	
			for(int i = 0;i<objectProperties.size();i++)
				{
					if(objectProperties.get(0).contains("tag"))
					{	
						//@ Reduce the Count of the links according to name
						if(objectProperties.get(0).split("=")[1].contains("a"))
						{
							//weEleList  = parentElement.findElements(By.xpath("//a[contains(text(),'" + strObjProp +"')]"));	
							weEleList  = parentElement.findElements(By.partialLinkText(strObjProp));

						}					
						
						//@ Reduce the Count of the Input Tag (Edit Box , Button) according to name
						if(objectProperties.get(0).split("=")[1].contains("input"))
						{								
							weEleList  = parentElement.findElements(By.id(strObjProp));

						}							
						
						//@ If weEleList has null objects 
						if (weEleList == null || weEleList.size() == 0)
						{
							weEleList = parentElement.findElements(By.tagName(objectProperties.get(0).split("=")[1]));
						}

						
					}
					
					//@ get the Count of the checkbox according to name
					if(objectProperties.get(0).split("=")[1].contains("checkbox"))
					{
						//weEleList  = parentElement.findElements(By.xpath("//a[contains(text(),'" + strObjProp +"')]"));	
						weEleList  = driver.findElements(By.xpath(parentObject + objectProperties.get(0).split("=")[1]));

					}
					
					//@ get the Count of the checkbox according to name
					if(objectProperties.get(0).split("=")[1].contains("radio"))
					{
						//weEleList  = parentElement.findElements(By.xpath("//a[contains(text(),'" + strObjProp +"')]"));	
						weEleList  = driver.findElements(By.xpath(parentObject + objectProperties.get(0).split("=")[1]));

					}					
									
					
					if(objectProperties.get(0).contains("xpath"))
					{
						weEleList = parentElement.findElements(By.xpath(objectProperties.get(0).split("=")[1]));
									
					}		
					
				}	
			
			int i = 0;
			if(weEleList!=null && weEleList.size() != 0)
			{
				for(WebElement wee : weEleList)
				{
					String compareValues= "";
					
					compareValues = getAttributeValues(strTag, wee);
					if (objectName.substring(0, 3).contains("lnk") || objectName.substring(0, 3).contains("chk")|| objectName.substring(0, 3).contains("edt")|| objectName.substring(0, 3).contains("btn"))
					{
						String [] arrValues = compareValues.split(";");
						for(String objValues: arrValues)
						{
							if (objValues.toLowerCase().trim().equals(strObjProp.toLowerCase().trim()))
							{
								
								if(isInteger(iDict.get("index")))
								{
									int intValues = Integer.parseInt(iDict.get("index"));
									if (i == intValues)
									{
										blnObjectFound =true;
										weElement =wee;								
										break;
									}
									i = i +1;
								
								}	
								else
								{
									blnObjectFound =true;
									weElement =wee;								
									break;
								}
								
																
							}
						}						
						
					}					
					else
					{
						if(compareValues.toLowerCase().contains(strObjProp.toLowerCase()) && wee.isDisplayed())
						{
							if(isInteger(iDict.get("index")))
							{
								int intValues = Integer.parseInt(iDict.get("index"));
								if (i == intValues)
								{
									blnObjectFound =true;
									weElement =wee;								
									break;
								}
								i = i +1;
								
							}	
							else
							{
								blnObjectFound =true;
								weElement =wee;								
								break;
							}
							
														
						}
					}
					
					if(blnObjectFound) break;
	
				}
			}
		}
		
		if(blnObjectFound)
		{
			if(actionToPerform.toLowerCase().equals("click"))
			{
				weElement.click();
				blnActionPerformed = true;
			}
			else if(actionToPerform.toLowerCase().equals("set"))
			{
				weElement.clear();
				weElement.sendKeys(strDataorProperty);
				blnActionPerformed = true;
			}
			else if(actionToPerform.toLowerCase().equals("highlight"))
			{
				highlightElement(weElement);
				//highlightElement(weElement);
				blnActionPerformed = true;
			}			
			else if(actionToPerform.toLowerCase().equals("select"))
			{				
				org.openqa.selenium.support.ui.Select objSelect = new org.openqa.selenium.support.ui.Select(weElement);
				List <WebElement> objList= objSelect.getOptions();
				int j =0;
				for (WebElement objItem:objList)
				{	
					if(objItem.getText().equalsIgnoreCase(strDataorProperty))
					{
						objSelect.selectByIndex(j);
						blnActionPerformed = true;
						objSelect = null;
						break;
					}
					j = j +1;
				}
				
				blnActionPerformed = true;
				objSelect = null;
			}
			else if(actionToPerform.toLowerCase().equals("exist"))
			{
				blnActionPerformed = weElement.isDisplayed();
				//weElement.
				
			}
			else if(actionToPerform.toLowerCase().equals("mousemove"))
			{
				mouseMoveElement(weElement);
				//highlightElement(weElement);
				blnActionPerformed = true;
			}
			else if(actionToPerform.toLowerCase().equals("getroproperty"))
			{
				String strValue = "";


				if(strDataorProperty.toLowerCase().contains("text"))
				{
					strValue = weElement.getText();
				}
				else
				{
					strValue = weElement.getAttribute(strDataorProperty);
				}
				

				iDict.put("strPropertyVaue",strValue);
				
				
			
  				blnActionPerformed = true;
			}						
			
		}
		else
		{
			if(actionToPerform.toLowerCase().equals("doesnotexist"))
				blnActionPerformed = true;
			else
				blnActionPerformed = false;
			
		}
		
		/*
		JavascriptExecutor js = (JavascriptExecutor) driver;  
		  js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", weElement, "style", "color: black; border: 5px solid black; background-color: yellow;"); 
		  Thread.sleep(3000);	
		*/
		if(iDict.containsKey("index"))
		{
			iDict.remove("index");
		}		
		return blnActionPerformed;
	}
		
		public ArrayList<String> getAssistiveProperty(String strProperty){
			
			
			String strPrefix = strProperty.substring(0, 3);
			ArrayList<String> returnValue = new ArrayList<String>();
			
			//@ Button & Edit Box Element
			if(strPrefix.equals("btn")|| strPrefix.equals("edt"))
				returnValue.add("tag=input");		
			else if(strPrefix.equals("ptn")|| strPrefix.equals("pdt"))
				returnValue.add("tag=input");		
			//@ Link Element 
			else if(strPrefix.equals("lnk"))
				returnValue.add("tag=a");
			else if(strPrefix.equals("pnk"))
				returnValue.add("tag=a");
			//@ List box Element
			else if(strPrefix.equals("lst"))
				returnValue.add("tag=select");
			//@ Image Element
			else if(strPrefix.equals("img"))
				returnValue.add("tag=img");
			//@ Check Box Element
			else if(strPrefix.equals("chk"))
				returnValue.add("xpath=//input[contains(@type,'checkbox')]");
			//@ Radio Box Element
			else if(strPrefix.equals("rdo"))
				returnValue.add("xpath=//input[contains(@type,'radio')]");			
			
			else if(strPrefix.equals("bin"))
			{
				returnValue.add("tag=input");
				returnValue.add("xpath=//*[@type='image']");
			}		
			
			return returnValue;
		}
		
		public String getAttributeValues(String strTag, WebElement wee)
		{
			String compareValues = "";

			try{
			compareValues = compareValues + wee.getAttribute("value") + ";";
			}
			catch(Exception e){}
			try{
			compareValues = compareValues + wee.getAttribute("name") + ";";
			}
			catch(Exception e){}
			try{
			compareValues = compareValues + wee.getAttribute("class") + ";";
			}
			catch(Exception e){}
			try{
			compareValues = compareValues + wee.getAttribute("onclick") + ";";
			}
			catch(Exception e){}
			try{
			compareValues = compareValues + wee.getAttribute("id") + ";";
			}
			catch(Exception e){}
			try{
			compareValues = compareValues + wee.getAttribute("href") + ";";
			}
			catch(Exception e){}
			try{
			compareValues = compareValues + wee.getText() + ";";
			}
			catch(Exception e){}
					
			try{
			compareValues = compareValues + wee.getAttribute("alt") + ";";
			}
			catch(Exception e){}	
			try{
			compareValues = compareValues + wee.getAttribute("src") + ";";
			}
			catch(Exception e){}	

			return compareValues;
		}
		
		public void highlightElement(WebElement element) {
			
			try
			{
			JavascriptExecutor js = (JavascriptExecutor) driver;  
			js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", "color: black; border: 5px solid black; background-color: yellow;");
			Thread.sleep(2000);
			}
			catch(Exception e){}

		}
		
		public void mouseMoveElement(WebElement element) {
			try
			{
				Actions builder = new Actions(driver);
				builder.moveToElement(element).build().perform();
				//Point coordinates = element.getLocation();
//				int x = element.getLocation().x;
//				int y = element.getLocation().y;			
//				int h = element.getSize().height;
//				int w = element.getSize().width;
//						
//				Robot robot = new Robot();
				
				//robot.mouseMove((x + w)/2 ,(y + h)/2 + 30);
			
				//Thread.sleep(5000);
//				robot.mouseMove(x + w/2,y + h/2 );
				
				//Thread.sleep(5000);
				//robot.mouseMove(x ,y + 30);
				//Thread.sleep(5000);

			}
			catch(Exception e){}

		}
		
		public boolean WaitForScreenObject(String ObjectName, int intTime) 
		{
			
			int i = 0;
			boolean blnflag = false;
			while(blnflag != true)
			{
				
				i++;
				try
				{
					Thread.sleep(3000);
				}
				catch (Exception ee) {}
				
				ArrayList<String> objectProperties = getAssistiveProperty(ObjectName);
				if(objectProperties.size() != 0)
				{
					blnflag = ExecuteActionOnPage("", ObjectName,"exist","");	
				}
				else
				{
					blnflag = selenium.isElementPresent(ObjectName);
					if (blnflag == true)
					{
						try{
						blnflag = selenium.isVisible(ObjectName);
						}
						catch(Exception e){};
						
					}
				}
				if (i == intTime)
				{
					break;
				}
			}	
			
			if (blnflag = true)
			{
				return true;
				
			}
			else
			{
				return false;
			}
		}
		

		 public boolean isInteger( String input )  
		  {  
		     try  
		     {  
		        Integer.parseInt( input );  
		        return true;  
		     }  
		     catch( Exception e)  
		     {  
		        return false;  
		     }  
		  }
//////////////////////////////////ExecuteActionOnPage ends///////////////////		 
		 
////////////////////////////ExecuteActionOnBrowser///////////////////////////
			public static enum ActionList {
				back, newbrowsersession, selectwindowbyobject, selectwindow, geturl, navigate, gotooriginalwindow, close, verifytitle, getbrowserwithcreationtime, getpagesource;
			}
			public boolean ExecuteActionOnBrowser(String Action, String WindowTitle) {
				//PropertyUtil appConfig = new PropertyUtil(Constants.AppConstants.APP_CONFIG_FILENAME);
				String action;
				boolean Flag = false;
				action = Action.toLowerCase();
				action = Action.trim();
				try {
					switch (ActionList.valueOf(action)) {
					case back:
						GoBack();
						waitForPageToLoad(340);
						Flag = true;
						break;
					case newbrowsersession:
						if (WindowTitle == "")
							WindowTitle =Script.dicConfigValues.get("strApplicationURL");  //{{
						openWindow(WindowTitle, "1");
						selenium.setTimeout("120000");
						selectWindow("1");
						windowMaximize();
						Flag = true;
						break;
					case selectwindowbyobject:
						String strObject = getLocatorXPathProperty(WindowTitle);
						String strAttrValue = getEval("var abc = this.browserbot.findElement(\"xpath="
								+ strObject + "\").getAttribute('href');abc");
						if (strAttrValue == "")
							throw new Exception("Object property not defined");
						openWindow(strAttrValue, "SelectedWindow");
						selectWindow("SelectedWindow");
						Thread.sleep(2000);
						windowMaximize();
						windowFocus();
						waitForPageToLoad(340);
						Flag = true;
						break;
					case selectwindow:
						selectWindow(WindowTitle);
						Flag = true;
						break;
					case geturl:
						output.put("url", getLocation());
						Flag = true;
						break;
					case navigate:
						open(WindowTitle);
						waitForPageToLoad(240);
						String [] getLocationSplit = getLocation().split("\\s*[.]+\\s*");
				        String [] WindowTitleSplit = WindowTitle.split("\\s*[.]+\\s*");
						if (WindowTitleSplit[1].equals(getLocationSplit[1]))
							return true;
						else
							return false;
					case gotooriginalwindow:
						selectWindow(null);
						windowFocus();
						Flag = true;
						break;
					case close:
						close();
						Flag = true;
						break;
					case verifytitle:
						String strBrowserTitle = driver.getTitle();
						if (strBrowserTitle.contains(WindowTitle)) {
							return true;
						} else
							System.out.println("title not verified");
							return false;
					case getbrowserwithcreationtime:
						selectWindow(selenium.getAllWindowTitles()[Integer.parseInt(WindowTitle)]);
						windowFocus();
						windowMaximize();
						Flag = true;
						break;

					case getpagesource:
						output.put("PageSource", getHtmlSource());
						Flag = true;
						break;

					}

				} catch (Exception ee) {
					System.out.println(ee);
					Flag = false;
				}
				return Flag;
			}

/////////////////// End of ExecuteActionOnBrowser//////////////

//////////////////// Wrappers ////////////////////////////////

	public boolean clickAt(String strLocator, String strCoord) {
		try {
			selenium.clickAt(strLocator, strCoord);
		} catch (Exception e) {
			return false;
		}
		return true;

	}

	public String getLocation() {
		try {
			return driver.getCurrentUrl();
		} catch (Exception e) {
			return "";
		}
	}
	
	public String getEval(String strString) {
		try {
			return selenium.getEval(strString);
		} catch (Exception e) {
			return "getEval not working";
		}

	}
	
	public void highlight(String strObject) {
		try {
			selenium.highlight(strObject);
		} catch (Exception e) {
		}
	}
	
	public void mouseOverAndClick(String getEleBy, String strObject, int index) {
			try {
				JavascriptExecutor js = (JavascriptExecutor) driver;
				String script ="document.getElementsBy"+getEleBy+"('"+strObject+"]').click();";
				js.executeScript(script);
			} catch (Exception e) {
			}
	}
	
	public void mouseOver(WebElement strObject){
		Actions builder = new Actions(driver);
		builder.moveToElement(strObject).build().perform();

	}
	
	public void mouseMove(String strObject) {
		try {
			selenium.mouseMove(strObject);
		} catch (Exception e) {
		}
	}

	public Number getXpathCount(String strObject) {
		try {
			return selenium.getXpathCount(strObject);
		} catch (Exception e) {
			return 0;
		}
	}
	
	public String getHtmlSource() {
		try{
			ExecuteActionOnBrowser("getbrowserwithcreationtime", "1");
			return driver.getPageSource();
		}
		catch(Exception e){
		return "";
		}
	}
	
	public void GoBack() {
		try {
			driver.navigate().back();
		} catch (Exception e) {
		}
	}

	public void openWindow(String strURL, String strWinID) {
		try {
			selenium.openWindow(strURL, strWinID);
		} catch (Exception e) {
		}

	}

	public void open(String strURL) {
		try {
			driver.get(strURL);
		} catch (Exception e) {
		}

	}

	public void close() {
		try {
			driver.close();
		} catch (Exception e) {
		}

	}

	public void selectWindow(String strWinID) {
		try {
//			driver.navigate().to(strWinID);
			selenium.selectWindow(strWinID);
		} catch (Exception e) {
		}

	}

	public void windowMaximize() {
		try 
		{
			if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
				driver.manage().window().maximize();
			else
				selenium.windowMaximize();
		} catch (Exception e) {
			logger.error("Unable to Mximize the browser window");
		}

	}

	public void windowFocus() {
		try {
			driver.switchTo().window(getLocation());
		} catch (Exception e) {
		}

	}
	
	public void waitForSync(int waitTime) 
	{
		try 
		{
			Thread.sleep(waitTime*1000);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}

	// ////////////////// Wrappers End ////////////////////////////
}