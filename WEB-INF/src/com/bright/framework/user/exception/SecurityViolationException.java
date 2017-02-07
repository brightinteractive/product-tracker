package com.bright.framework.user.exception;

/* Bright Interactive - user exceptions
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 *
 * SecurityViolationException.java
 */
/*
Ver	Date		Who				Comments
--------------------------------------------------------------------------------
d1	10-Jun-2004	Martin Wilson	Created.
--------------------------------------------------------------------------------
*/

import com.bn2web.common.exception.Bn2Exception;

/**
 * Exception to indicate that a user/company's account has been suspended.
 */
public class SecurityViolationException extends Bn2Exception
{
    public SecurityViolationException( String a_sMessage )
    {
    	super( a_sMessage );
    }
}
