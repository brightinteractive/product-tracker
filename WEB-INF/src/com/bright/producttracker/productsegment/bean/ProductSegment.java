package com.bright.producttracker.productsegment.bean;

import com.bright.framework.database.bean.DataBean;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * ProductSegment.java
 *
 * Contains the ProductSegment class.
 */

/*
 Ver	Date				Who					Comments
 --------------------------------------------------------------------------------
 d1		23-Feb-2010		Kevin Bennett		Created
 --------------------------------------------------------------------------------
 */

/**
 * Product segment bean
 * 
 * @author Bright Interactive
 * @version d1
 */
public class ProductSegment extends DataBean implements Comparable<ProductSegment>
{
	public static ProductSegment UNKNOWN_SEGMENT = new ProductSegment("UNKNOWN");
	
	public ProductSegment()
	{
		
	}
	
	public ProductSegment(String a_sName)
	{
		m_sName = a_sName; 
	}
	
	private String m_sName = null;
	private String m_sIcon = null;
	
	public String getName()
	{
		return m_sName;
	}
	public void setName(String a_sName)
	{
		m_sName = a_sName;
	}
	public String getIcon()
	{
		return m_sIcon;
	}
	public void setIcon(String a_Icon)
	{
		m_sIcon = a_Icon;
	}

	public int compareTo(ProductSegment a_productSegment)
	{
		return this.getName().toLowerCase().compareTo(a_productSegment.getName().toLowerCase());
	}
}
