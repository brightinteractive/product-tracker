package com.bright.producttracker.product.form;

import java.util.Vector;

import com.bn2web.common.form.Bn2Form;
import com.bright.producttracker.product.bean.ProductMovement;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * ProductMovementForm
 *
 * Contains the ProductMovementForm class.
 */

/*
 Ver	Date				Who				Comments
 --------------------------------------------------------------------------------
 d1	18-Mar-2005		Matt Stevenson	Created
 d2	21-Mar-2005		Matt Stevenson	Modified to validate country
 d3	18-Apr-2005		Matt Stevenson	Added locationType, removed date validation
 d4	26-Oct-2007		Matt Stevenson	Modified validation
 --------------------------------------------------------------------------------
 */

/**
 * ProductMovementForm
 * 
 * @author Bright Interactive
 * @version d1
 */
public class ProductMovementForm extends Bn2Form
{
	private ProductMovement m_productMovement = new ProductMovement();
	private Vector m_vecEvents = null;
	private String m_sLocationType = null;
	
	/** @return Returns the vecEvents. */
	public Vector getEvents()
	{
		return m_vecEvents;
	}
	/** @param a_vecEvents The vecEvents to set. */
	public void setEvents(Vector a_vecEvents)
	{
		m_vecEvents = a_vecEvents;
	}

	/** @return Returns the productMovement. */
	public ProductMovement getProductMovement()
	{
		return m_productMovement;
	}
	
	/** @param a_productMovment The productMovment to set. */
	public void setProductMovement(ProductMovement a_productMovement)
	{
		m_productMovement = a_productMovement;
	}
	
	/**
	 * method to validate the contents of the form
	 */
	public void validate()
	{
		if (this.getProductMovement().getEvent().getId() > 0 &&
			this.getProductMovement().getLocationId() > 0)
		{
			this.addError("You need to select either an event or a location, not both");
		}
		
		if (this.getProductMovement().getEvent().getId() <= 0 && 
			this.getProductMovement().getLocationId() <= 0)
		{
			this.addError("You need to either select an event or location");
		}
		
		if (this.getProductMovement().getLocationId() > 0 &&
			this.getProductMovement().getCountry().getId() <= 0)
		{
			this.addError("When selecting a location you need to select the country that the product was at");
		}
	}
	
	/** @return Returns the locationType. */
	public String getLocationType()
	{
		return m_sLocationType;
	}
	
	/** @param a_sLocationType The locationType to set. */
	public void setLocationType(String a_sLocationType)
	{
		m_sLocationType = a_sLocationType;
	}
}
