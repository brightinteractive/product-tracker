package com.bright.framework.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.Predicate;

/**
 * Bright Interactive, Merchandise Manager
 *
 * Copyright 2006 Bright Interactive, All Rights Reserved.
 * CommandLineExec.java
 *
 * Useful operations on Collections.
 */

/*
 Ver		Date							Who							Comments
 --------------------------------------------------------------------------------
 d1		17-Oct-2006					Adam Bones					Created
 d2		26-Jan-2007					James Home					Added getFirstMatch
 d3 	22-May-2007					Kevin Bennett				Imported from image bank
 --------------------------------------------------------------------------------
 */

/**
 * @author Bright Interactive
 * @version d1
 */

public class CollectionUtil
{
	/**
	 * Fold up a collection into a string.
	 */
	public static String join(Collection a_collection, String a_sDelimiter)
	/*
	---------------------------------------------------------------
	d1		17-Oct-2006		Adam Bones		Created 
	-----------------------------------------------------------------
	*/	
	{
		StringBuffer buffer = new StringBuffer();
		Iterator it = a_collection.iterator();
		while (it.hasNext())
		{
			buffer.append(it.next());
			if (it.hasNext())
			{
				buffer.append(a_sDelimiter);
			}
		}
		return buffer.toString();
	}
	
	/**
	 * Returns the 
	 * 
	 * @param a_list
	 * @param a_predicate
	 * @return
	 */
	public static Object getFirstMatch(List a_list, Predicate a_predicate)
	/*
	---------------------------------------------------------------
	d2		26 Jan 2007		James Home		Created 
	-----------------------------------------------------------------
	 */
	{
		if(a_list!=null && a_predicate!=null)
		{
			Iterator it = a_list.iterator();
			while(it.hasNext())
			{
				Object obj = it.next();
				if(a_predicate.evaluate(obj))
				{
					return obj;
				}
			}
		}	
		return null;
	}
}
