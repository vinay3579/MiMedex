package com.pyramidconsulting.util;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;

import com.mercury.qualitycenter.otaclient.ClassFactory;
import com.mercury.qualitycenter.otaclient.IAttachment;
import com.mercury.qualitycenter.otaclient.IAttachmentFactory;
import com.mercury.qualitycenter.otaclient.IBaseFactory;
import com.mercury.qualitycenter.otaclient.IExtendedStorage;
import com.mercury.qualitycenter.otaclient.IList;
import com.mercury.qualitycenter.otaclient.IRun;
import com.mercury.qualitycenter.otaclient.ITDConnection;
import com.mercury.qualitycenter.otaclient.ITSTest;
import com.mercury.qualitycenter.otaclient.ITestSet;
import com.mercury.qualitycenter.otaclient.ITestSetFactory;
import com4j.Com4jObject;

public class QCManager {

	private ITDConnection connection = null;

	private static final Logger logger = Logger.getLogger(QCManager.class);

	public QCManager(final String qcUrl, final String domainName, final String projectName, final String userName, final String password) {
		connection = getQCConnection(qcUrl, domainName, projectName, userName, password);
	}


	public static void main(final String[] args) {
		try{
		final QCManager manager = new QCManager("http://10.4.0.18:8080/qcbin", "POC", "SEAP", "manojt", "");
		manager.updateTestStatus("Run: 6/28/2012 12:12:17 PM", "[1]Test1234", "SeAP Java", "Failed");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	public boolean updateTestStatus(final String runName, final String testName, final String testSetName, final String status) {

		final ITestSet iTestSet =  getTestSetObj(testSetName);
		logger.info("updating status of test run:"+ runName +": to:"+status);
		if(iTestSet==null) {
			logger.warn("No such testset exist:"+testSetName);
			return false;
		}

		final ITSTest test = getTSTestObj(iTestSet, testName);
		if(test==null) {
			logger.warn("No such test ["+testName+"] exist into testset:"+testSetName);
			return false;
		}

		final IRun run = getTSTestRunObj(test, runName);
		if(run==null) {
			logger.warn("No such run instance ["+runName+"] exist into test:"+testName);
			return false;
		}

		//final IRunFactory runFactory = test.runFactory().queryInterface(IRunFactory.class);
		//final IRun run = runFactory.addItem(runName).queryInterface(IRun.class);
		run.status(status);
		run.post();
		logger.debug("Status of test run ["+runName+"] is updated to:"+status);
		return true;
	}

	public void attachFolder(final String testName, final String testSetName, final File dir) throws FileNotFoundException {
		try{
		if(!dir.isDirectory() || !dir.exists()) {
			throw new FileNotFoundException("Either not a directory or does not exist:"+dir.getAbsolutePath());
		}
		for(final File file : dir.listFiles()) {
			attachFile(testName, testSetName, file);
		}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	public boolean attachFile(final String testName, final String testSetName, final File file) throws FileNotFoundException {
		final ITestSet iTestSet =  getTestSetObj(testSetName);
		logger.info("attaching file:"+file.getAbsolutePath());
		if(iTestSet==null) {
			logger.warn("No such testset exist:"+testSetName);
			return false;
		}

		final ITSTest test = getTSTestObj(iTestSet, testName);
		if(test==null) {
			logger.warn("No such test ["+testName+"] exist into testset:"+testSetName);
			return false;
		}
		return attachFile(test, file);
	}

	public boolean attachFile(final ITSTest test, final File file) throws FileNotFoundException {
		if(!file.isFile() || !file.exists()) {
			throw new FileNotFoundException("Either not a file or does not exist:"+file.getAbsolutePath());
		}
		logger.info("attaching File ["+file.getAbsolutePath()+"] to test["+test.name()+"]");
		final IAttachmentFactory attachmentFactory = test.attachments().queryInterface(IAttachmentFactory.class);
		final IAttachment attachment = attachmentFactory.addItem(file.getName()).queryInterface(IAttachment.class);
		final IExtendedStorage storage = attachment.attachmentStorage().queryInterface(IExtendedStorage.class);
		storage.clientPath(file.getParentFile().getAbsolutePath());
		storage.save("",true);
		attachment.post();
		logger.debug("File ["+file.getAbsolutePath()+"] attached to test["+test.name()+"]");
		return true;
	}


	public ITDConnection getQCConnection(final String qcUrl, final String domainName, final String projectName, final String userName, final String password) {
		final ITDConnection qcConn = ClassFactory.createTDConnection();
		qcConn.initConnectionEx(qcUrl);
		logger.debug("is connectetd to :"+qcUrl +"="+qcConn.connected());
		qcConn.connectProjectEx(domainName, projectName, userName, password);
		return qcConn;
	}

	private IList getTestSetList(final String filter) {
		IList list = null;
		final ITestSetFactory sTestSetFactory = connection.testSetFactory().queryInterface(ITestSetFactory.class);
		list = sTestSetFactory.newList(filter);
		return list;
	}

	private IList getTestCaseList(final ITestSet testSet, final String filter) {
		IList list = null;
		final IBaseFactory baseFactory = testSet.tsTestFactory().queryInterface(IBaseFactory.class);
		list = baseFactory.newList(filter);
		return list;
	}
	private IList getRunList(final ITSTest test, final String filter) {
		IList list = null;
		final IBaseFactory baseFactory = test.runFactory().queryInterface(IBaseFactory.class);
		list = baseFactory.newList(filter);
		return list;
	}

	private IRun getTSTestRunObj(final ITSTest test, final String runName) {
		IRun requiredRun = null;
		final IList runs = getRunList(test, "");
		for(final Com4jObject com4jObject : runs) {
			final IRun run = com4jObject.queryInterface(IRun.class);
			if(run.name().equals(runName)){
				requiredRun = run;
				break;
			}
		}
		return requiredRun;
	}

	private ITSTest getTSTestObj(final ITestSet testSet, final String testName) {
		ITSTest requiredTest = null;
		final IList testCases = getTestCaseList(testSet, "");
		for(final Com4jObject com4jObject : testCases) {
			final ITSTest test = com4jObject.queryInterface(ITSTest.class);
			if(test.name().equals(testName)){
				requiredTest = test;
				break;
			}
		}
		return requiredTest;
	}

	private ITestSet getTestSetObj(final String testSetName) {
		ITestSet requiredTS = null;
		final IList testSets = getTestSetList("");
		for(final Com4jObject com4jObject : testSets) {
			final ITestSet testSet = com4jObject.queryInterface(ITestSet.class);
			if(testSet.name().equals(testSetName)){
				requiredTS = testSet;
				break;
			}
		}
		return requiredTS;
	}
}
