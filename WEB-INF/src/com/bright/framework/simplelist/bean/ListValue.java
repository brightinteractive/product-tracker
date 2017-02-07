package com.bright.framework.simplelist.bean;

import com.bright.framework.database.bean.DataBean;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * ListValue.java
 *
 * Contains the ListValue class.
 */

/*
 Ver	Date				Who				Comments
 --------------------------------------------------------------------------------
 d1	29-Jan-2005	Martin Wilson		Created
 --------------------------------------------------------------------------------
 */

/**
 * Represents a value in a list
 * 
 * @author Bright Interactive
 * @version d1
 */
public class ListValue extends DataBean
{
	private String m_sValue = null;
	
	
	/** @return Returns the value. */
	public String getValue()
	{
		return m_sValue;
	}
	/** @param a_sValue The value to set. */
	public void setValue(String a_sValue)
	{
		m_sValue = a_sValue;
	}
}
