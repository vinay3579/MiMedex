package com.pyramidconsulting.util;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.pyramidconsulting.Constants;



public class FileUtil extends FileUtils {


	private static Logger logger = Logger.getLogger(FileUtil.class);
	private static  Properties repository = null;
	private static PropertyUtil property = new PropertyUtil(Constants.PROPERTY_CONFIG_FILENAME);
	private static boolean isInitialized = false;



	private static String getRootDir(){
		String dir = property.getProperty("framework.temp.data.root.location");
		if(StringUtils.isBlank(dir)) {
			dir = defaultDirectory();
		}
		return dir;
	}
	private static String getDir(){
		String folder = property.getProperty("framework.temp.data.dir.name");
		if (StringUtils.isBlank(folder)) {
			folder = "SeAP";
		}
		return folder;
	}
	private static String getProprtyFileName(){
		String proprtyName = property.getProperty("framework.temp.data.file.name");
		if(StringUtils.isBlank(proprtyName)) {
			proprtyName = "repository.properties";
		}
		return  proprtyName;
	}

	private static void init() throws FileNotFoundException, IOException{
		try{
		repository = new Properties();

		final String dir =getRootDir();
		final String folder = getDir();
		final String propertyName = getProprtyFileName();
		final boolean success = new File(dir+"/"+folder).mkdirs();
		if (!success) {
			logger.debug("The application directory= '"+folder+"' at location = '"+dir+"' cann't be created");
		}
		logger.info("The application directory= '"+folder+"' at location = '"+dir+"' has been created");

		final File file = new File(dir+"/"+folder+"/"+propertyName);
		if(!file.exists()) {
			file.createNewFile();
		}
		logger.info("The repository file = '"+file.getAbsolutePath()+"' has been created");
		repository.load(new FileInputStream(file));
		isInitialized = true;
		logger.info("The Object of FileUtil has been Initialized");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}


	private static String defaultDirectory()
	{
		final String OS = System.getProperty("os.name").toUpperCase();
		if (OS.contains("WIN")) {
			return System.getenv("APPDATA");
		} else if (OS.contains("MAC")) {
			return System.getProperty("user.home") + "/Library/Application "
					+ "Support";
		} else if (OS.contains("NUX")) {
			return System.getProperty("user.home");
		}
		return System.getProperty("user.dir");
	}


	private  static void setLastModifiedTime(final File file) throws FileNotFoundException, IOException{
		try{
		repository.put(file.getAbsolutePath(), file.lastModified()+"");
		final FileOutputStream fileOutputStream = new FileOutputStream(getRootDir()+"/"+getDir()+"/"+getProprtyFileName());
		repository.store(fileOutputStream, null);
		logger.info("The script file '"+file.getAbsolutePath()+"' has added in repository");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

	}
	public static boolean isModified(final File file) throws FileNotFoundException, IOException {
		if(!isInitialized) {
			init();
		}
		if(!file.exists()){
			logger.error("The script file '"+file.getAbsolutePath()+"' doesn't exist");
			throw new RuntimeException("The script file '"+file.getAbsolutePath()+"' doesn't exist ");
		}
		boolean isModified = false;
		if(!repository.containsKey(file.getAbsolutePath()))
		{
			setLastModifiedTime(file);
			isModified = true;
		}else{
			final long lastModified = Long.parseLong((String) repository.get(file.getAbsolutePath()));
			final long actualLastModified = file.lastModified();
			if(lastModified != actualLastModified ){
				isModified = true;
				setLastModifiedTime(file);
			}



		}
		return isModified;
	}
	public boolean isModified(final String fileNmae) throws FileNotFoundException, IOException{
		return isModified(new File(fileNmae));
	}
	public boolean isModifiedIntoLib(final String fileName) throws FileNotFoundException, IOException{
		boolean isModified = false;
		if(!isInitialized) {
			init();
		}
		isModified = isModified(fileName);
		String libDirLocation =  property.getProperty("script.lib.root.dir.location");
		if(StringUtils.isEmpty(libDirLocation)) {
			libDirLocation = new File(fileName).getParent();
		}
		String libName = property.getProperty("script.lib.dir.name");
		if(StringUtils.isEmpty(libName)) {
			libName = "lib";
		}
		final File libDir =new File(libDirLocation+"/"+libName);
		final File[] files = libDir.listFiles();
		for(final File file : files) {
			if(file.isFile() && isModified(file)) {
				isModified = true;
			}
		}
		return isModified;
	}
	public boolean isModifiedIntoLib(final File file) throws FileNotFoundException, IOException{
		return isModifiedIntoLib(file.getAbsolutePath());
	}
}
