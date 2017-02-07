package com.bright.producttracker.product.form;

import java.util.Vector;

import com.bn2web.common.form.Bn2Form;
import com.bright.producttracker.product.bean.SearchCriteria;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * ReportForm
 *
 * Contains the ReportForm class.
 */

/*
 Ver	Date				Who					Comments
 --------------------------------------------------------------------------------
 d1	22-Mar-2005		Matt Stevenson		Created
 d2	23-Mar-2005		Matt Stevenson		Added method names
 d3	14-Jun-2005		Matt Stevenson		Added search criteria
 d4	22-Jun-2005		Matt Stevenson		Added switch for location dates
 d5	03-Nov-2006		Matt Stevenson		Added field ordering
 --------------------------------------------------------------------------------
 */

/**
 * ReportForm
 * 
 * @author Bright Interactive
 * @version d1
 */
public class ReportForm extends Bn2Form
{
	private Vector m_vecColumnNames = null;
	private Vector m_vecFullColumnNames = null;
	private Vector m_vecMethodNames = null;
	private Vector m_vecReportLines = null;
	private boolean m_bIsPrint = false;
	private SearchCriteria m_searchCriteria = null;
	private boolean m_bShowLocations = false;
	private boolean m_bShowLocationDates = false;
	private boolean m_bShowEvents = false;
	private String m_sFieldOrder = null;
	
	/** @return Returns the isPrint. */
	public boolean getIsPrint()
	{
		return m_bIsPrint;
	}
	
	/** @param a_bIsPrint The isPrint to set. */
	public void setIsPrint(boolean a_bIsPrint)
	{
		m_bIsPrint = a_bIsPrint;
	}
	
	/** @return Returns the MethodNames. */
	public Vector getMethodNames()
	{
		return m_vecMethodNames;
	}
	
	/** @param a_vecMethodNames The MethodNames to set. */
	public void setMethodNames(Vector a_vecMethodNames)
	{
		m_vecMethodNames = a_vecMethodNames;
	}
	
	/** @return Returns the vecColumnNames. */
	public Vector getColumnNames()
	{
		return m_vecColumnNames;
	}
	
	/** @param a_vecColumnNames The vecColumnNames to set. */
	public void setColumnNames(Vector a_vecColumnNames)
	{
		m_vecColumnNames = a_vecColumnNames;
	}
	/** @return Returns the FullColumnNames. */
	public Vector getFullColumnNames()
	{
		return m_vecFullColumnNames;
	}
	
	/** @param a_vecFullColumnNames The FullColumnNames to set. */
	public void setFullColumnNames(Vector a_vecFullColumnNames)
	{
		m_vecFullColumnNames = a_vecFullColumnNames;
	}
	/** @return Returns the vecReportLines. */
	public Vector getReportLines()
	{
		return m_vecReportLines;
	}
	/** @param a_vecReportLines The vecReportLines to set. */
	public void setReportLines(Vector a_vecReportLines)
	{
		m_vecReportLines = a_vecReportLines;
	}
	
	public void setSearchCriteria (SearchCriteria a_searchCriteria)
	{
		m_searchCriteria = a_searchCriteria;
	}
	
	public SearchCriteria getSearchCriteria()
	{
		return (m_searchCriteria);
	}
	
	public void setShowLocations (boolean a_bShowLocations)
	{
		m_bShowLocations = a_bShowLocations;
	}
	
	public boolean getShowLocations ()
	{
		return (m_bShowLocations);
	}
	
	public void setShowLocationDates (boolean a_bShowLocationDates)
	{
		m_bShowLocationDates = a_bShowLocationDates;
	}
	
	public boolean getShowLocationDates ()
	{
		return (m_bShowLocationDates);
	}
	
	public void setShowEvents (boolean a_bShowEvents)
	{
		m_bShowEvents = a_bShowEvents;
	}
	
	public boolean getShowEvents ()
	{
		return (m_bShowEvents);
	}
	
	public void setFieldOrder (String a_sFieldOrder)
	{
		m_sFieldOrder = a_sFieldOrder;
	}
	
	public String getFieldOrder ()
	{
		return (m_sFieldOrder);
	}
	
	public String getMethodName (int iIndex)
	{
		try
		{
			return ((String)(this.getMethodNames().elementAt(iIndex)));
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
