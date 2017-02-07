package com.bright.framework.constant;

/**
 * Bright Interactive, Framework
 *
 * Copyright 2003 Bright Interactive, All Rights Reserved.
 *
 * Contains the Settings for the framework.
 */

/*
Ver  Date	    Who			Comments
--------------------------------------------------------------------------------
     02-Oct-2003    James Home         Created
     02-Apr-2004    Matt Stevenson     Added getFileStoreWebappLocation
--------------------------------------------------------------------------------
d1   10-Apr-2004    Chris Preager		Created with email settings
d2   29-Jun-2004    Chris Preager      Added default password
d3   07-Jan-2005    Martin Wilson		Removed use of 'use relative directories' as of course we want to.
d4	 26-Jul-2010	Kevin Bennett		Added smtpEmailPort
--------------------------------------------------------------------------------
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.bn2web.common.constant.GlobalSettings;

/**
 * Settings that are available to any class.
 *
 * @author  Bright Interactive
 * @version d1
 */
public class FrameworkSettings extends GlobalSettings implements FrameworkConstants
{
   // Add static methods here to access each of the properties in the properties file:
	private static DateFormat k_dtDateFormat = null;
	private static Object k_oDateLock = new Object();
	
	public static String getStandardDateFormatStr()
	{
      return (getInstance().getStringSetting("date-format"));
	}
	
	public static DateFormat getStandardDateFormat()
	{
		if (k_dtDateFormat == null)
		{
			synchronized (k_oDateLock)
			{
				k_dtDateFormat = new SimpleDateFormat(getStandardDateFormatStr());
			}
		}
		
      return (k_dtDateFormat);
	}	

	public static String getDefaultPassword()
	{
		return getInstance().getStringSetting("default-password");
	}

   public static String getCmsFileStoreRoot()
   {
      return (getInstance().getStringSetting("cmsFilestoreRoot"));
   }

   public static String getEmailAddress()
   {
      return getInstance().getStringSetting("emailAddress");
   }

   public static String getEmailSMTP()
   {
      return getInstance().getStringSetting("emailSMTP");
   }
   
   public static String getEmailSMTPPort()
   {
      return getInstance().getStringSetting("emailSMTPPort");
   }

   public static String getEmailTemplateDirectory()
   {
      return getInstance().getStringSetting("cmsEmailTemplateDir");
   }

   public static String getAlertTemplateDirectory()
   {
      return getInstance().getStringSetting("cmsAlertTemplateDir");
   }
   
   public static String getSMSMessageTemplateDirectory()
   {
      return getInstance().getStringSetting("cmsSMSMessageTemplateDir");
   }

   public static String getSampleMessageTemplate()
   {
      return getInstance().getStringSetting("sampleMessageTemplate");
   }

   public static String getMatchImageFileRegularExpression()
   {
       return getInstance().getStringSetting("matchImageFileRegularExpression");
   }

	public static String getFullyQualifiedWebServerAppPath()
   {
       return getInstance().getStringSetting("fullyQualifiedWebServerAppPath");
   }
	
	public static boolean getUseRelativeDirectories()
	{
		return Boolean.parseBoolean(getInstance().getStringSetting("useRelativeDirectories"));
	}
	
	public static String getApplicationUrl()
	{
		return (getInstance().getStringSetting("application-url"));
	}
	
	public static String getFileStoreRoot()
   {
      return (getInstance().getStringSetting("filestoreRoot"));
   }
	
	public static String getImageMagickPath()
   {
      return (getInstance().getStringSetting("image-magick-path"));
   }
   
   	public static String getFilestoreRootFullPath()
   {
		if (getUseRelativeDirectories())
		{
			return getApplicationPath() + "/" + getFileStoreRoot();
		}
		return getFileStoreRoot();
	}
	
 /* * * * * * * * * * * * * * * * * * * * */

}