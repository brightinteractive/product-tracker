package com.bright.framework.simplelist.form;

import com.bn2web.common.form.Bn2Form;
import com.bright.framework.simplelist.bean.ListValue;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * ListValueForm.java
 *
 * Contains the ListValueForm class.
 */

/*
 Ver	Date				Who				Comments
 --------------------------------------------------------------------------------
 d1	29-Jan-2005	Martin Wilson		Created
 --------------------------------------------------------------------------------
 */

/**
 * Data for a list value
 * 
 * @author Bright Interactive
 * @version d1
 */
public class ListValueForm extends Bn2Form
{
	private ListValue m_listValue = null;
	private String m_lListId = null;
	
	/** @return Returns the listValue. */
	public ListValue getListValue()
	{
		if (m_listValue == null)
		{
			m_listValue = new ListValue();
		}
		return m_listValue;
	}
	/** @param a_sListValue The listValue to set. */
	public void setListValue(ListValue a_listValue)
	{
		m_listValue = a_listValue;
	}
		
	/** @return Returns the listId. */
	public String getListId()
	{
		return m_lListId;
	}
	/** @param a_sListId The listId to set. */
	public void setListId(String a_sListId)
	{
		m_lListId = a_sListId;
	}
}
