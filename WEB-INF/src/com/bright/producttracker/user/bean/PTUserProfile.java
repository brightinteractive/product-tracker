package com.bright.producttracker.user.bean;

/**
 * RTUserProfile, Bright Interactive , GKN
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * PTUserProfile
 *
 * Contains the RTUserProfile class.
 */
/*
 Ver  Date				Who					Comments
 --------------------------------------------------------------------------------
 d1	18-Jan-2005		Martin Wilson		Created
 d2	14-Jun-2005		Matt Stevenson		Added last search criteria 
 d3	25-Oct-2007		Matt Stevenson		Added methods for search result traversing
 --------------------------------------------------------------------------------
 */

import java.util.List;
import java.util.Vector;

import com.bn2web.common.service.GlobalApplication;
import com.bright.framework.user.bean.UserProfile;
import com.bright.producttracker.product.bean.Product;
import com.bright.producttracker.product.bean.SearchCriteria;

/**
 * A wrapper for session scope objects. This object will be stored in the session.
 *
 * @author  Bright Interactive
 * @version d1
 */
public class PTUserProfile extends UserProfile
{
	private List<Product> m_vecLastSearchResults = null;
	private SearchCriteria m_lastSearchCriteria = null;
	private SearchCriteria m_goBackSearchCriteria = null;
	
	/*
	 * Default constructor- private to force use of getUserProfile.
	 */
	protected PTUserProfile()
	/*
	 ------------------------------------------------------------------------
	d1	18-Jan-2005		Martin Wilson		Created 
	 ------------------------------------------------------------------------
	 */
	{
		/* Empty */
	}
	
	/**
	 * Returns true if this user is admin
	 * 
	 * @see com.bright.framework.user.bean.UserProfile#getIsAdmin()
	 * 
	 * @return
	 */
	public boolean getIsAdmin()
	/*
	---------------------------------------------------------------
	d1	18-Jan-2005		Martin Wilson		Created 
	-----------------------------------------------------------------
	*/	
	{
		// There are only admin users (at the moment, anyway).
		return (true);
	}
	
	
	/** @return Returns the vecLastSearchResults. */
	public List<Product> getLastSearchResults()
	{
		return m_vecLastSearchResults;
	}
	/** @param a_sVecLastSearchResults The vecLastSearchResults to set. */
	public void setLastSearchResults(List<Product> a_vecLastSearchResults)
	{
		m_vecLastSearchResults = a_vecLastSearchResults;
	}
	
	
	public long getPreviousSearchResultId (String a_sId)
	{
		int iIndex = getSearchResultIndex(a_sId);
		iIndex--;
		return (this.getSearchResultIndexId(iIndex));
	}
	
	
	public long getNextSearchResultId (String a_sId)
	{
		int iIndex = getSearchResultIndex(a_sId);
		iIndex++;
		return (this.getSearchResultIndexId(iIndex));
	}
	
	
	private long getSearchResultIndexId (int a_iIndex)
	{
		long lId = 0;
		if (this.getLastSearchResults() != null)
		{
			if (a_iIndex >= 0 && a_iIndex < this.getLastSearchResults().size())
			{
				Product temp = (Product)(this.getLastSearchResults().get(a_iIndex));
				lId = temp.getId();
			}
		}
		return (lId);
	}
	
	
	private int getSearchResultIndex (String a_sId)
	{
		try
		{
			long lCurrentId = Long.parseLong(a_sId);
			
			//iterate over the last search results...
			if (this.getLastSearchResults() != null)
			{
				for (int i=0; i<this.getLastSearchResults().size(); i++)
				{
					Product temp = (Product)(this.getLastSearchResults().get(i));
					
					if (temp.getId() == lCurrentId)
					{
						return (i);
					}
				}
			}
		}
		catch (Exception e)
		{
			GlobalApplication.getInstance().getLogger().error ("PTUserProfile.getPreviousSearchResultId: Error: "+e.getMessage());
		}
		
		return (-1);
	}
	
	public void setLastSearchCriteria (SearchCriteria a_lastSearchCriteria)
	{
		m_lastSearchCriteria = a_lastSearchCriteria;
	}
	
	public SearchCriteria getLastSearchCriteria()
	{
		return (m_lastSearchCriteria);
	}
	
	public void setGoBackSearchCriteria (SearchCriteria a_goBackSearchCriteria)
	{
		m_goBackSearchCriteria = a_goBackSearchCriteria;
	}
	
	public SearchCriteria getGoBackSearchCriteria()
	{
		return (m_goBackSearchCriteria);
	}
}