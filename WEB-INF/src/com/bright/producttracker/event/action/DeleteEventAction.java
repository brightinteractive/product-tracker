package com.bright.producttracker.event.action;

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
import com.bright.producttracker.event.service.EventManager;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * DeleteEventAction.java
 *
 * Contains the DeleteEventAction class.
 */

/*
 Ver	Date				Who					Comments
 --------------------------------------------------------------------------------
 d1	20-Jan-2005		Martin Wilson		Created
 --------------------------------------------------------------------------------
 */

/**
 * Shows the events, to enable the admin user to edit them or add new ones
 * 
 * @author Bright Interactive
 * @version d1
 */
public class DeleteEventAction extends Bn2Action
{
	protected EventManager m_eventManager = null;
	protected DBTransactionManager m_transactionManager = null;
	
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
		
		DBTransaction dbTransaction = null;
		
		long lId = getLongParameter(a_request, CommonConstants.k_sIDParam);		
		
		try
		{			
			// Get a transaction:
			dbTransaction = m_transactionManager.getNewTransaction();
			
			// Remove any dependencies on this list value:
			removeDependencies(dbTransaction, lId);			
			
			//++ Delete all products at event etc.
			
			m_eventManager.deleteEvent(dbTransaction, lId);
		
			afForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY); 
		}
		catch (Bn2Exception bn2e)
		{			
			 m_logger.error("Exception in DeleteEvent:" + bn2e.getMessage());
			 afForward = a_mapping.findForward (CommonConstants.FAILURE_KEY);
			 
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
				  m_logger.error("Exception commiting transaction in DeleteEvent:" + sqle.getMessage());
				  afForward = a_mapping.findForward (CommonConstants.FAILURE_KEY);
			 }
		}					
		
		return (afForward);
	}
	
	/**
	 * Implement this in a subclass to remove any dependencies.
	 * 
	 * @param a_dbTransaction
	 * @param a_lValueId
	 */
	protected void removeDependencies(DBTransaction a_dbTransaction, 
												 long a_lEventId) throws Bn2Exception
	/*
	---------------------------------------------------------------
	d1		03-Feb-2005		Martin Wilson		Created 
	-----------------------------------------------------------------
	*/	
	{
		// This is here to stop compliation warning!
		if (a_lEventId == 0)
		{
			throw new Bn2Exception("DeleteEvent:removeDependencies, id = 0");
		}
	}
	
	public void setTransactionManager(DBTransactionManager a_transactionManager)
	{
		m_transactionManager = a_transactionManager;
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
	
}
