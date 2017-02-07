package com.bright.framework.user.bean;

/**
 * Bright Interactive, Role
 *
 * Copyright 2002 Bright Interactive, All Rights Reserved.
 * Role.java
 *
 * Contains the Role class.
 */
/*
Ver  Date	    Who			Comments
--------------------------------------------------------------------------------
d1   16-Jan-2004    Matt Stevenson      Created
--------------------------------------------------------------------------------
*/

import com.bright.framework.database.bean.DataBean;

/**
 * Represents an User Role
 *
 * @author  Bright Interactive
 * @version d1
 */
public class UserType extends DataBean
{
    private String m_sName = null;
    private String m_sDescription = null;

    public void setName(String a_sName)
    {
        m_sName = a_sName;
    }

    public String getName()
    {
        return (m_sName);
    }

    public void setDescription(String a_sDescription)
    {
        m_sDescription = a_sDescription;
    }

    public String getDescription()
    {
        return (m_sDescription);
    }
}