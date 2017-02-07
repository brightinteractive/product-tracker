package com.bright.producttracker.product.action;

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
import com.bright.producttracker.event.service.EventManager;
import com.bright.producttracker.product.bean.Product;
import com.bright.producttracker.product.form.ProductEventsForm;
import com.bright.producttracker.product.service.ProductManager;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * ViewProductEventsAction.java
 *
 * Contains the ViewProductEventsAction class.
 */

/*
 Ver	Date				Who					Comments
 --------------------------------------------------------------------------------
 d1	02-Feb-2005		Martin Wilson		Created
 --------------------------------------------------------------------------------
 */

/**
 * Shows the events a product is used at
 * 
 * @author Bright Interactive
 * @version d1
 */
public class ViewProductEventsAction extends Bn2Action
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
	 d1	02-Feb-2005	Martin Wilson		Created
	 ------------------------------------------------------------------------
	 */
	{
		// Go to the default:
		ActionForward afForward = null;
		
		try
		{
			ProductEventsForm form = (ProductEventsForm)a_form;
			
			// See if the id is in the form first:
			long lId = form.getProduct().getId();
			
			if (lId <= 0)
			{
				lId = getLongParameter(a_request, CommonConstants.k_sIDParam);
				
				if (lId <= 0)
				{
					throw new Bn2Exception("ViewProductEventsAction called with invalid id");
				}
			}
			
			// Get all the events:
			Vector vecEvents = m_eventManager.getProductEvents(null, lId);
			
			// Set in the form:
			form.setEvents(vecEvents);
			
			// Get the product:
			Product product = m_productManager.getProduct(null, lId);
			form.setProduct(product);
		
			afForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY); 
		}
		catch (Bn2Exception bn2)
		{
			m_logger.error("Bn2Exception in ViewProductEventsAction: " + bn2.getMessage());
			afForward = a_mapping.findForward(CommonConstants.FAILURE_KEY); 
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
	
	/** @param a_sProductManager The productManager to set. */
	public void setProductManager(ProductManager a_productManager)
	{
		m_productManager = a_productManager;
	}
}
