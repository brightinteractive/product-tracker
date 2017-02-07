package com.bright.framework.mail.bean;

/**
 * Bright Interactive
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * EmailDetails.java
 * 
 * Contains the EmailDetails class.
 */
/*
Ver  Date	    Who			Comments
--------------------------------------------------------------------------------
d1   08-Apr-2004    Martin Wilson	Created.
--------------------------------------------------------------------------------
*/

import java.util.Vector;

/**
 * Contains details of an email to be sent to one or more recipients.
 *
 * @author  bn2web
 * @version d1
 */
public class BatchEmail
{
    private Vector m_vecRecipients = new Vector();   
    private String m_sSubject = null;
    private String m_sBody = null;  
    private String m_sDirectoryContainingHtmlFile = null;
    private String m_sHtmlFilename = null;     
    private String m_sFromAddress = null; 

    /*
    * Adds a recipient to the batch
    *
    *@param BatchRecipient a_recipient

    */   
    public void addRecipient(BatchRecipient a_recipient)
    /*
    ------------------------------------------------------------------------
    d6  29-Mar-2004         Martin Wilson     Added for backward compatibility.
    ------------------------------------------------------------------------
    */
    {    
        m_vecRecipients.add(a_recipient);
    }
    
    public Vector getRecipients()
    {
        return (m_vecRecipients);
    }
 
    public void setSubject(String a_sSubject)
    {
        m_sSubject = a_sSubject;
    }
  
    public String getSubject() 
    {
        return (m_sSubject);
    } 
    
    public void setBody(String a_sBody)
    {
        m_sBody = a_sBody;
    }
  
    public String getBody() 
    {
        return (m_sBody);
    } 

    public void setHtmlFilename(String a_sHtmlFilename)
    {
        m_sHtmlFilename = a_sHtmlFilename;
    }
  
    public String getHtmlFilename() 
    {
        return (m_sHtmlFilename);
    } 
    
    public void setDirectoryContainingHtmlFile(String a_sDirectoryContainingHtmlFile)
    {
        m_sDirectoryContainingHtmlFile = a_sDirectoryContainingHtmlFile;
    }
  
    public String getDirectoryContainingHtmlFile() 
    {
        return (m_sDirectoryContainingHtmlFile);
    }     
    
    public void setFromAddress(String a_sFromAddress)
    {
        m_sFromAddress = a_sFromAddress;
    }
  
    public String getFromAddress() 
    {
        return (m_sFromAddress);
    }   
}