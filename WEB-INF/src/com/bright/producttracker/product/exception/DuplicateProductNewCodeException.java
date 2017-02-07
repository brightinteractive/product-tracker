package com.bright.producttracker.product.exception;

import com.bn2web.common.exception.Bn2Exception;

/**
 * Bright Interactive, GKN Product Tracker
 *
 * Copyright 2010 Bright Interactive, All Rights Reserved.
 * DuplicateProductNewCodeException.java
 *
 * Contains the DuplicateProductNewCodeException class.
 */

/*
 Ver	Date				Who					Comments
 --------------------------------------------------------------------------------
 d1	22-Jul-2010		Kevin Bennett		Created
 --------------------------------------------------------------------------------
 */

/**
 * Exception detailing that a duplicate product new code was found when trying to save
 * 
 * @author Bright Interactive
 * @version d1
 */
public class DuplicateProductNewCodeException extends Bn2Exception
{
	public DuplicateProductNewCodeException (String a_sMessage)
	{
		super(a_sMessage);
	}
}
