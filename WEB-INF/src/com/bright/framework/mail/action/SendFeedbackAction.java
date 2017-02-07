package com.bright.framework.mail.action;


/**
 * bn2web, framework
 *
 * Copyright 2002 bn2web, All Rights Reserved.
 * SendFeedbackAction.java
 * 
 * Contains the SendFeedbackAction class.
 */
/*
Ver  Date	    Who			Comments
--------------------------------------------------------------------------------
d1   29-Apr-2003     Matt Stevenson	Created.
d2   30-Apr-2003     Matt Stevenson     Modified to check for forward in the 
                                        params before going to the default one
--------------------------------------------------------------------------------
*/

import java.util.Enumeration;
import java.util.HashMap;

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
import com.bright.framework.mail.service.EmailManager;

/**
 * Sends the Feedback Email and displays the thank you page.
 *
 * @author  bn2web
 * @version d1
 */
public class SendFeedbackAction extends Bn2Action implements CommonConstants, FrameworkConstants
{

    private EmailManager m_emailManager = null;
    
    /**
    * The execute method of the Action.
    * Gets the user details.
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
     d1   29-Apr-2003   Matt Stevenson    Created.
     d2   30-Apr-2003   Matt Stevenson    Modified forwarding method
    ------------------------------------------------------------------------
    */                                 
    {
        // The ActionForward to return when completed:
        ActionForward sForward = null;

        // Replace this with logging call:
        m_logger.debug("In SendFeedbackAction.execute");

        //get the names of all parameters and use them to construct a hash of the results
        Enumeration e = a_request.getParameterNames();
        HashMap params = new HashMap();
        
        while (e.hasMoreElements())
        {
            //insert the param into the hash
            String sParamName = (String)e.nextElement();
            String sParam = a_request.getParameter(sParamName);
            params.put(sParamName, sParam);
        }
                 
        //use the email manager to send the templatedemail
        try
        {
            m_emailManager.sendFeedbackEmail(params); 
            String sSuccess = (String)params.get(k_sFEEDBACKSUCCESSParam);
            
            if (sSuccess != null)
            {
                sForward = new ActionForward(sSuccess);
            }
            else
            {
                sForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY);
            }
        }
        catch (Bn2Exception bne)
        {
            String sFailure = (String)params.get(k_sFEEDBACKFAILUREParam);
            
            if (sFailure != null)
            {
                sForward = new ActionForward(sFailure);
            }
            else
            {
                sForward = a_mapping.findForward(CommonConstants.FAILURE_KEY);
            }
        }        
        
        return (sForward);
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
     d1   29-Apr-2003  Matt Stevenson    Created.
    ------------------------------------------------------------------------
    */                                 
    {
        m_emailManager = a_emailManager; 
    } 
}