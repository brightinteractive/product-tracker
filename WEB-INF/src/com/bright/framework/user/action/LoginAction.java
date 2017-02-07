package com.bright.framework.user.action;


/**
 * Bright Interactive
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * RTLoginAction.java
 *
 * Contains the PTLoginAction class.
 *
/*
Ver  Date				Who					Comments
--------------------------------------------------------------------------------
d1   03-Jun-2004		Martin Wilson     Created
d2   27-Jul-2004     Martin Wilson     Added ignorePasswordIfAdmin
d3   18-Jan-2005     Martin Wilson     Added to PT project
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
import com.bright.framework.user.constant.UserConstants;
import com.bright.framework.user.exception.AccountSuspendedException;
import com.bright.framework.user.exception.InvalidLoginException;
import com.bright.framework.user.form.LoginForm;
import com.bright.framework.user.service.UserManager;

/**
 * Called to log in an admin user.
 *
 * @author  Bright Interactive
 * @version d1
 */
public class LoginAction extends Bn2Action implements CommonConstants, UserConstants
{
    protected UserManager m_userManager = null;

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
    public ActionForward execute(ActionMapping a_mapping,
                                 ActionForm a_form,
                                 HttpServletRequest a_request,
                                 HttpServletResponse a_response)
                                 throws Bn2Exception
    /*
    ------------------------------------------------------------------------
		d1   03-Jun-2004		Martin Wilson     Created
 	   d2   27-Jul-2004     Martin Wilson     Added ignorePasswordIfAdmin
    ------------------------------------------------------------------------
    */
    {
        m_logger.debug("In LoginAction.execute");

        ActionForward afForward = null;

        // Get the form:
        LoginForm loginForm = (LoginForm)a_form;

        // See if the user cancelled:
        String sSubmitButton = a_request.getParameter(k_sSubmitParamName);
        if ((sSubmitButton != null) && (sSubmitButton.equals(k_sCancelParam)))
        {
            afForward = a_mapping.findForward(CANCEL_KEY);
        }
        else
        {
            try
            {
					// See if the user has logged in successfully:
					m_userManager.login(	a_request.getSession(),
												loginForm.getUsername(),
												loginForm.getPassword(),
												loginForm.getIgnorePasswordIfAdmin());


					// Go to the default:
					afForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY);


            }
            catch (AccountSuspendedException ase)
            {
                loginForm.setAccountSuspended(true);
                afForward = a_mapping.findForward(CommonConstants.FAILURE_KEY);
            }
            catch (InvalidLoginException ile)
            {
                loginForm.setLoginFailed(true);
                afForward = a_mapping.findForward(CommonConstants.FAILURE_KEY);
            }
            catch (Bn2Exception e)
            {
                m_logger.error("Error in LoginAction : "+e.getMessage());
                throw e;
            }
        }
        return (afForward);
    }

     /**
      * Prevent RequestProcessor from calling preProcess - as
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