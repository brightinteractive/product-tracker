package com.bright.producttracker.product.bean;

import java.util.Date;
import java.util.Vector;

import com.bright.framework.database.bean.DataBean;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * ReportLine
 *
 * Contains the ReportLine class.
 */

/*
 Ver	Date				Who					Comments
 --------------------------------------------------------------------------------
 d1	22-Mar-2005		Matt Stevenson		Created
 d2	22-Jun-2005		Matt Stevenson		Added location and event fields
 d3	10-Mar-2008		Matt Stevenson		Added next location and dates
 --------------------------------------------------------------------------------
 */

/**
 * Represents a ReportLine
 * 
 * @author Bright Interactive
 * @version d1
 */
public class ReportLine extends DataBean
{
	private Vector m_vecFieldValues = null;
	private String m_sLocation = null;
	private Date m_dtLocationArrivalDate = null;
	private Date m_dtLocationDepartureDate = null;
	private String m_sNextLocation = null;
	private Date m_dtNextLocationArrivalDate = null;
	private Date m_dtNextLocationDepartureDate = null;
	private String m_sEvent = null;

	/** @return Returns the dtLocationArrivalDate. */
	public Date getLocationArrivalDate()
	{
		return m_dtLocationArrivalDate;
	}
	
	/** @param a_sDtLocationArrivalDate The dtLocationArrivalDate to set. */
	public void setLocationArrivalDate(Date a_dtLocationArrivalDate)
	{
		m_dtLocationArrivalDate = a_dtLocationArrivalDate;
	}
	
	/** @return Returns the dtLocationDepartureDate. */
	public Date getLocationDepartureDate()
	{
		return m_dtLocationDepartureDate;
	}
	
	/** @param a_sDtLocationDepartureDate The dtLocationDepartureDate to set. */
	public void setLocationDepartureDate(Date a_dtLocationDepartureDate)
	{
		m_dtLocationDepartureDate = a_dtLocationDepartureDate;
	}
	
	/** @return Returns the dtLocationArrivalDate. */
	public Date getNextLocationArrivalDate()
	{
		return m_dtNextLocationArrivalDate;
	}
	
	/** @param a_sDtLocationArrivalDate The dtLocationArrivalDate to set. */
	public void setNextLocationArrivalDate(Date a_dtNextLocationArrivalDate)
	{
		m_dtNextLocationArrivalDate = a_dtNextLocationArrivalDate;
	}
	
	/** @return Returns the dtLocationDepartureDate. */
	public Date getNextLocationDepartureDate()
	{
		return m_dtNextLocationDepartureDate;
	}
	
	/** @param a_sDtLocationDepartureDate The dtLocationDepartureDate to set. */
	public void setNextLocationDepartureDate(Date a_dtNextLocationDepartureDate)
	{
		m_dtNextLocationDepartureDate = a_dtNextLocationDepartureDate;
	}
	
	/** @return Returns the event. */
	public String getEvent()
	{
		return m_sEvent;
	}
	
	/** @param a_sEvent The event to set. */
	public void setEvent(String a_sEvent)
	{
		m_sEvent = a_sEvent;
	}
	
	/** @return Returns the location. */
	public String getLocation()
	{
		return m_sLocation;
	}
	
	/** @param a_sLocation The location to set. */
	public void setLocation(String a_sLocation)
	{
		m_sLocation = a_sLocation;
	}
	
	/** @return Returns the location. */
	public String getNextLocation()
	{
		return m_sNextLocation;
	}
	
	/** @param a_sLocation The location to set. */
	public void setNextLocation(String a_sNextLocation)
	{
		m_sNextLocation = a_sNextLocation;
	}
	
	/** @return Returns the vecFieldValues. */
	public Vector getFieldValues()
	{
		return m_vecFieldValues;
	}
	
	/** @param a_vecFieldValues The vecFieldValues to set. */
	public void setVecFieldValues(Vector a_vecFieldValues)
	{
		m_vecFieldValues = a_vecFieldValues;
	}
}
