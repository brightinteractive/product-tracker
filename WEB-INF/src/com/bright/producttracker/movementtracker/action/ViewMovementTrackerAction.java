package com.bright.producttracker.movementtracker.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.bn2web.common.action.Bn2Action;
import com.bn2web.common.exception.Bn2Exception;
import com.bright.producttracker.movementtracker.form.MovementTrackerForm;
import com.bright.producttracker.productsegment.bean.ProductSegment;
import com.bright.producttracker.productsegment.service.ProductSegmentManager;

public class ViewMovementTrackerAction extends Bn2Action
{		
	ProductSegmentManager m_productSegmentManager = null;
	
	public ActionForward execute(ActionMapping a_mapping,
			  ActionForm a_form,
			  HttpServletRequest a_request,
			  HttpServletResponse a_response)
	throws Bn2Exception
	{					
		MovementTrackerForm form = (MovementTrackerForm) a_form;
		form.setProductSegments(m_productSegmentManager.getProductSegments(null));
		
		if(a_request.getParameter("default") != null)
		{
			String[] segmentIds = new String[form.getProductSegments().size()];
			int i = 0;
			for (ProductSegment productSegment : form.getProductSegments())
			{
				segmentIds[i] = String.valueOf(productSegment.getId());
				i++;
			}
			form.setProductSegmentIds(segmentIds);
			form.setIncludeUnknownProductSegments(true);
		}
		
		return a_mapping.findForward(SUCCESS_KEY);
	}
	
	public void setProductSegmentManager(ProductSegmentManager a_productSegmentManager)
	{
		m_productSegmentManager = a_productSegmentManager;
	}
}
