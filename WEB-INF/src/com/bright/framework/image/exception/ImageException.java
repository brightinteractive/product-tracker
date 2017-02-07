package com.bright.framework.image.exception;

import com.bn2web.common.exception.Bn2Exception;

/**
 * Bright Interactive, Merchandise Manager
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * ImageException.java
 *
 * Contains the ImageException class.
 */

/*
 Ver	Date				Who				Comments
 --------------------------------------------------------------------------------
 d1	15-Jun-2005	James Home		Created
 d2 22-May-2007	Kevin Bennett    Imported from image bank
 --------------------------------------------------------------------------------
 */

/**
 * An exception that is thrown during image operations
 * 
 * @author Bright Interactive
 * @version d1
 */
public class ImageException extends Bn2Exception
{
	public ImageException(String a_sMessage)	
	{
		super(a_sMessage);
	}
	
	public ImageException(String a_sMessage, Exception a_cause)	
	{
		super(a_sMessage,a_cause);
	}
}
