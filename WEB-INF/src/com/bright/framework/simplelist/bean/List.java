package com.bright.framework.simplelist.bean;


/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * List.java
 *
 * Contains the List class.
 */

/*
 Ver	Date				Who				Comments
 --------------------------------------------------------------------------------
 d1	29-Jan-2005	Martin Wilson		Created
 --------------------------------------------------------------------------------
 */

/**
 * Represents a simple list
 * 
 * @author Bright Interactive
 * @version d1
 */
public class List
{
	private String m_sId = null;
	private String m_sDescription = null;
	
	/** @return Returns the description. */
	public String getDescription()
	{
		return m_sDescription;
	}
	/** @param a_sDescription The description to set. */
	public void setDescription(String a_sDescription)
	{
		m_sDescription = a_sDescription;
	}
	/** @return Returns the id. */
	public String getId()
	{
		return m_sId;
	}
	/** @param a_sId the id to set. */
	public void setId(String a_sId)
	{
		m_sId = a_sId;
	}

}
