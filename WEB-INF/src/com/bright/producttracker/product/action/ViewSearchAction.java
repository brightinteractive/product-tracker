package com.bright.producttracker.product.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.bn2web.common.action.Bn2Action;
import com.bn2web.common.constant.CommonConstants;
import com.bn2web.common.exception.Bn2Exception;
import com.bright.producttracker.product.form.SearchForm;
import com.bright.producttracker.productsegment.service.ProductSegmentManager;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * ViewEventsAction.java
 *
 * Contains the ViewEventsAction class.
 */

/*
 Ver	Date				Who					Comments
 --------------------------------------------------------------------------------
 d1	20-Jan-2005		Martin Wilson		Created
 d2	26-Jul-2010		Kevin Bennett		Added getting of product segment list
 --------------------------------------------------------------------------------
 */

/**
 * Shows the events, to enable the admin user to edit them or add new ones
 * 
 * @author Bright Interactive
 * @version d1
 */
public class ViewSearchAction extends Bn2Action
{
	protected ProductSegmentManager m_productSegmentManager = null;
	
	/**
	 * The execute method of the Action.
	 *
	 * @param ActionMapping a_mapping - the action mapping [explain it here].
	 * @param ActionForm a_form - the action form- in this case containing the login info entered by user.
	 * @param HttpServletRequest a_request - the request object.
	 * @param HttpServletResponse a_response - the response object.
	 * @return ActionForward - where to forward or redirect to when completed.
	 * @throws	ServletException    Servlet Exception
	 */
	public ActionForward execute(ActionMapping a_mapping,
										  ActionForm a_form,
										  HttpServletRequest a_request,
										  HttpServletResponse a_response)
	throws Bn2Exception
	/*
	 ------------------------------------------------------------------------
	 d1	20-Jan-2005	Martin Wilson		Created
	 ------------------------------------------------------------------------
	 */
	{
		// Go to the default:
		ActionForward afForward = null;		
		afForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY);
		
		SearchForm form = (SearchForm) a_form;
		form.setProductSegments(m_productSegmentManager.getProductSegments(null));

		return (afForward);
	}
	
	public void setProductSegmentManager(ProductSegmentManager a_productSegmentManager)
	{
		m_productSegmentManager = a_productSegmentManager;
	}

	
}
