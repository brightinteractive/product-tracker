package com.bright.producttracker.event.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;

import com.bn2web.common.exception.Bn2Exception;
import com.bn2web.common.service.Bn2Manager;
import com.bright.framework.common.bean.Country;
import com.bright.framework.constant.FrameworkSettings;
import com.bright.framework.database.bean.DBTransaction;
import com.bright.framework.database.util.DBUtil;
import com.bright.framework.util.StringUtil;
import com.bright.producttracker.event.bean.Event;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * EventManager.java
 *
 * Contains the EventManager class.
 */

/*
 Ver	Date			Who					Comments
 --------------------------------------------------------------------------------
 d1		20-Jan-2005		Martin Wilson		Created
 d2		18-Mar-2005		Matt Stevenson		Modified to get products used based on the 
 											ProductMovement information
 d3		16-May-2005		Matt Stevenson		Modified getProductEvents
 d4		25-Oct-2007		Matt Stevenson		Added sorting to getEvents
 d5		26-Oct-2007		Matt Stevenson		Added coordinator email address to event
 --------------------------------------------------------------------------------
 */

/**
 * Manager for adding, listing and editing events
 * 
 * @author Bright Interactive
 * @version d1
 */
public class EventManager extends Bn2Manager
{
	protected DataSourceComponent m_dataSource = null;
	
