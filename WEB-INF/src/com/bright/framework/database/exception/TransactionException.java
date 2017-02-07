package com.bright.framework.database.exception;

/**
 * Bright Interactive, Category
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * TransactionException.java
 *
 * Contains the TransactionException class.
 */
/*
Ver  Date			Who						Comments
--------------------------------------------------------------------------------
d1   15-Jun-2004	James Home				Created
d2   15-Jun-2004	Martin Wilson			Added constructors.
--------------------------------------------------------------------------------
 */

import com.bn2web.common.exception.Bn2Exception;

/**
 *
 * @author  James Home
 */
public class TransactionException extends Bn2Exception
{
	/**
	 * @param a_sMessage the exception message
	 */
	public TransactionException ( String a_sMessage)
	 /*
	 Ver	Date		Who	   Comments
	 ----------------------------------------------------------------------------
	 d2	15-Jun-2004			Martin Wilson		Created.
	 ----------------------------------------------------------------------------
	  */
	{
		super ( a_sMessage );
	}

	/**
	 * @param a_sMessage the exception message
	 *	@param a_thr the cause of the exception
	 */
	public TransactionException ( String a_sMessage, Throwable a_thrCause )
	 /*
	 Ver	Date		Who	   Comments
	 ----------------------------------------------------------------------------
	 d2	15-Jun-2004			Martin Wilson		Created.
	 ----------------------------------------------------------------------------
	  */
	{
		super ( a_sMessage, a_thrCause );
	}
}
