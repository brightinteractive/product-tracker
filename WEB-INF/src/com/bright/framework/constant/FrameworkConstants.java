package com.bright.framework.constant;

/**
 * Bn2web common classes
 *
 * Copyright 2003 Bright Interactive, All Rights Reserved.
 * FrameworkConstants.java
 *
 * Contains the FrameworkConstants Interface.
 */
/*
Ver  Date	    Who			Comments
--------------------------------------------------------------------------------
d1   10-Jun-2004    Chris Preager          Created
--------------------------------------------------------------------------------
*/

/**
 * Contains constants that are global to the framework.
 * To use, implement this interface in your class.
 *
 * @author  Bright Interactive
 * @version d1
 */
public interface FrameworkConstants
{



/* * * * * * * * * * * * * * * * * * * * *
 * Generic constants for EmailManager..  *
 * * * * * * * * * * * * * * * * * * * * */

   public static final String k_sTOEMAILParam = "TO_EMAIL";
   public static final String k_sREDIRECTSUCCESSParam = "REDIRECT_SUCCESS";
   public static final String k_sREDIRECTFAILUREParam = "REDIRECT_FAILURE";

   public static final String k_sTemplateFileSuffix = ".xml";

	public static final String k_sTemplateParam = "template";

   // Note: these strings must not contain RegExp characters
   public static final String k_sTemplateVariableStart = "#";
   public static final String k_sTemplateVariableEnd = "#";
	
    public static final String k_sCancelParam = "Cancel";    
    public static final String CANCEL_KEY = "Cancel"; 	
    public static final long k_lOneDayInMillis = 1000*60*60*24;	
	
    public static final String k_sFEEDBACKSUCCESSParam = "REDIRECT_SUCCESS";
    public static final String k_sFEEDBACKFAILUREParam = "REDIRECT_FAILURE";
    
    public static final String k_sEmailTemplateFileSuffix = ".xml";
    
    public static final String k_sMandatoryFieldPrefix = "mandatory_";
    public static final int k_sMaxEmailParamLength = 50000;
	
    
 /* * * * * * * * * * * * * * * * * * * * */
    
    
    public static final String DOWNLOAD_KEY = "Download";
    public static final String k_sAttributeName_DownloadFile = "downloadFile";
	public static final String k_sAttributeName_DownloadFilename = "downloadFilename"; 
	public static final String k_sAttributeName_DeleteFileAfterUse = "deleteFileAfterUse";
	public static final String k_sAttributeName_DownloadFailureUrl = "downloadFaliureUrl";
	public static final String k_sAttributeName_CompressFile = "compressFile";
    

}