	/*
	 * Returns a list of events.
	 *
	 *@param DBTransaction a_dbTransaction
	 *@param boolean a_bIncludeHidden - if true returns hidden events as well
	 */
	public Vector getEvents(DBTransaction a_dbTransaction,
							boolean a_bIncludeNotVisible,
							String a_sSort)
	throws Bn2Exception
	/*
	 ------------------------------------------------------------------------
	 d1   20-Jan-2005  Martin Wilson    Created
	 d4	  25-Oct-2007  Matt Stevenson	Added sorting
	 d5	  26-Oct-2007  Matt Stevenson	Added coordinator email address to event
 	------------------------------------------------------------------------
	 */
	{
		Connection con = null;
		Vector vecResults = new Vector();
		
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

			String sSql = "SELECT e.Id, e.Name, e.StartDate, e.EndDate, " + 
								"e.NotVisible, e.CoordinatorEmailAddress, c.Id, c.Name " +
								"FROM Event e LEFT JOIN Country c " +
								"ON e.CountryId = c.Id";
			
			if (!a_bIncludeNotVisible)
			{
				sSql += " WHERE NotVisible=0";
			}
			
			if (StringUtil.stringIsPopulated(a_sSort))
			{
				sSql += " ORDER BY "+a_sSort;
			}

			PreparedStatement psql = con.prepareStatement(sSql);

			ResultSet rs = psql.executeQuery();

			while(rs.next())
			{
				// Create the Event object:
				Event event = buildEvent(rs);		
				vecResults.add(event);
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

			m_logger.error("SQL Exception in EventManager.getEvents: " + e.getMessage());
			throw new Bn2Exception("SQL Exception in EventManager.getEvents: " + e.getMessage());
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
	 * Returns a list of events for a product.
	 *
	 *@param DBTransaction a_dbTransaction
	 *@param long a_lProductId - the id of the product
	 */
	public Vector getProductEvents(DBTransaction a_dbTransaction,
											 long a_lProductId)
	throws Bn2Exception
	/*
	 ------------------------------------------------------------------------
	 d1  20-Jan-2005   	 Martin Wilson           Created
	 d2	 18-Mar-2005	 Matt Stevenson			 Changed to use ProductMovement
	 d3	 16-May-2005	 Matt Stevenson			 Modified to get distinct events
	 d5	 26-Oct-2007  	 Matt Stevenson			 Added coordinator email address to event
 	------------------------------------------------------------------------
	 */
	{
		Connection con = null;
		Vector vecResults = new Vector();
		
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

			String sSql = "SELECT e.Id, e.Name, e.StartDate, e.EndDate, " + 
								"e.NotVisible, e.CoordinatorEmailAddress, c.Id, c.Name " +
								"FROM Event e INNER JOIN ProductMovement pm ON pm.EventId = e.ID " +
								"LEFT JOIN Country c " +
								"ON e.CountryId = c.Id " +
								"WHERE pm.ProductId = ?";

			PreparedStatement psql = con.prepareStatement(sSql);
			psql.setLong(1, a_lProductId);

			ResultSet rs = psql.executeQuery();
			HashMap hmEvents = new HashMap();

			while(rs.next())
			{
				//check that we haven't got this event already...
				if (!hmEvents.containsKey(new Long(rs.getLong("e.Id"))))
				{
					// Create the Event object:
					Event event = buildEvent(rs);
	
					// Add to the results:
					vecResults.add(event);
					hmEvents.put(new Long(rs.getLong("e.Id")), null);
				}
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

			m_logger.error("SQL Exception in EventManager.getProductEvents: " + e.getMessage());
			throw new Bn2Exception("SQL Exception in EventManager.getProductEvents: " + e.getMessage());
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
	 * Returns all the events that are visible.
	 *
	 */
	public Vector getActiveEvents()
	throws Bn2Exception
	/*
	 ------------------------------------------------------------------------
	 d1   20-Jan-2005  Martin Wilson           Created
	 ------------------------------------------------------------------------
	 */
	{
		return (getEvents(null, false, "e.Name"));
	}
	
	/*
	 * Returns an event
	 *
	 *@param DBTransaction a_dbTransaction
	 *@param long a_lEventId
	 */
	public Event getEvent (DBTransaction a_dbTransaction,
									long a_lEventId)
	throws Bn2Exception
	/*
	 ------------------------------------------------------------------------
	 d1   20-Jan-2005  Martin Wilson    Created
	 d5	  26-Oct-2007  Matt Stevenson	Added coordinator email address to event
 	------------------------------------------------------------------------
	 */
	{
		Connection con = null;
		Event event = null;
		
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

			String sSql = "SELECT e.Id, e.Name, e.StartDate, e.EndDate, " + 
								"e.NotVisible, e.CoordinatorEmailAddress, c.Id, c.Name " +
								"FROM Event e LEFT JOIN Country c " +
								"ON e.CountryId = c.Id " +
								"WHERE e.Id=?";


			PreparedStatement psql = con.prepareStatement(sSql);
			psql.setLong(1, a_lEventId);

			ResultSet rs = psql.executeQuery();

			if (rs.next())
			{
				// Create the Event object:
				event = buildEvent(rs);
			}
			else
			{
				m_logger.error("EventManager.getEvent: there is no event with id " + a_lEventId + " in the database");
				throw new Bn2Exception("EventManager.getEvent: there is no event with id " + a_lEventId + " in the database");
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

			m_logger.error("SQL Exception in EventManager.getEvent: " + e.getMessage());
			throw new Bn2Exception("SQL Exception in EventManager.getEvent: " + e.getMessage());
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

		return (event);
	}	
	
	/**
	 * Saves an event.
	 * 
	 * @param a_dbTransaction
	 * @param a_event
	 * @throws Bn2Exception
	 */
	public void saveEvent (DBTransaction a_dbTransaction, 
								  Event a_event) 
	throws Bn2Exception
	 /*
	 ------------------------------------------------------------------------
	  d1   20-Jan-2005  Martin Wilson       Created
	  d5   26-Oct-2007  Matt Stevenson		Added coordinator email address to event
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

			String sSql = null;

			PreparedStatement psql = null;

			if (a_event.getId() <= 0)
			{
				sSql = "INSERT INTO Event (Name, StartDate, EndDate, NotVisible, CountryId, CoordinatorEmailAddress) " +
							" VALUES (?,?,?,?,?,?)";
			}
			else
			{
				sSql = "UPDATE Event SET Name=?, StartDate=?, EndDate=?, NotVisible=?, " +
							"CountryId=?, CoordinatorEmailAddress=? WHERE Id=?";
			}

			int iField = 1;
			psql = cCon.prepareStatement ( sSql );

			psql.setString (iField++, a_event.getName());
			DBUtil.setFieldDateOrNull (psql, iField++, a_event.getStartDate());
			DBUtil.setFieldDateOrNull (psql, iField++, a_event.getEndDate());
			psql.setBoolean(iField++, a_event.getNotVisible());
			DBUtil.setFieldIdOrNull (psql, iField++, a_event.getCountry());
			psql.setString(iField++, a_event.getCoordinatorEmailAddress());
			
			if (a_event.getId() <= 0)
			{
				psql.executeUpdate ();
				psql.close();
				
				// Get the Id for this new address:
				sSql = "SELECT LAST_INSERT_ID() AS NewId FROM Event";
				psql = cCon.prepareStatement ( sSql );
				ResultSet rs = psql.executeQuery ();
				if(rs.next ())
				{
					a_event.setId (rs.getLong ("NewId"));
				}
			}
			else
			{
				psql.setLong (iField++, a_event.getId());
				psql.executeUpdate ();
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

			m_logger.error("SQL Exception in EventManager.saveEvent : " + e.getMessage());
			throw new Bn2Exception("SQL Exception in EventManager.saveEvent : " + e.getMessage());
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
	 * Deletes an event
	 * 
	 * @param a_dbTransaction
	 * @param a_lEventId
	 */
	/*
	---------------------------------------------------------------
	d1		20-Jan-2005		Martin Wilson		Created 
	-----------------------------------------------------------------
	*/
	public void deleteEvent (DBTransaction a_dbTransaction, 
								  long a_lEventId)
	throws Bn2Exception
	 /*
	 ------------------------------------------------------------------------
	  d1   20-Jan-2005  Martin Wilson         Created
	  d2	 18-Mar-2005  Matt Stevenson			Modified to use ProductMovements
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
			
			// Remove all the product at event links:
			String sSql = "DELETE FROM ProductMovement WHERE EventId=?";

			PreparedStatement psql = cCon.prepareStatement ( sSql );
			psql.setLong (1, a_lEventId);
			psql.executeUpdate ();
			
			// Now delete the event:
			sSql = "DELETE FROM Event WHERE Id=?";

			psql = cCon.prepareStatement ( sSql );
			psql.setLong (1, a_lEventId);			
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

			m_logger.error("SQL Exception in EventManager.deleteEvent: " + e.getMessage());
			throw new Bn2Exception("SQL Exception in EventManager.deleteEvent: " + e.getMessage());
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
	
	
	private Event buildEvent (ResultSet a_rs) throws SQLException
	{
		Event event = new Event();
		event.setId(a_rs.getLong("e.Id"));				
		event.setName(a_rs.getString("e.Name"));
		event.setStartDate(a_rs.getDate("e.StartDate"));
		event.setEndDate(a_rs.getDate("e.EndDate"));
		event.setCoordinatorEmailAddress(a_rs.getString("e.CoordinatorEmailAddress"));
		event.setNotVisible(a_rs.getBoolean("e.NotVisible"));
		
//		 Set the date strings:
		if (event.getStartDate() != null)
		{
			event.setStartDateStr(FrameworkSettings.getStandardDateFormat().format(event.getStartDate()));
		}
		if (event.getEndDate() != null)
		{
			event.setEndDateStr(FrameworkSettings.getStandardDateFormat().format(event.getEndDate()));
		}				

		if(a_rs.getLong("c.Id") > 0)
		{
			// Create the country object:
			Country country = new Country();			
			country.setId(a_rs.getLong("c.Id"));
			country.setName(a_rs.getString("c.Name"));
			event.setCountry(country);
		}			
		
		return (event);
	}
	
	
	public void setDataSourceComponent (DataSourceComponent a_datasource)
	{
		m_dataSource = a_datasource;
	}
}
