package com.bright.framework.user.form;

/**
 * Bright Interactive, framework
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * RTLoginForm.java
 *
 * Contains the RTLoginForm class.
 */
/*
Ver  Date               Who			Comments
--------------------------------------------------------------------------------
d1   21-Oct-2003        Martin Wilson          Created from Img Mangr
d2   16-Feb-2004        Matt Stevenson         Added redirect id
d3   16-Mar-2004        Martin Wilson          Added forwardUrl property. Removed redirect id.
d4   27-Jul-2004        Martin Wilson          Added ignorePasswordIfAdmin
--------------------------------------------------------------------------------
*/


import org.apache.struts.action.ActionForm;

/**
 * Form to store objects to do with admin functions.
 *
 * @author  Bright Interactive
 * @version d1
 */
public class LoginForm extends ActionForm
{
    private String m_sUsername = null;
    private String m_sPassword = null;
    private boolean m_bLoginFailed = false;
    private boolean m_bAccountSuspended = false;
    private String m_sForwardUrl = null;
	 private boolean m_bIgnorePasswordIfAdmin = false;
	 
    /*
     * Gets the username entered by the user.
     *
     * @return String -  the username entered by the user.
     */
    public String getUsername()
     /*
    ------------------------------------------------------------------------
     d1   21-Oct-2003   Martin Wilson		Created from Img Mangr.
    ------------------------------------------------------------------------
    */
    {
        return (m_sUsername);
    }

    /*
     * Set the username entered by the user.
     *
     * @param String a_sUsername -  the username entered by the user..
     */
    public void setUsername(String a_sUsername)
     /*
    ------------------------------------------------------------------------
     d1   21-Oct-2003   Martin Wilson		Created from Img Mangr.
    ------------------------------------------------------------------------
    */
    {
        m_sUsername = a_sUsername;
    }

    /*
     * Gets the password entered by the user.
     *
     * @return String -  the password entered by the user.
     */
    public String getPassword()
     /*
    ------------------------------------------------------------------------
     d1   21-Oct-2003   Martin Wilson		Created from Img Mangr.
    ------------------------------------------------------------------------
    */
    {
        return (m_sPassword);
    }

    /*
     * Set the password entered by the user.
     *
     * @param String a_sPassword -  the password entered by the user..
     */
    public void setPassword(String a_sPassword)
     /*
    ------------------------------------------------------------------------
     d1   21-Oct-2003   Martin Wilson		Created from Img Mangr.
    ------------------------------------------------------------------------
    */
    {
        m_sPassword = a_sPassword;
    }

    public void setLoginFailed(boolean a_bLoginFailed)
    {
        m_bLoginFailed = a_bLoginFailed;
    }

    public boolean getLoginFailed()
    {
        return (m_bLoginFailed);
    }
    
    public void setAccountSuspended(boolean a_bAccountSuspended)
    {
        m_bAccountSuspended = a_bAccountSuspended;
    }

    public boolean getAccountSuspended()
    {
        return (m_bAccountSuspended);
    }
    
    public void setForwardUrl(String a_sForwardUrl)
    {
        m_sForwardUrl = a_sForwardUrl;
    }
  
    public String getForwardUrl() 
    {
        return (m_sForwardUrl);
    } 
	 
	 public boolean getIgnorePasswordIfAdmin()
	 {
		 return (m_bIgnorePasswordIfAdmin);
	 }
	 
	 public void setIgnorePasswordIfAdmin(boolean a_bIgnorePasswordIfAdmin)
	 {
		 m_bIgnorePasswordIfAdmin = a_bIgnorePasswordIfAdmin;
	 }
	 
}