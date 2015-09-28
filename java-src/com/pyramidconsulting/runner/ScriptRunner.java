package com.pyramidconsulting.runner;

import java.io.File;


public interface ScriptRunner {
	public void runScript(final String scriptFileName) throws Exception;
	public void runScript(final File scriptFile) throws Exception;
}
