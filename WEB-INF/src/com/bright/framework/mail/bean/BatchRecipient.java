package com.bright.framework.mail.bean;

/**
 * Bright Interactive
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * BatchEmail.java
 * 
 * Contains the BatchEmail class.
 */
/*
Ver  Date	    Who			Comments
--------------------------------------------------------------------------------
d1   08-Apr-2004    Martin Wilson	Created.
--------------------------------------------------------------------------------
*/

import java.util.HashMap;

/**
 * Represents an email to be sent to one or more recipients.
 *
 * @author  bn2web
 * @version d1
 */
public class BatchRecipient
{
    private String m_sEmailAddress = null;
    private HashMap m_hmPersonalisation = null;    
    

    public void setEmailAddress(String a_sEmailAddress)
    {
        m_sEmailAddress = a_sEmailAddress;
    }
  
    public String getEmailAddress() 
    {
        return (m_sEmailAddress);
    } 
    
    public void setPersonalisation(HashMap a_hmPersonalisation)
    {
        m_hmPersonalisation = a_hmPersonalisation;
    }
  
    public HashMap getPersonalisation() 
    {
        return (m_hmPersonalisation);
    } 


}