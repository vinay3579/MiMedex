package com.pyramidconsulting;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.script.ScriptException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.pyramidconsulting.reader.ExcelReader;
import com.pyramidconsulting.reader.ExcelReaderImpl;
import com.pyramidconsulting.runner.Script;
import com.pyramidconsulting.util.PropertyUtil;
import com.thoughtworks.selenium.DefaultSelenium;

//import java.lang.Object;


public class Action {
	
	        static String strPageNameInOR = null;
	        static String strFrameNameInOR = null;
	        static String strObjectProperty = null;
	        static String strObjectNameInOR = null;
	         static Launcher launcher = new Launcher();
	         public  static PropertyUtil properties = new PropertyUtil(Constants.PROPERTY_CONFIG_FILENAME);
	         //public static PropertyUtil appConfig = new PropertyUtil(Constants.AppConstants.APP_CONFIG_FILENAME);
		
	        public Elements Page(String strPageName)
	        {
	            strPageNameInOR = strPageName;
	            strFrameNameInOR = null;
	            if(Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
	            	Launcher.driver.switchTo().defaultContent();
	            
	            return new Elements();
	        }

	        public class Elements
	        {
	            //Constructor
	            public Elements()
	            {
	                Err.Description = null;
	            }

	            public Elements Frame(String strFrameName)
	            {
	            	strFrameNameInOR = strFrameName;
	            	return new Elements();
	            }
	            
	            public Events Element(String strElementName)
	            {
	                Action actions = new Action();
	                if(Script.dicConfigValues.get("ObjectRepository")!=null)  //{{
	                {
	                	String [] arr = Script.dicConfigValues.get("ObjectRepository").split(",");
	                    for (String strObjectRepository: arr)
	                    {
							try {
								if(strElementName.startsWith("/")){
									strObjectProperty = strElementName;
								}
								else if(strElementName.startsWith("(//")){
									strObjectProperty = strElementName;
								}else {
									strObjectProperty = actions.getObjectProperty(strObjectRepository, strPageNameInOR, strElementName,"xpath",strFrameNameInOR);
								}
							} catch (SAXException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ParserConfigurationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	                        if (strObjectProperty != "")
	                            break;
	                        else
	                            continue;
	                    }
	                }
	                else
	                    Err.Description = "Object Repository Name is not set in Config.xml";
	                return new Events();
	                
	            }
	        }

	        //----------------------------------------------------------------------------------------//
	        // Function Name : getObjectProperty
	        // Function Description : This function is called internally. It searches for the object in the OR XML file (based on the object name and page name) and returns the attribute value for the passed attribute. 'xpath' is default. If page or object is not found, it treats the parameter passed for 'strObjectName' as Xpath.
	        // Input Variable : Page name & Object name
	        // OutPut : true / false
	        // example : getObjectProperty("Google","SearchBox")
	        //---------------------------------------------------------------------------------------//
	        private String getObjectProperty(String strObjectRepository, String strPageName, String strObjectName,  String strAttributeName, String...strFrames ) throws SAXException, IOException, ParserConfigurationException
	        {
	        	String strFrameName=strFrames[0];
	        	strAttributeName = "xpath";

	            //Assigning Object Name to static variable access in other methods
	            strObjectNameInOR = strObjectName;		  //}} since strFrameNameInOR is static, it has to be cleared. Otherwise it has some value, but should be null in case frame is not passed.
	            String strObjectProperty = "";
	            
	            if(strFrameName==null)
	            {
		            try
		            {
		            	    //MultiMap dictMap = new MultiHashMap();
		            		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		            		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		            		Document document = docBuilder.parse(new File(properties.getProperty("objectrepository.location") + strObjectRepository + ".xml"));
		            		String val;
		            		NodeList pagelist = document.getElementsByTagName("Page");
		            		for (int i = 0; i < pagelist.getLength(); i++) 
		            		{
		            		    Node page = pagelist.item(i);
		            		    Element element=(Element)page;
		            		    
		            		    //page.getUserData("strObjUniq")
		            		    if(element.getAttribute("strObjUniq").equals(strPageName))	//change this condition to find "test" header in page node tag
		            		    {
		            		    NodeList elementlist  = page.getChildNodes();
		            		    int childcount=elementlist.getLength();
		            		    int index=0;
		            		    while (index < childcount)
		            		    {
		            		    	Node ele = elementlist.item(index);
			            		    Element elenode=(Element)ele;
			            		    
			            		    if(elenode.getAttribute("strObjUniq").equals(strObjectName))
			            		    {
			            		    	
			            		    	System.out.println(elenode.getAttribute("xpath"));
			            		    	strObjectProperty=elenode.getAttribute("xpath");
			            		    	index++;
			            		    	break;
			            		    }
			            		    else
			            		    	index++;
		            		    }
		            		    }
		            	}
		            		
		            
		            }
		            catch(Exception ee)
		            {
		            	
		            	System.out.println(ee.getMessage());
		            }
	            }
	            else
	            {
	            //with frame code	
	            	 try
			            {
			            		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			            		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			            		Document document = docBuilder.parse(new File(properties.getProperty("objectrepository.location") + strObjectRepository + ".xml"));
			            		String val;
			            		NodeList framelist = document.getElementsByTagName("Frame");
			            		for (int i = 0; i < framelist.getLength(); i++) 
			            		{
			            		    Node frame = framelist.item(i);
			            		    Element element=(Element)frame;
			            		    if(element.getAttribute("strObjUniq").equals(strFrameName))
			            		    {
			            		    	Node frameparent = frame.getParentNode();
			            		    	Element eleframeparent=(Element)frameparent;
			            		    	if(eleframeparent.getAttribute("strObjUniq").equals(strPageName))
			            		    	{
			            		    		//switch to code required
			            		    		if(Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
			            		    			Launcher.driver.switchTo().frame(Launcher.driver.findElement(By.xpath("//*[@src='"+element.getAttribute("src")+"']")));
			            		    		
			            		    		
			            		    		//////////////////////////
			            		    		NodeList elementlist  = frame.getChildNodes();
					            		    int childcount=elementlist.getLength();
					            		    int index=0;
					            		    while (index < childcount)
					            		    {
					            		    	Node ele = elementlist.item(index);
						            		    Element elenode=(Element)ele;
						            		    
						            		    if(elenode.getAttribute("strObjUniq").equals(strObjectName))
						            		    {
						            		    	
						            		    	System.out.println(elenode.getAttribute("xpath"));
						            		    	strObjectProperty=elenode.getAttribute("xpath");
						            		    	index++;
						            		    	break;
						            		    }
						            		    else
						            		    	index++;
					            		    }
			            		    	}
			            		    }
			            		}
			            }
			            catch(Exception ee)
			            {
			            	
			            	System.out.println(ee.getMessage());
			            }
					
	            }
	            return strObjectProperty;
	        }


	        //This Class contains functions for various actions
	        public class Events
	        {
	        	Action builder;
	            //Constructor for handling Webdriver
	            WebElement element = null;
	            public Events()
	            {
	                if (Err.Description == null)
	                {
	                	if(Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
	                    //if (GenericClass.dicConfig["SeleniumVariant"].ToLower().Contains("webdriver"))
	                    {
	                        try
	                        {
	                        	
	                            element = Launcher.driver.findElement(By.xpath(strObjectProperty));
	                            if (element.isDisplayed() == true || element.isEnabled() == true)
	                                Err.Description = null;
	                            else
	                                Err.Description = "OBJECT '" + strObjectNameInOR + "' is not displayed on PAGE '" + strPageNameInOR + "'";
	                        }
	                        catch (Exception e)
	                        {
	                            element = null;
	                            Err.Description = "OBJECT '" + strObjectNameInOR + "' is not found on PAGE '" + strPageNameInOR + "'";
	                        }
	                    }
	                    else
	                    {
	                        if (!Launcher.selenium.isElementPresent(strObjectProperty))
	                            Err.Description = "OBJECT '" + strObjectNameInOR + "' is not found on PAGE '" + strPageNameInOR + "'";
	                    }
	                }
	            }
	            //----------------------------------------------------------------------------------------//
	            // Function Name : Click
	            // Function Description : This function perform click operation on object passed to 'Element(String strObjectName)'
	            // Input Variable : Optional (Timeout)
	            // OutPut : true / false
	            // example : Page("Google").Element("SearchButton").Click(120)
	            //---------------------------------------------------------------------------------------//
	            public boolean Click(int... intTimeout)
	            {
	            	if(intTimeout == null)
	            		intTimeout[0] = 120;
	                boolean flag = false;
	                try
	                {
	                    if (Err.Description != null)
	                        return false;
	                    if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
	                    {
							try
							{
	                    		Thread.sleep(1000);  
	                    		// execute javascript in case of IE for clicking
	                    		try
								{
									element.click();
                                }
								catch(Exception ex)
								{
									((JavascriptExecutor) Launcher.driver).executeScript("arguments[0].click();", element);
								}
								flag=Browser.WaitForPageToLoad(intTimeout[0]);
							}
							catch(Exception e)
	                    	{
	                    		System.out.println(e.getMessage());
	                    		if(e.getMessage().contains("Timed out"))
	                    		{
		                    		try
		                    		{
		                    			Robot robot = new Robot();
			                            robot.keyPress(KeyEvent.VK_ESCAPE);
			                            robot.keyPress(KeyEvent.VK_ESCAPE);
			                            flag = true;
		                    		}
		                    		catch(Exception ee)
		                    		{
		                    			System.out.println(e.getMessage());
		                    			flag = true;
		                    		}
	                    		}
	                    	}
	                    }
	                        
	                    else
	                    {
	                        Launcher.selenium.click(strObjectProperty);
	                        flag =Browser.WaitForPageToLoad(intTimeout);
	                    }
	                    //flag= true;
	                }
	                catch (Exception e)
	                {
	                    flag = false;
	                    Err.Description = e.getMessage();
	                }
	                return flag;
	            }
				
				//----------------------------------------------------------------------------------------//
	            // Function Name : Click
	            // Function Description : This function perform click operation on object passed to 'Element(String strObjectName)'
	            // Input Variable : Optional (Timeout)
	            // OutPut : true / false
	            // example : Page("Google").Element("SearchButton").Click("Google", "SubmitButton", 120)
				//----------------------------------------------------------------------------------------//
	            public boolean Click(String sPageName, String sObjectName, int intTimeout)
	            {
	            	intTimeout=120;
	                boolean flag = false;
	                try
	                {
	                    flag = Click(1);
	                    strPageNameInOR = sPageName;
	                    strObjectNameInOR = sObjectName;
	                    if (flag)
	                        flag = WaitForElement(intTimeout);
	                }
	                catch (Exception e)
	                {
	                    flag = false;
	                }
	                return flag;
	            }
				
				/*
				//{}
	            public boolean Highlight (int intTimeout)
	            {
	            	boolean flag = false;
	                try
	                {
	                    if (Err.Description != null)
	                        return false;
	                    if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
	                    {
	                    	try{
	                    	element.notify();
	                    	}
	                    	catch(Exception e)
	                    	{
	                    		//if(e.getMessage() 
	                    	}
	                        //flag=Browser.WaitForPageToLoad(intTimeout);
	                    	flag = true;
	                    }
	                        
	                    else
	                    {
	                        Launcher.selenium.click(strObjectProperty);
	                        flag =Browser.WaitForPageToLoad(intTimeout);
	                    }
	                    //flag= true;
	                }
	                catch (Exception e)
	                {
	                    flag = false;
	                    Err.Description = e.getMessage();
	                }
	                return flag;
	            } */
				
	            
	          //----------------------------------------------------------------------------------------//
	            // Function Name : WaitForElement
	            // Function Description : This function waits for a particular element in a page until timeout is reached. 120 seconds is default
	            // Input Variable : 
	            // OutPut : true / false
	            // example : Page("Google").Element("SearchButton").WaitForElement(60)
	            //---------------------------------------------------------------------------------------//
	            public boolean WaitForElement(int iTimeout)
	            {
	                boolean flag = false;
	                try
	                {
	                	int intWait = Integer.parseInt(Script.dicConfigValues.get("intWait"));
	                    for (int i = 0; i <= iTimeout*intWait; i++)
	                    {
	                        if (Exist())
	                        {
	                            flag = true;
	                            break;
	                        }
	                        else
	                        {
//	                        	String dataSheetFileNames = launcher.getTestDataFileName();
//                 	        	ExcelReader td = new ExcelReaderImpl(dataSheetFileNames);
//	                        	Thread.sleep((Integer.parseInt(Script.dicConfigValues.get("intWait"))));
	                        	Thread.sleep(1000);
	                        }
	                    }
	                    if (!flag)
	                        Err.Description = "OBJECT '" + strObjectNameInOR + "' is not found in PAGE '" + strPageNameInOR + "'. Waited for " + iTimeout + " seconds.";
	                }
	                catch (Exception e)
	                {
	                    flag = false;
	                }
	                return flag;
	            }
	            
	            
	            //----------------------------------------------------------------------------------------//
	            // Function Name : MouseHover
	            // Function Description : This function perform mouse hover operation on object passed to 'Element(string strObjectName)'
	            // Input Variable : 
	            // OutPut : true / false
	            // example : Page("Google").Element("SearchButton").MouseHover()
	            //---------------------------------------------------------------------------------------//
	            public boolean MouseHover()
	            {
	                boolean flag = false;
	                try
	                {
	                    if (Err.Description != null)
	                        return false;
	                    if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
	                    {
	                        Actions builder = new Actions(Launcher.driver);
	                        builder.moveToElement(element).build().perform();
	                    }
	                    else
	                    	Launcher.selenium.mouseOver(strObjectProperty);
	                    Browser.WaitForPageToLoad(5);
	                    flag = true;
	                }
	                catch (Exception e)
	                {
	                    flag = false;
	                    Err.Description = e.getMessage();
	                }
	                return flag;
	            }
	            
	          //----------------------------------------------------------------------------------------//
	            // Function Name : Exist
	            // Function Description : This function checks existance of object passed to 'Element(string strObjectName)'
	            // Input Variable : 
	            // OutPut : true / false
	            // example : Page("Google").Element("SearchButton").Exist()
	            //---------------------------------------------------------------------------------------//
	            public boolean Exist()
	            {
	                boolean flag = false;
	                try
	                {
	                    if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
	                    {
	                        if (Launcher.driver.findElements(By.xpath(strObjectProperty)).size() > 0)
	                        {
	                            if (Launcher.driver.findElement(By.xpath(strObjectProperty)).isDisplayed())
	                                flag = true;
	                            else
	                                throw new Exception("OBJECT - " + strObjectProperty + " is not visible on PAGE - " + strPageNameInOR);
	                        }
	                        else
	                            Err.Description = "OBJECT '" + strObjectNameInOR + "' is not found on PAGE '" + strPageNameInOR + "'";
	                    }
	                    else
	                    {
	                    	if(Launcher.selenium.isElementPresent(strObjectProperty))
	                        {
	                            if (Launcher.selenium.isVisible(strObjectProperty))
	                                flag = true;
	                            else
	                                throw new Exception("OBJECT - " + strObjectProperty + " is not visible on PAGE - " + strPageNameInOR);
	                        }
	                        else
	                            throw new Exception("OBJECT - " + strObjectProperty + " is not found on PAGE - " + strPageNameInOR);
	                    }
	                }
	                catch (Exception e)
	                {
	                    flag = false;
	                    Err.Description = e.getMessage();
	                }
	                return flag;
	            }
	            //----------------------------------------------------------------------------------------//
	            // Function Name : Type
	            // Function Description : This function types into input box object passed to 'Element(string strObjectName)'
	            // Input Variable : String Keyword
	            // OutPut : true / false
	            // example : Page("Google").Element("SearchBox").Type("test")
	            //---------------------------------------------------------------------------------------//
	            public boolean Type(String strKeyword)
	            {
	                boolean flag = false;
	                try
	                {
	                    if (Err.Description != null)
	                        return false;
	                    if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
	                    {
	                    	try
                            {
                                element.clear();
                                element.sendKeys(strKeyword);
                            }
                            catch(Exception ex)
                            {
                                ((JavascriptExecutor) Launcher.driver).executeScript("arguments[0].value=''", element);
                                ((JavascriptExecutor) Launcher.driver).executeScript("arguments[0].click();", element);
                                ((JavascriptExecutor) Launcher.driver).executeScript("arguments[0].value='" + strKeyword + "'", element);
                            }
	                    }
	                    else
	                    	Launcher.selenium.type(strObjectProperty, strKeyword);
	                    flag = true;
	                }
	                catch (Exception e)
	                {
	                    flag = false;
	                    Err.Description = e.getMessage();
	                }
	                return flag;
	            }
	          //----------------------------------------------------------------------------------------//
	            // Function Name : Select
	            // Function Description : This function types into input box object passed to 'Element(string strObjectName)'
	            // Input Variable : String List Value
	            // OutPut : true / false
	            // example : Page("Google").Element("ListCountry").Select("test")
	            //---------------------------------------------------------------------------------------//
	            public boolean Select(String strListValue)
	            {
	                boolean flag = false;
	                try
	                {
	                    if (element == null)
	                        return false;
	                    if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
	                    {
	                    	
	                        try
	                        {
	                        	org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(element);  
	                            //openQA.Selenium.Support.UI.SelectElement select = new OpenQA.Selenium.Support.UI.SelectElement(element);	                        	
	                        	select.selectByVisibleText(strListValue);
	                        }
	                        catch (Exception e)
	                        {
	                            element.click();
	                            element.findElement(By.xpath("//*[text()='" + strListValue + "']"));
	                        }
	                    }
	                    else
	                    	Launcher.selenium.select(strObjectProperty, strListValue);
	                    flag = true;
	                }
	                catch (Exception e)
	                {
	                    flag = false;
	                    Err.Description = e.getMessage();
	                }
	                return flag;
	            }
	            
	          //----------------------------------------------------------------------------------------//
	            // Function Name : SelectPartialText
	            // Function Description : This function selects the value based on partial text match for 'Element(string strObjectName)'
	            // Input Variable : String List Value
	            // OutPut : true / false
	            // example : Page("Google").Element("ListCountry").SelectPartialText("test")
	            //---------------------------------------------------------------------------------------//
	            public boolean SelectPartialText(String strListValue)
	            {
	                boolean flag = false;
	                try
	                {
	                    if (element == null)
	                        return false;
	                    if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
	                    {
	                    	
	                        try
	                        {
	                        	org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(element);  
	                            List<WebElement> lstOptions = select.getOptions();
	                            for(WebElement option:lstOptions){
	                                if(option.getText().contains(strListValue)) {
	                                  	String strvalue = option.getAttribute("value"); 
	                                  	select.selectByValue(strvalue);
	                                	break;                                 
	                                }    
	                            }
	                        	
	                        }
	                        catch (Exception e)
	                        {
	                            element.click();
	                            element.findElement(By.xpath("//*[contains(text(),'" + strListValue + "']"));
	                        }
	                    }
	                    else
	                    	Launcher.selenium.select(strObjectProperty, strListValue);
	                    flag = true;
	                }
	                catch (Exception e)
	                {
	                    flag = false;
	                    Err.Description = e.getMessage();
	                }
	                return flag;
	            }
	            
	    		//----------------------------------------------------------------------------------------//
	            // Function Name : IsValueSelected
	            // Function Description : This function validates if the values passed from outside is the selected value in the List'
	            // Input Variable : String List Value to Check
	            // OutPut : true / false
	            // example : Page("Google").Element("ListCountry").IsValueSelected("test")
	            //---------------------------------------------------------------------------------------//
	            public boolean IsValueSelected(String strListValuetoCheck)
	            {
	                boolean flag = false;
	                String strSelectedText = "";
	                try
	                {
	                    if (element == null)
	                        return false;
	                    if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
	                    {	                    	
							org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(element);  
							WebElement option = select.getFirstSelectedOption();
							strSelectedText = option.getText();                 
	                    }
	                    else
	                    	strSelectedText = Launcher.selenium.getSelectedValue(strObjectProperty);
						
						if(strSelectedText.equalsIgnoreCase(strListValuetoCheck))
							flag = true;
						else
							flag = false;
	                }
	                catch (Exception e)
	                {
	                    flag = false;
	                    Err.Description = e.getMessage();
	                }
	                return flag;
	            }
	            
	          //----------------------------------------------------------------------------------------//
	            // Function Name : isVisible
	            // Function Description : This function verifies the visibility of an object passed to 'Element(string strObjectName)'
	            // Input Variable : 
	            // OutPut : true / false
	            // example : Page("Google").Element("lnkEspanol").isVisible()
	            //---------------------------------------------------------------------------------------//
	            public boolean isVisible()
	            {
	                boolean flag = false;
	                try
	                {
	                    if (Err.Description != null)
	                        return false;
	                    if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
	                        flag = element.isDisplayed();
	                    else
	                        flag = Launcher.selenium.isVisible(strObjectProperty);
	                }
	                catch (Exception e)
	                {
	                    flag = false;
	                    Err.Description = e.getMessage();
	                }
	                return flag;
	            }
	          //----------------------------------------------------------------------------------------//
	            // Function Name : isSelected
	            // Function Description : This function verifies for selected radio or list object passed to 'Element(string strObjectName)'
	            // Input Variable : 
	            // OutPut : true / false
	            // example : Page("Google").Element("chkbox1").isSelected()
	            //---------------------------------------------------------------------------------------//
	            public boolean isSelected()
	            {
	                boolean flag = false;
	                try
	                {
	                    if (Err.Description != null)
	                        return false;
	                    if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
	                        flag = element.isSelected();
	                    else
	                        flag = Launcher.selenium.isChecked(strObjectProperty);
	                }
	                catch (Exception e)
	                {
	                    flag = false;
	                    Err.Description = e.getMessage();
	                }
	                return flag;
	            }

	          //----------------------------------------------------------------------------------------//
	            // Function Name : getText
	            // Function Description : This function gets the text over an object passed to 'Element(string strObjectName)'
	            // Input Variable : 
	            // OutPut : String captured text
	            // example : Page("Google").Element("element1").getText()
	            //---------------------------------------------------------------------------------------//
	            public String GetText()
	            {
	                String strElementText = "";
	                try
	                {
	                    if (Err.Description != null)
	                        throw new Exception(Err.Description);
	                    if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
	                        strElementText = element.getText();
	                    else
	                        strElementText = launcher.selenium.getText(strObjectProperty);
	                }
	                catch (Exception e)
	                {
	                    strElementText = "";
	                    Err.Description = e.getMessage();
	                }
	                return strElementText;
	            }
	          //----------------------------------------------------------------------------------------//
	            // Function Name : getValue
	            // Function Description : This function gets the value of an input box object passed to 'Element(string strObjectName)'
	            // Input Variable : 
	            // OutPut : String text - for input box, on/off - for checkbox or radio
	            // example : Page("Google").Element("SearchBox").getValue()
	            //---------------------------------------------------------------------------------------//
	            public String GetValue()
	            {
	                String strElementValue = "";
	                try
	                {
	                    if (Err.Description != null)
	                        throw new Exception(Err.Description);
	                    if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
	                        strElementValue = element.getAttribute("value");
	                    else
	                        strElementValue = Launcher.selenium.getValue(strObjectProperty);
	                }
	                catch (Exception e)
	                {
	                    strElementValue = "";
	                    Err.Description = e.getMessage();
	                }
	                return strElementValue;
	            }
	            
	          //----------------------------------------------------------------------------------------//
	            // Function Name : getXpath
	            // Function Description : This function returns xpath of the element
	            //---------------------------------------------------------------------------------------//
	            public String GetXpath()
	            {
	                return strObjectProperty;
	            }
	    }

/////////////////////////////////////////////////////
	    public static class Err
	    {
	        public static String Description = null;
	    }
	    

	    public static class Browser
	    {
//////////////////////*********************** wait for page to load *****************/////////////////////////////
    public static boolean WaitForPageToLoad(int... intTimeOut) throws ScriptException, InterruptedException
      {
      		String dataSheetFileNames = launcher.getTestDataFileName();
        	ExcelReader td = new ExcelReaderImpl(dataSheetFileNames);
			int multiplier = Integer.parseInt(Script.dicConfigValues.get("intWait")); //{}
			if(intTimeOut == null)
        		intTimeOut[0] = 120*multiplier;
        	else
        		intTimeOut[0] = intTimeOut[0]*multiplier;
				
  //      	intTimeOut = (int) (intTimeOut *  Float.parseFloat(td.getValue("ConfigValues","iWait", "VALUE")));
        	String strBrowserState = "";
        	int intTime = 0;
        	JavascriptExecutor js = (JavascriptExecutor) Launcher.driver;
        	toGotoHere:
        		try
        		{
        			if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
        			{
          	          	
          	while(js.executeScript("return navigator.onLine").toString().toLowerCase().equals("false") && intTime <= intTimeOut[0])
          	{
          		Thread.sleep(1000);
          		intTime += 1;
          	}
          }
          else
          {
          	while(Launcher.selenium.getEval("return navigator.onLine").toString().toLowerCase().equals("false") && intTime <= intTimeOut[0])
              {
              Thread.sleep(1000);
              intTime += 1;
              }	
          }
          }
          catch(Exception ee)
          {
          	Thread.sleep(1000);
              intTime += 1;
              break toGotoHere;
          }
          
          Thread.sleep(2000);
          intTime = 0;
		  
		  
          while ((!strBrowserState.toLowerCase().equals("complete")) && intTime <= intTimeOut[0])
          {
              Thread.sleep(1000);
              if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
              	strBrowserState = js.executeScript("return window.document.readyState").toString();
              else
              	 strBrowserState = Launcher.selenium.getEval("window.document.readyState");
              intTime += 1;
          }
          String strBrowserNavigatorState = "";
          if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
          	strBrowserNavigatorState = js.executeScript("return navigator.onLine").toString();
          else
          	strBrowserNavigatorState = Launcher.selenium.getEval("navigator.onLine");
          if ((!strBrowserState.toLowerCase().equals("complete")) || (!strBrowserNavigatorState.equals("true")))
          {
              return false;
          }
          return true;
      }
      
    //----------------------------------------------------------------------------------------//
      // Function Name : Back
      // Function Description : This function performs Browser back operation
      // Input Variable : 
      // OutPut : true / false
      // example : Browser.Back
      //---------------------------------------------------------------------------------------//
      public static boolean Back()
      {
          boolean flag = false;
          try
          {
              if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
            	  Launcher.driver.navigate().back();
              else
            	  Launcher.selenium.goBack();
              Browser.WaitForPageToLoad(240);
              flag = true;
          }
          catch (Exception e)
          {
              flag = false;
              Err.Description = e.getMessage();
          }
          return flag;
      }
    //----------------------------------------------------------------------------------------//
      // Function Name : Navigate
      // Function Description : This function navigates the opened browser window to the passed url
      // Input Variable : Page URL
      // OutPut : true / false
      // example : Browser.Navigate("http://www/google.com")
      //---------------------------------------------------------------------------------------//
      public static boolean Navigate(String strURL)
      {
          boolean flag = false;
          try
          {
              if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
            	  Launcher.driver.navigate().to(strURL);
              else
            	  Launcher.selenium.open(strURL);
              Browser.WaitForPageToLoad(240);
              flag = true;
          }
          catch (Exception e)
          {
              flag = false;
              Err.Description = e.getMessage();
          }
          return flag;
      }
    //----------------------------------------------------------------------------------------//
      // Function Name : Refresh
      // Function Description : This function performs Page Refresh operation
      // Input Variable : 
      // OutPut : true / false
      // example : Browser.Refresh
      //---------------------------------------------------------------------------------------//
      public static boolean Refresh()
      {
          boolean flag = false;
          try
          {
              if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
            	  Launcher.driver.navigate().refresh();
              else
            	  Launcher.selenium.refresh();
              Browser.WaitForPageToLoad(240);
              flag = true;
          }
          catch (Exception e)
          {
              flag = false;
              Err.Description = e.getMessage();
          }
          return flag;
      }


    //----------------------------------------------------------------------------------------//
      // Function Name : maximize
      // Function Description : This function maximizes the opened browser window
      // Input Variable : 
      // OutPut : true / false
      // example : Browser.Maximize()
      //---------------------------------------------------------------------------------------//
      public static boolean Maximize()
      {
          boolean flag = false;
          try
          {
              if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
            	  Launcher.driver.manage().window().maximize();
              else
              {
				  Launcher.selenium.windowMaximize();
            	  
              }
              
              flag = true;
          }
          catch (Exception e)
          {
              flag = false;
              Err.Description = e.getMessage();
          }
          return flag;
      }
    //----------------------------------------------------------------------------------------//
      // Function Name : SelectOriginalWindow
      // Function Description : This function selects the original browser window whose creation time is 0
      // Input Variable : 
      // OutPut : true / false
      // example : Browser.SelectOriginalWindow()
      //---------------------------------------------------------------------------------------//
      public static boolean SelectOriginalWindow()
      {
          boolean flag = false;
          try
          {
              if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
            	  Launcher.driver.switchTo().window(null);
              else
              {
            	  Launcher.selenium.selectWindow(null);
            	  Launcher.selenium.windowFocus();
              }
              flag = true;
          }
          catch (Exception e)
          {
              flag = false;
              Err.Description = e.getMessage();
          }
          return flag;
      }
    //----------------------------------------------------------------------------------------//
      // Function Name : WindowFocus
      // Function Description : This function focuses the selected browser window
      // Input Variable : 
      // OutPut : true / false
      // example : Browser.WindowFocus()
      //---------------------------------------------------------------------------------------//
      public static boolean WindowFocus()
      {
    	  JavascriptExecutor js = (JavascriptExecutor) Launcher.driver;
          boolean flag = false;
          try
          {
              if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
        	  js.executeScript("window.focus()");
              else
                  Launcher.selenium.windowFocus();
              flag = true;
          }
          catch (Exception e)
          {
              flag = false;
              Err.Description = e.getMessage();
          }
          return flag;
      }
      //----------------------------------------------------------------------------------------//
      // Function Name : GetURL
      // Function Description : This function gets the page URL and saves it in GenericClass.dicOutput["url"]
      // Input Variable : 
      // OutPut : true / false
      // example : Browser.GetURL()
      //---------------------------------------------------------------------------------------//
      public static String GetURL()
      {
          String strURL = "";
      try
      {
          if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
              strURL = Launcher.driver.getCurrentUrl();
          else
              strURL = Launcher.selenium.getLocation();
      }
      catch (Exception e)
      {
          strURL = "";
              Err.Description = e.getMessage();
          }
          return strURL;
      }
      //----------------------------------------------------------------------------------------//
      // Function Name : VerifyTitle
      // Function Description : This function compares the actual page title with the passed string
      // Input Variable : Page Title
      // OutPut : true / false
      // example : Browser.VerifyTitle("Google")
      //---------------------------------------------------------------------------------------//
      public static boolean VerifyTitle(String strWindowTitle)
      {
          boolean flag = false;
          try
          {
              String strBrowserTitle = "";
          if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
                  strBrowserTitle = Launcher.driver.getTitle();
              else
            	  Launcher.selenium.getTitle();
              if (strBrowserTitle.contains(strWindowTitle))
                  flag = true;
              else
                  flag = false;
          }
          catch (Exception e)
          {
              flag = false;
              Err.Description = e.getMessage();
          }
          return flag;
      }
    //----------------------------------------------------------------------------------------//
      // Function Name : Close
      // Function Description : This function closes the page window.
      // Input Variable : 
      // OutPut : true / false
      // example : Browser.Close()
      //---------------------------------------------------------------------------------------//
      public static boolean Close()
      {
          boolean flag = false;
          try
          {
              if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
            	  Launcher.driver.quit();
              else
            	  Launcher.selenium.close();
              flag = true;
          }
          catch (Exception e)
          {
              flag = false;
              Err.Description = e.getMessage();
          }
          return flag;
      }
      //----------------------------------------------------------------------------------------//
      // Function Name : SelectFrame
      // Function Description : This function selects the frame.
      // Input Variable : Frame name
      // OutPut : true / false
      // example : Browser.SelectFrame("Freme1")
      //---------------------------------------------------------------------------------------//
      public static boolean SelectFrame(String strFrameName)
      {
          boolean flag = false;
          try
          {
              if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
            	  Launcher.driver.switchTo().frame(strFrameName);
              else
            	  Launcher.selenium.selectFrame(strFrameName);
              flag = true;
          }
          catch (Exception e)
          {
              flag = false;
              Err.Description = e.getMessage();
          }
          return flag;
      }
      
      
    //----------------------------------------------------------------------------------------//
      // Function Name : OpenNewWindow
      // Function Description : This function opens new browser window and navigates the page to the passed url
      // Input Variable : Page URL
      // OutPut : true / false
      // example : Browser.OpenNewWindow("http://www/google.com")
      //---------------------------------------------------------------------------------------//
      public static boolean OpenNewWindow(String strBaseURL)
      {
    	  JavascriptExecutor js = (JavascriptExecutor) launcher.driver;
    	  if (strBaseURL == null)
    		  strBaseURL = "";
      boolean flag = false;
      if (strBaseURL == "")
          strBaseURL = Script.dicCommonValue.get("strBrowserPath");
      try
      {
          if (Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
          {
              //Opening new window through Javascript
        	  //while(js.executeScript("return navigator.onLine").toString().toLowerCase().equals("false") && intTime <= intTimeOut)
              js.executeScript("window.open('" + strBaseURL + "','NewWindow')");
              Launcher.driver.switchTo().window("NewWindow");
              Browser.Maximize();
          }
          else
          {
        	 //Selenium selenium1 = new WebDriverBackedSelenium(launcher.driver, "");
        	  Launcher.selenium1 = new DefaultSelenium("localhost", 4444,Script.dicConfigValues.get("strBrowserType").toLowerCase(), "http://www.google.com");
        	  Launcher.selenium1.start();
        	  Launcher.selenium1.setTimeout("120000");
              //GenericClass.selenium1 = new DefaultSelenium("localhost", 4444, appConfig.getProperty("app.browser"), strBaseURL);
              //GenericClass.selenium1.Start();
              //GenericClass.selenium1.SetTimeout("120000");
              if (!Script.dicCommonValue.containsKey("URLQualifier"))
            	  Script.dicCommonValue.put("URLQualifier", "/");
              //selenium1.open(Script.dicCommonValue.get("URLQualifier"));
              Launcher.selenium1.openWindow(Script.dicCommonValue.get("URLQualifier"), "false");
              Launcher.selenium1.windowMaximize();
              }
              Browser.WaitForPageToLoad(1000);
              flag = true;
          }
          catch (Exception e)
          {
              flag = false;
              Err.Description = e.getMessage();
          }
          return flag;
      } 
      
      //----------------------------------------------------------------------------------------//
      // Function Name : SelectWindowByObject
      // Function Description : This function selects the browser window on which passed object is found
      // Input Variable : Object XPath
      // OutPut : true / false
      // example : Browser.SelectWindowByObject("//[@id='login']")
      //---------------------------------------------------------------------------------------//
      public static boolean SelectWindowByObject(String strObjectXpath)
      {
    	  boolean flag = false;
    	  boolean isObjectFound = false;
          try
          {
        	  
    	  if(Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
          {
              for(String strWindowHandle : Launcher.driver.getWindowHandles())
              {
            	  Launcher.driver.switchTo().window(strWindowHandle);
                  if (Launcher.driver.findElements(By.xpath(strObjectXpath)).size() > 0)
                  {
                      isObjectFound = true;
                      break;
                  }
                  else
                      continue;
              }
          }
          else
          {
              for (String strWindowTitle : Launcher.selenium.getAllWindowTitles())
              {
            	  
            	  Launcher.selenium.selectWindow(strWindowTitle);
                  if (Launcher.selenium.isElementPresent(strObjectXpath))
                  {
                      isObjectFound = true;
                      break;
                  }
                  else
                      continue;
              }
          }
          if (!isObjectFound)
              throw new Exception("No object found with xpath " + strObjectXpath + " in any of the opened windows.");
          }
          catch (Exception e)
          {
              flag = false;
              Err.Description = e.getMessage();
          }
          return flag;
      }
      

	    }
	    
	}
