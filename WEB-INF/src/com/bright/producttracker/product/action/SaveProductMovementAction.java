package com.bright.producttracker.product.action;

import java.sql.SQLException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.bn2web.common.action.Bn2Action;
import com.bn2web.common.constant.CommonConstants;
import com.bn2web.common.exception.Bn2Exception;
import com.bright.framework.constant.FrameworkConstants;
import com.bright.framework.constant.FrameworkSettings;
import com.bright.framework.database.bean.DBTransaction;
import com.bright.framework.database.service.DBTransactionManager;
import com.bright.framework.util.StringUtil;
import com.bright.producttracker.event.service.EventManager;
import com.bright.producttracker.product.bean.ProductMovement;
import com.bright.producttracker.product.form.ProductMovementForm;
import com.bright.producttracker.product.service.ProductManager;
import com.bright.producttracker.product.constant.ProductConstants;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * SaveProductMovementAction
 *
 * Contains the SaveProductMovementAction class.
 */

/*
 Ver	Date				Who					Comments
 --------------------------------------------------------------------------------
 d1	18-Mar-2005		Matt Stevenson		Created
 --------------------------------------------------------------------------------
 */

/**
 * SaveProductMovementAction
 * 
 * @author Bright Interactive
 * @version d1
 */
public class SaveProductMovementAction extends Bn2Action implements ProductConstants
{
	protected ProductManager m_productManager = null;
	protected DBTransactionManager m_transactionManager = null;
	protected EventManager m_eventManager = null;
	
	/**
	 * The execute method of the Action.
	 *
	 * @param ActionMapping a_mapping - the action mapping [explain it here].
	 * @param ActionForm a_form - the action form
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
 ------------------------------------------------------------------------
	 */
	{
		// Go to the default:
		ActionForward afForward = null;
		
		DBTransaction dbTransaction = null;
		
		// See if the user cancelled:
		if (a_request.getParameter(FrameworkConstants.k_sCancelParam) != null
			&& a_request.getParameter(FrameworkConstants.k_sCancelParam).length() > 0)
		{
			return a_mapping.findForward(FrameworkConstants.CANCEL_KEY); 
		}		
		
		try
		{						
			// Get a transaction:
			dbTransaction = m_transactionManager.getNewTransaction();
			
			ProductMovementForm form = (ProductMovementForm)a_form;
			
			// Get the product movement:
			ProductMovement productMovement = form.getProductMovement();
			
			//populate the product movement dates
			try
			{
				if (StringUtil.stringIsDate(productMovement.getArrivalDateString()))
				{
					// Set the date:
					productMovement.setArrivalDate(FrameworkSettings.getStandardDateFormat().parse(productMovement.getArrivalDateString()));
				}
				
				if (StringUtil.stringIsDate(productMovement.getDepartureDateString()))
				{
					// Set the date:
					productMovement.setDepartureDate(FrameworkSettings.getStandardDateFormat().parse(productMovement.getDepartureDateString()));
				}
			}
			catch (ParseException e)
			{
				//log the error - but don't do anything with it - will be caught by form validation
				m_logger.error("SaveProductMovementAction.execute : Error : "+e.getMessage());
			}
			
			// Check for required fields:
			form.validate();
								
			if (!form.getHasErrors())
			{
				// Save the product movement:
				boolean bCurrentLocation = false;
				
				if (form.getLocationType().equals(k_sLocationType_Current))
				{
					bCurrentLocation = true;
				}
				
				m_productManager.saveProductMovement(dbTransaction, productMovement, bCurrentLocation);
				
     			// Get the id of the product and add as an attribute in case we need the new id:
				long lProductId = productMovement.getProductId();
				
				a_request.setAttribute(CommonConstants.k_sIDParam, new Long(lProductId));
			
				afForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY); 
			}
			else
			{
				//set the events in the form...
				form.setEvents(m_eventManager.getEvents(null, false, null));
				afForward = a_mapping.findForward(CommonConstants.FAILURE_KEY); 				
			}
		}
		catch (Bn2Exception bn2e)
		{			
			 m_logger.error("Exception in SaveProductMovementAction:" + bn2e.getMessage());
			 afForward = a_mapping.findForward (CommonConstants.SYSTEM_FAILURE_KEY);
			 
			 try
			 {
				  dbTransaction.rollback();
			 }
			 catch (SQLException se)
			 {
				  // Do nothing.
			 }
		}
		finally
		{
			 // Commit the transaction:
			 try
			 {
				  dbTransaction.commit();
			 }
			 catch (SQLException sqle)
			 {
				  m_logger.error("Exception commiting transaction in SaveProductMovementAction:" + sqle.getMessage());
				  afForward = a_mapping.findForward (CommonConstants.FAILURE_KEY);
			 }
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

	public void setTransactionManager(DBTransactionManager a_transactionManager)
	{
		m_transactionManager = a_transactionManager;
	}	
	
	public void setEventManager(EventManager a_eventManager)
	{
		m_eventManager = a_eventManager;
	}	
}
