package com.bright.producttracker.product.bean;

import java.util.ArrayList;
import java.util.List;

import com.bright.producttracker.product.constant.ProductConstants;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * SearchCriteria.java
 *
 * Contains the SearchCriteria class.
 */

/*
 Ver	Date				Who					Comments
 --------------------------------------------------------------------------------
 d1	01-Feb-2005		Martin Wilson		Created
 d2	14-Jun-2005		Matt Stevenson		Modified to include new 'string' properties for list boxes
 d3	26-Oct-2007		Matt Stevenson		Added product status field
 --------------------------------------------------------------------------------
 */

/**
 * Criteria used in a search
 * 
 * @author Bright Interactive
 * @version d1
 */
public class SearchCriteria extends Product implements ProductConstants
{
	private String m_sKeywords = null;
	private long m_lEventId = 0;
	private String m_sEventName = null;
	private String m_sEventString = k_sStringNoCriteriaString;
	private String m_sFromDate = null;
	private String m_sToDate = null;
	private String m_sLocationDescription = null;
	private long m_lCountryId = 0;
	private String m_sVehicleString = k_sStringNoCriteriaString;
	private String m_sProductTypeString = k_sStringNoCriteriaString;
	private String m_sManufacturingLocationString = k_sStringNoCriteriaString;
	private boolean m_bAll = false;
	private String m_sOrdering = k_sDefaultOrdering;
	private String m_sProductStatusString = null;
	private List<Long> m_productSegmentIds = new ArrayList<Long>();
	private Boolean m_bUnknownProductSegments = null;
	
	/** @return Returns the eventId. */
	public long getEventId()
	{
		return m_lEventId;
	}
	/** @param a_sEventId The eventId to set. */
	public void setEventId(long a_lEventId)
	{
		m_lEventId = a_lEventId;
	}
	/** @return Returns the keywords. */
	public String getKeywords()
	{
		return m_sKeywords;
	}
	/** @param a_sKeywords The keywords to set. */
	public void setKeywords(String a_sKeywords)
	{
		m_sKeywords = a_sKeywords;
	}
	/** @return Returns the fromDate. */
	public String getFromDate()
	{
		return m_sFromDate;
	}
	/** @param a_sFromDate The fromDate to set. */
	public void setFromDate(String a_sFromDate)
	{
		m_sFromDate = a_sFromDate;
	}
	/** @return Returns the toDate. */
	public String getToDate()
	{
		return m_sToDate;
	}
	/** @param a_sToDate The toDate to set. */
	public void setToDate(String a_sToDate)
	{
		m_sToDate = a_sToDate;
	}
	
	/** @return Returns the locationDescription. */
	public String getLocationDescription()
	{
		return m_sLocationDescription;
	}
	
	/** @param a_sLocationDescription The locationDescription to set. */
	public void setLocationDescription(String a_sLocationDescription)
	{
		m_sLocationDescription = a_sLocationDescription;
	}
	
	/** @return Returns the countryId. */
	public long getCountryId()
	{
		return m_lCountryId;
	}
	
	/** @param a_sCountryId The countryId to set. */
	public void setCountryId(long a_lCountryId)
	{
		m_lCountryId = a_lCountryId;
	}
	
	/** @return Returns the eventName. */
	public String getEventName()
	{
		return m_sEventName;
	}
	
	/** @param a_sEventName The eventName to set. */
	public void setEventName(String a_sEventName)
	{
		m_sEventName = a_sEventName;
	}
	
	/** @return Returns the eventString. */
	public String getEventString()
	{
		return m_sEventString;
	}
	
	/** @param a_sEventString The eventString to set. */
	public void setEventString(String a_sEventString)
	{
		m_sEventString = a_sEventString;
	}
	
	public String getDatesString ()
	{
		String sDateString = null;
		
		if (this.getFromDate() != null)
		{
			sDateString = this.getFromDate().toString();
		}
		
		//check if we need to add the to date...
		if ((sDateString != null) && (!sDateString.equals("")) && (this.getToDate() != null))
		{
			sDateString = sDateString + " - ";
		}
		
		if (this.getToDate() != null)
		{
			if (sDateString == null)
			{
				sDateString = "";
			}
			
			sDateString = sDateString + this.getToDate().toString();
		}
		
		return (sDateString);
	}
	
	/** @return Returns the vehicleString. */
	public String getVehicleString()
	{
		return m_sVehicleString;
	}
	
	/** @param a_sVehicleString The vehicleString to set. */
	public void setVehicleString(String a_sVehicleString)
	{
		m_sVehicleString = a_sVehicleString;
	}
		
	/** @return Returns the productTypeString. */
	public String getProductTypeString()
	{
		return m_sProductTypeString;
	}

	/** @param a_sProductTypeString The productTypeString to set. */
	public void setProductTypeString(String a_sProductTypeString)
	{
		m_sProductTypeString = a_sProductTypeString;
	}
	
	/** @return Returns the manufacturingLocationString. */
	public String getManufacturingLocationString()
	{
		return m_sManufacturingLocationString;
	}
	
	/** @param a_sManufacturingLocationString The manufacturingLocationString to set. */
	public void setManufacturingLocationString(String a_sManufacturingLocationString)
	{
		m_sManufacturingLocationString = a_sManufacturingLocationString;
	}
	
	public void setAll (boolean a_bAll)
	{
		m_bAll = a_bAll;
	}
	
	public boolean getAll ()
	{
		return (m_bAll);
	}
	
	public void setOrdering (String a_sOrdering)
	{
		m_sOrdering = a_sOrdering;
	}
	
	public String getOrdering ()
	{
		return (m_sOrdering);
	}
	
	public String getProductStatusString ()
	{
		return (m_sProductStatusString);
	}
	
	public void setProductStatusString (String a_sProductStatusString)
	{
		m_sProductStatusString = a_sProductStatusString;
	}
	
	public List<Long> getProductSegmentIds()
	{
		return m_productSegmentIds;
	}
	
	public void setProductSegmentIds(List<Long> a_productSegmentIds)
	{
		m_productSegmentIds = a_productSegmentIds;
	}
	
	public Boolean getUnknownProductSegments()
	{
		return m_bUnknownProductSegments;
	}
	
	public void setUnknownProductSegments(Boolean a_unknownProductSegments)
	{
		m_bUnknownProductSegments = a_unknownProductSegments;
	}
	
	
}
