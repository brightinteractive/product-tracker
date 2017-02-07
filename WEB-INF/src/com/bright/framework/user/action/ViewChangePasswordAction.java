package com.bright.framework.user.action;

/**
 * Bright Interactive
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * ViewChangePasswordAction
 *
 * Contains the ViewChangePasswordAction class.
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

/**
 * Called to shows the page to allow a user to change their password.
 *
 * @author  bright interactive
 * @version d1
 */
public class ViewChangePasswordAction extends Bn2Action
{

	/**
	 * The execute method of the Action.
	 * Attempts to login the use	 *
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
		afForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY);
		
		return (afForward);
	}

}