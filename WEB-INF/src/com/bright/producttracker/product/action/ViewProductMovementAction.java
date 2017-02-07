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
import com.bright.producttracker.event.service.EventManager;
import com.bright.producttracker.product.bean.ProductMovement;
import com.bright.producttracker.product.constant.ProductConstants;
import com.bright.producttracker.product.form.ProductMovementForm;
import com.bright.producttracker.product.service.ProductManager;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * ViewProductMovementAction
 *
 * Contains the ViewProductMovementAction class.
 */

/*
 Ver	Date				Who					Comments
 --------------------------------------------------------------------------------
 d1	18-Mar-2005		Matt Stevenson		Created
 d2	18-Apr-2005		Matt Stevenson		Added current movement check
 --------------------------------------------------------------------------------
 */

/**
 * ViewProductMovementAction
 * 
 * @author Bright Interactive
 * @version d1
 */
public class ViewProductMovementAction extends Bn2Action implements ProductConstants
{
	protected ProductManager m_productManager = null;
	protected EventManager m_eventManager = null;
	
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
	 d1	18-Mar-2005		Matt Stevenson		Created
	 d2	18-Apr-2005		Matt Stevenson		Added current movement check
    ------------------------------------------------------------------------
	 */
	{
		// Go to the default:
		ActionForward afForward = null;
		
		// See if an id has been passed in
		long lId = getLongParameter(a_request, CommonConstants.k_sIDParam);
		long lProductId = getLongParameter(a_request, k_sParam_ProductId);
		
		ProductMovementForm form = (ProductMovementForm)a_form;
		form.getProductMovement().setProductId(lProductId);
		
		//get and set the events in the form...
		form.setEvents(m_eventManager.getEvents(null, false, null));
				
		if (lId > 0)
		{
			// We're editing/viewing an existing ProductMovement.
			try
			{
				// Get the ProductMovement:
				ProductMovement productMovement = m_productManager.getProductMovement(null, lId);
								
				// Set in the form:
				form.setProductMovement(productMovement);
				
				if (productMovement.getIsCurrentMovement())
				{
					form.setLocationType(k_sLocationType_Current);
				}
								
				afForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY); 
			}
			catch (Bn2Exception bn2)
			{
				m_logger.error("Bn2Exception in ViewProductMovementAction: " + bn2.getMessage());
				afForward = a_mapping.findForward(CommonConstants.FAILURE_KEY); 
			}
		}
		else
		{
			// We want to add a new ProductMovment:
			afForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY); 			
		}
		
		return (afForward);
	}
	
	/**
	 * Sets the Product manager. This method is called in the baseclass when linking in components,
	 * as specified in the framework config file.
	 *
	 * @param ProductManager the Product manager component to use.
	 */
	public void setProductManager(ProductManager a_ProductManager)
	{
		m_productManager = a_ProductManager;
	}
	
	public void setEventManager(EventManager a_EventManager)
	{
		m_eventManager = a_EventManager;
	}
}
