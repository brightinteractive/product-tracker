package com.bright.framework.user.action;


/**
 * Bright Interactive
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * LogoutAction.java
 *
 * Contains the LogoutAction class.
 *
/*
Ver  Date	    	Who             Comments
--------------------------------------------------------------------------------
d1   26-May-2004        Martin Wilson   Created
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
import com.bright.framework.user.service.UserManager;

/**
 * Called to logout a user.
 *
 * @author  Bright Interactive
 * @version d1
 */
public class LogoutAction extends Bn2Action implements CommonConstants
{
    private UserManager m_userManager = null;

    /**
    * The execute method of the Action.
    * Logs out the user.
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
     d1   26-May-2004        Martin Wilson   Created
    ------------------------------------------------------------------------
    */
    {
        m_logger.debug("In LogoutAction.execute");

        ActionForward afForward = null;

        // Log out the user:
        m_userManager.logout(a_request.getSession());

        afForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY);

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