package com.bright.producttracker.event.bean;

import java.util.Date;

import com.bright.framework.common.bean.Country;
import com.bright.framework.database.bean.DataBean;


/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * Event.java
 *
 * Contains the Event class.
 */

/*
 Ver	Date			Who					Comments
 --------------------------------------------------------------------------------
 d1		20-Jan-2005		Martin Wilson		Created
 d2		26-Oct-2007		Matt Stevenson		Added coordinator email address
 --------------------------------------------------------------------------------
 */

/**
 * Represents an event
 * 
 * @author Bright Interactive
 * @version d1
 */
public class Event extends DataBean
{
	private String m_sName = null;
	private Date m_dtStartDate = null;
	private Date m_dtEndDate = null;
	private boolean m_bNotVisible = false;
	private Country m_country = null;	
	private String m_sStartDateStr = null;
	private String m_sEndDateStr = null;
	private String m_sCoordinatorEmailAddress = null;
	
	/** @return Returns the notVisible. */
	public boolean getNotVisible()
	{
		return m_bNotVisible;
	}
	/** @param a_sNotVisible The notVisible to set. */
	public void setNotVisible(boolean a_bNotVisible)
	{
		m_bNotVisible = a_bNotVisible;
	}
	/** @return Returns the country. */
	public Country getCountry()
	{
		if (m_country == null)
		{
			m_country = new Country();
		}
		return m_country;
	}
	/** @param a_sCountry The country to set. */
	public void setCountry(Country a_country)
	{
		m_country = a_country;
	}
	/** @return Returns the dtEndDate. */
	public Date getEndDate()
	{
		return m_dtEndDate;
	}
	/** @param a_sDtEndDate The dtEndDate to set. */
	public void setEndDate(Date a_dtEndDate)
	{
		m_dtEndDate = a_dtEndDate;
	}
	/** @return Returns the dtStartDate. */
	public Date getStartDate()
	{
		return m_dtStartDate;
	}
	/** @param a_sDtStartDate The dtStartDate to set. */
	public void setStartDate(Date a_dtStartDate)
	{
		m_dtStartDate = a_dtStartDate;
	}
	/** @return Returns the name. */
	public String getName()
	{
		return m_sName;
	}
	/** @param a_sName The name to set. */
	public void setName(String a_sName)
	{
		m_sName = a_sName;
	}


	/** @return Returns the endDateStr. */
	public String getEndDateStr()
	{
		return m_sEndDateStr;
	}
	/** @param a_sEndDateStr The endDateStr to set. */
	public void setEndDateStr(String a_sEndDateStr)
	{
		m_sEndDateStr = a_sEndDateStr;
	}
	/** @return Returns the startDateStr. */
	public String getStartDateStr()
	{
		return m_sStartDateStr;
	}
	/** @param a_sStartDateStr The startDateStr to set. */
	public void setStartDateStr(String a_sStartDateStr)
	{
		m_sStartDateStr = a_sStartDateStr;
	}
	
	public void setCoordinatorEmailAddress (String a_sCoordinatorEmailAddress)
	{
		m_sCoordinatorEmailAddress = a_sCoordinatorEmailAddress;
	}
	
	public String getCoordinatorEmailAddress ()
	{
		return (m_sCoordinatorEmailAddress);
	}
}
