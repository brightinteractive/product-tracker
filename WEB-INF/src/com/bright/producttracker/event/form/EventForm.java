package com.bright.producttracker.event.form;

import java.util.List;
import java.util.Vector;

import com.bn2web.common.form.Bn2Form;
import com.bright.producttracker.event.bean.Event;
import com.bright.producttracker.product.bean.Product;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * EventForm.java
 *
 * Contains the EventForm class.
 */

/*
 Ver	Date				Who				Comments
 --------------------------------------------------------------------------------
 d1	20-Jan-2005	Martin Wilson		Created
 --------------------------------------------------------------------------------
 */

/**
 * Holds the event object
 * 
 * @author Bright Interactive
 * @version d1
 */
public class EventForm extends Bn2Form
{
	private Event m_event = null;
	private List<Product> m_vecProducts = null;
	
	/** @return Returns the event. */
	public Event getEvent()
	{
		if (m_event == null)
		{
			m_event = new Event();
		}
		
		return m_event;
	}
	/** @param a_sEvent The event to set. */
	public void setEvent(Event a_event)
	{
		m_event = a_event;
	}
	/** @return Returns the vecProducts. */
	public List<Product> getProducts()
	{
		return m_vecProducts;
	}
	/** @param a_sVecProducts The vecProducts to set. */
	public void setProducts(List<Product> a_vecProducts)
	{
		m_vecProducts = a_vecProducts;
	}
}
