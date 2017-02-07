package com.bright.producttracker.application.action;

import com.bn2web.common.exception.Bn2Exception;
import com.bright.framework.database.bean.DBTransaction;
import com.bright.producttracker.product.service.ProductManager;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * DeleteListValue.java
 *
 * Contains the DeleteListValue class.
 */

/*
 Ver	Date				Who				Comments
 --------------------------------------------------------------------------------
 d1	03-Feb-2005	Martin Wilson		Created
 --------------------------------------------------------------------------------
 */

/**
 * Overrides the framework action to remove dependencies.
 * 
 * @author Bright Interactive
 * @version d1
 */
public class DeleteListValue extends com.bright.framework.simplelist.action.DeleteListValue
{
	protected ProductManager m_productManager = null;
	
	/**
	 * Implement this in a subclass to remove any dependencies.
	 * 
	 * @param a_dbTransaction
	 * @param a_lValueId
	 */
	protected void removeDependencies(DBTransaction a_dbTransaction, 
												 long a_lValueId)
	throws Bn2Exception
	/*
	---------------------------------------------------------------
	d1		03-Feb-2005		Martin Wilson		Created 
	-----------------------------------------------------------------
	*/	
	{
		m_productManager.removeListValueReferences(a_dbTransaction, a_lValueId);
	}	
	
	public void setProductManager(ProductManager a_productManager)
	{
		m_productManager = a_productManager;
	}
}
