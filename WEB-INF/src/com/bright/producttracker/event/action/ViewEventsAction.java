package com.bright.producttracker.event.action;

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
import com.bright.framework.util.StringUtil;
import com.bright.producttracker.application.constant.ApplicationConstants;
import com.bright.producttracker.event.form.ManageEventsForm;
import com.bright.producttracker.event.service.EventManager;

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
 d1		20-Jan-2005		Martin Wilson		Created
 d2		25-Oct-2007		Matt Stevenson		Modified execute method
 --------------------------------------------------------------------------------
 */

/**
 * Shows the events, to enable the admin user to edit them or add new ones
 * 
 * @author Bright Interactive
 * @version d1
 */
public class ViewEventsAction extends Bn2Action implements ApplicationConstants
{
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
	 d1		20-Jan-2005		Martin Wilson		Created
	 d2		25-Oct-2007		Matt Stevenson		Added sorting
	 ------------------------------------------------------------------------
	 */
	{
		// Go to the default:
		ActionForward afForward = null;
		
		try
		{
			ManageEventsForm form = (ManageEventsForm)a_form;
			
			//get the sort parameter...
			String sSort = a_request.getParameter(k_sParam_Ordering);
			
			if (!StringUtil.stringIsPopulated(sSort))
			{
				sSort = k_sDefaultSort_Events;
			}
			
			// Get all the events:
			Vector vecEvents = m_eventManager.getEvents(null, true, sSort);
			
			// Set in the form:
			form.setEvents(vecEvents);
		
			afForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY); 
		}
		catch (Bn2Exception bn2)
		{
			m_logger.error("Bn2Exception in ViewEventsAction: " + bn2.getMessage());
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
	
}
