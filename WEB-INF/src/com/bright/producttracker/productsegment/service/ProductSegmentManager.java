package com.bright.producttracker.productsegment.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;

import com.bn2web.common.exception.Bn2Exception;
import com.bn2web.common.service.Bn2Manager;
import com.bright.framework.database.bean.DBTransaction;
import com.bright.producttracker.productsegment.bean.ProductSegment;



/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * ProductSegmentManager.java
 *
 * Contains the ProductSegmentManager class.
 */

/*
 Ver	Date				Who					Comments
 --------------------------------------------------------------------------------
 d1		23-Feb-2010		Kevin Bennett		Created
 --------------------------------------------------------------------------------
 */

/**
 * Manager for listing product segments
 * 
 * @author Bright Interactive
 * @version d1
 */


public class ProductSegmentManager extends Bn2Manager
{
	protected DataSourceComponent m_dataSource = null;
	public void setDataSourceComponent (DataSourceComponent a_datasource)
	{
		m_dataSource = a_datasource;
	}
	
	
	/*
	 * Returns an Product Segment
	 *
	 *@param DBTransaction a_dbTransaction
	 *@param long a_lProductSegmentId
	 */
	public ProductSegment getProductSegment (DBTransaction a_dbTransaction,
										long a_lProductSegmentId)
	throws Bn2Exception
	{
		Connection con = null;
		ProductSegment productSegment = new ProductSegment();
		if(a_lProductSegmentId > 0)
		{
		
			try
			{
				if(a_dbTransaction == null)
				{
					con = m_dataSource.getConnection();
				}
				else
				{
					con = a_dbTransaction.getConnection();
				}
	
				String sSql = "SELECT * FROM ProductSegment WHERE Id = ?";								
	
				PreparedStatement psql = con.prepareStatement(sSql);
				psql.setLong(1, a_lProductSegmentId);
	
				ResultSet rs = psql.executeQuery();
				
				while (rs.next())
				{
					// Create the Product object:
					productSegment = buildProductSegment(rs);
					
				}
				psql.close();
				
				if (productSegment == null)
				{
					m_logger.error("ProductSegmentManager.getProductSegment: there is no Product Segment with id " + a_lProductSegmentId + " in the database");
					throw new Bn2Exception("ProductSegmentManager.getProductSegment: there is no Product Segment with id " + a_lProductSegmentId + " in the database");
				}
			}
			catch(SQLException e)
			{
				if(a_dbTransaction == null)
				{
					try
					{
						con.rollback();
					}
					catch(SQLException sqle)
					{
						// Do nothing.
					}
				}
	
				m_logger.error("SQL Exception in ProductSegmentManager.getProductSegment: " + e.getMessage());
				throw new Bn2Exception("SQL Exception in ProductSegmentManager.getProductSegment: " + e.getMessage());
			}
			finally
			{
				if(a_dbTransaction == null)
				{
					// Return the connection to the pool:
					if (con != null)
					{
						try
						{
							con.commit();
							con.close();
						}
						catch(SQLException sqle)
						{
							m_logger.error("Sorry, SQL Exception whilst trying to close connection " + sqle.getMessage());
						}
					}
				}
			}
		}

		return (productSegment);
	}	
	
	
	/*
	 * Returns a list of all Product Segment
	 *
	 *@param DBTransaction a_dbTransaction*
	 */
	public List<ProductSegment> getProductSegments (DBTransaction a_dbTransaction)
	throws Bn2Exception
	{
		Connection con = null;
		List<ProductSegment> productSegments = new ArrayList<ProductSegment>();
		
		try
		{
			if(a_dbTransaction == null)
			{
				con = m_dataSource.getConnection();
			}
			else
			{
				con = a_dbTransaction.getConnection();
			}

			String sSql = "SELECT * FROM ProductSegment ORDER BY Name";								

			PreparedStatement psql = con.prepareStatement(sSql);			

			ResultSet rs = psql.executeQuery();
			
			while (rs.next())
			{
				// Create the Product object and add to list
				productSegments.add(buildProductSegment(rs));				
			}
			psql.close();
						
		}
		catch(SQLException e)
		{
			if(a_dbTransaction == null)
			{
				try
				{
					con.rollback();
				}
				catch(SQLException sqle)
				{
					// Do nothing.
				}
			}

			m_logger.error("SQL Exception in ProductSegmentManager.getProductSegments: " + e.getMessage());
			throw new Bn2Exception("SQL Exception in ProductSegmentManager.getProductSegments: " + e.getMessage());
		}
		finally
		{
			if(a_dbTransaction == null)
			{
				// Return the connection to the pool:
				if (con != null)
				{
					try
					{
						con.commit();
						con.close();
					}
					catch(SQLException sqle)
					{
						m_logger.error("Sorry, SQL Exception whilst trying to close connection " + sqle.getMessage());
					}
				}
			}
		}

		return (productSegments);
	}	
	
	
	private ProductSegment buildProductSegment (ResultSet a_rs) throws SQLException
	{
		ProductSegment productSegment = new ProductSegment();
		productSegment.setId(a_rs.getLong("Id"));							
		productSegment.setName(a_rs.getString("Name"));
		productSegment.setIcon(a_rs.getString("Icon"));
				
		return (productSegment);
	}
	
	

}
