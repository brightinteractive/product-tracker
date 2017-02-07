package com.bright.producttracker.movementtracker.form;

import java.util.List;

import com.bn2web.common.form.Bn2Form;
import com.bright.producttracker.product.bean.SearchCriteria;
import com.bright.producttracker.productsegment.bean.ProductSegment;

public class MovementTrackerForm extends Bn2Form
{
	private String[] m_productSegmentIds = null;
	private Boolean m_includeUnknownProductSegments = null;
	
	private List<ProductSegment> m_productSegments = null;
	
	public String[] getProductSegmentIds()
	{
		return m_productSegmentIds;
	}
	
	public void setProductSegmentIds(String[] a_productSegmentIds)
	{
		m_productSegmentIds = a_productSegmentIds;
	}
	
	public Boolean getIncludeUnknownProductSegments()
	{
		return m_includeUnknownProductSegments;
	}
	
	public void setIncludeUnknownProductSegments(Boolean a_includeUnknownSegments)
	{
		m_includeUnknownProductSegments = a_includeUnknownSegments;
	}
	
	public List<ProductSegment> getProductSegments()
	{
		return m_productSegments;
	}

	public void setProductSegments(List<ProductSegment> a_productSegments)
	{
		m_productSegments = a_productSegments;
	}
	
	public SearchCriteria getSearchCriteria()
	{
		SearchCriteria searchCriteria = new SearchCriteria();
		
		if(m_productSegmentIds != null)
		{
			for (String productSegmentId : m_productSegmentIds)
			{
				searchCriteria.getProductSegmentIds().add(Long.valueOf(productSegmentId));
			}
		}
		
		if(m_includeUnknownProductSegments != null)
		{
			searchCriteria.setUnknownProductSegments(m_includeUnknownProductSegments);
		}
		
		return searchCriteria;
	}
		
}
