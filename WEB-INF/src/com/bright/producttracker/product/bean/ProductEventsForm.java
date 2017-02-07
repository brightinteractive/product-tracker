package com.bright.producttracker.product.bean;

import java.util.Vector;

import com.bn2web.common.form.Bn2Form;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * ProductEventsForm.java
 *
 * Contains the ProductEventsForm class.
 */

/*
 Ver	Date				Who				Comments
 --------------------------------------------------------------------------------
 d1	02-Feb-2005	Martin Wilson		Created
 --------------------------------------------------------------------------------
 */

/**
 * Holds data for managing products at events
 * 
 * @author Bright Interactive
 * @version d1
 */
public class ProductEventsForm extends Bn2Form
{
	private Vector m_vecEvents = null;
	private Product m_product = null;
	private long m_lNewEventId = 0;
	
	/** @return Returns the newEventId. */
	public long getNewEventId()
	{
		return m_lNewEventId;
	}
	/** @param a_sNewEventId The newEventId to set. */
	public void setNewEventId(long a_lNewEventId)
	{
		m_lNewEventId = a_lNewEventId;
	}
	
	/** @return Returns the product. */
	public Product getProduct()
	{
		return m_product;
	}
	/** @param a_sProduct The product to set. */
	public void setProduct(Product a_product)
	{
		m_product = a_product;
	}
	/** @return Returns the vecEvents. */
	public Vector getEvents()
	{
		return m_vecEvents;
	}
	/** @param a_sVecEvents The vecEvents to set. */
	public void setEvents(Vector a_vecEvents)
	{
		m_vecEvents = a_vecEvents;
	}
}
