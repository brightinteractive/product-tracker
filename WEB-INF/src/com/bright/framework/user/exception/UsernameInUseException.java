package com.bright.framework.user.exception;

/* Bright Interactive - user exceptions
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 *
 * GuestLoginsUsedException.java
 */
/*
Ver	Date		Who				Comments
--------------------------------------------------------------------------------
d1	16-May-2004	Martin Wilson	Created.
--------------------------------------------------------------------------------
*/

import com.bn2web.common.exception.Bn2Exception;

/**
 * Exception to indicate that a user/company's account has been suspended.
 */
public class UsernameInUseException extends Bn2Exception
{
    public UsernameInUseException(String a_sMessage)
    {
    	super( a_sMessage );
    }
}
