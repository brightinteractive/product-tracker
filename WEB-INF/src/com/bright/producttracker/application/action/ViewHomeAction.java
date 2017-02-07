package com.bright.producttracker.application.action;

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
 * ViewHomeAction.java
 *
 * Contains the ViewHomeAction class.
 */

/*
 Ver	Date				Who				Comments
 --------------------------------------------------------------------------------
 d1	18-Jan-2005	Martin Wilson		Created
 --------------------------------------------------------------------------------
 */

/**
 * Shows the homepage
 * 
 * @author Bright Interactive
 * @version d1
 */
public class ViewHomeAction extends Bn2Action
{
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
    d1	18-Jan-2005	Martin Wilson		Created
    ------------------------------------------------------------------------
    */
    {

        ActionForward afForward = null;

        afForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY);
        
        return (afForward);
    }
}
