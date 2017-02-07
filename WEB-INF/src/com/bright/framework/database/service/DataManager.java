package com.bright.framework.database.service;


/**
 * Bright Interactive, Framework
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * DataManager.java
 *
 *
 */
/*
Ver  	Date					Who						Comments
--------------------------------------------------------------------------------
d1  	28-Oct-2004       Matt Stevenson			Created
--------------------------------------------------------------------------------
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;

import com.bn2web.common.service.Bn2Manager;

/**
 *
 * DataManager - base class with helper methods for data access managers
 *
 * @author  Bright Interactive
 * @version d1
 */
public class DataManager extends Bn2Manager
{
	protected DataSourceComponent m_dataSource = null;

	/**
	  * helper method to get last insert id
	  *
	  *@param connection
	  *@return the last insert id
	  */
	protected long getInsertId (Connection a_cCon) throws SQLException
	/*
	------------------------------------------------------------------------
	d1  28-Oct-2004        Matt Stevenson		Created
	------------------------------------------------------------------------
	*/
	{
		//prepare the return var
		long lInsertId = 0;
		
		//do the select
		String sSql = "SELECT LAST_INSERT_ID() insertID";
		PreparedStatement	psql = a_cCon.prepareStatement( sSql );
		ResultSet rs = psql.executeQuery();

		rs.next();

		lInsertId = rs.getLong("insertID");
		psql.close();
		
		return (lInsertId);
	}
	
	public void setDataSourceComponent (DataSourceComponent a_datasource)
	{
		m_dataSource = a_datasource;
	}
	protected DataSourceComponent getDataSourceComponent ()
	{
		return (m_dataSource);
	}
}
