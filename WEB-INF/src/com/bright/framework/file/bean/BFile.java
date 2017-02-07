package com.bright.framework.file.bean;

import com.bright.framework.database.bean.DataBean;

/**
 * bright interactive, BFile
 *
 * Copyright 2002 bn2web, All Rights Reserved.
 * BFile.java
 * 
 * Contains the BFile class.
 */
/*
Ver  Date	    Who			Comments
--------------------------------------------------------------------------------
d1   08-Oct-2003    Matt Stevenson      Created
d2   16-Jan-2004    Matt Stevenson      Imported into merchandise manager
d3   17-Jan-2004    Martin Wilson       Moved into 'file' package.
d4	  15-Oct-2004	  Matt Stevenson		 Imported into rt framework
d5	  20-Oct-2004	  Matt Stevenson		 Changed to extend DataBean
--------------------------------------------------------------------------------
*/

/**
 * Represents an File
 *
 * @author  bright interactive
 * @version d1
 */
public class BFile extends DataBean
{
    
    protected long m_lId = 0;
    protected String m_sUrl = null;
    
    public long getId()
    {
        return (m_lId);
    }
    
    public void setId(long a_lId)
    {
        m_lId = a_lId;
    }
    
    public String getUrl()
    {
        return (m_sUrl);
    }
    
    public void setUrl(String a_sUrl)
    {
        m_sUrl = a_sUrl;
    }
}