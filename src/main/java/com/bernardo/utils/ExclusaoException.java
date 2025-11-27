package com.bernardo.utils;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class ExclusaoException extends RuntimeException {
	public ExclusaoException(String msg, Throwable cause) {
		super(msg, cause);
	}
}