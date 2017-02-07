package com.bright.producttracker.application.constant;

/**
 * Bright Interactive, ApplicationSettings
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 *
 * Contains the Settings for the application
 */
/*
 Ver  Date				Who                Comments
 --------------------------------------------------------------------------------
 d1   04-Oct-2004     	Chris Preager      Created
 --------------------------------------------------------------------------------
 */

import com.bn2web.common.constant.GlobalSettings;

/**
 * Application settings that are available to any class.
 *
 * @author  bright interactive
 * @version d1
 */
public class ApplicationSettings extends GlobalSettings
{
	public static int getEmailsHour ()
	{
		return (getInstance().getIntSetting("emails-hour"));
	}

}