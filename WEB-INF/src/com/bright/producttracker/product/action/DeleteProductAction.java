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
import com.bright.producttracker.product.constant.ProductConstants;
import com.bright.producttracker.product.service.ProductManager;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * AddProductEventAction.java
 *
 * Contains the AddProductEventAction class.
 */

/*
 Ver	Date				Who					Comments
 --------------------------------------------------------------------------------
 d1	02-Feb-2005		Martin Wilson		Created
 --------------------------------------------------------------------------------
 */

/**
 * Adds a product to an event
 * 
 * @author Bright Interactive
 * @version d1
 */
public class DeleteProductAction extends Bn2Action implements CommonConstants, ProductConstants
{
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
			// Get the id:
			long lProductId = getLongParameter(a_request, k_sIDParam);
			
			if (lProductId > 0)
			{
				// Save:
				m_productManager.deleteProduct(null, lProductId);	
			}
		
			afForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY); 

		}
		catch (Bn2Exception bn2)
		{
			m_logger.error("Bn2Exception in SaveProductAction: " + bn2.getMessage());
			afForward = a_mapping.findForward(CommonConstants.SYSTEM_FAILURE_KEY); 
		}
		
		return (afForward);
	}
	
	/**
	 * Sets the event manager. This method is called in the baseclass when linking in components,
	 * as specified in the framework config file.
	 *
	 * @param ProductManager the event manager component to use.
	 */
	public void setProductManager(ProductManager a_ProductManager)
	{
		m_productManager = a_ProductManager;
	}
	
}
