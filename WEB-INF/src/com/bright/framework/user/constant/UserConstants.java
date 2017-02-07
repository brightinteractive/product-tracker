package com.bright.framework.user.constant;

/**
 * Bright Interactive, Reputable Trades
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * UserConstants.java
 *
 * Contains the constants for UserManager and other user classes.
 */
/*
Ver  Date				Who					Comments
--------------------------------------------------------------------------------
d1   25-May-2004		Martin Wilson     Created for framework.
d2   29-May-2004		Chris Preager     Removed k_sPasswordNotToEncrypt (use setting instead)
--------------------------------------------------------------------------------
*/

/**
 * Contains global constants.
 * To use, implement this interface in your class.
 *
 * @author  Bright Interactive
 * @version d1
 */
public interface UserConstants
{
    public static final String SECURITY_VIOLATION_KEY = "SecurityViolation";

    public static final long k_lUserNotLoggedInId = 0;

    // Encryption:
    // the encryption password must be numbers (or within ASCII 32 - 63)
    // so the resulting expression does not go in to extended UniCode characters.
    public static final String k_sEncryptionPassword = "534836598276491234623965329837659823";

    public static final String k_sSubmitParamName = "submit";
    public static final String k_sCancelParam = "Cancel";
    public static final String CANCEL_KEY = "Cancel";
    public static final String k_sPostLoginRedirectParam = "redirecturl";

    public static final String k_sAdminViewingCompanyIdParam = "adminViewingCompanyId";
    
    public static final int k_iNoOfLoginCodes = 1000;
	public static final int k_iLoginCodeLength = 10;

}