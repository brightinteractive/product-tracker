package com.bright.framework.simplelist.form;

import java.util.Vector;

import com.bn2web.common.form.Bn2Form;
import com.bright.framework.simplelist.bean.List;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * ListForm.java
 *
 * Contains the ListForm class.
 */

/*
 Ver	Date				Who				Comments
 --------------------------------------------------------------------------------
 d1	29-Jan-2005	Martin Wilson		Created
 --------------------------------------------------------------------------------
 */

/**
 * Holds lists
 * 
 * @author Bright Interactive
 * @version d1
 */
public class ListForm extends Bn2Form
{
	private List m_list = null;
	private Vector m_vecList = null;
	
	/** @return Returns the vecList. */
	public Vector getListItems()
	{
		return m_vecList;
	}
	/** @param a_sVecList The vecList to set. */
	public void setListItems(Vector a_vecList)
	{
		m_vecList = a_vecList;
	}
	/** @return Returns the list. */
	public List getList()
	{
		return m_list;
	}
	/** @param a_sList The list to set. */
	public void setList(List a_sList)
	{
		m_list = a_sList;
	}
}
