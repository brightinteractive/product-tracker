package com.bright.framework.mail.bean;

/**
 * bn2web
 *
 * Copyright 2002 bn2web, All Rights Reserved.
 * EmailPreview.java
 * 
 * Contains the EmailPreview class.
 */
/*
Ver  Date           Who          Comments
--------------------------------------------------------------------------------
d1   04-Mar-2003    James Home	Created.
--------------------------------------------------------------------------------
*/


/**
 * Represents an email preview.
 *
 * @author  bn2web
 * @version d1
 */
public class EmailPreview
{
    private String m_sSubject = null;    
    private String m_sBody = null;

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
}