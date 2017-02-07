package com.bright.framework.common.bean;

/**
 * Bright Interactive, Country
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * StringBean.java
 *
 * Contains the StringBean class.
 */

/*
 Ver     Date           Who            Comments
 --------------------------------------------------------------------------------
 d1      23-Jun-2004    James Home     Created
 --------------------------------------------------------------------------------
*/

/**
 * A string, wrapped as a bean
 *
 * @author  Bright Interactive
 * @version d1
 */
public class StringBean
{	
	/**
	 * Holds value of property Value.
	 */
	private String m_sValue;

	public StringBean(String a_sValue)
	{
		m_sValue = a_sValue;
	}
	
	/**
	 * Getter for property Value.
	 * @return Value of property Value.
	 */
	public String getValue()
	{
		return m_sValue;
	}
	
	/**
	 * Setter for property Value.
	 * @param string New value of property Value.
	 */
	public void setValue(String a_sValue)
	{
		m_sValue = a_sValue;
	}
	
}