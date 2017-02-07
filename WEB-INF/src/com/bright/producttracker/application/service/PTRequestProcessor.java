package com.bright.producttracker.application.service;

/**
 * Bright Interactive, internal
 *
 * Copyright 2002 bn2web, All Rights Reserved.
 * PTRequestProcessor.java
 *
 * Contains the PTRequestProcessor class.
 */
/*
Ver		Date			Who					Comments
--------------------------------------------------------------------------------
d1		18-Jan-2005		Martin Wilson		Created.
d2		11-Jan-2008		Matt Stevenson		Modified to allow for signon without login
--------------------------------------------------------------------------------
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.bn2web.common.constant.CommonConstants;
import com.bn2web.common.exception.Bn2Exception;
import com.bn2web.common.service.Bn2RequestProcessor;
import com.bn2web.common.service.GlobalApplication;
import com.bright.framework.user.bean.UserProfile;
import com.bright.framework.user.service.UserManager;
import com.bright.framework.util.StringUtil;
import com.bright.producttracker.application.constant.ApplicationConstants;

/**
 * Overrides the bn2 RequestProcessor - so that we can check the user is logged in.
 *
 * @author  bn2web
 * @version d1
 */
public class PTRequestProcessor extends Bn2RequestProcessor
{	
	private UserManager m_userManager = null;
	
	/**
	 * Check the the user is logged in: if not returns a 'no permission' exception.
	 *
	 * @param HttpServletRequest request
	 * @param HttpServletResponse response
	 * @param Action action - the Action to process this request
	 * @param ActionForm formInstance
	 * @param ActionMapping mapping
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet exception occurs
	 */
	protected ActionForward preProcessActionPerform (	HttpServletRequest a_request,
																		HttpServletResponse a_response,
																		Action a_action,
																		ActionForm a_formInstance,
																		ActionMapping a_mapping)
	throws IOException, ServletException
	/*
	------------------------------------------------------------------------
	  d1	18-Jan-2005		Martin Wilson		Created.
	  d2	11-Jan-2008		Matt Stevenson		Modified to allow for signon without login
	------------------------------------------------------------------------
	*/
	{
		ActionForward forward = null;
		
		// Get the profile:
		UserProfile userProfile = UserProfile.getUserProfile (a_request);
			
		// See if the user needs to log in:
		if (!userProfile.getIsLoggedIn())
		{
			//check for a code to signon without login...
			String sCode = a_request.getParameter(ApplicationConstants.k_sParam_Code);
			boolean bLoginWithoutSignon = false;
			
			if (StringUtil.stringIsPopulated(sCode))
			{
				bLoginWithoutSignon = this.getUserManager().canSignonWithoutLogin(sCode);
				
				//login the user...
				if (bLoginWithoutSignon)
				{
					try
					{
						m_userManager.login(a_request.getSession(), ApplicationConstants.k_sAdminUsername, null, true);
					}
					catch (Bn2Exception e)
					{
						GlobalApplication.getInstance().getLogger().error("PTRequestProcess.preProcessActionPerform: Error: "+e.getMessage());
						bLoginWithoutSignon = false;
					}
				}
			}
			
			if (!bLoginWithoutSignon)
			{
				forward = a_mapping.findForward (CommonConstants.NO_PERMISSION_KEY);
			}
		}		
		// Else return null, which continues.
		
		return (forward);
	}	
	
	private UserManager getUserManager ()
	{
		if (m_userManager == null)
		{
			initComponents();
		}
		
		return (m_userManager);
	}
	
	private void initComponents() 
	{
		try
		{
			m_userManager = (UserManager)GlobalApplication.getInstance().getComponentManager().lookup("UserManager");
		}
		catch (Exception e)
		{
			GlobalApplication.getInstance().getLogger().error("PTRequestProcess.initComponents(): "+e.getMessage());
		}
	}
}
