package com.bright.framework.mail.action;


/**
 * Bright Interactive
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * MailFormAction
 *
 * Contains the MailFormAction class.
 */
/*
 Ver  Date         Who               Comments
 --------------------------------------------------------------------------------
 d1   27-Jan-2005  Martin Wilson     Created
 d2	31-Mar-2005	 Matt Stevenson    Modified to allow multiple emails to be sent
 --------------------------------------------------------------------------------
 */

import java.util.Enumeration;
import java.util.HashMap;
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
import com.bn2web.common.form.Bn2Form;
import com.bright.framework.constant.FrameworkConstants;
import com.bright.framework.mail.service.EmailManager;

/**
 * Action for sending form parameters to an email address
 * 
 * @author  Bright Interactive
 * @version d1
 */
public class MailFormAction extends Bn2Action implements FrameworkConstants
{   
	protected EmailManager m_emailManager = null;
	protected HashMap m_hmParams = new HashMap();
	
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
	 d1   27-Jan-2005  Martin Wilson		Created
	 d2	31-Mar-2005	 Matt Stevenson	Modified to send multiple emails based
	 												on templates
	 ------------------------------------------------------------------------
	 */
	{
		
		ActionForward afForward = null;
		
		m_logger.debug("In MailFormAction.execute");
		
		Vector vecTemplateNames = new Vector();
		
		// See if the form validates:
		if (validate((Bn2Form)a_form, a_request))
		{
		
			// Get all the parameters startwith 'param':
			Enumeration eParameters = a_request.getParameterNames();
			
			while(eParameters.hasMoreElements())
			{
				String sParamName = (String)eParameters.nextElement();
				String sParamValue = null;
				
				if (a_request.getParameter(sParamName) != null)
				{				
					sParamValue = a_request.getParameter(sParamName);
				
					if (sParamName.startsWith(k_sMandatoryFieldPrefix))
					{
						// See if a value has been supplied for the field with this name:
						sParamName = sParamName.substring(k_sMandatoryFieldPrefix.length());
					}
					
					// Add to the hm:
					m_hmParams.put(sParamName, sParamValue);
					
					if (sParamName.startsWith(k_sTemplateParam))
					{
						//add to the list of templates...
						vecTemplateNames.add(sParamValue);
					}
				}
			}
			
			// Put into a hashmap:
			
			// Get the required parameters from the request:
			if(vecTemplateNames.size() <= 0)
			{
				m_logger.error("MailFormAction.execute requires template parameters " + k_sTemplateParam);
				afForward = a_mapping.findForward(CommonConstants.SYSTEM_FAILURE_KEY);
			}
			else
			{
				//send all the templates...
				for (int i=0; i<vecTemplateNames.size(); i++)
				{
					// Add the email parameters for the forward and template
					if (m_hmParams.containsKey(FrameworkConstants.k_sTemplateParam))
					{
						m_hmParams.remove(FrameworkConstants.k_sTemplateParam);
					}
					
					m_hmParams.put(FrameworkConstants.k_sTemplateParam, vecTemplateNames.elementAt(i));
					
					// Send the email
					m_emailManager.sendTemplatedEmail(m_hmParams);
				}
				
				afForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY);
			}
		}
		else
		{
			// Validation failed:
			afForward = a_mapping.findForward(CommonConstants.FAILURE_KEY);			
		}
		return (afForward);
	}
	
	/**
	 * Returns true if the mail form validates ok. Override this in a base class
	 * if necessary.
	 * 
	 * @param a_form
	 * @param a_request
	 * @return
	 */
	/*
	---------------------------------------------------------------
	d1		29-Jan-2005		Martin Wilson		Created 
	d2		31-Mar-2005		Matt Stevenson		Reworked to not require 
	-----------------------------------------------------------------
	*/
	protected boolean validate(Bn2Form a_form,
										HttpServletRequest a_request)
	{
		String sParamName = null;
		String sFieldValue = null;			
		
		// See if any mandatory fields have been provided:
		Enumeration eParameters = a_request.getParameterNames();
		
		while(eParameters.hasMoreElements())
		{
			sParamName = (String)eParameters.nextElement();
			
			// See if this is a mandatory field definition:
			if (sParamName.startsWith(k_sMandatoryFieldPrefix))
			{
				// See if a value has been supplied for the field with this name:
				String sFieldName = sParamName.substring(k_sMandatoryFieldPrefix.length());
				sFieldValue = a_request.getParameter(sParamName);
					
				if (sFieldValue == null || sFieldValue.length()==0)
				{
					// Add the error:
					a_form.addError("Please enter a value for the " + sFieldName + " field.");
				}
			}
		}			
		
		return (!a_form.getHasErrors());
	}
	
	
	/**
	 * Sets the email manager. This method is called in the baseclass when linking in components, 
	 * as specified in the framework config file.
	 * 
	 * @param EmailManager the email manager component to use.
	 */      
	public void setEmailManager(EmailManager a_emailManager)
	/*
	 ------------------------------------------------------------------------
	 d1   27-Jan-2005  Martin Wilson		Create
	 ------------------------------------------------------------------------
	 */                                 
	{
		m_emailManager = a_emailManager; 
	} 
}