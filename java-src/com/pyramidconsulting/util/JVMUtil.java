package com.pyramidconsulting.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.pyramidconsulting.Constants;
import com.pyramidconsulting.Launcher;
import com.pyramidconsulting.runner.Script;

public class JVMUtil {

	private static final String CLASS_NAME_PH = "__CLASS_NAME__";
	private static final String SCRIPT_CODE_PH = "__SCRIPT_CODE__";
	private static final String PACKAGE_NAME_PH = "__PACKAGE_NAME__";
	private static final String LIB_PACKAGE_NAME_PH = "__LIB_PACKAGE_NAME__";
	private static final String LIB_METHODS_PH = "__LIB_METHODS__";

	private static PropertyUtil properties = new PropertyUtil(Constants.PROPERTY_CONFIG_FILENAME);
	//private static PropertyUtil appProperties = new PropertyUtil(Constants.AppConstants.APP_CONFIG_FILENAME);

	private static final Logger logger = Logger.getLogger(JVMUtil.class);

	public static File convertToJavaSource(String testScriptFile)
			throws IOException {

		if (!testScriptFile.toLowerCase().endsWith(".txt")) {
			testScriptFile = testScriptFile + ".txt";
		}
		
		final File file = new File(testScriptFile);
		return convertToJavaSource(file);
	}

	private static String generatePackageName(final String url) {
		final StringBuilder packageName = new StringBuilder("");
		try {
			final URI uri = new URI(url);
			final String host = uri.getHost().replaceAll("^((ww|z|a)[a-zA-Z0-9-]{0,}\\.)", "");
			final String[] words = host.split("\\.");
			for(int i =words.length-1;i>=0;i--) {
				if(!StringUtils.isNumeric(words[i])) {
					packageName.append(words[i]);
					if(i>0) {
						packageName.append(".");
					}
				}
			}
			//packageName = uri.getHost();
		} catch (final Exception e) {
			packageName.append("nopackage");
		}
		if("".equals(packageName.toString())) {
			packageName.append("nopackage");
		}
		logger.debug("packagename:"+packageName);
		return packageName.toString();
	}

	public static String generatePackageName() {
		return generatePackageName(Script.dicConfigValues.get("strApplicationURL"));
	}

	public static File generateJavaFileObj(final String scriptFile, final String fileExtn) {
		final String fileName = properties.getProperty("script.java.source.location")
				+"/" + generatePackageName().replace(".","/") +"/"+getClassName(scriptFile) +"."+fileExtn;
		return new File(fileName);
	}

	public static File convertToJavaSource(final File testScriptFile)
			throws IOException {
		final String script = FileUtils.readFileToString(testScriptFile);

		final String templateFile = properties
				.getProperty("java.source.template");
		logger.info("Reading java source template file " + templateFile
				+ " from classpath.");
		/*String fileContent = IOUtils.readFileContentFromClasspath(templateFile);

		final File templateFileObj = new File(templateFile);
		if(!templateFileObj.exists()) {
			logger.error("Template file ["+templateFileObj.getAbsolutePath()+"] does not exist");
			throw new RuntimeException("Template file ["+templateFileObj.getAbsolutePath()+"] does not exist");
		}
		String javaSource = FileUtils.readFileToString(templateFileObj);
		 */
		String javaSource = IOUtils.readFileContentFromClasspath(templateFile);
		if(StringUtils.isEmpty(javaSource)) {
			logger.error("could not read template file ["+templateFile+"] from classpath.");
			throw new RuntimeException("could not read template file ["+templateFile+"] from classpath.");
		}
		final String packageName = generatePackageName();
		// Save source in .java file.
		final File root = new File(
				properties.getProperty("script.java.source.location"),
				packageName.replace(".", "/"));

		// create lib package;
		final File libDir = new File(properties.getProperty("script.lib.dir.name"),"lib");
		//final File libDir = new File();
		libDir.mkdirs();
		logger.debug("scripts lib dir:"+libDir.getAbsolutePath());
		//final int libClassCount = libDir.list().length;
		final String libPackage = generatePackageName();
		//		if (libClassCount > 0) {
		//			libPackage += ".lib";
		//		}
		javaSource = javaSource.replace(LIB_PACKAGE_NAME_PH, libPackage + ".*");


		// there is any library function written any file in lib folder, then integrate those
		// methods into this java file.
		final StringBuffer libMethods = new StringBuffer();
		if(libDir.exists()) {
			final File[] files = libDir.listFiles();
			for(final File file : files) {
				if(file.isFile()) {
					logger.info("Merging methods written in :"+file.getAbsolutePath());
					final String fileContent = FileUtils.readFileToString(file);
					libMethods.append("\r\n//Methods from :"+file.getName()).append("\r\n")
					.append(fileContent).append("\r\n");
				}
			}
		}

		javaSource = javaSource.replace(LIB_METHODS_PH, libMethods.toString());


		final String className = getClassName(testScriptFile.getName());

		javaSource = javaSource.replaceAll(CLASS_NAME_PH, className);

		javaSource = javaSource.replace(SCRIPT_CODE_PH, script).replace(
				PACKAGE_NAME_PH, generatePackageName());
		final File sourceFile = new File(root, className + ".java");
		if (sourceFile.exists()) {
			sourceFile.delete();
		}
		sourceFile.getParentFile().mkdirs();
		FileUtils.write(sourceFile, javaSource);
		return sourceFile;
	}

