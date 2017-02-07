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
import com.bright.producttracker.product.form.ProductForm;
import com.bright.producttracker.product.service.ProductManager;
import com.bright.producttracker.productsegment.service.ProductSegmentManager;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * ViewAddProductAction.java
 *
 * Contains the ViewProductAction class.
 */

/*
 Ver	Date				Who					Comments
 --------------------------------------------------------------------------------
 d1	01-Feb-2005		Martin Wilson		Created
 d2	19-Jul-2005		Matt Stevenson		Modified to store old code for later comparison
 d3	26-Jul-2010		Kevin Bennett		Modified for old/new code fields
 --------------------------------------------------------------------------------
 */

/**
 * Used to edit or add a new Product
 * 
 * @author Bright Interactive
 * @version d1
 */
public class ViewProductAction extends Bn2Action
{
	protected ProductManager m_productManager = null;
	protected ProductSegmentManager m_productSegmentManager = null;
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
	 d1	01-Feb-2005	Martin Wilson		Created
	 d2	19-Jul-2005	Matt Stevenson		Modified to store old code for later comparison
	 ------------------------------------------------------------------------
	 */
	{
		// Go to the default:
		ActionForward afForward = null;
		ProductForm form = (ProductForm)a_form;
		
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
			// We're editing/viewing an existing Product.
			try
			{				
				
				// Get the Product:
				Product product = m_productManager.getProduct(null, lId);
				
				// Set in the form:
				form.setProduct(product);
				form.setPreEditOldProductCode(product.getOldCode());
				form.setPreEditNewProductCode(product.getNewCode());
				
				// Get the events:
				Vector vecEvents = m_eventManager.getProductEvents(null, lId);
				form.setEvents(vecEvents);
				
				
			
				afForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY); 
			}
			catch (Bn2Exception bn2)
			{
				m_logger.error("Bn2Exception in ViewEditProductAction: " + bn2.getMessage());
				afForward = a_mapping.findForward(CommonConstants.FAILURE_KEY); 
			}
		}
		else
		{
			// We want to add a new Product:
			afForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY); 			
		}
		
		form.setProductSegments(m_productSegmentManager.getProductSegments(null));
		
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
	
	
	public void setProductSegmentManager(ProductSegmentManager a_productSegmentManager)
	{
		m_productSegmentManager = a_productSegmentManager;
	}

	public void setEventManager(EventManager a_eventManager)
	{
		m_eventManager = a_eventManager;
	}	
}
