package com.bright.framework.user.exception;

/* Bright Interactive - user deletion excpetions
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 *
 * CannotDeleteException.java
 */
/*
Ver	Date		Who				Comments
--------------------------------------------------------------------------------
d1	28-Jun-2004	Chris Preager	Created.
--------------------------------------------------------------------------------
*/

import com.bn2web.common.exception.Bn2Exception;

/**
 * Exception to indicate that a user cannot be deleted
 */
public class CannotDeleteException extends Bn2Exception
{
    public CannotDeleteException(String a_sMessage)
    {
    	super( a_sMessage );
    }
}
