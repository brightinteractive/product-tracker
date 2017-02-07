package com.bright.framework.user.form;

/**
 * Bright Interactive, framework
 *
 * Copyright 2002 Bright Interactive, All Rights Reserved.
 * UserForm.java
 *
 * Contains the UserForm class.
 */
/*
 Ver  Date               Who                 Comments
 --------------------------------------------------------------------------------
 d1   04-Oct-2004        Chris Preager       Created
 --------------------------------------------------------------------------------
 */

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.struts.util.LabelValueBean;

import com.bn2web.common.form.Bn2Form;
import com.bright.framework.user.bean.Permission;
import com.bright.framework.user.bean.User;

/**
 * UserForm
 *
 * @author  Bright Interactive
 * @version d1
 */
public class UserForm extends Bn2Form 
{
	protected User m_user = null;
	private String m_sPassword = null;
	private String m_sConfirmPassword = null;
	private Vector m_vecPermissions = null;
//	private Vector m_vecUserTypes = null;
	private String[] m_asSelectedPermissions = null;
	

	/**
	 * This provides the JSP multibox bean with the required LabelValueBean objects
	 * with which it renders its list of checkboxes.
	 *
	 */
	public LabelValueBean[] getPermissionOptions()
	/*
	 ------------------------------------------------------------------------
	 d1   27-Oct-2004    Chris Preager    Created
	 ------------------------------------------------------------------------
	 */
	{
		LabelValueBean[] alvbPermissionOptions = null;
		if (m_vecPermissions == null)
		{
			alvbPermissionOptions = new LabelValueBean[0];
		}
		else
		{
			alvbPermissionOptions = new LabelValueBean[m_vecPermissions.size()];
			for (int i=0;i<m_vecPermissions.size();i++)
			{
				Permission perm = (Permission)m_vecPermissions.get(i);
				alvbPermissionOptions[i] = new LabelValueBean(perm.getDescription(), Long.toString(perm.getId()));
			}
		}
		return alvbPermissionOptions;
	}
	public void setPermissions(Vector a_vecPermissions)
	{
		m_vecPermissions = a_vecPermissions;
	}
	
	
	public String[] getSelectedPermissions()
	{
		return m_asSelectedPermissions;
	}
	public void setSelectedPermissions(String[] a_asSelectedPermissions)
	{
		m_asSelectedPermissions = a_asSelectedPermissions;
	}
	
	
//	public Vector getUserTypes()
//	{
//		return m_vecUserTypes;
//	}
//	public void setUserTypes(Vector a_vecUserTypes)
//	{
//		m_vecUserTypes = a_vecUserTypes;
//	}


	public void setPassword(String a_sPassword)
	{
		m_sPassword = a_sPassword;
	}
	public String getPassword()
	{
		return (m_sPassword);
	}
	public void setConfirmPassword(String a_sConfirmPassword)
	{
		m_sConfirmPassword = a_sConfirmPassword;
	}
	public String getConfirmPassword()
	{
		return (m_sConfirmPassword);
	}
	public User getUser()
	{
		if (m_user == null)
		{
			m_user = new User();
		}
		return m_user;
	}
	public void setUser(User a_user)
	{
		m_user = a_user;
	}
	/**
	 * Method to populate the selected options String[] from the permissions Collection
	 * 
	 * @param a_colPermissions
	 */
	public void setSelectedPermissions(Collection a_colPermissions)
	/*
	---------------------------------------------------------------
	d1		01-Nov-2004		Chris Preager		Created 
	-----------------------------------------------------------------
	*/
	{
		if (a_colPermissions != null)
		{
			m_asSelectedPermissions = new String[a_colPermissions.size()];
			
			Iterator itPermissions = a_colPermissions.iterator ();
	
			// Now go through and add each Permission
			int i = 0;
			while (itPermissions.hasNext ())
			{
				Permission permission = (Permission)itPermissions.next ();
				m_asSelectedPermissions[i++] = Long.toString(permission.getId());
			}
		}
		else
		{
			m_asSelectedPermissions = null;
		}
	}
}