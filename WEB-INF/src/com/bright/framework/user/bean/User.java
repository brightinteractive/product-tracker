package com.bright.framework.user.bean;

/**
 * Bright Interactive, User
 *
 * Copyright 2002 Bright Interactive, All Rights Reserved.
 * User.java
 *
 * Contains the User class.
 */
/*
 Ver  Date	    		Who					Comments
 --------------------------------------------------------------------------------
 d1   04-Oct-2004    	Chris Preager     Created
 --------------------------------------------------------------------------------
 */

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.bright.framework.database.bean.DataBean;

/**
 * Represents a User
 *
 * @author  Bright Interactive
 * @version d1
 */
public class User extends DataBean
{

	private String m_sUsername = null;
	private String m_sPassword = null;
	private boolean m_bIsSuspended = false;

	private UserType m_userType = null;
	private HashMap m_hmPermissions = null;

	/*
	 * Adds a permission to the user.
	 *
	 *@param Permission a_permission - the permission to add
	 */
	public void addPermission(Permission a_permission)
	/*
	 ------------------------------------------------------------------------
	 d1   24-May-2004    Martin Wilson       Created.
	 ------------------------------------------------------------------------
	 */
	{
		if(m_hmPermissions == null)
		{
			m_hmPermissions = new HashMap(20);
		}

		m_hmPermissions.put(new Long(a_permission.getId()), a_permission);
	}

	/*
	 * Returns true if the user has this permission
	 *
	 *@param  long a_lRoleID  - a constant representing the role in question.
	 *@return boolean
	 */
	public boolean hasPermission(long a_lPermissionId)
	/*
	 ------------------------------------------------------------------------
	 d2   22-Jan-2004    Martin Wilson       Created.
	 ------------------------------------------------------------------------
	 */
	{
		boolean bHasPermission = false;

		if(m_hmPermissions != null)
		{
			if(m_hmPermissions.containsKey(new Long(a_lPermissionId)))
			{
				bHasPermission = true;
			}
		}

		return (bHasPermission);
	}

	/*
	 * Returns all the permissions
	 *
	 *@return Collection
	 */
	public Collection getPermissions()
	/*
	 ------------------------------------------------------------------------
	 d2   26-Jan-2004    Martin Wilson       Created.
	 d3   21-Jun-2004    Chris Preager      Added checks in getPermission functions for m_hmPermissions == null
	 ------------------------------------------------------------------------
	 */
	{
		if(m_hmPermissions == null)
		{
			return (null);
		}

		return (m_hmPermissions.values());
	}

	/*
	 * Returns the first permission that this user has.
	 *
	 *@return Permission
	 */
	public Permission getFirstPermission()
	/*
	 ------------------------------------------------------------------------
	 d2   16-Jun-2004    Martin Wilson      Added getFirstPermission
	 d3   21-Jun-2004    Chris Preager      Added checks in getPermission functions for m_hmPermissions == null
	 ------------------------------------------------------------------------
	 */
	{
		if(m_hmPermissions == null)
		{
			return (null);
		}

		Iterator it = m_hmPermissions.values().iterator();
		return ((Permission) it.next());

	}

	public void setUsername(String a_sUsername)
	{
		m_sUsername = a_sUsername;
	}
	public String getUsername()
	{
		return (m_sUsername);
	}
	public void setPassword(String a_sPassword)
	{
		m_sPassword = a_sPassword;
	}
	public String getPassword()
	{
		return (m_sPassword);
	}

	public UserType getUserType()
	{
		if (m_userType == null)
		{
			m_userType = new UserType();
		}
		return m_userType;
	}
	public void setUserType(UserType a_userType)
	{
		m_userType = a_userType;
	}
	public boolean getIsSuspended()
	{
		return m_bIsSuspended;
	}
	public void setIsSuspended(boolean a_bIsSuspended)
	{
		m_bIsSuspended = a_bIsSuspended;
	}
}