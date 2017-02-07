package com.bright.framework.database.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bn2web.common.exception.Bn2Exception;
import com.bn2web.common.service.Bn2Manager;

/**
 * Bright Interactive, stewart-jms
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * TransactionManager.java
 *
 * Contains the TransactionManager class.
 */

/*
 Ver	Date				Who				Comments
 --------------------------------------------------------------------------------
 d1	Sep 8, 2004		James Home		Created
 --------------------------------------------------------------------------------
 */

/**
 * A superclass for managers that are primarily concerned with database access
 * 
 * @author Bright Interactive
 * @version d1
 */
public abstract class TransactionManager extends Bn2Manager
{
	private DBTransactionManager m_transactionManager = null;

	/**
	 * Returns the last auto-increment id from the specified table name in this connection
	 * (transaction)
	 * 
	 * @param a_con : The current connection
	 * @param a_sTableName : The table from which the id should be retrieved
	 * @return : The last inserted id 
	 * @throws SQLException
	 * @throws Bn2Exception
	 */	
	protected long getLastInsertId(Connection a_con, String a_sTableName)
	throws SQLException, Bn2Exception
	/*
	---------------------------------------------------------------
	d1		Sep 8, 2004		James Home		Created 
	-----------------------------------------------------------------
	*/
	{
		String sSQL = null;
		PreparedStatement psql = null;
		ResultSet rs = null; 
			
		if(a_con==null)
		{
			throw new Bn2Exception("TransactionManager.getLastInsertId : passed connection is null.");
		}
		
		sSQL = "SELECT LAST_INSERT_ID() FROM " + a_sTableName;
		psql = a_con.prepareStatement(sSQL);
		rs = psql.executeQuery();
		
		if(rs.next())
		{
			return rs.getLong("LAST_INSERT_ID()");
		}
		
		throw new Bn2Exception("TransactionManager.getLastInsertId : could not get LAST_INSERT_ID() for " + a_sTableName);
	}
	
	/**
	 * @return  Returns the transactionManager. 
	 */
	public DBTransactionManager getTransactionManager()
	{
		return m_transactionManager;
	}

	/**
	 * @param a_sTransactionManager  The transactionManager to set. 
	 */
	public void setTransactionManager(DBTransactionManager a_sTransactionManager)
	{
		m_transactionManager = a_sTransactionManager;
	}
	
}
