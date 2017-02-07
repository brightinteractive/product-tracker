package com.bright.producttracker.product.constant;

/**
 * Bright Interactive, ApplicationSettings
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 *
 * Contains the Settings for the product package
 */
/*
 Ver  Date					Who                Comments
 --------------------------------------------------------------------------------
 d1   04-Feb-2005     	Martin Wilson      Created
 --------------------------------------------------------------------------------
 */

import com.bn2web.common.constant.GlobalSettings;

/**
 * Product settings.
 *
 * @author  bright interactive
 * @version d1
 */
public class ProductSettings extends GlobalSettings
{
	public static int getMaxImageWidth()
	{
		return (getInstance().getIntSetting("max-image-width"));
	}

	public static int getMaxImageHeight()
	{
		return (getInstance().getIntSetting("max-image-height"));
	}	
	
	public static int getCoordinatorMovementNotificationPeriodInDays()
	{
		return (getInstance().getIntSetting("coordinatorMovementNotificationPeriodInDays"));
	}
			
	public static int getHourOfDayToSendMovementAdminNotificationEmails()
	{
		return (getInstance().getIntSetting("hourOfDayToSendMovementAdminNotificationEmails"));
	}
	
	public static int getHourOfDayToSendMovementReminderNotificationEmails()
	{
		return (getInstance().getIntSetting("hourOfDayToSendMovementReminderNotificationEmails"));
	}
	
	public static int getHourOfDayToSendMovementCoordinatorNotificationEmails()
	{
		return (getInstance().getIntSetting("hourOfDayToSendMovementCoordinatorNotificationEmails"));
	}
	
	public static int getAdminMovementNotificationPeriodInDays ()
	{
		return (getInstance().getIntSetting("adminMovementNotificationPeriodInDays"));
	}
	
	public static int getMovementNotificationReminderDaysBefore ()
	{
		return (getInstance().getIntSetting("movementNotificationReminderDaysBefore"));
	}
}