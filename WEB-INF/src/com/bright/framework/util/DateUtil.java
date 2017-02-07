package com.bright.framework.util;

/**
 * Bright Interactive image manager classes
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * StringUtil.java
 *
 * Contains the StringUtil class.
 */
/*
	 Ver     Date            Who             Comments
	 --------------------------------------------------------------------------------
	 d1      30-Oct-2007	Matt Stevenson		Created
	 --------------------------------------------------------------------------------
 */

import java.util.Date;
import java.util.GregorianCalendar;


/**
 * Static methods for common date operations.
 *
 * @author  Bright Interactive
 * @version d1
 */
public class DateUtil
{
	public static boolean datesAreEqual (Date a_dtOne, Date a_dtTwo)
	/*
	---------------------------------------------------------------
	d1      30-Oct-2007	Matt Stevenson		Created
	-----------------------------------------------------------------
	*/
	{
		GregorianCalendar one = new GregorianCalendar();
		one.setTimeInMillis(a_dtOne.getTime());
		one.getTimeInMillis();
		
		GregorianCalendar two = new GregorianCalendar();
		two.setTimeInMillis(a_dtTwo.getTime());
		two.getTimeInMillis();
			
		//check the fields...
		if (one.get(GregorianCalendar.DAY_OF_MONTH) != two.get(GregorianCalendar.DAY_OF_MONTH))
		{
			return (false);
		}
		
		if (one.get(GregorianCalendar.MONTH) != two.get(GregorianCalendar.MONTH))
		{
			return (false);
		}
		
		if (one.get(GregorianCalendar.YEAR) != two.get(GregorianCalendar.YEAR))
		{
			return (false);
		}
		
		return (true);
	}
	
	
	
	
}