	public static String getClassName(final String string) {
		final String className = string.substring(0, string.indexOf("."));
		return className.replaceAll("[^\\p{L}\\p{N}]", "").replaceFirst(
				"^[0-9]+(?!$)", "");
	}

	public static Script compileJavaSourceFile(final File sourceFile)
			throws MalformedURLException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		// Compile source file.
		final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler == null) {
			logger.error("To compile a java class, JDK is required, probably you are using JRE. the java.home is:"
					+ System.getProperty("java.home"));
			throw new RuntimeException(
					"To compile a java class, JDK is required, probably you are using JRE");
		}

		final String compilationResultLogFileName = properties
				.getProperty("logs.location");
		final File compLogs = new File(compilationResultLogFileName+"/"+"CompilationResult.txt");
		compLogs.getParentFile().mkdirs();
		if(compLogs.exists()) {
			logger.info("removing previous compilation log file:"+compLogs.getAbsolutePath());
			compLogs.delete();
		}

		OutputStream out = null;
		try {
			out = new FileOutputStream(compLogs);
		} catch (final FileNotFoundException e) {
			logger.error("could not read file:" + compLogs.getAbsolutePath()+", redirecting output to console.", e);
			out = System.out;
		}
		final int compilationResult = compiler.run(null, out, out,
				sourceFile.getPath());

		if (compilationResult != 0) {
			throw new RuntimeException("Could not compile the class:"
					+ sourceFile.getPath() + ", compilation result saved at:"
					+ compLogs.getAbsolutePath());
		}
		// Load and instantiate compiled class.
		final URLClassLoader classLoader = URLClassLoader
				.newInstance(new URL[] { new File(properties
						.getProperty("script.java.source.location"))
				.toURI()
				.toURL() });

		final String className = getClassName(sourceFile.getName());
		final Class<?> cls = Class.forName(generatePackageName() + "."
				+ className, true, classLoader);
		final Script instance = (Script) cls.newInstance();
		return instance;
	}


	public static Script getClassObject(final File file) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		final URLClassLoader classLoader = URLClassLoader
				.newInstance(new URL[] { new File(properties
						.getProperty("script.java.source.location"))
				.toURI()
				.toURL() });
		final Class<?> cls = Class.forName(generatePackageName() + "."
				+ getClassName(file.getName()), true, classLoader);
		final Script instance = (Script)cls.newInstance();
		return instance;
	}

	public static void main(final String[] args) {
		//System.out.println(generatePackageName(url));
		try{
		final File file = generateJavaFileObj("Localizestorenavigationflow.txt","class");
		System.out.println(file.getAbsolutePath());
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
