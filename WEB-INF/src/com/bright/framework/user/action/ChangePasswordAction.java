package com.bright.framework.user.action;

/**
 * Bright Interactive
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * ChangePasswordAction.java
 *
 * Contains the ChangePasswordAction class.
 *
 /*
 Ver  Date	    		Who					Comments
 --------------------------------------------------------------------------------
 d1   04-Feb-2005   	Martin Wilson		Created from ETA
 --------------------------------------------------------------------------------
 */

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
import com.bright.framework.user.bean.UserProfile;
import com.bright.framework.user.form.ChangePasswordForm;
import com.bright.framework.user.service.UserManager;

/**
 * Called to change a user's password.
 *
 * @author  bright interactive
 * @version d1
 */
public class ChangePasswordAction extends Bn2Action
{
	private UserManager m_userManager = null;

	/**
	 * The execute method of the Action.
	 * Attempts to login the user
	 *
	 * @param ActionMapping a_mapping - the action mapping [explain it here].
	 * @param ActionForm a_form - the action form- in this case containing the login info entered by user.
	 * @param HttpServletRequest a_request - the request object.
	 * @param HttpServletResponse a_response - the response object.
	 * @return ActionForward - where to forward or redirect to when completed.
	 * @throws	ServletException    Servlet Exception
	 */
	public ActionForward execute(	ActionMapping a_mapping,
											ActionForm a_form,
											HttpServletRequest a_request,
											HttpServletResponse a_response)
	throws Bn2Exception
	/*
	 ------------------------------------------------------------------------
	 d1   04-Feb-2005   Martin Wilson		Created from ETA
	 ------------------------------------------------------------------------
	 */
	{
		ActionForward afForward = null;
		ChangePasswordForm passwordForm = (ChangePasswordForm) a_form;
		UserProfile userProfile = UserProfile.getUserProfile(a_request.getSession());
		boolean bError = false;

		// See if the user hit cancel:
		String sCancel = a_request.getParameter(FrameworkConstants.k_sCancelParam);

		if (sCancel != null && sCancel.length() > 0)
		{
			// Yes, so go to the cancel forward:
			return (a_mapping.findForward (FrameworkConstants.CANCEL_KEY));			
		}

		long lUserId = 0;
		
		if (passwordForm.getUserId() > 0)
		{
			lUserId = passwordForm.getUserId();
		}
		else
		{
			lUserId = userProfile.getUser().getId();
		}

		// Check if the user entered values:
		if((passwordForm.getOldPassword() == null || passwordForm.getOldPassword().trim().length() == 0) || (passwordForm.getNewPassword() == null || passwordForm.getNewPassword().trim().length() == 0) || (passwordForm.getConfirmNewPassword() == null || passwordForm.getConfirmNewPassword().trim().length() == 0))
		{
			passwordForm.addError("The password cannot be empty - please enter a value in all fields.");
			bError = true;
		}
		else if(!passwordForm.getNewPassword().trim().equals(passwordForm.getConfirmNewPassword().trim()))
		{
			passwordForm.addError("The two new passwords you have entered do not match.");
			bError = true;
		}

		if(!bError)
		{
			try
			{
				// Attempt to change the password:
				bError = !m_userManager.changePassword(lUserId, passwordForm.getOldPassword().trim(),
																	passwordForm.getNewPassword().trim());

			}
			catch(Bn2Exception e)
			{
				passwordForm.addError("The old password you entered is incorrect.");
				bError = true;
			}
		}

		// See where to direct:
		if(bError)
		{
			afForward = a_mapping.findForward(CommonConstants.FAILURE_KEY);
		}
		else
		{
			afForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY);
		}
		return (afForward);
	}

	/**
	 * Sets the user manager. This method is called in the baseclass when linking in components,
	 * as specified in the framework config file.
	 *
	 * @param UserManager the user manager component to use.
	 */
	public void setUserManager(UserManager a_userManager)
	{
		m_userManager = a_userManager;
	}
}