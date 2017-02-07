package com.bright.producttracker.event.form;

import java.util.Vector;

import com.bn2web.common.form.Bn2Form;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * ManageEventsForm.java
 *
 * Contains the ManageEventsForm class.
 */

/*
 Ver	Date				Who				Comments
 --------------------------------------------------------------------------------
 d1	20-Jan-2005	Martin Wilson		Created
 --------------------------------------------------------------------------------
 */

/**
 * Holds objects for the manage events page 
 * 
 * @author Bright Interactive
 * @version d1
 */
public class ManageEventsForm extends Bn2Form
{
	private Vector m_vecEvents = null;
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
