package com.bright.framework.mail.action;


/**
 * Bright Interactive, framework
 *
 * Copyright 2002 Bright Interactive, All Rights Reserved.
 * SendTemplatedEmailAction.java
 * 
 * Contains the SendTemplatedEmailAction class.
 */
/*
Ver  Date         Who               Comments
--------------------------------------------------------------------------------
d1   27-Oct-2003  James Home        Created
d2   18-Mar-2004  Matt Stevenson    Updated to send from multiple templates
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
import com.bright.framework.constant.FrameworkConstants;
import com.bright.framework.mail.service.EmailManager;

/**
 * Sends a templated email, which is constructed by the EmailManager component
 * using a template XML file and a map of form parameters.
 *
 * The success forward (action or page) can be specified by adding a (hidden) parameter
 * to the email form with the name defined by constant k_sFEEDBACKSUCCESSParam.
 * Similarly, the failure forward can be specified using k_sFEEDBACKFAILUREParam.
 *
 * @author  Bright Interactive
 * @version d1
 */
public class SendTemplatedEmailAction extends Bn2Action implements CommonConstants, FrameworkConstants
{

    private EmailManager m_emailManager = null;
    
    /**
    * The execute method of the Action. Sends a templated email.
    * 
    * @param ActionMapping a_mapping - the action mapping.
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
     d1   27-Oct-2003  James Home       Created
     d2   18-Mar-2004  Matt Stevenson   Updated to send from multiple templates
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
        Vector vecTemplates = new Vector();
        
        while (e.hasMoreElements())
        {
            //insert the param into the hash
            String sParamName = (String)e.nextElement();
            String sParam = a_request.getParameter(sParamName);
            
            if(sParam!=null && sParam.length()>k_sMaxEmailParamLength)
            {
               sParam = sParam.substring(0,k_sMaxEmailParamLength);
            }
            
            //check if this is a 'template' param - if so add the Param value to the vector 
            //of templates that need to be sent.
            if ((sParamName.length() >= k_sTemplateParam.length()) && ((sParamName.substring(0, k_sTemplateParam.length())).equals(k_sTemplateParam)))
            {
                vecTemplates.add(sParam);
            }
            else
            {
                params.put(sParamName, sParam);
            }
        }
                 
        //use the email manager to send the templated email
        try
        {
            //iterate through the templates to send...
            for (int i=0; i<vecTemplates.size(); i++)
            {
                //add the current template name as a parameter to the
                //hash map then send the email...
                params.put(k_sTemplateParam, vecTemplates.elementAt(i));
                m_emailManager.sendTemplatedEmail(params);
            }
            
            // Get the success forward from the form parameters
            String sSuccess = (String)params.get(k_sFEEDBACKSUCCESSParam);                        
            
            // If present, use this for forwarding
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
           // Get the failure forward from the form parameters
            String sFailure = (String)params.get(k_sFEEDBACKFAILUREParam);
            
            // If present, use this for forwarding
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