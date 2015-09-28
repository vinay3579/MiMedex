package com.pyramidconsulting.runner;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.pyramidconsulting.reader.ExcelReader;
import com.pyramidconsulting.report.Reporter;
import com.pyramidconsulting.util.FileUtil;
import com.pyramidconsulting.util.JVMUtil;
import com.thoughtworks.selenium.Selenium;

public class ExternalScriptRunner implements ScriptRunner {

	private static  Logger logger = Logger
			.getLogger(ExternalScriptRunner.class);

	// private final String scriptPackage = "script";



	private  Selenium selenium;
	private  ExcelReader or; // object repository
	private  ExcelReader td; //test data
	private  WebDriver driver;
	private  Reporter reporter;
	private  String testCaseName;


	//private final PropertyUtil properties = PropertyUtil.getInstance();

	// public ExternalScriptRunner() {
	// super();
	// }
	public ExternalScriptRunner( Selenium selenium,
			 WebDriver driver,  ExcelReader or,  ExcelReader td,
			 Reporter reporter,  String testCaseName) {
		super();
		this.selenium = selenium;
		this.or = or;
		this.td = td;
		this.driver = driver;
		this.reporter = reporter;
		this.testCaseName = testCaseName;
	}

	@Override
	public void runScript( File scriptFile) throws Exception {
		
		try{
			
		Script script = null;
		script.isNotePadFileError = false;
		//final File file = new File(fileName);
		if (!scriptFile.exists() || !scriptFile.isFile()) {
			//script.isNotePadFileError = true;
			logger.error("File not found:"+scriptFile.getAbsolutePath());
			throw new FileNotFoundException(
					"Either file does not exist or not a file:"
							+ scriptFile.getAbsolutePath());
		}
		logger.info("running script:" + scriptFile.getAbsolutePath());

		if(FileUtil.isModified(scriptFile) || !JVMUtil.generateJavaFileObj(scriptFile.getName(),".class").exists()) {
			 File javaSourceClassFile = JVMUtil.convertToJavaSource(scriptFile);
			script = JVMUtil.compileJavaSourceFile(javaSourceClassFile);
		} else {
			 File file = JVMUtil.generateJavaFileObj(scriptFile.getName(),".class");
			script = JVMUtil.getClassObject(file);
		}
		try {
			script.init(selenium, driver, or, td, reporter,testCaseName);
			script.run();
		} catch ( Throwable e) {
			logger.error("There was some error while running the script", e);
		}
		}
		catch(Exception e)
		{
			Script.isNotePadFileError = true;
			reporter.ReportStep("Compilation Error", "Compilation Error", e.toString(), "Fail");
			System.out.println(e);
			
		}
	}



	@Override
	public void runScript(String scriptFileName) throws Exception 
	{
		try
		{
			if (!scriptFileName.toLowerCase().endsWith(".txt")) 
			{
				scriptFileName +=".txt";
			}
			runScript(new File(scriptFileName));
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
