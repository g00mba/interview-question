package com.backbase.interview.persistence;

public class RecordNotFoundException extends Exception {
    /**
	 * returns generic exception for records not found.
	 */
	private static final long serialVersionUID = 1L;

	public RecordNotFoundException(String msg) {
        super(msg);
    }
}
