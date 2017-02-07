package com.bright.framework.simplelist.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;

import com.bn2web.common.exception.Bn2Exception;
import com.bn2web.common.service.Bn2Manager;
import com.bright.framework.database.bean.DBTransaction;
import com.bright.framework.simplelist.bean.List;
import com.bright.framework.simplelist.bean.ListValue;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * ListManager.java
 *
 * Contains the ListManager class.
 */

/*
 Ver	Date			Who					Comments
 --------------------------------------------------------------------------------
 d1		29-Jan-2005		Martin Wilson		Created
 --------------------------------------------------------------------------------
 */

/**
 * Manager for managing simple lists.
 * 
 * @author Bright Interactive
 * @version d1
 */
public class ListManager extends Bn2Manager
{
	protected DataSourceComponent m_dataSource = null;
	
	/*
	 * Returns a Vector of values for a list, specified by the list id.
	 *
	 *@param String a_sListName - the name of the list
	 */
	public Vector getListValues(String a_sListId)
	throws Bn2Exception
	/*
	 ------------------------------------------------------------------------
	 d1   29-Jan-2005  Martin Wilson           Created
	 ------------------------------------------------------------------------
	 */
	{
		return (getListValues(null, a_sListId));	
	}
	
	/*
	 * Returns a Vector of values for a list, specified by the list id.
	 *
	 *@param DBTransaction a_dbTransaction
	 *@param String a_sListName - the name of the list
	 */
	public Vector getListValues(DBTransaction a_dbTransaction,
										 String a_sListId)
	throws Bn2Exception
	/*
	 ------------------------------------------------------------------------
	 d1   29-Jan-2005  Martin Wilson           Created
	 ------------------------------------------------------------------------
	 */
	{
		Connection con = null;
		Vector vecResults = new Vector();
		ListValue listValue = null;
		
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

			String sSql = "SELECT lv.Id, lv.Value FROM ListValue lv " + 
								"WHERE lv.ListId = ? " +
								"ORDER BY lv.Value ASC";			

			PreparedStatement psql = con.prepareStatement(sSql);
			
			psql.setString (1, a_sListId);			

			ResultSet rs = psql.executeQuery();

			while(rs.next())
			{
				// Create the List object:
				listValue = new ListValue();
				listValue.setId(rs.getLong("lv.Id"));				
				listValue.setValue(rs.getString("lv.Value"));	

				// Add to the results:
				vecResults.add(listValue);
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

			m_logger.error("SQL Exception in ListManager.getListValues: " + e.getMessage());
			throw new Bn2Exception("SQL Exception in ListManager.getListValues: " + e.getMessage());
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

		return (vecResults);
	}
	
	/*
	 * Returns a Vector of List objects.
	 *
	 *@param DBTransaction a_dbTransaction
	 */
	public Vector getLists(DBTransaction a_dbTransaction)
	throws Bn2Exception
	/*
	 ------------------------------------------------------------------------
	 d1   29-Jan-2005  Martin Wilson           Created
	 ------------------------------------------------------------------------
	 */
	{
		Connection con = null;
		Vector vecResults = new Vector();
		List list = null;
		
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

			String sSql = "SELECT l.Id, l.Description FROM List l " + 
								"ORDER BY l.Description ASC";			

			PreparedStatement psql = con.prepareStatement(sSql);
			ResultSet rs = psql.executeQuery();

			while(rs.next())
			{
				// Create the List object:
				list = new List();
				list.setId(rs.getString("l.Id"));					
				list.setDescription(rs.getString("l.Description"));
				
				// Add to the results:
				vecResults.add(list);
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

			m_logger.error("SQL Exception in ListManager.getLists: " + e.getMessage());
			throw new Bn2Exception("SQL Exception in ListManager.getLists: " + e.getMessage());
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

		return (vecResults);
	}	
	
	/*
	 * Returns a List object.
	 *
	 *@param DBTransaction a_dbTransaction
	 */
	public List getList(DBTransaction a_dbTransaction, String a_sId)
	throws Bn2Exception
	/*
	 ------------------------------------------------------------------------
	 d1   29-Jan-2005  Martin Wilson           Created
	 ------------------------------------------------------------------------
	 */
	{
		Connection con = null;
		List list = null;
		
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

			String sSql = "SELECT l.Id, l.Description FROM List l " + 
								"WHERE Id = ?";			

			PreparedStatement psql = con.prepareStatement(sSql);
			psql.setString(1, a_sId);
			
			ResultSet rs = psql.executeQuery();

			if (rs.next())
			{
				// Create the List object:
				list = new List();
				list.setId(rs.getString("l.Id"));					
				list.setDescription(rs.getString("l.Description"));
			}
			else
			{
				throw new Bn2Exception("ListManager.getList: there is no list with id=" + a_sId);
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

			m_logger.error("SQL Exception in ListManager.getList: " + e.getMessage());
			throw new Bn2Exception("SQL Exception in ListManager.getList: " + e.getMessage());
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

		return (list);
	}		
	
	/*
	 * Returns a ListValue, given its id
	 *
	 *@param DBTransaction a_dbTransaction
	 *@param long a_lListValueId
	 */
	public ListValue getListValue (DBTransaction a_dbTransaction,
											 long a_lListValueId)
	throws Bn2Exception
	/*
	 ------------------------------------------------------------------------
	 d1   29-Jan-2005  Martin Wilson           Created
	 ------------------------------------------------------------------------
	 */
	{
		Connection con = null;
		ListValue listValue = null;
		
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

			String sSql = "SELECT lv.Id, lv.Value FROM ListValue lv WHERE lv.Id = ?";

			PreparedStatement psql = con.prepareStatement(sSql);
			psql.setLong(1, a_lListValueId);

			ResultSet rs = psql.executeQuery();

			if (rs.next())
			{
				// Create the List object:
				listValue = new ListValue();
				listValue.setId(rs.getLong("lv.Id"));				
				listValue.setValue(rs.getString("lv.Value"));					

			}
			else
			{
				m_logger.error("ListManager.getListValue: there is no ListValue with id " + a_lListValueId + " in the database");
				throw new Bn2Exception("ListManager.getListValue: there is no ListValue with id " + a_lListValueId + " in the database");
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

			m_logger.error("SQL Exception in ListManager.getListValue: " + e.getMessage());
			throw new Bn2Exception("SQL Exception in ListManager.getListValue: " + e.getMessage());
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

		return (listValue);
	}	
	
	/**
	 * Adds a value to a list.
	 * 
	 * @param a_dbTransaction
	 * @param long a_lListId - the id of the list
	 * @param ListValue a_newValue - the value to add
	 * @throws Bn2Exception
	 */
	public void addListValue (DBTransaction a_dbTransaction, 
									  String a_sListId,
									  ListValue a_newListValue) 
	throws Bn2Exception
	 /*
	 ------------------------------------------------------------------------
	  d1   29-Jan-2005  Martin Wilson         Created
	 ------------------------------------------------------------------------
	  */
	{
		Connection cCon = null;

		try
		{
			// Get a connection from the transaction or pool:
			if(a_dbTransaction == null)
			{
				cCon = m_dataSource.getConnection();
			}
			else
			{
				cCon = a_dbTransaction.getConnection();
			}

			String sSql = "INSERT INTO ListValue (ListId, Value) " +
						" VALUES (?,?)";


			int iField = 1;
			PreparedStatement psql = cCon.prepareStatement ( sSql );

			psql.setString (iField++, a_sListId);
			psql.setString (iField++, a_newListValue.getValue());
			
			psql.executeUpdate ();
			psql.close();
			
			// Get the Id for this new value:
			sSql = "SELECT LAST_INSERT_ID() AS NewId FROM ListValue";
			psql = cCon.prepareStatement ( sSql );
			ResultSet rs = psql.executeQuery ();
			if(rs.next ())
			{
				a_newListValue.setId (rs.getLong ("NewId"));
			}	
			
			psql.close();
		}
		catch(SQLException e)
		{
			if(a_dbTransaction == null)
			{
				try
				{
					cCon.rollback();
				}
				catch(SQLException sqle)
				{
					// Do nothing.
				}
			}

			m_logger.error("SQL Exception in ListManager.addListValue : " + e.getMessage());
			throw new Bn2Exception("SQL Exception in ListManager.addListValue : " + e.getMessage());
		}
		finally
		{
			if(a_dbTransaction == null)
			{
				// Return the connection to the pool:
				if (cCon != null)
				{
					try
					{
						cCon.commit();
						cCon.close();
					}
					catch(SQLException sqle)
					{
						m_logger.error("Sorry, SQL Exception whilst trying to close connection " + sqle.getMessage());
					}
				}
			}
		}

	}	
	

	/**
	 * Deletes a value from a list. Assumes all reference to the item have been removed - otherwise
	 * will throw a sql exception
	 * 
	 * @param a_dbTransaction
	 * @param a_lListId
	 */
	public void deleteListValue (DBTransaction a_dbTransaction, 
										  long a_lListValueId)
	throws Bn2Exception
	 /*
	 ------------------------------------------------------------------------
	  d1   29-Jan-2005  Martin Wilson         Created
	 ------------------------------------------------------------------------
	  */
	{
		Connection cCon = null;

		try
		{
			// Get a connection from the transaction or pool:
			if(a_dbTransaction == null)
			{
				cCon = m_dataSource.getConnection();
			}
			else
			{
				cCon = a_dbTransaction.getConnection();
			}
			
			
			// Remove all the product at List links:
			String sSql = "DELETE FROM ListValue WHERE Id=?";

			PreparedStatement psql = cCon.prepareStatement ( sSql );
			psql.setLong (1, a_lListValueId);
			psql.executeUpdate ();
			psql.close();
		}
		catch(SQLException e)
		{
			if(a_dbTransaction == null)
			{
				try
				{
					cCon.rollback();
				}
				catch(SQLException sqle)
				{
					// Do nothing.
				}
			}

			m_logger.error("SQL Exception in ListManager.deleteList: " + e.getMessage());
			throw new Bn2Exception("SQL Exception in ListManager.deleteList: " + e.getMessage());
		}
		finally
		{
			if(a_dbTransaction == null)
			{
				// Return the connection to the pool:
				if (cCon != null)
				{
					try
					{
						cCon.commit();
						cCon.close();
					}
					catch(SQLException sqle)
					{
						m_logger.error("Sorry, SQL Exception whilst trying to close connection " + sqle.getMessage());
					}
				}
			}
		}

	}			
	
	public void setDataSourceComponent (DataSourceComponent a_datasource)
	{
		m_dataSource = a_datasource;
	}
}
