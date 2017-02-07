package com.bright.framework.user.bean;

/**
 * Bright Interactive, Permission
 *
 * Copyright 2002 Bright Interactive, All Rights Reserved.
 * Permission.java
 *
 * Contains the Permission class.
 */
/*
Ver  Date	    Who			Comments
--------------------------------------------------------------------------------
d1   24-May-2004    Martin Wilson      Created
--------------------------------------------------------------------------------
*/

import com.bright.framework.database.bean.DataBean;

/**
 * Represents an User Permission
 *
 * @author  Bright Interactive
 * @version d1
 */
public class Permission extends DataBean
{
    private String m_sDescription = null;

    public void setDescription(String a_sDescription)
    {
        m_sDescription = a_sDescription;
    }

    public String getDescription()
    {
        return (m_sDescription);
    }
}