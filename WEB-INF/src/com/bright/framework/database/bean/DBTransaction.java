package com.bright.framework.database.bean;

/**
 * Bright Interactive, Category
 *
 * Copyright 2002 Bright Interactive, All Rights Reserved.
 * DBTransaction.java
 *
 * Contains the DBTransaction class.
 */
/*
Ver  Date       Who         Comments
--------------------------------------------------------------------------------
d1   17-Oct-2003    Martin Wilson      Created
d2   24-Oct-2003    Martin Wilson      Fixed problem with closed properly boolean
d3   21-Jun-2004    Martin Wilson      Changed commit to only commit and close connection if still valid
--------------------------------------------------------------------------------
*/

import java.sql.Connection;
import java.sql.SQLException;

import com.bn2web.common.exception.Bn2Exception;
import com.bn2web.common.service.GlobalApplication;

/**
 * Represents a database transaction. An instance should be obtained from a Manager class.
 * Then call getConnection to get a connection and rollback/commit when finished.
 *
 * @author  Bright Interactive
 * @version d1
 */
public class DBTransaction
{

    Connection m_connection = null;
    boolean m_bClosed = false;

    /**
      * Default constructor - private to force the use of DBTransaction(Connection)
      *
      */
    private DBTransaction()
    /*
    ------------------------------------------------------------------------
     d1   17-Oct-2003    Martin Wilson      Created
    ------------------------------------------------------------------------
    */
    {
        /* Empty */
    }

    /**
      * Constructor.
      *
      * @param Connection a_connection - a database connection.
      */
    public DBTransaction(Connection a_connection)
    /*
    ------------------------------------------------------------------------
     d1   17-Oct-2003    Martin Wilson      Created
    ------------------------------------------------------------------------
    */
    {
        m_connection = a_connection;
    }

    /**
      * Finalize method - checks that the transaction was ended properly.
      *
      * @param Connection a_connection - a database connection.
      */
    protected void finalize()
    /*
    ------------------------------------------------------------------------
     d1   17-Oct-2003    Martin Wilson      Created
    ------------------------------------------------------------------------
    */
    {
        if (!m_bClosed)
        {
            GlobalApplication.getInstance().getLogger().error("DEBUG ERROR: A transaction was not properly closed - you must call commit or rollback");
        }
    }

    /**
      * Returns the connection for use.
      *
      * @return Connection a_connection - a database connection.
      */
    public Connection getConnection()
    /*
    ------------------------------------------------------------------------
     d1   17-Oct-2003    Martin Wilson      Created
    ------------------------------------------------------------------------
    */
    {
        return (m_connection);
    }

    /**
      * Commits the transaction.
      *
      * @throws Bn2Exception on error.
      */
    public void commit() throws Bn2Exception, SQLException
    /*
    ------------------------------------------------------------------------
     d1   17-Oct-2003    Martin Wilson      Created
     d2   24-Oct-2003    Martin Wilson      Set m_bClosedProperly
	  d3   21-Jun-2004    Martin Wilson      Changed to only commit and close connection if still valid
    ------------------------------------------------------------------------
    */
    {
        if (m_connection == null)
        {
            throw new Bn2Exception("The connection is null in DBTransaction.commit");
        }

		  // Only do this if the transaction is still open, i.e. if commit or rollback has
		  // not been called already. This is because we always call commit in a finally clause.
		  if (!m_bClosed)
		  {
			  try
			  {
					// Commit:
					m_connection.commit();
			  }
			  finally
			  {
					// Return to the pool:
					m_connection.close();
			  }

			  m_bClosed = true;
		  }
    }

    /**
      * Rolls back the transaction.
      *
      * @throws Bn2Exception on error.
      */
    public void rollback() throws Bn2Exception, SQLException
    /*
    ------------------------------------------------------------------------
     d1   17-Oct-2003    Martin Wilson      Created
     d2   24-Oct-2003    Martin Wilson      Set m_bClosedProperly
    ------------------------------------------------------------------------
    */
    {
        if (m_connection == null)
        {
            throw new Bn2Exception("The connection is null in DBTransaction.commit");
        }

        try
        {
            // Rollback:
            m_connection.rollback();
        }
        finally
        {
            // Return to the pool:
            m_connection.close();
        }

        m_bClosed = true;
    }

}