package com.bright.producttracker.product.bean;

import java.util.Date;

import com.bright.framework.common.bean.Country;
import com.bright.framework.database.bean.DataBean;
import com.bright.producttracker.event.bean.Event;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * ProductMovement.java
 *
 * Contains the ProductMovement class.
 */

/*
 Ver	Date				Who				Comments
 --------------------------------------------------------------------------------
 d1	18-Mar-2005		Matt Stevenson	Created
 d2	21-Mar-2005		Matt Stevenson	Added country
 d3	18-Apr-2005		Matt Stevenson	Added isCurrentProductMovement
 d4	26-Oct-2007		Matt Stevenson	Added LocationId and removed Description
 d5	30-Oct-2007		Matt Stevenson	Added reminder email address
 --------------------------------------------------------------------------------
 */

/**
 * Represents a ProductMovement
 * 
 * @author Bright Interactive
 * @version d1
 */
public class ProductMovement extends DataBean
{
	private Event m_event = new Event();
	private Date m_dtArrivalDate = null;
	private Date m_dtDepartureDate = null;
	private String m_sArrivalDateString = null;
	private String m_sDepartureDateString = null;
	private long m_lProductId = 0;
	private boolean m_bMovedOn = false;
	private Country m_country = new Country();
	private boolean m_bIsCurrentMovement = false;
	private long m_lLocationId = 0;
	private String m_sLocationValue = null;
	private String m_sReminderEmail = null;
	
	
	/** @return Returns the movedOn. */
	public boolean getMovedOn()
	{
		return m_bMovedOn;
	}
	/** @param a_bMovedOn The movedOn to set. */
	public void setMovedOn(boolean a_bMovedOn)
	{
		m_bMovedOn = a_bMovedOn;
	}
	/** @return Returns the arrivalDateString. */
	public String getArrivalDateString()
	{
		return m_sArrivalDateString;
	}
	/** @param a_sArrivalDateString The arrivalDateString to set. */
	public void setArrivalDateString(String a_sArrivalDateString)
	{
		m_sArrivalDateString = a_sArrivalDateString;
	}
	/** @return Returns the departureDateString. */
	public String getDepartureDateString()
	{
		return m_sDepartureDateString;
	}
	/** @param a_sDepartureDateString The departureDateString to set. */
	public void setDepartureDateString(String a_sDepartureDateString)
	{
		m_sDepartureDateString = a_sDepartureDateString;
	}
	/** @return Returns the dtArrivalDate. */
	public Date getArrivalDate()
	{
		return m_dtArrivalDate;
	}
	
	/** @return Returns the productId. */
	public long getProductId()
	{
		return m_lProductId;
	}
	
	/** @param a_sProductId The productId to set. */
	public void setProductId(long a_lProductId)
	{
		m_lProductId = a_lProductId;
	}
	
	/** @param a_sDtArrivalDate The dtArrivalDate to set. */
	public void setArrivalDate(Date a_dtArrivalDate)
	{
		m_dtArrivalDate = a_dtArrivalDate;
	}
	
	/** @return Returns the dtDepartureDate. */
	public Date getDepartureDate()
	{
		return m_dtDepartureDate;
	}
	
	/** @param a_sDtDepartureDate The dtDepartureDate to set. */
	public void setDepartureDate(Date a_dtDepartureDate)
	{
		m_dtDepartureDate = a_dtDepartureDate;
	}
	
	/** @return Returns the event. */
	public Event getEvent()
	{
		return m_event;
	}
	
	/** @param a_sEvent The event to set. */
	public void setEvent(Event a_sEvent)
	{
		m_event = a_sEvent;
	}
	
	/** @return Returns the country. */
	public Country getCountry()
	{
		return m_country;
	}
	
	/** @param a_country The country to set. */
	public void setCountry(Country a_country)
	{
		m_country = a_country;
	}
	
	/** @return Returns the isCurrentMovement. */
	public boolean getIsCurrentMovement()
	{
		return m_bIsCurrentMovement;
	}
	
	/** @param a_bIsCurrentMovement The isCurrentMovement to set. */
	public void setIsCurrentMovement(boolean a_bIsCurrentMovement)
	{
		m_bIsCurrentMovement = a_bIsCurrentMovement;
	}
	
	public void setLocationId (long a_lLocationId)
	{
		m_lLocationId = a_lLocationId;
	}
	
	public long getLocationId ()
	{
		return (m_lLocationId);
	}
	
	public void setLocationValue (String a_sLocationValue)
	{
		m_sLocationValue = a_sLocationValue;
	}
	
	public String getLocationValue()
	{
		return (m_sLocationValue);
	}
	
	public void setReminderEmail (String a_sReminderEmail)
	{
		m_sReminderEmail = a_sReminderEmail;
	}
	
	public String getReminderEmail ()
	{
		return (m_sReminderEmail);
	}
}
