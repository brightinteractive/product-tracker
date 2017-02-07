package com.bright.producttracker.product.action;

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
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * ViewGenerateReportAction
 *
 * Contains the ViewGenerateReportAction class.
 */

/*
 Ver	Date				Who					Comments
 --------------------------------------------------------------------------------
 d1	14-Jun-2005		Matt Stevenson		Created
 --------------------------------------------------------------------------------
 */

/**
 * Used to view the generate a report page
 * 
 * @author Bright Interactive
 * @version d1
 */
public class ViewGenerateReportAction extends Bn2Action
{
	/**
	 * The execute method of the Action.
	 *
	 * @param ActionMapping a_mapping - the action mapping 
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
	 d1	14-Jun-2005		Matt Stevenson				Created
	 ------------------------------------------------------------------------
	 */
	{
		// Go to the default:
		ActionForward afForward = null;
		
		//forward to the success page
		afForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY); 			
		
		return (afForward);
	}
}
