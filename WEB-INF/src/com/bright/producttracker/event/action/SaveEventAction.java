package com.bright.producttracker.event.action;

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
import com.bright.framework.util.StringUtil;
import com.bright.producttracker.event.bean.Event;
import com.bright.producttracker.event.form.EventForm;
import com.bright.producttracker.event.service.EventManager;
import com.bright.producttracker.product.constant.ProductConstants;

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
 d1	02-Feb-2005		Martin Wilson		Created
 --------------------------------------------------------------------------------
 */

/**
 * Shows the events, to enable the admin user to edit them or add new ones
 * 
 * @author Bright Interactive
 * @version d1
 */
public class SaveEventAction extends Bn2Action
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
	 d1	02-Feb-2005	Martin Wilson		Created
	 ------------------------------------------------------------------------
	 */
	{
		// Go to the default:
		ActionForward afForward = null;
		
		try
		{
			EventForm form = (EventForm)a_form;
			
			// See if the user cancelled:
			if (a_request.getParameter(FrameworkConstants.k_sCancelParam) != null
				&& a_request.getParameter(FrameworkConstants.k_sCancelParam).length() > 0)
			{
				if (form.getEvent().getId() > 0)
				{
					long lEventId = form.getEvent().getId();
					a_request.setAttribute(CommonConstants.k_sIDParam, new Long(lEventId));
					return a_mapping.findForward(FrameworkConstants.CANCEL_KEY); 
				}
				return a_mapping.findForward(ProductConstants.k_sForward_AddCancel); 
			}
				
			// Get the event:
			Event event = form.getEvent();
			
			// Check an event name was added:
			if (!StringUtil.stringIsPopulated(event.getName()))
			{			
				form.addError("Please enter a name for the event");
			}
			
			// Check that the dates are ok:
			if (StringUtil.stringIsPopulated(event.getStartDateStr()))
			{
				if (!StringUtil.stringIsDate(event.getStartDateStr()))
				{
					form.addError("Please enter a valid date Start Date (" + FrameworkSettings.getStandardDateFormatStr().toLowerCase() +")");
				}		
				else
				{
					// Set the date:
					event.setStartDate(FrameworkSettings.getStandardDateFormat().parse(event.getStartDateStr()));
				}
			}
			else
			{
				form.addError("Please enter a Start Date (" + FrameworkSettings.getStandardDateFormatStr().toLowerCase() + ")");
			}
			
			if (StringUtil.stringIsPopulated(event.getEndDateStr()))
			{
				if (!StringUtil.stringIsDate(event.getEndDateStr()))
				{
					form.addError("Please enter a valid End Date (" + FrameworkSettings.getStandardDateFormatStr().toLowerCase());

				}				
				else
				{
					// Set the date:
					event.setEndDate(FrameworkSettings.getStandardDateFormat().parse(event.getEndDateStr()));
				}		
			}
			else
			{
				form.addError("Please enter an End Date ("+ FrameworkSettings.getStandardDateFormatStr().toLowerCase() + ")");
			}		
			
			//validate the email address...
			if (StringUtil.stringIsPopulated(event.getCoordinatorEmailAddress()))
			{
				if (!StringUtil.isValidEmailAddress(event.getCoordinatorEmailAddress()))
				{
					form.addError("You have provided a coordinator email address that is not valid - please try again");
				}
			}
			
			if (!form.getHasErrors())
			{
				// Save the event:
				m_eventManager.saveEvent(null, event);
				
				// Get the id of the event and add as an attribute in case we need the new id:
				long lEventId = form.getEvent().getId();
				
				a_request.setAttribute(CommonConstants.k_sIDParam, new Long(lEventId));
				afForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY); 
			}
			else
			{
				afForward = a_mapping.findForward(CommonConstants.FAILURE_KEY); 				
			}
		}
		catch (Bn2Exception bn2)
		{
			m_logger.error("Bn2Exception in SaveEventAction: " + bn2.getMessage());
			afForward = a_mapping.findForward(CommonConstants.SYSTEM_FAILURE_KEY); 
		}
		catch (ParseException pe)
		{
			m_logger.error("ParseException in SaveEventAction: " + pe.getMessage());
			afForward = a_mapping.findForward(CommonConstants.SYSTEM_FAILURE_KEY); 
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
