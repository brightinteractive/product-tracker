package com.bright.producttracker.product.form;

import java.util.List;
import java.util.Vector;

import com.bn2web.common.form.Bn2Form;
import com.bright.producttracker.product.bean.Product;
import com.bright.producttracker.product.bean.SearchCriteria;
import com.bright.producttracker.productsegment.bean.ProductSegment;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * SearchForm.java
 *
 * Contains the SearchForm class.
 */

/*
 Ver	Date				Who				Comments
 --------------------------------------------------------------------------------
 d1	01-Feb-2005		Martin Wilson		Created
 d2	23-Jun-2005		Matt Stevenson		Added report sort
 d3	26-Jul-2010		Kevin Bennett		Added product segment list
 --------------------------------------------------------------------------------
 */

/**
 * Holds search criteria and results.
 * 
 * @author Bright Interactive
 * @version d1
 */
public class SearchForm extends Bn2Form
{
	private SearchCriteria m_criteria = null;
	private List<Product> m_vecResults = null;
	private int m_iReportSort = 0;
	private boolean m_bAll = false;
	private List<ProductSegment> m_productSegments = null;
	
	/** @return Returns the criteria. */
	public SearchCriteria getSearchCriteria()
	{
		if (m_criteria == null)
		{
			m_criteria = new SearchCriteria();
		}
		return m_criteria;
	}
	/** @param a_sCriteria The criteria to set. */
	public void setSearchCriteria(SearchCriteria a_criteria)
	{
		m_criteria = a_criteria;
	}
	/** @return Returns the vecResults. */
	public List<Product> getResults()
	{
		return m_vecResults;
	}
	/** @param a_sVecResults The vecResults to set. */
	public void setResults(List<Product> a_vecResults)
	{
		m_vecResults = a_vecResults;
	}
	
	/** @return Returns the reportSort. */
	public int getReportSort()
	{
		return m_iReportSort;
	}
	
	/** @param a_iReportSort The reportSort to set. */
	public void setReportSort(int a_iReportSort)
	{
		m_iReportSort = a_iReportSort;
	}
	
	public void setAll (boolean a_bAll)
	{
		m_bAll = a_bAll;
	}
	
	public boolean getAll ()
	{
		return (m_bAll);
	}
	
	public List<ProductSegment> getProductSegments()
	{
		return m_productSegments;
	}

	public void setProductSegments(List<ProductSegment> a_productSegments)
	{
		m_productSegments = a_productSegments;
	}
}
