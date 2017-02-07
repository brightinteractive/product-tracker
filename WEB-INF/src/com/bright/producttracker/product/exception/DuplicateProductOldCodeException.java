package com.bright.producttracker.product.exception;

import com.bn2web.common.exception.Bn2Exception;

/**
 * Bright Interactive, GKN Product Tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * DuplicateProductOldCodeException.java
 *
 * Contains the DuplicateProductOldCodeException class.
 */

/*
 Ver	Date				Who					Comments
 --------------------------------------------------------------------------------
 d1	14-Jun-2005		Matt Stevenson		Created
 --------------------------------------------------------------------------------
 */

/**
 * Exception detailing that a duplicate product old code was found when trying to save
 * 
 * @author Bright Interactive
 * @version d1
 */
public class DuplicateProductOldCodeException extends Bn2Exception
{
	public DuplicateProductOldCodeException (String a_sMessage)
	{
		super(a_sMessage);
	}
}
