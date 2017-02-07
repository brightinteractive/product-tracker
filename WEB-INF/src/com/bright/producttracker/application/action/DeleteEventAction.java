package com.bright.producttracker.application.action;

import com.bn2web.common.exception.Bn2Exception;
import com.bright.framework.database.bean.DBTransaction;
import com.bright.producttracker.product.service.ProductManager;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * DeleteEventAction.java
 *
 * Contains the DeleteEventAction class.
 */

/*
 Ver	Date				Who				Comments
 --------------------------------------------------------------------------------
 d1	03-Feb-2005	Martin Wilson		Created
 --------------------------------------------------------------------------------
 */

/**
 * Removes event dependencies before deleting an event
 * 
 * @author Bright Interactive
 * @version d1
 */
public class DeleteEventAction extends com.bright.producttracker.event.action.DeleteEventAction
{
	protected ProductManager m_productManager = null;
	
	/**
	 * Implement this in a subclass to remove any dependencies.
	 * 
	 * @param a_dbTransaction
	 * @param a_lValueId
	 */
	protected void removeDependencies(DBTransaction a_dbTransaction, 
												 long a_lEventId)
	throws Bn2Exception
	/*
	---------------------------------------------------------------
	d1		03-Feb-2005		Martin Wilson		Created 
	-----------------------------------------------------------------
	*/	
	{
		m_productManager.removeEventReferences(a_dbTransaction, a_lEventId);
	}	
	
	public void setProductManager(ProductManager a_productManager)
	{
		m_productManager = a_productManager;
	}
}
