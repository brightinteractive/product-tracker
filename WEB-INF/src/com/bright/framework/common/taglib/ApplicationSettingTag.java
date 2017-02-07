/**
 * Bright Interactive, internal
 *
 * Copyright 2002 Bright Interactive, All Rights Reserved.
 * ApplicationSettingTag
 *
 * Contains the ApplicationSettingTag class.
 */
/*
Ver  Date	    		Who				Comments
--------------------------------------------------------------------------------
d1   11-March-2005   Martin Wilson	Created.
--------------------------------------------------------------------------------
 */

package com.bright.framework.common.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.bn2web.common.constant.GlobalSettings;

/**
 * Sets a bean to the value of an application setting.
 * 
 * @author Martin Wilson
 * @version $Revision: 1.1 $
 */

public class ApplicationSettingTag extends BodyTagSupport
{
	protected String m_sId = null;
	protected String m_sSettingName = null;
	
	
	public String getId ()
	{
		return (m_sId);
	}

	public void setId (String a_sId)
	{
		m_sId = a_sId;
	}

	public String getSettingName ()
	{
		return (m_sSettingName);
	}

	public void setSettingName (String a_sSettingName)
	{
		m_sSettingName = a_sSettingName;
	}

	// --------------------------------------------------------- Public Methods

	/**
	 * Check if we need to evaluate the body of the tag
	 *
	 * @exception JspException if a JSP exception has occurred
	 */
	public int doStartTag () throws JspException
	/*
	Ver  Date	    Who			Comments
	--------------------------------------------------------------------------------
	d1   11-March-2005   Martin Wilson	Created.
	--------------------------------------------------------------------------------
	 */
	{
		return (EVAL_BODY_BUFFERED);
	}


	/**
	 * Do nothing...
	 *
	 * @exception JspException if value was defined by an attribute
	 */
	public int doAfterBody () throws JspException
	/*
	Ver  Date	    Who			Comments
	--------------------------------------------------------------------------------
	d1   11-March-2005   Martin Wilson	Created.
	--------------------------------------------------------------------------------
	 */
	{
		return (SKIP_BODY);
	}


	/**
	 * Retrieve the required Vector of ref data and expose it as a scripting variable.
	 *
	 * @exception JspException if a JSP exception has occurred
	 */
	public int doEndTag () throws JspException
	/*
	Ver  Date	    Who			Comments
	--------------------------------------------------------------------------------
	d1   11-March-2005   Martin Wilson	Created.
	--------------------------------------------------------------------------------
	 */
	{
		// Connect to the component:
		String sValue = GlobalSettings.getInstance().getStringSetting(m_sSettingName);
		
		pageContext.setAttribute (m_sId, sValue, PageContext.PAGE_SCOPE);

		// Continue processing this page
		return (EVAL_PAGE);

	}

	/**
	 * Release all allocated resources.
	 */
	public void release ()
	{
		super.release ();
		m_sId = null;
		m_sSettingName = null;
	}

}
