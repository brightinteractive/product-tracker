package com.bright.framework.database.service;


/**
 * Bright Interactive, TransactionManager
 *
 * Copyright 2002 Bright Interactive, All Rights Reserved.
 * TransactionManager.java
 *
 *
 */
/*
    Ver     Date            Who             Comments
    --------------------------------------------------------------------------------
    d1      17-Oct-2003     Martin Wilson   Created.
    d2      17-Oct-2003     Matt Stevenson  Fixed problem with getNewTransaction
    --------------------------------------------------------------------------------
*/

import java.sql.SQLException;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;

import com.bn2web.common.exception.Bn2Exception;
import com.bn2web.common.service.Bn2Manager;
import com.bright.framework.database.bean.DBTransaction;

/**
 *
 * TransactionManager - used to start transactions for use across managers.
 *
 * @author  Bright Interactive
 * @version d1
 */
public class DBTransactionManager extends Bn2Manager
{

    private DataSourceComponent m_dataSource = null;

    /**
      * Initialise this component - check it has been composed and configured
      * and that it hasn't been disposed.
      *
      * @param
      * @return void
      * @throws Exception
      */
    public void initialize() throws Exception
    /*
    ------------------------------------------------------------------------
     d1   17-Oct-2003   Martin Wilson       Created.
    ------------------------------------------------------------------------
    */
    {
        super.initialize();
    }

    /**
      * Returns a new DBTransaction object.
      *
      * @return DBTransaction - a new DBTransaction object
      * @throws Bn2Exception
      */
    public DBTransaction getNewTransaction() throws Bn2Exception
    /*
    ------------------------------------------------------------------------
     d1   17-Oct-2003   Martin Wilson       Created.
     d2   17-Oct-2003   Matt Stevenson      Fixed minor problem
    ------------------------------------------------------------------------
    */
    {
        DBTransaction transaction = null;

        try
        {
            // Create a new transaction, containing a connection:
            transaction = new DBTransaction(m_dataSource.getConnection());
        }
        catch (SQLException sqle)
        {
            throw new Bn2Exception("Could not get connection from pool in DBTransaction.getNewTransaction");
        }

        return (transaction);
    }

  /*
     * Links the data source component for use in DB pool retrieval
     *
     * @param DataSourceComponent a_datasource - the datasource component to link
     */
    public void setDataSourceComponent(DataSourceComponent a_datasource)
    /*
    ------------------------------------------------------------------------
     d1   16-Oct-2003  Martin Wilson        Created.
    ------------------------------------------------------------------------
    */
    {
        m_dataSource = a_datasource;
    }
 }
