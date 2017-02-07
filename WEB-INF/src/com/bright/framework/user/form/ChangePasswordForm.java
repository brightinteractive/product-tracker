package com.bright.framework.user.form;

/**
 * Bright Interactive, framework
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * ChangePasswordForm.java
 *
 * Contains the ChangePasswordForm class.
 */
/*
Ver  Date               Who			Comments
--------------------------------------------------------------------------------
d1   26-Oct-2003        Martin Wilson          Created.
--------------------------------------------------------------------------------
*/


import com.bn2web.common.form.Bn2Form;

/**
 * Form to change a user's password.
 *
 * @author  Bright Interactive
 * @version d1
 */
public class ChangePasswordForm extends Bn2Form
{
    private long m_lUserId = 0;
    private String m_sOldPassword = null;
    private String m_sNewPassword = null;
    private String m_sConfirmNewPassword = null;
    private boolean m_bChangeFailed = false;

    public void setUserId(long a_lId)
    {
        m_lUserId = a_lId;
    }

    public long getUserId()
    {
        return (m_lUserId);
    }

    /*
     * Gets the OldPassword entered by the user.
     *
     * @return String -  the OldPassword entered by the user.
     */
    public String getOldPassword()
     /*
    ------------------------------------------------------------------------
     d1   21-Oct-2003   Martin Wilson		Created from Img Mangr.
    ------------------------------------------------------------------------
    */
    {
        return (m_sOldPassword);
    }

    /*
     * Set the OldPassword entered by the user.
     *
     * @param String a_sOldPassword -  the OldPassword entered by the user..
     */
    public void setOldPassword(String a_sOldPassword)
     /*
    ------------------------------------------------------------------------
     d1   21-Oct-2003   Martin Wilson		Created from Img Mangr.
    ------------------------------------------------------------------------
    */
    {
        m_sOldPassword = a_sOldPassword;
    }

    /*
     * Gets the password entered by the user.
     *
     * @return String -  the password entered by the user.
     */
    public String getNewPassword()
     /*
    ------------------------------------------------------------------------
     d1   21-Oct-2003   Martin Wilson		Created from Img Mangr.
    ------------------------------------------------------------------------
    */
    {
        return (m_sNewPassword);
    }

    /*
     * Set the password entered by the user.
     *
     * @param String a_sPassword -  the password entered by the user..
     */
    public void setNewPassword(String a_sNewPassword)
     /*
    ------------------------------------------------------------------------
     d1   21-Oct-2003   Martin Wilson		Created from Img Mangr.
    ------------------------------------------------------------------------
    */
    {
        m_sNewPassword = a_sNewPassword;
    }

    /*
     * Gets the password entered by the user.
     *
     * @return String -  the password entered by the user.
     */
    public String getConfirmNewPassword()
     /*
    ------------------------------------------------------------------------
     d1   21-Oct-2003   Martin Wilson		Created from Img Mangr.
    ------------------------------------------------------------------------
    */
    {
        return (m_sConfirmNewPassword);
    }

    /*
     * Set the password entered by the user.
     *
     * @param String a_sPassword -  the password entered by the user..
     */
    public void setConfirmNewPassword(String a_sConfirmNewPassword)
     /*
    ------------------------------------------------------------------------
     d1   21-Oct-2003   Martin Wilson		Created from Img Mangr.
    ------------------------------------------------------------------------
    */
    {
        m_sConfirmNewPassword = a_sConfirmNewPassword;
    }

    public void setChangeFailed(boolean a_bChangeFailed)
    {
        m_bChangeFailed = a_bChangeFailed;
    }

    public boolean getChangeFailed()
    {
        return (m_bChangeFailed);
    }
}