package com.bright.framework.user.action;

/**
 * Bright Interactive, Reputable Trades
 *
 * Copyright 2003 Bright Interactive, All Rights Reserved.
 * ViewLoginAction.java
 *
 * Contains the ViewLoginAction class.
 */
/*
 Ver  Date				Who						Comments
 --------------------------------------------------------------------------------
 d1  03-Jun-2004		Martin Wilson        Created
 --------------------------------------------------------------------------------
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.bn2web.common.action.Bn2Action;
import com.bn2web.common.constant.CommonConstants;
import com.bn2web.common.exception.Bn2Exception;
import com.bright.framework.user.bean.UserProfile;
import com.bright.framework.user.constant.UserConstants;
import com.bright.framework.user.form.LoginForm;

/**
 * ViewLoginAction
 *
 * @author  Bright Interactive
 * @version d1
 */
public class ViewLoginAction extends Bn2Action
{
	
	public ActionForward execute(ActionMapping a_mapping,
										  ActionForm a_form,
										  HttpServletRequest a_request,
										  HttpServletResponse a_response)
	throws Bn2Exception
	/*
	 Ver  Date           Who             Comments
	 --------------------------------------------------------------------------------
	 d1  03-Jun-2004		Martin Wilson        Created
	 --------------------------------------------------------------------------------
	 */
	{
		// The ActionForward to return when completed:
		ActionForward afForward = null;
		
		LoginForm loginForm = (LoginForm)a_form;
		
		// Call get on the user profile to set the attribute:
		UserProfile.getUserProfile(a_request);
		
		// See if we have a URL to redirect to:
		String sRedirectUrl = a_request.getParameter(UserConstants.k_sPostLoginRedirectParam);
		
		loginForm.setForwardUrl(sRedirectUrl);
		
		afForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY);
		
		return (afForward);
	}
	
	/**
	 * Prevent ETARequestProcessor from calling preProcess - as
	 * that checks that the user is logged in!
	 *
	 * @return  boolean
	 */
	public boolean doPreprocessing()
	/*
	 ------------------------------------------------------------------------
	 d1   15-Jun-2004  Martin Wilson    Created.
	 ------------------------------------------------------------------------
	 */
	{
		return (false);
	}
}
