package com.bright.producttracker.application.service;


/**
 * bright interactive, ETA
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * RefDataManager.java
 *
 *
 */
/*
Ver  Date					Who					Comments
--------------------------------------------------------------------------------
d1   31-Jan-2005			Martin Wilson		Created
d2	 24-Oct-2007			Matt Stevenson		Added continent to country list
--------------------------------------------------------------------------------
 */


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;

import com.bn2web.common.exception.Bn2Exception;
import com.bn2web.common.service.Bn2Manager;
import com.bright.framework.common.bean.Continent;
import com.bright.framework.common.bean.Country;
import com.bright.producttracker.application.constant.ApplicationConstants;

/**
 *
 * RefDataManager. Contains methods to return lists etc.
 *
 * @author  bn2web
 * @version d1
 */
public class RefDataManager extends Bn2Manager implements ApplicationConstants
{		
	protected DataSourceComponent m_dataSource = null;
	
	/*
	 * Returns a list of Country objects
	 *
	 *
	 *@return Vector - of Country objects
	 *@exception Bn2Exception - on error.
	 */
	public Vector getCountryList() throws Bn2Exception
	/*
	------------------------------------------------------------------------
	d1   31-Jan-2005  Martin Wilson         Created
	d2	 24-Oct-2007  Matt Stevenson		Added continent to country list
	------------------------------------------------------------------------
	 */
	{
		Connection cCon = null;
		Vector vecCountry = new Vector ();
		
		try
		{
			cCon = m_dataSource.getConnection ();
			
			PreparedStatement psql = null;
			String sSql = null;
			
			sSql = "SELECT c.Id cId, c.Name cName, co.Id coId, co.Name coName "+
				   "FROM Country c "+
				   "LEFT JOIN Continent co ON c.ContinentId=co.Id ORDER BY co.Name, c.Name";
			
			psql = cCon.prepareStatement (sSql);			
			ResultSet rs = psql.executeQuery ();
			
			// Get all the rows:
			while (rs.next ())
			{
				Continent continent = null;
				
				if (rs.getLong("coId") > 0)
				{
					continent = new Continent();
					continent.setId(rs.getLong("coId"));
					continent.setName(rs.getString("coName"));
				}
				
				// Create the Country object:
				Country country = new Country();
				country.setId(rs.getLong ("cId"));
				country.setName(rs.getString ("cName"));	
				country.setContinent(continent);
				
				vecCountry.add(country);
			}
			
			psql.close();
		}
		catch (SQLException e)
		{
			try
			{
				cCon.rollback ();
			}
			catch (SQLException sqle)
			{
				//ignore.
			}
			
			m_logger.error("Exception occurred in RefDataManager:getCountry : "+e.getMessage());
			throw new Bn2Exception ("Exception occurred in RefDataManager:getCountry : "+e.getMessage());
		}
		finally
		{
			// Return the connection to the pool:
			if (cCon != null)
			{
				try
				{
					cCon.commit ();
					cCon.close ();
				}
				
				catch (SQLException sqle)
				{
					m_logger.error ("SQL Exception whilst trying to close connection " + sqle.getMessage ());
				}
			}
		}
		return (vecCountry);
	}		

	public void setDataSourceComponent (DataSourceComponent a_datasource)
	{
		m_dataSource = a_datasource;
	}	
}
