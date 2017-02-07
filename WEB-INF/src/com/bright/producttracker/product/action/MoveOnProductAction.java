package com.bright.producttracker.product.action;

import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.bn2web.common.action.Bn2Action;
import com.bn2web.common.constant.CommonConstants;
import com.bn2web.common.exception.Bn2Exception;
import com.bright.framework.database.bean.DBTransaction;
import com.bright.framework.database.service.DBTransactionManager;
import com.bright.producttracker.product.service.ProductManager;
import com.bright.producttracker.product.constant.ProductConstants;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * MoveOnProductAction
 *
 * Contains the MoveOnProductAction class.
 */

/*
 Ver	Date				Who					Comments
 --------------------------------------------------------------------------------
 d1	18-Mar-2005		Matt Stevenson		Created
 --------------------------------------------------------------------------------
 */

/**
 * MoveOnProductAction
 * 
 * @author Bright Interactive
 * @version d1
 */
public class MoveOnProductAction extends Bn2Action
{
	protected ProductManager m_productManager = null;
	protected DBTransactionManager m_transactionManager = null;
	
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
		
		try
		{						
			// Get a transaction:
			dbTransaction = m_transactionManager.getNewTransaction();
			
			//get the id of the product movement...
			long lId = getLongParameter(a_request, CommonConstants.k_sIDParam);
			
			//use the manager to move the product on...
			m_productManager.moveProductOn(dbTransaction, lId);
			
			//put the product id in the attributes to return to
			long lProductId = getLongParameter(a_request, ProductConstants.k_sParam_ProductId);
			a_request.setAttribute(CommonConstants.k_sIDParam, new Long(lProductId));
			
			afForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY); 
			
		}
		catch (Bn2Exception bn2e)
		{			
			 m_logger.error("Exception in MoveOnProductAction:" + bn2e.getMessage());
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
				  m_logger.error("Exception commiting transaction in MoveOnProductAction:" + sqle.getMessage());
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
}
