package com.bright.producttracker.product.form;


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
public class ProductEventsForm extends ProductForm
{

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
	
}
