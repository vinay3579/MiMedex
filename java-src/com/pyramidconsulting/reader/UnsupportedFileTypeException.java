package com.pyramidconsulting.reader;

public class UnsupportedFileTypeException extends RuntimeException {

	/**
	 * 
	 */
	private static  long serialVersionUID = 1L;

	public UnsupportedFileTypeException() {
		super();
	}

	public UnsupportedFileTypeException( String arg0,  Throwable arg1) {
		super(arg0, arg1);
	}

	public UnsupportedFileTypeException( String arg0) {
		super(arg0);
	}

	public UnsupportedFileTypeException( Throwable arg0) {
		super(arg0);
	}

}
