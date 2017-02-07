/**
 * Bright Interactive, DataBean
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * DataBean.java
 * Contains the DataBean class.
 */

/*
 Ver     Date            Who						Comments
 --------------------------------------------------------------------------------
 d1      28-May-2004     Martin Wilson			Created
 --------------------------------------------------------------------------------
 */

package com.bright.framework.database.bean;

/**
 * Extend from this to create objects that will be stored in a database. Saves implementing
 * the id property, and enables use of util classes.
 *
 * @author  Martin Wilson
 */
public class DataBean
{
	private long m_lId = 0;
	
	public void setId(long a_lId)
	{
		m_lId = a_lId;
	}

	public long getId()
	{
		return (m_lId);
	}
	
	
}
