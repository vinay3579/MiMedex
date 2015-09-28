/**
 * 
 */
package com.pyramidconsulting.reader;

/**
 * @author manojt
 *
 */
public class DataFileNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	public DataFileNotFoundException() {
	}

	/**
	 * @param arg0
	 */
	public DataFileNotFoundException( String arg0) {

		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public DataFileNotFoundException( Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public DataFileNotFoundException( String arg0,  Throwable arg1) {
		super(arg0, arg1);
	}
}
