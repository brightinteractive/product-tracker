package com.bright.producttracker.event.action;

import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.bn2web.common.action.Bn2Action;
import com.bn2web.common.constant.CommonConstants;
import com.bn2web.common.exception.Bn2Exception;
import com.bright.producttracker.event.bean.Event;
import com.bright.producttracker.event.form.EventForm;
import com.bright.producttracker.event.service.EventManager;
import com.bright.producttracker.product.bean.Product;
import com.bright.producttracker.product.bean.SearchCriteria;
import com.bright.producttracker.product.constant.ProductConstants;
import com.bright.producttracker.product.service.ProductManager;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * ViewAddEventAction.java
 *
 * Contains the ViewEditEventAction class.
 */

/*
 Ver	Date				Who					Comments
 --------------------------------------------------------------------------------
 d1	20-Jan-2005		Martin Wilson		Created
 d2	22-Jun-2005		Matt Stevenson		Modified execute method
 d3	26-Jul-2010		Kevin Bennett		Modified for old/new code fields
 --------------------------------------------------------------------------------
 */

/**
 * Used to edit or add a new event
 * 
 * @author Bright Interactive
 * @version d1
 */
public class ViewEventAction extends Bn2Action implements ProductConstants
{
	protected EventManager m_eventManager = null;
	protected ProductManager m_productManager = null;	
	
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
	 d2	22-Jun-2005	Matt Stevenson		Changed call to search to include ordering clause
	 ------------------------------------------------------------------------
	 */
	{
		// Go to the default:
		ActionForward afForward = null;
		
		// See if an id has been passed in as an attribute:
		Object oId = a_request.getAttribute(CommonConstants.k_sIDParam);
		long lId = 0;
		
		if (oId != null)
		{
			lId = ((Long)oId).longValue();
		}
		else
		{
			lId = getLongParameter(a_request, CommonConstants.k_sIDParam);
		}
		
		if (lId > 0)
		{
			// We're editing an existing event.
			try
			{
				EventForm form = (EventForm)a_form;
				
				// Get the event:
				Event event = m_eventManager.getEvent(null, lId);
				
				// Set in the form:
				form.setEvent(event);				
				
				// Get the products at the event:
				SearchCriteria criteria = new SearchCriteria();
				criteria.setEventId(lId);
				
				List<Product> vecProducts = m_productManager.search(null, criteria, k_sSortConstant_OldCode);
				form.setProducts(vecProducts);
			
				afForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY); 
			}
			catch (Bn2Exception bn2)
			{
				m_logger.error("Bn2Exception in ViewEditEventAction: " + bn2.getMessage());
				afForward = a_mapping.findForward(CommonConstants.FAILURE_KEY); 
			}
		}
		else
		{
			// We want to add a new event:
			afForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY); 			
		}
		
		return (afForward);
	}
	
	/**
	 * Sets the event manager. This method is called in the baseclass when linking in components,
	 * as specified in the framework config file.
	 *
	 * @param EventManager the event manager component to use.
	 */
	public void setEventManager(EventManager a_eventManager)
	{
		m_eventManager = a_eventManager;
	}
	
	public void setProductManager(ProductManager a_productManager)
	{
		m_productManager = a_productManager;
	}
}
