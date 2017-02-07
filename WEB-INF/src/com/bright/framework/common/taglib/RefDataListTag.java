/**
 * Bright Interactive, internal
 *
 * Copyright 2002 Bright Interactive, All Rights Reserved.
 * RefDataListTag.java
 *
 * Contains the RefDataListTag class.
 */
/*
Ver  Date	    Who			Comments
--------------------------------------------------------------------------------
d1   02-June-2004   Martin Wilson	Created.
--------------------------------------------------------------------------------
 */

package com.bright.framework.common.taglib;

import java.lang.reflect.Method;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.avalon.framework.component.Component;
import org.apache.avalon.framework.component.ComponentException;

import com.bn2web.common.exception.Bn2Exception;
import com.bn2web.common.service.GlobalApplication;

/**
 * Gets a Vector of objects from ReferenceDataManager and sets a bean to refer to them.
 *
 * @author Martin Wilson
 * @version $Revision: 1.2 $
 */

public class RefDataListTag extends BodyTagSupport
{
	protected String m_sId = null;
	protected String m_sComponentName = null;
	protected String m_sMethodName = null;
	protected String m_sArgumentValue = null;	

	public String getId ()
	{
		return (m_sId);
	}

	public void setId (String a_sId)
	{
		m_sId = a_sId;
	}

	public String getComponentName ()
	{
		return (m_sComponentName);
	}

	public void setComponentName (String a_sComponentName)
	{
		m_sComponentName = a_sComponentName;
	}

	public String getMethodName ()
	{
		return (m_sMethodName);
	}

	public void setMethodName (String a_sMethodName)
	{
		m_sMethodName = a_sMethodName;
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
	d1   02-June-2004   Martin Wilson	Created.
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
	d1   02-June-2004   Martin Wilson	Created.
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
	d1   02-June-2004   Martin Wilson	Created.
	--------------------------------------------------------------------------------
	 */
	{
		// Get the vector from the manager:
		Vector vecResults = null;

		try
		{
			// Connect to the component:
			Component component = GlobalApplication.getInstance().getComponentManager().lookup(m_sComponentName);

			try
			{
				// We'return only interested in get methods with no paramters:
				Method getMethod = null;
				Object[] oaArgs = null;
				
				// See if there's an argument:
				if (m_sArgumentValue != null && m_sArgumentValue.length() > 0)
				{
					Class[] caArgTypes = {new String().getClass()};
					getMethod = component.getClass().getMethod(m_sMethodName, caArgTypes);
					
					oaArgs = new Object[1];
					oaArgs[0] = m_sArgumentValue;
				}
				else
				{
					// No arguments:
					getMethod = component.getClass().getMethod(m_sMethodName, null);
				}
				
				// Invoke the method, with no args:
				vecResults = (Vector)getMethod.invoke(component, oaArgs);

			}
			catch (Exception e)
			{
				GlobalApplication.getInstance().getLogger().error("RefDataTag: exception: " + e.getMessage());
				throw new JspException (e);
			}
		}
		catch (Bn2Exception bn2e)
		{
			new JspException (bn2e);
		}
		catch (ComponentException e)
		{
			throw new JspException ("Component Exception occured whilst getting component for RefDataListTag tag: "+e.getMessage());
		}

		pageContext.setAttribute (m_sId, vecResults, PageContext.PAGE_SCOPE);

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
		m_sComponentName = null;
		m_sMethodName = null;
		m_sArgumentValue = null;
	}


	/** @return Returns the ArgumentValue. */
	public String getArgumentValue()
	{
		return m_sArgumentValue;
	}
	/** @param a_sArgumentValue The ArgumentValue to set. */
	public void setArgumentValue(String a_sArgumentValue)
	{
		m_sArgumentValue = a_sArgumentValue;
	}
}
